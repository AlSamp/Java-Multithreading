package CO3408;
/**
 *
 * @author Nick
 */
public class Present
{
    String ageGroup;
    int id;



    public Present(String destination)
    {
        ageGroup = destination;
    }

    public String getAgeGroup()
    {
        return ageGroup;
    }

    public int getId()
    {
        return id;
    }

    public String readDestination()
    {
        return ageGroup;
    }
    
}
