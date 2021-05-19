package Model;

import org.pcap4j.util.MacAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class UserHandler {
    private final HashMap<MacAddress,NetworkUser> users;

    public UserHandler(){
        this.users = new HashMap<>();
    }

    public void addToList(NetworkUser networkUser){
        if(!users.containsKey(networkUser.getMac())){
            this.users.put(networkUser.getMac(),networkUser);
        }
    }

    public void saveFile(String fileName){

        if(users.isEmpty()){
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

            frmtr.format("%d\n",users.size()); //write size
            for (MacAddress mac :
                    users.keySet()) {
                frmtr.format("%s",users.get(mac).fileString());
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
            if(!scanner.hasNext()){
                System.out.println("File is empty");
                return;
            }
            String line = scanner.nextLine();
            int size = Integer.parseInt(line);
            NetworkUser user;
            for(int i=0;i<size;i++){
                user = NetworkUser.readUserFromFile(scanner);
                this.users.put(user.getMac(),user);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public NetworkUser findByName(String name){
        for (NetworkUser u :
                users.values()) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        return null;
    }

    public boolean containUserByMac(MacAddress ma){
        return users.containsKey(ma);
    }

    public boolean containUserByUser(NetworkUser user){
        return users.containsKey(user.getMac());
    }

    public NetworkUser getUserByMac(MacAddress ma){
        return this.users.get(ma);
    }

    public Set<MacAddress> getUserMacList(){
        return this.users.keySet();
    }
}
