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

    public NetworkUser(MacAddress m, Inet4Address ip) {
        this.mac = m;
        this.ipAddr = ip;
        this.name = m.toString();
        this.trustFactor = 5;
        this.blockedSites = new LinkedList<>();
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
        sb.append(this.getName()).append("\n").append(this.getMac()).append("\n").append(this.getIpAddr()).append("\n");
        sb.append(this.trustFactor).append("\n");
        sb.append(size).append("\n");
        for (Site site :
                this.getBlockedSites()) {
            sb.append(site.getFileString());
        }

        return sb.toString();
    }

    public static NetworkUser readUserFromFile(Scanner scanner) {
        String name = scanner.nextLine();
        String mac;
        String ip;
        float trustF;
        try {
            mac = scanner.nextLine();
            ip = scanner.nextLine().substring(1);
            NetworkUser user = new NetworkUser(MacAddress.getByName(mac), (Inet4Address) InetAddress.getByName(ip));
            user.setName(name);
            trustF = Float.parseFloat(scanner.nextLine());
            user.setTrustFactor(trustF);
            List<Site> blocked = new LinkedList<>();
            int size = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < size; i++) {
                blocked.add(Site.readSiteFromFile(scanner));
            }
            user.setBlockedSites(blocked);
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
        String command = "sudo iptables-legacy -A FORWARD -s " + getIpAddr().toString().substring(1) + " -j DROP";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unBlockUser(){
        String command = "sudo iptables-legacy -D FORWARD -s " + getIpAddr().toString().substring(1) + " -j DROP";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
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
}