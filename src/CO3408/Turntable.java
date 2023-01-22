package CO3408;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nick
 */


public class Turntable extends Thread
{

    private Semaphore mutex = new Semaphore(1);
    private Semaphore items;
    private Semaphore spaces;
    private final int MAX_INPUT_BELTS = 2;
    final int ROTATION_TIME = 500;
    final int MOVE_PRESENT_TIME = 750;

    boolean stall;
    String id;

    Present present;
    int pos;
    boolean running;
    static int N = 0;
    static int E = 1;
    static int S = 2;
    static int W = 3;

    // input belt connection ect
    Connection[] connections = new Connection[4];
        
    // global lookup: age-range -> SackID
    static HashMap<String, Integer> destinations = new HashMap<>();
    
    // this individual table's lookup: SackID -> output port
    HashMap<Integer, Integer> outputMap = new HashMap<>();
    
    public Turntable (String ID)
    {
        pos = 0;
        running = true;
        id = ID;
        System.out.println("Turntable Constructor called - id = " + id + " created");
        stall = false;

        // initialise the number of spaces available for production
        spaces = new Semaphore(1);
        // initialise the number of items available for consumption
        items = new Semaphore(0);
    }

    
    public void addConnection(int port, Connection conn)
    {
        connections[port] = conn;
        System.out.println("Turntable connection type = " + conn.connType);
        if(conn != null)
        {
            if(conn.connType == ConnectionType.OutputBelt)
            {
                Iterator<Integer> it = conn.belt.destinations.iterator();
                while(it.hasNext())
                {
                    outputMap.put(it.next(), port);

                }
            }
            else if(conn.connType == ConnectionType.OutputSack)
            {
                outputMap.put(conn.sack.id, port);

            }
        }
        System.out.println("Turntable " + id + " output map = "+ outputMap);
        System.out.println("Turntable " + id + " destination map = "+ destinations);
    }
    
    public void run()
    {
        // TODO
        // if collection is not empty


            //try
            //{
                // Establish number of input belts for turntables
                int[] inputBelt = new int[MAX_INPUT_BELTS]; // turntables have a max of 2 input belts
                int ib = 0;

                System.out.println("Input belt setup for turntable  " + id);
                for(int i = 0; i < connections.length; i++)
                {

                    try
                    {
                        if(connections[i].connType == ConnectionType.InputBelt)
                        {
                            inputBelt[ib] = i;
                            ib++;
                        }
                    }
                    catch(Exception ignored)
                    {
                        System.out.println("No additional input belt found for turntable " + id);

                    }

                }
                for(int i = 0; i < inputBelt.length ; i++)
                {
                    System.out.println("Turntable " + id + " input belt at connection port " + inputBelt[i]);

                }
                while(running == true)
                {
                    // TODO working on the connections

                    // Establish input belts of the turntable.
                    for(int i = 0; i < inputBelt.length; i++)
                    {
                        //try
                        //{
                            //connections[inputBelt[i]].belt.extractPresentToSack(connections[2].sack);#
                            if(stall == false)
                            {
                                present = connections[inputBelt[i]].belt.extractPresent();
                            }



                            if(present != null)
                            {
                                //System.out.println("----------------Present: " + present.id +  "- ageGroup : "+ present.ageGroup + " being sent to turntable " + id + " extracted from port " + inputBelt[i] + "----------------");
                                sortPresent();
                                //System.out.println("Turntable Thread " + id + " sleeping after successfully executing presentToSack");
                                //Thread.sleep(1000);
                            }else {
                                //i--;
                                //System.out.println("Turntable Thread " + id + " sleeping after present was null");
                                //Thread.sleep(1000);
                            }
                        //}
                        //catch (InterruptedException e)
                        //{
                        //    i--;
                        //    System.out.println("Error within thread run");
                        //}
                    }
                    //connections[0].belt.extractPresentToSack(connections[2].sack);
                    //System.out.println("Turntable Thread " + id + " sleeping after executing presentToSack");
                    //Thread.sleep(1000);
                }

            //}
            //catch (InterruptedException e)
            //{
            //    System.out.println("Turntable " + id + " interrupted");
            //}
    }


