import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import Model.*;


public class DNSLookUp {

    public static ArrayList<String> lookup(Site s) {
        ArrayList<String> output = new ArrayList<>();
        InetAddress[] myHost;
        String test=SiteCheck(s);
        // Print the IP addresses
        try {
            myHost = InetAddress.getAllByName(test);
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
    public static String SiteCheck(Site s)
    {
        int size =s.getDomain().length();
        String tmp=s.getDomain().substring(0,6);
        if(tmp.equals("http:"))
        {
            String result=s.getDomain().substring(11,size);
            System.out.println(result);
            return result;
        }
        else if(tmp.equals("https"))
        {
            String result=s.getDomain().substring(12,size);
            System.out.println(result);
            return result;
        }
        return s.getDomain();
    }
}
