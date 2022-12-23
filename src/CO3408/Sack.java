package CO3408;
/**
 *
 * @author Nick
 */
public class Sack
{
    int id;
    Present[] accumulation;
    
    public Sack(int id, int capacity)
    {
        accumulation = new Present[capacity];
        this.id = id;

        System.out.println("Sack Constructor called - sack " + id + " created");
    }

    //TODO - Add more methods

    public int getCapcity()
    {
        int capacity = 0;
        for(int i = 0; i < (accumulation.length - 1); i++)
        {
            if(accumulation[i] != null)
            {
                capacity++;
            }
        }

        return capacity;
    }
    
}
