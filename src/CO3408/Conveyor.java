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
    private Semaphore items;
    private Semaphore spaces;
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

        // initialise the number of spaces available for production
        spaces = new Semaphore(capacity);
        // initialise the number of items available for consumption
        items = new Semaphore(0);
        
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
            // grab the lock on the number of spaces available
            spaces.acquire();
            // grab the lock on the mutex
            mutex.acquire();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        //if(capacity < presentsOnBelt.length)
        if(isFull() == false )
        {
            //System.out.println("Belt " + id + " goes to grab Present " + presentFromHopper.getId() + " from Hopper " );
            for(int i = (presentsOnBelt.length - 1); i >= 0; i--)
            {
                if( presentsOnBelt[i] == null) // search through presents on belt
                {

                    presentsOnBelt[i] = presentFromHopper; // add present to the end of conveyor belt array
                    System.out.println("Present " + presentsOnBelt[i].id + " removed from hopper onto belt -----------------------------------------");
                    capacity++;
                    //System.out.println("Present " + presentsOnBelt[i].getId() + " released on conveyor " + id);
                    break;
                }

            }
        }


        // release the lock on the number of available items
        items.release();
        // release the lock on the mutex
        mutex.release();


    }



    public void extractPresentToSack(Sack s)
    {
        try
        {
            // grab the lock on the number of free slots
            items.acquire();
            // grab the lock on the mutex
            mutex.acquire();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("Present to sack initiated");


        for(int i = (presentsOnBelt.length - 1); i >= 0; i--) // starting from the end of the belt take a present if possible
        {
            //System.out.println("Testing this out present to sack");


            if(presentsOnBelt[i] != null) // once a present is found on the belt move it to the sack
            {
                System.out.println("Sack "+ s.id + " is receiving present " + presentsOnBelt[i].id);
                // find available slot in sack


                //switch (presentsOnBelt[i].getId())
                //{
                //    case 1:
                //        for(int j = 0; j < (s.accumulation.length - 1); j++)
                //        {
                //            // TODO sacks 1,2,3,4
                //            if (s.accumulation[j] == null) // navigate sack until a slot is available
                //            {
                //                s.accumulation[j] = presentsOnBelt[i]; // add present to the sack
                //                presentsOnBelt[i] = null; // remove present from the conveyor belt
                //                System.out.println("Present " + s.accumulation[j].getId() + " added to sack " + s.id);
                //                System.out.println("Present " + s.accumulation[j].getId() + " successfully removed from conveyor " + id);
                //                System.out.println("Decrementing capacity");
                //                capacity--;
                //                break; // only consume from the buffer once so end the loop
                //            }
                //        }
                //        break;
                //    case 2:
//
                //        break;
                //    case 3:
                //        break;
                //    default:
                //        System.out.println("YEETING PRESENT " + presentsOnBelt[i].getId());
                //        break;
                //}

                for(int j = 0; j < (s.accumulation.length - 1); j++)
                {
                    if (s.accumulation[j] == null) // navigate sack until a slot is available
                    {
                        s.accumulation[j] = presentsOnBelt[i]; // add present to the sack
                        presentsOnBelt[i] = null; // remove present from the conveyor belt
                        System.out.println("Present " + s.accumulation[j].id + " added to sack " + s.id);
                        System.out.println("Present " + s.accumulation[j].id + " successfully removed from conveyor " + id);
                        capacity--;
                        break; // only consume from the buffer once so end the loop
                    }

                }

            }
        }

        // release the lock on the number of available items
        spaces.release();
        // release the lock on the mutex
        mutex.release();

    }

    public Present extractPresent()
    {
        Present extractedPresent = null;

        try
        {
            // grab the lock on the number of free slots
            items.acquire();
            // grab the lock on the mutex
            mutex.acquire();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for(int i = (presentsOnBelt.length - 1); i >= 0; i--) // starting from the end of the belt take a present if possible
        {

            if(presentsOnBelt[i] != null) // once a present is found on the belt move it to the turntable
            {

                extractedPresent = presentsOnBelt[i];
                System.out.println("Present " + extractedPresent.id + " removed from conveyor belt");
                presentsOnBelt[i] = null; // remove present from the conveyor belt
                capacity--;
                break; // break loop once a present is extracted
            }
        }


        // release the lock on the number of available items
        spaces.release();
        // release the lock on the mutex
        mutex.release();

        return extractedPresent;

    }

    // TODO - add more functions
    
}
