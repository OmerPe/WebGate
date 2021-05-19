package Model;

import java.io.*;
import java.util.*;

public class IpTable {
    static HashMap<Site, Boolean> siteList = new HashMap<>();

    //block IP addresses
    public void BlockIP(Site site){
        if(siteList.containsKey(site) && siteList.get(site)){
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
        siteList.put(site,true);
    }


    //unblock IP addresses
    public void unBlockIP(Site site){
        if(siteList.containsKey(site) && !siteList.get(site)){
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
            siteList.put(site,false);
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
            String line = scanner.nextLine();
            int size = Integer.parseInt(line);
            Site site;
            for(int i=0;i<size;i++){
                site = Site.readSiteFromFile(scanner);
                line = scanner.nextLine();
                if(line.equals("1")){
                    siteList.put(site,true);
                }else{
                    siteList.put(site,false);
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

            frmtr.format("%d\n",siteList.size()); //write size
            for (Site site :
                    siteList.keySet()) {
                frmtr.format("%s%c\n",site.getFileString(),siteList.get(site)? '1':'0');
            }

            frmtr.close();
            fwrtr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Site getSite(String name){
        for (Site site :
                siteList.keySet()) {
            if(name.equals(site.getDomain())){
                return site;
            }
        }
        return null;
    }

    public void printSiteList(){//for DeBugging
        for (Site site :
                siteList.keySet()) {
            System.out.println(site.getDomain());
            site.getIps().forEach(System.out::println);
        }
    }

    public boolean siteExists(String name){

        for (Site site :
                siteList.keySet()) {
            if(site.getDomain().equals(name)){
                return true;
            }
        }

        return false;
    }

    public Set<Site> getSiteList(){
        return siteList.keySet();
    }

    public boolean isBlocked(Site site){
        if(!this.siteExists(site.getDomain())){
            return false;
        }else{
            return siteList.get(site);
        }
    }
}
