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

            System.out.println("Belt ID ************************************ :" + connections[0].belt.id);
            try
            {



                //for (Map.Entry<Integer, Integer> entry : outputMap.entrySet()) {
                //    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                //}
                System.out.println("Turntable Thread " + id + " sleeping");
                Thread.sleep(4000);
                System.out.println("Turntable Thread " + id + " awake");

                System.out.println("******************");


                // check belt capacity
                //while(connections[0].belt.capacity != 0)
                //while(connections[2].sack.getCapacity() != 10) // while the sack is not full

                // Establish number of input belts for turntables
                int[] inputBelt = new int[MAX_INPUT_BELTS]; // turntables have a max of 2 input belts
                int ib = 0;

                System.out.println("Input belt setup for turntable  " + id);
                //for(int i = 0; i < (connections.length -1); i++)
                //{
//
                //    try
                //    {
                //        if(connections[i].connType == ConnectionType.InputBelt)
                //        {
                //            inputBelt[ib] = i;
                //            ib++;
                //        }
                //    }
                //    catch(Exception ignored)
                //    {
                //        System.out.println("No additional input belt found for turntable " + id);
//
                //    }
//
                //}




                while(running == true)
                {
                    // TODO working on the connections

                    // Establish input belts of the turntable.

                    // 0-3 || 4-6 || 7-10
                    //string destination =

                    for(int i = 0; i < (inputBelt.length - 1); i++)
                    {
                        try
                        {
                            //connections[inputBelt[i]].belt.extractPresentToSack(connections[2].sack);
                            present = connections[inputBelt[i]].belt.extractPresent();

                            if(present != null)
                            {
                                presentToSack(present);
                            }else {
                                //i--;
                            }

                            //System.out.println("Turntable Thread " + id + " sleeping after executing presentToSack");
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            i--;
                            System.out.println("Error within thread run");
                        }


                    }


                    //connections[0].belt.extractPresentToSack(connections[2].sack);
                    //System.out.println("Turntable Thread " + id + " sleeping after executing presentToSack");
                    //Thread.sleep(1000);
                }

            }
            catch (InterruptedException e)
            {
                System.out.println("Turntable " + id + " interrupted");
            }
    }


    public void presentToSack(Present p)
    {

        try
        {
            mutex.acquire();
        }catch(InterruptedException e)
        {
            System.out.println("Problem with present to sack!!!");
        }


        for (Map.Entry<String, Integer> entry : destinations.entrySet())  // iterate through the destinations map to find the sack for the correct age
        {
            if(p == null)
            {
                break;
            }

            if(entry.getKey().equals(p.ageGroup)) // hatred
            {
                System.out.println(entry.getKey() + "-" + p.ageGroup);

                if(p.ageGroup == "7-10")
                {
                    System.out.println("Testing this");
                }

                    //output to port getValue
                    for(int i = 0; i < connections.length;i++)
                    {
                        if(p == null)
                        {
                            break;
                        }
                        try// don't break if side is null
                        {
                            if (connections[i].connType == ConnectionType.OutputSack) // if an output sack is connected
                            {
                                //if(connections[i].sack.id == entry.getValue()) // if it is the correct sack
                                if (entry.getValue().equals(connections[i].sack.id))
                                // now place present into sack
                                {
                                    System.out.println("Target sack found. Proceeding with sack " + connections[i].sack.id);
                                    // prepare to turn if alignment is off
                                    if (pos % 2 == 0) {
                                        pos = i;
                                    } else {
                                        pos = i;
                                        try {
                                            System.out.println("Turntable " + id + " rotating to align with Sack " + connections[i].sack.id);
                                            Thread.sleep(ROTATION_TIME);
                                        } catch (InterruptedException e) {
                                            System.out.println("Turntable " + id + " interrupted");
                                        }
                                    }

                                    for (int j = 0; j < (connections[i].sack.accumulation.length - 1); j++) // iterate through target sack
                                    {
                                        if (connections[i].sack.accumulation[j] == null) // find an empty slot in the sack
                                        {
                                            // place present in sack

                                            try {
                                                System.out.println("Turntable " + id + " moving present " + p.id + "  to Sack " + connections[i].sack.id);
                                                Thread.sleep(MOVE_PRESENT_TIME);
                                                connections[i].sack.accumulation[j] = p;
                                                p = null; // remove present from turntable memory
                                                break; // end loop once present has been placed


                                            } catch (InterruptedException e) {
                                                System.out.println("Turntable " + id + " interrupted");
                                            }


                                        }

                                    }

                                }
                            }
                            // }
                            // catch(Exception e)
                            // {
                            //     //System.out.println(e);
                            // }
                            //break; // end loop once present has been placed
                        }
                         catch(Exception e)
                         {
                             //System.out.println(e);
                         }
                    }


            }


        }
        mutex.release();

    }

    public void presentToBelt(Present p)
    {

    }


    public void terminate()
    {
        System.out.println("Thread Termination called");
        running = false;
    }
}
