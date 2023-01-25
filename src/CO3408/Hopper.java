package CO3408;
/**
 *
 * @author Nick
 */
public class Hopper extends Thread
{
    static public int presentIdCounter = 0;    // allows for tracking of presents

    static public int depositCounter = 0; // track how many present have been deposited across all hoppers
    int id;

    long timeWaiting = 0;
    Conveyor belt;
    int speed;

    boolean running;
    Present[] collection;
    
    public Hopper(int id, Conveyor con, int capacity, int speed)
    {
        running = true;
        collection = new Present[capacity];
        this.id = id;
        belt = con;
        this.speed = speed;
    }
    
    public void fill(Present p)
    {
        // TODO
        presentIdCounter++;
        // Add present to collection
        p.id = presentIdCounter; // add id to present

        System.out.println("Hopper " + id + " adding present '" + p.id + "' to collection");
       // System.out.println("Present " + p.getId() +  " age group = " + p.getAgeGroup());

        // TODO fill into collection individually

        for(int i = 0; i < (collection.length); i++)
        {
            if(collection[i] == null)
            {
                collection[i] = p;
                break;
            }
        }

    }

    public int getCapacity()
    {
        int capacity = 0;
        for(int i = 0; i < collection.length; i++)
        {
            if(collection[i] != null)
            {
                capacity++;
            }
        }

        return capacity;
    }

    public void run()
    {
        while(running == true)
             {
                for (int i = 0; i < (collection.length); i++) {

                    try {
                       //y System.out.println("i = " + i );

                        if (belt.spaces.tryAcquire() && running == true)
                        {
                            // try aquire space mutex of conveyor belt
                            System.out.println("Hopper " + id + " ready to add present to belt " + belt.id);
                            belt.insertPresentOnToBelt(collection[i]); // add present to conveyor belt
                            Thread.sleep(1000 / speed); // place present at set intervals
                            collection[i] = null; // remove present from hopper
                            //depositCounter++;// track how many present have been deposited
                            ////System.out.println("Conveyor " + belt.id + " is full");
                            //System.out.println("Belt " + belt.id + " full, going to attempt to add present after sleep");
                            //--i; //** IMPORTANT ** - decrement i to ensure that the present is not skipped



                        }
                        //else if(belt.isFull() == false && collection[i] != null)
                        else
                        {
                            //// try aquire item mutex of conveyor belt
                            //System.out.println("Hopper " + id + " ready to add present to belt " + belt.id);
                            //belt.insertPresentOnToBelt(collection[i]); // add present to conveyor belt
                            //collection[i] = null; // remove present from hopper

                            //System.out.println("Conveyor " + belt.id + " is full");
                            if(running == false)
                            {
                                break; // break loop and allow thread to fully terminate
                            }
                            else
                            {
                                System.out.println("Hopper " + id + ": Belt " + belt.id + " full, going to attempt to add present after sleep");
                                --i; //** IMPORTANT ** - decrement i to ensure that the present is not skipped
                                Thread.sleep(1000);
                                timeWaiting+= 1000;
                                //depositCounter--;
                            }
                        }

                    } catch (InterruptedException e) {
                        System.out.println("Hopper " + id + " interrupted");
                        running = false;
                        return;
                    }

                }
            }

    }

    public void terminate()
    {
        System.out.println("Hopper " + id +" Thread terminated");
        running = false;
    }

}
