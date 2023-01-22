package CO3408;
/**
 *
 * @author Nick
 */
public class Sack
{
    int id;
    String age;
    Present[] accumulation;
    
    public Sack(int id, int capacity)
    {
        accumulation = new Present[capacity];
        this.id = id;

        System.out.println("Sack Constructor called - sack " + id + " created \n" + "age group = " + age );
    }

    //TODO - Add more methods

    public int getCapacity()
    {
        int capacity = 0;
        for(int i = 0; i < (accumulation.length ); i++)
        {
            if(accumulation[i] != null)
            {
                capacity++;
            }
        }

        return capacity;
    }
    
}
