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
    public Semaphore items;
    private Semaphore spaces;
    int id;
    public Present[] presentsOnBelt; // The requirements say this must be a fixed size array
    public  HashSet<Integer> destinations = new HashSet();

    int capacity;
    
    // TODO - add more members?
    
    public Conveyor(int id, int capacity)
    {
        this.capacity = 0;
        this.id = id;
        presentsOnBelt = new Present[capacity];
        System.out.println("Conveyor " + id + " capacity = " + capacity);

        // initialise the number of spaces available for production
        spaces = new Semaphore(capacity);
        // initialise the number of items available for consumption
        items = new Semaphore(0);

        System.out.println("Mutex Space Available Upon Construction  || "  + spaces.availablePermits());
    }
    public void addDestination(int sackDestination)
    {
        System.out.println("Adding sackID to destination " + sackDestination);
        destinations.add(sackDestination);
        System.out.println("sackID destinations : " + destinations);
    }

    public boolean isFull() // Returns true if the conveyor is full
    {
       // System.out.println(" Belt " +  id + " Capacity = " + capacity + "/" + presentsOnBelt.length);

        if (capacity == presentsOnBelt.length )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void insertPresentOnToBelt(Present presentFromHopper)
    {
            System.out.println("Mutex Space insertPresentOnToBelt  || "  + spaces.availablePermits());
            try
            {
                if (spaces.tryAcquire())
                {
                    // grab the lock on the number of spaces available
                    //spaces.acquire();
                    // grab the lock on the mutex
                    mutex.acquire();

                    //System.out.println("Mutex Space Available  || "  + spaces.availablePermits());

                    //if(capacity < presentsOnBelt.length)
                    if(isFull() == false)
                    {
                        System.out.println("Mutex Space Available  || "  + spaces.availablePermits());
                        //System.out.println("Belt " + id + " goes to grab Present " + presentFromHopper.getId() + " from Hopper " );
                        for(int i = (presentsOnBelt.length - 1); i >= 0; i--)
                        {
                            if( presentsOnBelt[i] == null) // search through presents on belt
                            {
                                presentsOnBelt[i] = presentFromHopper; // add present to the end of conveyor belt array
                                System.out.println("Present " + presentsOnBelt[i].id + " removed from hopper onto belt " + id);
                                capacity++;
                                break;
                            }

                        }

                    }
                    // release the lock on the number of available items
                    items.release();
                    // release the lock on the mutex
                    mutex.release();

                }
                else
                {

                    System.out.println("Mutex Space Not Available  || "  + spaces.availablePermits());

                   // System.out.println("Belt " + id + " sleeping. Unable to add present ______________________________________________________");
                    //isFull();
                   // Thread.sleep(1000);

                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

    public Present extractPresent()
    {
        Present extractedPresent = null;



        if(items.tryAcquire()) // try to get the semaphore
        {
            try
            {
                //System.out.println("Items on belt " + id + " = " + items.availablePermits());
                //if(items.tryAcquire()) // try to get the semaphore
                //{

                // grab the lock on the number of free slots
                //items.acquire();
                // grab the lock on the mutex
                mutex.acquire();
                //}
                for(int i = (presentsOnBelt.length - 1); i >= 0; i--) // starting from the end of the belt take a present if possible
                {
                    if(presentsOnBelt[i] != null) // once a present is found on the belt move it to the turntable
                    {

                        extractedPresent = presentsOnBelt[i];
                        System.out.println("Belt " + id + " sending present " + extractedPresent.id +  " to turntable");
                        System.out.println("Present " + extractedPresent.id + " removed from conveyor belt --------------------------" + extractedPresent.ageGroup);
                        presentsOnBelt[i] = null; // remove present from the conveyor belt
                        capacity--;
                        break; // break loop once a present is extracted
                    }
                }
                // release the lock on the number of available items
                spaces.release();
                // release the lock on the mutex
                mutex.release();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
        //displayPresents();
        return extractedPresent;

    }
    void displayPresents()
    {
        for(int i = presentsOnBelt.length - 1; i > 0; i --)
        {
            System.out.println("---------------------------------------------");
            System.out.println(presentsOnBelt[i].id);
            System.out.println(presentsOnBelt[i].ageGroup);
            System.out.println("---------------------------------------------");
        }
    }
}
