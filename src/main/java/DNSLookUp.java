import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import Model.*;


public class DNSLookUp {

    public static ArrayList<String> lookup(Site s) {
        ArrayList<String> output = new ArrayList<>();
        InetAddress[] myHost;

        // Print the IP addresses
        try {
            myHost = InetAddress.getAllByName(s.getDomain());
            for (InetAddress ad:
                    myHost) {
                output.add(ad.getHostAddress());
            }
        } catch (UnknownHostException e) {
            System.out.println("Address wrongly written or it does not exist " + s.getDomain());
            return null;
        }

        s.addMultipleIp(output);
        return output;
    }
}
