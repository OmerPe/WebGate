package Model;

import org.pcap4j.util.MacAddress;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class NetworkUser {
    private String name;
    private final MacAddress mac;
    private Inet4Address ipAddr;
    private List<Site> blockedSites;
    private float trustFactor;

    private boolean isBlocked;

    public NetworkUser(MacAddress m, Inet4Address ip) {
        this.mac = m;
        this.ipAddr = ip;
        this.name = m.toString();
        this.trustFactor = 5;
        this.blockedSites = new LinkedList<>();
        this.isBlocked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MacAddress getMac() {
        return mac;
    }

    public Inet4Address getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(Inet4Address ipAddr) {
        this.ipAddr = ipAddr;
    }

    public List<Site> getBlockedSites() {
        return blockedSites;
    }

    public void setBlockedSites(List<Site> blockedSites) {
        this.blockedSites = blockedSites;
    }

    public void BlockSite(Site site) {

        String command;
        for (int i = 0; i < site.getIps().size(); i++) {
            if (site.getIps().get(i).length() > 16) {
                command = "sudo ip6tables-legacy -A FORWARD -p all -m mac --mac-source " + mac + " -d " + site.getIps().get(i) + " -j DROP";
            } else {
                command = "sudo iptables-legacy -A FORWARD -p all -m mac --mac-source " + mac + " -d " + site.getIps().get(i) + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.blockedSites.add(site);
    }

    public void UnblockSite(Site site) {

        String command;
        for (int i = 0; i < site.getIps().size(); i++) {
            if (site.getIps().get(i).length() > 16) {
                command = "sudo ip6tables-legacy -D FORWARD -p all -m mac --mac-source " + mac + " -d " + site.getIps().get(i) + " -j DROP";
            } else {
                command = "sudo iptables-legacy -D FORWARD -p all -m mac --mac-source " + mac + " -d " + site.getIps().get(i) + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.blockedSites.remove(site);
    }

    public Site getSiteByName(String name) {
        for (Site s :
                this.blockedSites) {
            if (s.getDomain().equals(name)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkUser that = (NetworkUser) o;
        return mac.equals(that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mac);
    }

    @Override
    public String toString() {
        return "Model.NetworkUser{" +
                "name='" + name + '\'' +
                ", mac=" + mac +
                ", ipAddr=" + ipAddr +
                ", blockedSites=" + blockedSites +
                '}';
    }

    public String fileString() {
        StringBuilder sb = new StringBuilder();
        int size = this.getBlockedSites().size();
        sb.append(this.getName()).append(",").append(this.getMac()).append(",").append(this.getIpAddr()).append(",");
        sb.append(this.trustFactor).append(",").append(isBlocked?"1":"0").append(",");
        sb.append(size).append("|");
        for (Site site :
                this.getBlockedSites()) {
            sb.append(site.getDomain()).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("\n");

        return sb.toString();
    }

    public static NetworkUser readUserFromFile(String line) {
        String[] split = line.split(",");
        String name = split[0];
        String mac = split[1];
        String ip = split[2].substring(1);
        float trustF = Float.parseFloat(split[3]);
        boolean isBlocked = split[4].equals("1");
        try {
            NetworkUser user = new NetworkUser(MacAddress.getByName(mac), (Inet4Address) InetAddress.getByName(ip));
            user.setName(name);
            user.setTrustFactor(trustF);
            List<Site> blocked = new LinkedList<>();
            String[] siteNameList = split[5].split("\\|");
            int size = Integer.parseInt(siteNameList[0]);
            for (int i = 1; i < size; i++) {
                blocked.add(Model.instance.getIptable().getSite(siteNameList[i]));
            }
            user.setBlockedSites(blocked);
            user.setBlocked(isBlocked);
            return user;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean siteExists(String txt) {
        for (Site site : this.getBlockedSites()) {
            if (site.getDomain().equals(txt)) {
                return true;
            }
        }
        return false;
    }

    public void BlockUser(){
        if(!isBlocked){
            String command = "sudo iptables-legacy -A FORWARD -s " + getIpAddr().toString().substring(1) + " -j DROP";
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void unBlockUser(){
        if(isBlocked){
            String command = "sudo iptables-legacy -D FORWARD -s " + getIpAddr().toString().substring(1) + " -j DROP";
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public float getTrustFactor(){return this.trustFactor;}

    public void increaseTrustFactor(float amount){
        this.trustFactor+=amount;
    }

    public void decreaseTrustFactor(float amount){
        this.trustFactor-=amount;
    }

    public void setTrustFactor(float trustFactor){
        this.trustFactor = trustFactor;
    }


    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
