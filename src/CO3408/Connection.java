package CO3408;
/**
 *
 * @author npmitchell
 */
public class Connection {
    ConnectionType connType;
    Conveyor belt;
    Sack sack;
    
    public Connection(ConnectionType ct, Conveyor c, Sack s)
    {
        connType = ct;
        belt = c;
        sack = s;

        //System.out.println("Connection Constructor called - connection type = " + connType + "\n sack id = " + sack.id + "\n belt id = " + belt.id);

    }
}
