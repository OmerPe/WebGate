package Model;

import org.pcap4j.util.MacAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class D_search {
    public static void D_search(ArrayList<String> SiteInfo,ArrayList<String> IPlist) {
        HashMap<String, Float> SearchIT = new HashMap<>();
        File Dictionary = new File("Dictionary.txt");
        if (!Dictionary.exists()) {
            try {
                Dictionary.createNewFile();
            } catch (IOException e) {
                System.out.println("couldnt create file");
                e.printStackTrace();
            }
        }
        Scanner scanner;
        try {
            scanner = new Scanner(Dictionary);
            if (!scanner.hasNext()) {
                System.out.println("File is empty");
                return;
            }
            String word;
            Float score;
            while (scanner.hasNextLine()) {
                word = scanner.nextLine();
                score = Float.valueOf(scanner.nextLine());
                SearchIT.put(word, score);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] str = SiteInfo.get(0).split("\\.");
        for (String s : str) {
            if (SearchIT.containsKey(s)) {
                if (SearchIT.get(s) == 10) {//10-block site
                    for (String ip : IPlist) {
                        String command;
                        if (ip.length() > 16) {//block specific website
                            command = "sudo ip6tables-legacy -I INPUT -s " + ip + " -j DROP";
                        } else {
                            command = "sudo iptables-legacy -I INPUT -s " + ip + " -j DROP";
                        }
                        try {
                            Runtime.getRuntime().exec(command);
                            System.out.println("blocked");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (int i = 1; i < SiteInfo.size(); i++) {
                if (SearchIT.containsKey(SiteInfo.get(i))) {
                    if (SearchIT.get(SiteInfo.get(i)) == 10) {
                        for (String ip : IPlist) {
                            String command;
                            if (ip.length() > 16) {//block specific website
                                command = "sudo ip6tables-legacy -I INPUT -s " + ip + " -j DROP";
                            } else {
                                command = "sudo iptables-legacy -I INPUT -s " + ip + " -j DROP";
                            }
                            try {
                                Runtime.getRuntime().exec(command);
                                System.out.println("blocked");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("ynet.co.il");
        list.add("195.95.193.250");
        list.add("Israel");
        list.add("https");
        ArrayList<String> IPlist = AiPrep.lookup(new Site(list.get(0)));
        D_search(list,IPlist);
    }
}
