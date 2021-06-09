package Model;

import java.io.*;
import java.util.*;

public class IpTable {
    static HashMap<String, Boolean> siteList = new HashMap<>();

    //block IP addresses
    public void BlockIP(Site site){
        if(siteList.containsKey(site.getDomain()) && siteList.get(site.getDomain())){
            System.out.println("site already blocked");
            return;
        }
        String command;
        for (String ip :
                site.getIps()) {
            if(ip.length() > 16){
                command = "sudo ip6tables-legacy -I INPUT -s " + ip + " -j DROP";
            }else{
                command = "sudo iptables-legacy -I INPUT -s " + ip + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        siteList.put(site.getDomain(),true);
        Model.instance.getSiteHandler().addSite(site);
    }


    //unblock IP addresses
    public void unBlockIP(Site site){
        if(siteList.containsKey(site.getDomain()) && !siteList.get(site.getDomain())){
            System.out.println("Model.Site is not blocked");
            return;
        }
        String command;
        for (String ip :
                site.getIps()) {
            if(ip.length() > 16){
                command = "sudo ip6tables-legacy -D INPUT -s " + ip + " -j DROP";
            }else{
                command = "sudo iptables-legacy -D INPUT -s " + ip + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
            siteList.put(site.getDomain(),false);
        }

    }

    //read the IP address and the boolean value from "blocked.dat" file to the hashmap
    public void ReadFromFile(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("Error,"+fileName+" does not exists");
            return;
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            if(!scanner.hasNext()){
                System.out.println("File is empty");
                return;
            }
            while(scanner.hasNext()){
                String[] line = scanner.nextLine().split(",");
                if(line[1].equals("1")){
                    siteList.put(line[0],true);
                }else{
                    siteList.put(line[0],false);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //write the ip and the boolean value from the list to "output.dat"
    public void WriteToFile(String fileName) {
        if(siteList.isEmpty()){
            return;
        }
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("Error,"+fileName+" does not exists");
            return;
        }
        try {
            FileWriter fwrtr = new FileWriter(file,false); //if you want to open in "append" mode change to true
            Formatter frmtr = new Formatter(fwrtr);

            for (String siteName :
                    siteList.keySet()) {
                frmtr.format("%s,%c\n",siteName,siteList.get(siteName)? '1':'0');
            }

            frmtr.close();
            fwrtr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Site getSite(String name){
        if(siteList.containsKey(name)){
            return Model.instance.getSiteHandler().getSite(name);
        }
        return null;
    }

    public boolean siteExists(String name){
        return siteList.containsKey(name);
    }

    public Set<String> getSiteList(){
        return siteList.keySet();
    }

    public boolean isBlocked(String site){
        if(siteList.containsKey(site)){
            return siteList.get(site);
        }
        return false;
    }
}
