package Model;

import java.io.IOException;
import java.util.ArrayList;

public class PortBlock {
    static void PortBlock(String port){
        String command;
                command = "iptables-legacy -A INPUT -p tcp --destination-port"+port+"-j DROP";
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        static void TargetPortBlock(String port,String ip)
        {
            String command;
            command = "iptables-legacy -A INPUT -p tcp --destination-port"+port+"-s"+ip+"-j DROP";
            try{
                Runtime.getRuntime().exec(command);
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    static void PortUnBlock(String port){
        String command;
        command = "iptables-legacy -D INPUT -p tcp --destination-port"+port+"-j DROP";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void TargetPortUnBlock(String port,String ip)
    {
        String command;
        command = "iptables-legacy -D INPUT -p tcp --destination-port"+port+"-s"+ip+"-j DROP";
        try{
            Runtime.getRuntime().exec(command);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    static  void TargetIPBlock(String ip)
    {
        String command;
        command = "iptables-legacy -A INPUT -s"+ip+"-j DROP";
        try{
            Runtime.getRuntime().exec(command);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    static  void TargetIPUnBlock(String ip)
    {
        String command;
        command = "iptables-legacy -D INPUT -s"+ip+"-j DROP";
        try{
            Runtime.getRuntime().exec(command);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
