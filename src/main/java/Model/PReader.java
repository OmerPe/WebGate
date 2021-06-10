package Model;

import inet.ipaddr.IPAddressString;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PReader implements Runnable{
    private final AtomicBoolean stp = new AtomicBoolean(false);
    private final BlockingQueue<Packet> packetList; // the packet list we capture
    private final String defaultGateway;
    private final String subnet;
    private final UserHandler userHandler = Model.instance.getUserHandler();

    public PReader(BlockingQueue<Packet> packetList,String gw,String subnet){
        this.packetList = packetList;
        this.defaultGateway = gw;
        this.subnet = subnet;
    }

    public void stop() {
        stp.set(true);
    }


    @Override
    public void run() {
        if(packetList == null){
            System.out.println("you did not set the pcap correctly, set handler/list");
        }
        try {
            while (!stp.get()){
                assert packetList != null;
                if(packetList.isEmpty() && stp.get()){
                    break;
                }else if(!packetList.isEmpty()){
                    Packet pkt = packetList.take();
                    String direction = "inbound";
                    MacAddress srcMac = ((EthernetPacket)pkt).getHeader().getSrcAddr();
                    if(((EthernetPacket)pkt).getHeader().getType() == EtherType.IPV4){
                        Inet4Address srcAddr = ((IpV4Packet.IpV4Header)pkt.getPayload().getHeader()).getSrcAddr();
                        if(contains(subnet,srcAddr.getHostAddress())){
                            direction = "outbound";
                            NetworkUser user = new NetworkUser(srcMac,srcAddr);
                            if(defaultGateway.equals(srcAddr.toString())){
                                user.setName("Router");
                            }
                            userHandler.addUser(user);
                        }
                    }
                    Model.instance.getStatisticsHandler().addStats(pkt,direction);
                    //System.out.println(Model.instance.getStatisticsHandler().getCurrentStat().toString());
                    System.out.println(pkt);

                }
            }
        }catch (InterruptedException e){
            this.stop();
            Thread.currentThread().interrupt();
            System.out.println("Printer thread was interrupted");
        }
        System.out.println("printer stopped");
    }

    public boolean contains(String subnet, String address) {
        IPAddressString one = new IPAddressString(subnet);
        IPAddressString two = new IPAddressString(address);
        return one.contains(two);
    }
}