    public void sortPresent()
    {

            //try {
                if (mutex.tryAcquire())
                {
                    System.out.println("Turntable " + id + " sortPresent() starting with present " + present.id + " age group = " + present.ageGroup);
                    //mutex.acquire();

                    for (Map.Entry<String, Integer> entry : destinations.entrySet())  // iterate through the destinations map to find the sack for the correct age
                    {
                        if (present == null) {
                            break;
                        }

                        if (entry.getKey().equals(present.ageGroup)) // hatred
                        {
                            System.out.println( "Turntable " + id + " Match confirmed for present " + present.id +" || " + entry.getKey() + " - " + present.ageGroup);
                            //output to port getValue
                            for (int i = 0; i < connections.length; i++)
                            {
                                if (present == null)
                                {
                                    break;
                                }
                                try// don't break if side is null
                                {
                                    if (connections[i].connType == ConnectionType.OutputSack) // if an output sack is connected
                                    {
                                        //if(connections[i].sack.id == entry.getValue()) // if it is the correct sack
                                        //System.out.println("Current presents in sack " + connections[i].sack.id + " : " + connections[i].sack.getCapacity() + "/" + connections[i].sack.accumulation.length);


                                            if (entry.getValue().equals(connections[i].sack.id))
                                            // now place present into sack
                                            {
                                                System.out.println("Turntable " + id + " Target sack found. Proceeding with sack " + connections[i].sack.id);
                                                // prepare to turn if alignment is off
                                                if (pos % 2 == 0) {
                                                    pos = i;
                                                } else {
                                                    pos = i;
                                                    try {
                                                        System.out.println("Turntable " + id + " Turntable " + id + " rotating to align with Sack " + connections[i].sack.id);
                                                        Thread.sleep(ROTATION_TIME);
                                                    } catch (InterruptedException e) {
                                                        System.out.println("Turntable " + id + " interrupted");
                                                    }
                                                }

                                                if(connections[i].sack.getCapacity() != connections[i].sack.accumulation.length) // if there is space available in the sack
                                                {
                                                    System.out.println("**Current presents in sack " + connections[i].sack.id + " : " + connections[i].sack.getCapacity() + "/" + connections[i].sack.accumulation.length);
                                                    for (int j = 0; j < (connections[i].sack.accumulation.length); j++) // iterate through target sack
                                                    {
                                                        if (connections[i].sack.accumulation[j] == null) // find an empty slot in the sack
                                                        {
                                                            // place present in sack

                                                            try {
                                                                System.out.println("Turntable " + id + " moving present " + present.id + " to Sack " + connections[i].sack.id);
                                                                Thread.sleep(MOVE_PRESENT_TIME);
                                                                connections[i].sack.accumulation[j] = present;
                                                                present = null; // remove present from turntable memory
                                                                System.out.println("Present placed into sack " + connections[i].sack.id + " : " + connections[i].sack.getCapacity() + "/" + connections[i].sack.accumulation.length);



                                                                break; // end loop once present has been placed


                                                            } catch (InterruptedException e) {
                                                                System.out.println("Turntable " + id + " interrupted");
                                                            }
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    System.out.println("ERROR CANNOT ADD PRESENT TO SACK " + connections[i].sack.id + " : " + connections[i].sack.getCapacity() + "/" + connections[i].sack.accumulation.length);
                                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    System.out.println("TURNTABLE " + id + " TERMINATED DUE TO SACK " + connections[i].sack.id + " BEING FULL CANNOT ADD PRESENT. MACHINE HAS STALLED!!!");
                                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                                                    stall = true;

                                                    //terminate();
                                                }



                                            }



                                    }// send present to belt
                                    else if (connections[i].connType == ConnectionType.OutputBelt) // if an output belt is connected)
                                    {
                                        //if(connections[i].sack.id == entry.getValue()) // if it is the correct sack

                                        // check if a present can be placed on the belt
                                        if(connections[i].belt.isFull() == false)
                                        {
                                            for (Integer element : connections[i].belt.destinations) {

                                                if (element.equals(entry.getValue())) // if the present destination is same as the belt destinations
                                                {
                                                    System.out.println("Turntable " + id + " Belt destination : " + element + " compared with present destination " + entry.getValue());
                                                }


                                                if (entry.getValue().equals(element))
                                                // now place present into belt
                                                {
                                                    System.out.println("Turntable " + id + " Target belt found. Sending present " + present.id + " to belt " + connections[i].belt.id + " at port " + i);
                                                    // prepare to turn if alignment is off
                                                    if (pos % 2 == 0) {
                                                        pos = i;
                                                    } else {
                                                        pos = i;
                                                        try {
                                                            System.out.println("Turntable " + id + " rotating to align with belt " + connections[i].belt.id);
                                                            Thread.sleep(ROTATION_TIME);
                                                        } catch (InterruptedException e) {
                                                            System.out.println("Turntable " + id + " interrupted");
                                                        }
                                                    }

                                                    for (int j = 0; j < connections[i].belt.presentsOnBelt.length ; j++) // iterate through target belt
                                                    {
                                                        if (connections[i].belt.presentsOnBelt[j] == null) // find an empty slot on the belt
                                                        {
                                                            // place present in belt

                                                            try {
                                                                System.out.println("Turntable " + id + " moving present " + present.id + "  to belt " + connections[i].belt.id + " via port " + i);
                                                                Thread.sleep(MOVE_PRESENT_TIME);
                                                                connections[i].belt.presentsOnBelt[j] = present;
                                                                present = null; // remove present from turntable memory
                                                                connections[i].belt.items.release();
                                                                connections[i].belt.capacity++;
                                                                //items.release(); // release an item onto the belt

                                                                break; // end loop once present has been placed


                                                            } catch (InterruptedException e) {
                                                                System.out.println("Turntable " + id + " interrupted");
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        else
                                        {
                                            //System.out.println("Turntable " + id + " sleeping as belt " + connections[i].belt.id + " is full");
                                            i--; // repeat until possible to place
                                            //Thread.sleep(1000);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }
                    if(stall == false)
                    {
                        mutex.release();
                    }

                } else {
                    //System.out.println("Turntable " + id + " sleeping");
                    //System.out.println("---------------------------------------------------------- Mutex availability = " + mutex.availablePermits());
                    //Thread.sleep(1000);
                }

            //} catch (InterruptedException e) {
            //    System.out.println("Problem with present to sack!!!");
            //}

    }

    public void terminate()
    {
        System.out.println("Turntable " + id +" Thread terminated");
        running = false;
    }
}
