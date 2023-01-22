package CO3408;
/**
 *
 * @author Nick
 */
public class Present
{
    String ageGroup;
    int id;

    int insertId;



    public Present(String destination)
    {
        insertId = 0;
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

    public String getDestination()
    {
        return ageGroup;
    }
    
}
