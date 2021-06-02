package Model;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class Location {
    HashMap<String,String> dCountries=new HashMap<String,String>(); // Dangrous Countries based on location
    public static void getLocation(String IP)
    {
        String dblocation ="GeoLite2-City.mmdb";
        File DataBase=new File(dblocation);
        try {
            DatabaseReader dbr=new DatabaseReader.Builder(DataBase).build();
            InetAddress addr=InetAddress.getByName(IP);
            CityResponse response=dbr.city(addr);
            /*CityResponse response2=dbr.city(usr.getIpAddr());
            System.out.println("Users Country:"+response2.getCountry().getName());
            */
            System.out.println("Users Country : "+response.getCountry().getName());
            System.out.println("Users City : "+response.getCity().getName());
            System.out.println("Postal code : "+ response.getPostal().getCode());
            System.out.println("Continent : "+ response.getContinent().getName());
        } catch (IOException e) {
            System.out.println("failed to create db reader");
        } catch (GeoIp2Exception e) {
            System.out.println("failed to use GeoIP ");
        }
    }
  public static void main(String[] args) {
      Location test = new Location();
      test.getLocation("5.181.234.220");
  }
}
