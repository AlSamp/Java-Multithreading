package CO3408;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Thread.sleep;
import java.util.Scanner;

// This code (which will compile) is to help you get started with the assignment.
// The first section reads the config file and creates the objects.
//
// Later there are some strong hints about what needs to be output, in line with
// the requirements given in the assignment brief.
//
// You WILL need to add code of your own to make it work, after about line 350.
// Feel free to generally improve upon what I have written!

/**
 *
 * @author Nick
 */


public class AssignmentPart1
{


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {


        System.out.print("Start of program\n");
        // These variables will store the configuration
        // of the Present sorting machine

        int numBelts;
        Conveyor[] belts;

        int numHoppers;
        Hopper[] hoppers;

        int numSacks;
        Sack[] sacks;

        int numTurntables;
        Turntable[] tables;

        int timerLength;

        ////////////////////////////////////////////////////////////////////////
        
        // READ FILE
        // =========
        String filename = "C:/Users/Alix/IdeaProjects/C03408-Assignment-Part1-backup/out/production/AssignmentPart1/scenario5.txt";
        Scanner inputStream = null;
        try
        {
            inputStream = new Scanner(new File(filename));
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Error opening file");
            System.exit(0);
        }

        String line = "";

        // READ BELTS
        // ----------
        // Skip though any blank lines to start
        while (!line.startsWith("BELTS") && inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
        }

        numBelts = inputStream.nextInt();
        inputStream.nextLine();

        belts = new Conveyor[numBelts]; // capacity of array is number of belts

        for (int b = 0; b < numBelts; b++)
        {
            line = inputStream.nextLine(); // e.g. 1 length 5 destinations 1 2

            Scanner beltStream = new Scanner(line);
            int id = beltStream.nextInt();
            beltStream.next(); // skip "length"

            int length = beltStream.nextInt();
            belts[b] = new Conveyor(id, length); //
            beltStream.next(); // skip "destinations"

            while (beltStream.hasNextInt())
            {
                int dest = beltStream.nextInt();
                belts[b].addDestination(dest);

            }
            System.out.println("Testing this : " + belts[b].destinations);
            System.out.println("Set up Belt " + id);
        } // end of reading belt lines

        // READ HOPPERS
        // ------------
        // Skip though any blank lines
        while (!line.startsWith("HOPPERS") && inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
        }

        numHoppers = inputStream.nextInt();
        inputStream.nextLine();

        System.out.println("Number of hoppers made = " + numHoppers);
        hoppers = new Hopper[numHoppers];

        for (int h = 0; h < numHoppers; h++)
        {
            // Each hopper line will look like this:
            // e.g. 1 belt 1 capacity 10 speed 1

            int id = inputStream.nextInt();
            inputStream.next(); // skip "belt"

            int belt = inputStream.nextInt();
            inputStream.next(); // skip "capacity"

            int capacity = inputStream.nextInt();
            inputStream.next(); // skip "speed"

            int speed = inputStream.nextInt();
            line = inputStream.nextLine(); // skip rest of line

            hoppers[h] = new Hopper(id, belts[belt - 1], capacity, speed);


            System.out.println("Set up Hopper " + id + "\ncapicity = " + capacity + "\nspeed = " + speed);
        } // end of reading hopper lines

        // READ SACKS
        // ------------
        // Skip though any blank lines
        while (!line.startsWith("SACKS") && inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
        }

        numSacks = inputStream.nextInt();
        inputStream.nextLine();

        sacks = new Sack[numSacks];
        System.out.println("Sacks = " + numSacks);

        for (int s = 0; s < numSacks; s++)
        {
            // Each sack line will look like this:
            // e.g. 1 capacity 20 age 0-3

            int id = inputStream.nextInt();
            inputStream.next(); // skip "capacity"

            int capacity = inputStream.nextInt();
            inputStream.next(); // skip "age"

            String age = inputStream.next();
            line = inputStream.nextLine(); // skip rest of line

            sacks[s] = new Sack(id, capacity);
            Turntable.destinations.put(age, id);

            System.out.println("Set up Sack " + sacks[s].id);
        } // end of reading sack lines

        // READ TURNTABLES
        // ---------------
        // Skip though any blank lines
        while (!line.startsWith("TURNTABLES") && inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
        }

        numTurntables = inputStream.nextInt();
        inputStream.nextLine();

        tables = new Turntable[numTurntables];

        for (int t = 0; t < numTurntables; t++)
        {
            // Each turntable line will look like this:
            // A N ib 1 E null S os 1 W null

            String tableId = inputStream.next();
            tables[t] = new Turntable(tableId);

            int connId = 0;

            inputStream.next(); // skip "N"
            Connection north = null;
            String Ntype = inputStream.next();
            if (!"null".equals(Ntype))
            {
                connId = inputStream.nextInt();
                if (null != Ntype)
                {
                    switch (Ntype)
                    {
                        case "os":
                            north = new Connection(ConnectionType.OutputSack, null, sacks[connId - 1]);
                            break;
                        case "ib":
                            north = new Connection(ConnectionType.InputBelt, belts[connId - 1], null);
                            break;
                        case "ob":
                            north = new Connection(ConnectionType.OutputBelt, belts[connId - 1], null);
                            break;
                    }
                    tables[t].addConnection(Turntable.N, north);                    
                }
            }

            inputStream.next(); // skip "E"
            Connection east = null;
            String Etype = inputStream.next();
            if (!"null".equals(Etype))
            {
                connId = inputStream.nextInt();
                if (null != Etype)
                {
                    switch (Etype)
                    {
                        case "os":
                            east = new Connection(ConnectionType.OutputSack, null, sacks[connId - 1]);
                            break;
                        case "ib":
                            east = new Connection(ConnectionType.InputBelt, belts[connId - 1], null);
                            break;
                        default:
                            east = new Connection(ConnectionType.OutputBelt, belts[connId - 1], null);
                            break;
                    }
                    tables[t].addConnection(Turntable.E, east);
                }
            }

            inputStream.next(); // skip "S"
            Connection south = null;
            String Stype = inputStream.next();
            if (!"null".equals(Stype))
            {
                connId = inputStream.nextInt();
                if (null != Stype)
                {
                    switch (Stype)
                    {
                        case "os":
                            south = new Connection(ConnectionType.OutputSack, null, sacks[connId - 1]);
                            break;
                        case "ib":
                            south = new Connection(ConnectionType.InputBelt, belts[connId - 1], null);
                            break;
                        default:
                            south = new Connection(ConnectionType.OutputBelt, belts[connId - 1], null);
                            break;
                    }
                    tables[t].addConnection(Turntable.S, south);
                }
            }

            inputStream.next(); // skip "W"
            Connection west = null;
            String Wtype = inputStream.next();
            if (!"null".equals(Wtype))
            {
                connId = inputStream.nextInt();
                if (null != Wtype)
                {
                    switch (Wtype)
                    {
                        case "os":
                            west = new Connection(ConnectionType.OutputSack, null, sacks[connId - 1]);
                            break;
                        case "ib":
                            west = new Connection(ConnectionType.InputBelt, belts[connId - 1], null);
                            break;
                        default:
                            west = new Connection(ConnectionType.OutputBelt, belts[connId - 1], null);
                            break;
                    }
                    tables[t].addConnection(Turntable.W, west);
                }
            }

            line = inputStream.nextLine(); // skip rest of line
            System.out.println("Set up turntable " + tableId);
        } // end of reading turntable lines

        // FILL THE HOPPERS
        // ----------------
        for (int i = 0; i < numHoppers; i++)
        {
            // Skip though any blank lines
            while (!line.startsWith("PRESENTS") && inputStream.hasNextLine())
            {
                line = inputStream.nextLine();
            }
            int numPresents = inputStream.nextInt();
            inputStream.nextLine();

            System.out.println("Presents = " + numPresents );
            for (int p = 0; p < numPresents; p++)
            {
                hoppers[i].fill(new Present(inputStream.next()));

                line = inputStream.nextLine();
            }

            System.out.println("Filled Hopper " + hoppers[i].id);
        }

        // READ TIMER LENGTH
        // -----------------
        // Skip though any blank lines
        while (!line.startsWith("TIMER") && inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
        }
        Scanner timerStream = new Scanner(line);
        timerStream.next(); // skip "length"
        timerLength = timerStream.nextInt(); ///////////////////////////////////////////// Give additional time here

        System.out.println("Machine will run for " + timerLength + "s.\n");

        ///////////////////////////////////////////////////////////////////////
        // END OF SETUP ///////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        
        // START the hoppers!
        for (int h = 0; h < numHoppers; h++)
        {
            hoppers[h].start();
        }

        // START the turntables!
        for (int t = 0; t < numTurntables; t++)
        {
            tables[t].start();
        }

        long time = 0;
        long currentTime = 0;
        long startTime = System.currentTimeMillis();
        System.out.println("*** Machine Started ***");
        while (time < timerLength)
        {
            // sleep in 10 second bursts
            try
            {
                sleep(10000);
            }
            catch (InterruptedException ex)
            {
            }
            currentTime = System.currentTimeMillis();
            time = (currentTime - startTime) / 1000;

            // ****************************** INTERIM REPORT  *******************************
            System.out.println("\nInterim Report @ " + time + "s:");






            int giftsInHoppers = 0;
            // TODO - calculate number & refactor
            // Store current number of gifts in hoppers in a variable
            int [] hoppersCapacity = new int[numHoppers];
            for(int i = 0; i < numHoppers; i++)
            {
                hoppersCapacity[i] = hoppers[i].getCapacity();
                giftsInHoppers += hoppersCapacity[i];
            }

            int giftsInSacks = 0;
            int totalSacks = 0;
            for (int i = 0; i < numSacks; i++)
            {
                giftsInSacks = sacks[i].getCapacity(); // output number of sacks in each sack
                totalSacks += sacks[i].getCapacity();

                System.out.println("Sack " + sacks[i].id + " contains " + giftsInSacks + " presents.");
            }

            System.out.println(giftsInHoppers + " presents remaining in hoppers;\n" + totalSacks + " presents sorted into sacks.\n");

        } // end of while loop

        long endTime = System.currentTimeMillis();
        System.out.println("*** Input Stopped after " + (endTime - startTime) / 1000 + "s. ***");

        // TODO
        // Stop the hoppers!
        // Stop the tables!
        // HINT - Wait for everything to finish...

        endTime = System.currentTimeMillis();
        System.out.println("*** Machine completed shutdown after " + (endTime - startTime) / 1000 + "s. ***");

        
        // FINAL REPORTING
        ////////////////////////////////////////////////////////////////////////
        
        System.out.println();
        System.out.println("\nFINAL REPORT\n");
        System.out.println("Configuration: " + filename);
        System.out.println("Total Run Time" + (endTime - startTime) / 1000 + "s.");

        // Calculate number of presents in the hoppers
        int totalGifts = (hoppers[0].presentIdCounter); // static idCounter across all hoppers
        
        for (int h = 0; h < numHoppers; h++)
        {
            System.out.println("Hopper " + hoppers[h].id + " deposited " + /* TODO */ " presents and waited " + /* TODO */ "s.");
        }
        System.out.println();

        // Calculate number of presents in the hoppers

        int giftsInHoppers = 0;

        for (int i = 0; i < numHoppers; i++)
        {
            giftsInHoppers += hoppers[i].getCapacity(); // calculate number of gifts remaining on the hoppers
        }



        // Calculate number of presents in the sacks
        int giftsInSacks = 0;
        for (int i = 0; i < numSacks; i++)
        {
            giftsInSacks = sacks[i].getCapacity(); // output number of sacks in each sack

            System.out.println("Sack " + sacks[i].id + " contains " + giftsInSacks + " presents.");
        }


        // Output number of presents on the conveyor belts
        int giftsOnBelt = 0;
        for (int i = 0; i < numBelts; i++)
        {

            for(int j = 0; j < belts[i].presentsOnBelt.length;j++)
            {
                if(belts[i].presentsOnBelt[j] != null)
                {
                    giftsOnBelt++;


                }
            }
        }


        // Output number of presents in the turntables
        int giftsInTurntables = 0;
        for (int i = 0; i < numTurntables; i++)
        {
                if(tables[i].present != null)
                {
                    giftsInTurntables++;
                }
        }




        // Output number of presents in sacks
        giftsInSacks = 0;


        for (int i = 0; i < numSacks; i++)
        {
            giftsInSacks += sacks[i].getCapacity(); // calculate number of gifts placed into all sacks
        }

        // calculate number of missing presents
        int missing = totalGifts - giftsInSacks - giftsInHoppers - giftsOnBelt - giftsInTurntables;

        // Output results
        System.out.print("\nOut of " + totalGifts + " gifts to be deposited, ");
        System.out.print(giftsInHoppers + " are still in the hoppers, and ");
        System.out.println(giftsInSacks + " made it into the sacks");
        System.out.println(giftsInTurntables + " presents inside the turntables.");
        System.out.println(giftsOnBelt + " presents on the conveyor belts.");
        System.out.println(missing + " gifts went missing.");





        // TERMINATE HOPPER THREADS
        int remainingGifts = giftsOnBelt + giftsInTurntables; // get total number of presents that made it onto the machine
        int totalGiftsPossible = giftsInSacks + remainingGifts;

        for (int i = 0; i < numHoppers; i++)
        {
           hoppers[i].terminate();
        }

        System.out.println("***");
        System.out.println(" ");
        System.out.println(" ");
        // Keep threads active after time limit until belts and turntables are empty
        boolean stall = false;
        while(giftsInSacks != totalGiftsPossible && stall == false) // TODO FIX THIS LOOP
        {
            // find out if a turntable has stalled
            for(int i = 0; i < numTurntables; i++)
            {
                if(tables[i].stall == true)
                {
                    stall = true;
                }
            }

            // reset gifts in sacks ready for calculation
            giftsInSacks = 0;
            for (int i = 0; i < numSacks; i++)
            {
                giftsInSacks += sacks[i].getCapacity(); // calculate number of gifts placed into all sacks
            }

        }






        // Post Thread Termination Output
        missing = totalGifts - giftsInSacks - giftsInHoppers;
        System.out.println("\n\nAfter time stopped a total of " + totalGifts + " presents had been placed onto the hopper, ");
        System.out.print(giftsInHoppers + " are still in the hoppers, and ");
        System.out.println(giftsInSacks + " made it into the sacks");
        System.out.println(missing + " gifts went missing.");


        for (int k = 0; k < numTurntables; k++)
        {
            tables[k].terminate();


        }


            //// Display presents from all locations
            //for (int i = 0; i < numHoppers; i++)
            //{
            //    System.out.println("Presents in hopper " + hoppers[i].id);
            //    for(int j = 0; j < hoppers[i].collection.length;j++)
            //    {
            //        if(hoppers[i].collection[j] != null)
            //        {
            //            System.out.println("Present "+ hoppers[i].collection[j].id);
//
            //        }
            //    }
            //}
//
            //for (int i = 0; i < numBelts; i++)
            //{
            //    System.out.println("Presents on belt " + belts[i].id);
            //    for(int j = 0; j < belts[i].presentsOnBelt.length;j++)
            //    {
            //        if(belts[i].presentsOnBelt[j] != null)
            //        {
            //            System.out.println("Present "+ belts[i].presentsOnBelt[j].id);
//
            //        }
            //    }
            //}
//
            //for (int i = 0; i < numTurntables; i++)
            //{
            //    System.out.println("Presents in turntable " + tables[i].id);
            //    for(int j = 0; j < 1;j++)
            //    {
            //        if(tables[i].present != null)
            //        {
            //            System.out.println("Present "+ tables[i].present.id);
//
            //        }
            //    }
            //}
//

        // output sack totals
            for (int i = 0; i < numSacks; i++)
            {
                //System.out.println("Presents in sack " + sacks[i].id);
                for(int j = 0; j < sacks[i].accumulation.length;j++)
                {
                    if(sacks[i].accumulation[j] != null)
                    {
                        //System.out.println("Present "+ sacks[i].accumulation[j].id + "----" + sacks[i].accumulation[j].ageGroup);

                    }

                }
                System.out.println("Total Presents in Sack "+ sacks[i].id + " - " + sacks[i].getCapacity()+ "/" + sacks[i].accumulation.length);
            }
            long newEndTime = System.currentTimeMillis();
            long addedTime = newEndTime - endTime;
            //long addedTime = (endTime - startTime - endTime) / 1000;
            System.out.println("Additional time added " + addedTime / 1000  + "s.");
            System.out.println("Machine Shut down after " + (newEndTime - startTime) / 1000 + "s.");
        }


}

