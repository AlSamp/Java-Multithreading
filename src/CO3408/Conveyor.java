package CO3408;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nick
 */
public class Conveyor
{

    private Semaphore mutex = new Semaphore(1);
    private Semaphore numAvail;
    private Semaphore numFree;
    int id;
    private Present[] presentsOnBelt; // The requirements say this must be a fixed size array
    public  HashSet<Integer> destinations = new HashSet();

    int capacity;
    
    // TODO - add more members?
    
    public Conveyor(int id, int capacity)
    {
        this.capacity = 0;
        this.id = id;
        presentsOnBelt = new Present[capacity];
        System.out.println("Conveyor " + id + " capacity = " + capacity);

        // initialise the number of free slots
        numFree = new Semaphore(capacity);
        // initialise the number of available items to 0 as there are no items available able at start.
        numAvail = new Semaphore(0);
        
        //TODO - more construction likely!
    }
    public void addDestination(int sackDestination)
    {
        System.out.println("Adding sackID to destination " + sackDestination);
        destinations.add(sackDestination);
        System.out.println("sackID destinations : " + destinations);
    }

    public boolean isFull() // Returns true if the conveyor is full
    {

        if (capacity == presentsOnBelt.length )
        {

            System.out.println("Capacity = " + capacity + "/" + presentsOnBelt.length);
            return true;
        }
        else
        {
            System.out.println("Capacity = " + capacity + "/" + presentsOnBelt.length);
            return false;
        }

    }
    public void insertPresentOnToBelt(Present presentFromHopper)
    {

        try
        {
            // grab the lock on the number of free slots
            numFree.acquire();
            // grab the lock on the mutex
            mutex.acquire();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if(capacity < presentsOnBelt.length)
        {
            //System.out.println("Belt " + id + " goes to grab Present " + presentFromHopper.getId() + " from Hopper " );
            for(int i = (presentsOnBelt.length - 1); i >= 0; i--)
            {
                if( presentsOnBelt[i] == null) // search through presents on belt
                {
                    presentsOnBelt[i] = presentFromHopper; // add present to the end of conveyor belt array
                    capacity++;
                    //System.out.println("Present " + presentsOnBelt[i].getId() + " released on conveyor " + id);
                    break;
                }

            }
        }

        // release the lock on the number of available items
        numAvail.release();
        // release the lock on the mutex
        mutex.release();


    }



    public void presentToSack(Sack s)
    {
        try
        {
            // grab the lock on the number of free slots
            numAvail.acquire();
            // grab the lock on the mutex
            mutex.acquire();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("Present to sack initiated");
        //Present present = presents[presents.length -1];
        // grab/remove present from belt

        //store present on turn table

        //release mutex

        // sleep for processing simulation
        //System.out.println("Turntable sending present to sack " + s.id);
        //place present in sack

        //for(int i = 0; i < (presentsOnBelt.length - 1); i++)
        for(int i = (presentsOnBelt.length - 1); i >= 0; i--)
        {
            //System.out.println("Testing this out present to sack");


            if(presentsOnBelt[i] != null)
            {
                System.out.println("Sack "+ s.id + " is receiving present " + presentsOnBelt[i].getId());
                // find available slot in sack

                for(int j = 0; j < (s.accumulation.length - 1); j++)
                {
                    if (s.accumulation[j] == null)
                    {
                        s.accumulation[j] = presentsOnBelt[i]; // add present to the sack
                        presentsOnBelt[i] = null; // remove present from the conveyor belt
                        System.out.println("Present " + s.accumulation[j].getId() + " added to sack " + s.id);
                        System.out.println("Present " + s.accumulation[j].getId() + " successfully removed from conveyor " + id);
                        capacity--;
                        break;
                    }
                }




            }
        }

        // release the lock on the number of available items
        numFree.release();
        // release the lock on the mutex
        mutex.release();

    }

    // TODO - add more functions
    
}
