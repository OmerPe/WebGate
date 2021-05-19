package Model;

import java.util.HashMap;

public class NetworkStatistics {
    private final HashMap<NetworkUser,Integer> userSendTraffic;
    private final HashMap<NetworkUser,Integer> userReceiveTraffic;
    private int overAllTraffic;
    private int numOfPackets;
    private final HashMap<String,Integer> protocolTraffic;
    private final HashMap<UnsignedShort,Integer> portSendTraffic;
    private final HashMap<UnsignedShort,Integer> portReceiveTraffic;

    public NetworkStatistics(){
        this.userSendTraffic = new HashMap<>();
        this.portSendTraffic = new HashMap<>();
        this.protocolTraffic = new HashMap<>();
        this.userReceiveTraffic = new HashMap<>();
        this.portReceiveTraffic = new HashMap<>();
        this.overAllTraffic = 0;
        this.numOfPackets = 0;
    }

    public void resetStats(){
        this.userSendTraffic.keySet().forEach((u)->{
            userSendTraffic.put(u,0);
        });
        this.userReceiveTraffic.keySet().forEach((u)->{
            userReceiveTraffic.put(u,0);
        });
        this.portSendTraffic.keySet().forEach(k ->{
            portSendTraffic.put(k,0);
        });
        this.portReceiveTraffic.keySet().forEach(k ->{
            portReceiveTraffic.put(k,0);
        });
        this.overAllTraffic = 0;
        this.numOfPackets = 0;
        this.protocolTraffic.keySet().forEach(k->{
            protocolTraffic.put(k,0);
        });
    }

    public void addUserSendTraffic(NetworkUser u, int traffic){
        this.userSendTraffic.put(u,traffic);
    }

    public void addUserReceiveTraffic(NetworkUser u, int traffic){
        this.userSendTraffic.put(u,traffic);
    }

    public void addPortSendTraffic(short port,int traffic){
        this.portSendTraffic.put(new UnsignedShort(port),traffic);
    }

    public void addPortReceiveTraffic(short port,int traffic){
        this.portReceiveTraffic.put(new UnsignedShort(port),traffic);
    }

    public void addNumOfPackets(){
        this.numOfPackets++;
    }

    public void addProtocolTraffic(String protocol,int size){
        this.protocolTraffic.put(protocol,size);
    }

    public void addOverAllTraffic(int traffic){
        this.overAllTraffic+=traffic;
    }

    public float getOverAllTraffic() {
        return overAllTraffic;
    }

    public int getNumOfPackets() {
        return numOfPackets;
    }

    public float getUserSendTraffic(NetworkUser u){
        return this.userSendTraffic.get(u);
    }

    public float getPortSendTraffic(short port){
        return this.portSendTraffic.get(new UnsignedShort(port));
    }

    public float getPortReceiveTraffic(short port){
        return this.portReceiveTraffic.get(new UnsignedShort(port));
    }

    public float getProtocolTraffic(String protocol){
        return this.protocolTraffic.get(protocol);
    }

    public float getUserReceiveTraffic(NetworkUser u){
        return this.userReceiveTraffic.get(u);
    }

    public String toString() {
        return  "userSendTraffic=" + userSendTraffic +
                "\n userReceiveTraffic=" + userReceiveTraffic +
                "\n overAllTraffic=" + overAllTraffic +
                "\n numOfPackets=" + numOfPackets +
                "\n protocolTraffic=" + protocolTraffic +
                "\n portSendTraffic=" + portSendTraffic +
                "\n portReceiveTraffic=" + portReceiveTraffic
                ;
    }
}
