package Model;

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
        fs.append(this.domain.length()).append("\n").append(this.domain).append("\n").append(this.ipList.size()).append("\n");
        for (String ip :
                ipList) {
            fs.append(ip.length()).append("\n").append(ip).append("\n");
        }
        return fs.toString();
    }

    public static Site readSiteFromFile(Scanner scanner){
        int len = Integer.parseInt(scanner.nextLine());
        String line = scanner.nextLine();
        Site site = new Site(line);
        len = Integer.parseInt(scanner.nextLine());
        int tmpInt;
        for (int i = 0; i<len;i++){
            tmpInt = Integer.parseInt(scanner.nextLine());//not used for now.
            site.addIp(scanner.nextLine());
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
