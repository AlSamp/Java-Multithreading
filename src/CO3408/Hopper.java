package CO3408;
/**
 *
 * @author Nick
 */
public class Hopper extends Thread
{
    static int presentIdCounter = 0;
    int id;

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
                        Thread.sleep(speed * 1000); // place present at set intervals
                        if (belt.isFull() == true)
                        {

                            //System.out.println("Conveyor " + belt.id + " is full");
                            System.out.println("Belt " + belt.id + " full, going to attempt to add present after sleep");
                            --i; //** IMPORTANT ** - decrement i to ensure that the present is not skipped



                        } else if(belt.isFull() == false && collection[i] != null)
                        {
                            // try aquire item mutex of conveyor belt
                            System.out.println("Hopper " + id + " ready to add present to belt " + belt.id);
                            belt.insertPresentOnToBelt(collection[i]); // add present to conveyor belt
                            collection[i] = null; // remove present from hopper

                        }

                    } catch (InterruptedException e) {
                        System.out.println("Hopper " + id + " interrupted");
                    }

                }
            }

    }

    public void terminate()
    {
        System.out.println("Hopper " + id +" Thread terminated");
        running = false;
    }

    
    // TODO Add more methods?
}
