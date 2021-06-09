package Model;

import org.pcap4j.util.MacAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class UserHandler {
    HashMap<MacAddress, NetworkUser> userList = new HashMap<>();

    public HashMap<MacAddress, NetworkUser> getList(){
        return userList;
    }

    public NetworkUser getUser(MacAddress mac){
        return userList.get(mac);
    }

    public void addUser(NetworkUser user){
        if(!userList.containsKey(user.getMac())){
            userList.put(user.getMac(),user);
        }
    }

    public boolean containUser(MacAddress mac){
        return userList.containsKey(mac);
    }

    public void WriteToFile(String fileName) {
        if (userList.isEmpty()) {
            return;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Error," + fileName + " does not exists");
            return;
        }
        try {
            FileWriter fwrtr = new FileWriter(file, false); //if you want to open in "append" mode change to true
            Formatter frmtr = new Formatter(fwrtr);

            for (NetworkUser user :
                    userList.values()) {
                frmtr.format("%s", user.fileString());
            }

            frmtr.close();
            fwrtr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadFile(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("Error,"+fileName+" does not exists");
            return;
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while(scanner.hasNext()){
                NetworkUser user = NetworkUser.readUserFromFile(scanner.nextLine());
                if(user != null){
                    userList.put(user.getMac(),user);
                }else{
                    System.out.println("Error reading user");
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Set<MacAddress> getUserMacList() {
        return userList.keySet();
    }

    public NetworkUser findByName(String name) {
        for (NetworkUser user :
                userList.values()) {
            if (user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }
}
