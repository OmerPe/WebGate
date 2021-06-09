package Model;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Site {
    private String domain;
    private LinkedList<String> ipList;

    public Site(String name){
        this.domain = name;
        ipList = new LinkedList<>();
    }

    public void addIp(String ip){
        this.ipList.add(ip);
    }

    public void addMultipleIp(List<String> lst){
        this.ipList.addAll(lst);
    }

    public List<String> getIps(){
        return this.ipList;
    }

    public String getDomain(){ return this.domain;}

    public String getFileString(){
        StringBuilder fs = new StringBuilder();
        fs.append(this.domain).append(",").append(this.ipList.size()).append("|");
        for (String ip :
                ipList) {
            fs.append(ip).append("|");
        }
        fs.deleteCharAt(fs.length()-1);
        fs.append("\n");
        return fs.toString();
    }

    public static Site readSiteFromFile(String line){
        String[] split = line.split(",");
        Site site = new Site(split[0]);
        String[] ipList = split[1].split("\\|");
        int len = Integer.parseInt(ipList[0]);
        for (int i = 1; i<len;i++){
            site.addIp(ipList[i]);
        }
        return site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Objects.equals(domain, site.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }
}
