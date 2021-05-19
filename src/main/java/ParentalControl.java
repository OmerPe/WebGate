import java.io.IOException;
import java.util.ArrayList;

public class ParentalControl {

    static void BlockByMac(String macAddress, ArrayList<String> ipList){
        String command;
        for(int i = 0;i<ipList.size();i++) {
            if (ipList.get(i).length() > 16) {
                command = "sudo ip6tables -A FORWARD -p all -m mac --mac-source " + macAddress + " -d " + ipList.get(i) + " -j DROP";
            } else {
                command = "sudo iptables -A FORWARD -p all -m mac --mac-source " + macAddress + " -d " + ipList.get(i) + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void unBlockByMac(String macAddress, ArrayList<String> ipList){
        String command;
        for(int i = 0;i<ipList.size();i++) {
            if (ipList.get(i).length() > 16) {
                command = "sudo ip6tables -D FORWARD -p all -m mac --mac-source " + macAddress + " -d " + ipList.get(i) + " -j DROP";
            } else {
                command = "sudo iptables -D FORWARD -p all -m mac --mac-source " + macAddress + " -d " + ipList.get(i) + " -j DROP";
            }
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
