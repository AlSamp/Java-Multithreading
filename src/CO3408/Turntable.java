package CO3408;
import java.util.HashMap;
import java.util.*;

/**
 *
 * @author Nick
 */
public class Turntable extends Thread
{
    String id;

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
        id = ID;

        System.out.println("Turntable Constructor called - id = " + id + " created");
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
        //System.out.println("Turntable output map = "+ outputMap);
        //System.out.println("Turntable destination map = "+ destinations);
    }
    
    public void run()
    {
        // TODO
        // if collection is not empty

            System.out.println("What is this ************************************ :" + connections[0].belt.id);
            try
            {
                System.out.println( "Connection type:" + connections[2].connType);
                System.out.println("Turntable Thread " + id + " sleeping");
                Thread.sleep(3000);
                System.out.println("Turntable Thread " + id + " awake");

                System.out.println("******************");
                //connections[0].sack.id = 9001;
                //System.out.println(connections[0].sack);
                //connections[0].sack.id = 0;

                // check belt capacity
                while(connections[0].belt.capacity != 0)
                {

                    connections[0].belt.presentToSack(connections[2].sack);
                    System.out.println("Turntable Thread " + id + " sleeping after executing presentToSack");
                    Thread.sleep(1000);
                }

            }
            catch (InterruptedException e)
            {
                System.out.println("Turntable " + id + " interrupted");
            }







    }
}
