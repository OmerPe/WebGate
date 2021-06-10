package Model;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class AiPrep {
    static HashMap<String, ArrayList<String>> sMap = new HashMap<>();

    public static ArrayList<String> prep(Site cURL) {
        if (sMap.containsKey(cURL)) {
            System.out.println("The wanted url already exists in the data set");
            return sMap.get(cURL);
        }
        ArrayList<String> answer = new ArrayList<>(); //url,ip,country,protocol.
        answer.add(cURL.getDomain());////acquiring the URL of the site
        answer.add(lookup(cURL).get(0));//acquiring the ip address of the current URL
        answer.add(getLocation(cURL.getIps().get(0)));// acquiring the location of the wanted URL
        String tmp = cURL.getDomain();
        try {
            URL tmp2 = new URL(cURL.getDomain());
            answer.add(tmp2.getProtocol());
        } catch (MalformedURLException e) {
            System.out.println("no protocol was given ");
        }
        sMap.put(cURL.getDomain(), answer);//saving the data set to the hashmap for further use
        System.out.println(answer);
        return answer;
    }

    HashMap<String, String> dCountries = new HashMap<String, String>(); // Dangerous Countries based on location

    public static String getLocation(String IP) {
        String dblocation = "GeoLite2-City.mmdb";
        File DataBase = new File(dblocation);
        try {
            DatabaseReader dbr = new DatabaseReader.Builder(DataBase).build();
            InetAddress addr = InetAddress.getByName(IP);
            CityResponse response = dbr.city(addr);
            System.out.println("Users Country : " + response.getCountry().getName());
            return response.getCountry().getName();
        } catch (IOException e) {
            System.out.println("failed to create db reader");
        } catch (GeoIp2Exception e) {
            System.out.println("failed to use GeoIP ");
        }
        return "Location Unknown";
    }

    public static ArrayList<String> lookup(Site s) {
        ArrayList<String> output = new ArrayList<>();
        InetAddress[] myHost;
        String test = SiteCheck(s);
        // Print the IP addresses
        try {
            myHost = InetAddress.getAllByName(test);
            for (InetAddress ad :
                    myHost) {
                output.add(ad.getHostAddress());
            }
        } catch (UnknownHostException e) {
            System.out.println("Address wrongly written or it does not exist " + s.getDomain());
            return null;
        }

        s.addMultipleIp(output);
        System.out.println(output);
        return output;
    }

    public static String SiteCheck(Site s) {
        int size = s.getDomain().length();
        String tmp = s.getDomain().substring(0, 5);
        if (tmp.equals("http:")) {
            String result = s.getDomain().substring(11, size);
            return result;
        } else if (tmp.equals("https")) {
            String result = s.getDomain().substring(12, size);
            return result;
        }
        return s.getDomain();
    }

    /*public static void main(String[] args) {
        Site tmp = new Site("https://www.mapleroyals.com");
        lookup(tmp);
        prep(tmp);
    }*/

}
