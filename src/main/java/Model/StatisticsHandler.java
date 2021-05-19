package Model;

import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class StatisticsHandler {
    private final NetworkStatistics current;
    private final Vector<NetworkStatistics> statisticsLog = new Vector<>(60);
    private final Timer timer;

    public StatisticsHandler(){
        this.current = new NetworkStatistics();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reset();
            }
        }, 0, 1000);//wait 0 ms before doing the action and do it every 1000ms (1second)
    }

    public void addStats(Packet packet,String Direction){
        int size = packet.length();
        current.addNumOfPackets();
        current.addOverAllTraffic(size);
        EthernetPacket epkt = (EthernetPacket) packet;
        //adding the packet size to the user (either send or recieve)
        if(Model.instance.getUserHandler().containUserByMac(epkt.getHeader().getSrcAddr())){
            current.addUserSendTraffic(Model.instance.getUserHandler().getUserByMac(epkt.getHeader().getSrcAddr()),size);
        }else if(Model.instance.getUserHandler().containUserByMac(epkt.getHeader().getDstAddr())){
            current.addUserReceiveTraffic(Model.instance.getUserHandler().getUserByMac(epkt.getHeader().getDstAddr()),size);
        }
        EtherType type = epkt.getHeader().getType();
        if(type == EtherType.ARP){
            ArpPacket arpPacket = (ArpPacket)epkt.getPayload();
            current.addProtocolTraffic("ARP",size);

        }else if(type == EtherType.IPV4){
            IpV4Packet ipV4Packet = (IpV4Packet)epkt.getPayload();
            String protocol = IpNumber.getInstance(ipV4Packet.getHeader().getProtocol().value()).name();
            current.addProtocolTraffic(protocol,size);
            if(protocol.equals("TCP")){
                TcpPacket tcp = (TcpPacket) ipV4Packet.getPayload();
                if(Direction.equals("inbound")){
                    current.addPortReceiveTraffic(tcp.getHeader().getDstPort().value(),size);
                }else if(Direction.equals("outbound")){
                    current.addPortSendTraffic(tcp.getHeader().getSrcPort().value(),size);
                }


            }else if(protocol.equals("UDP")){
                UdpPacket udp = (UdpPacket) ipV4Packet.getPayload();
                if(Direction.equals("inbound")){
                    current.addPortReceiveTraffic(udp.getHeader().getDstPort().value(),size);
                }else if(Direction.equals("outbound")){
                    current.addPortSendTraffic(udp.getHeader().getSrcPort().value(),size);
                }

            }


        }else if(type == EtherType.IPV6){
            IpV6Packet ipV6Packet = (IpV6Packet)epkt.getPayload();
            String protocol = IpNumber.getInstance(ipV6Packet.getHeader().getProtocol().value()).name();
            current.addProtocolTraffic(protocol,size);
            if(protocol.equals("TCP")){
                TcpPacket tcp = (TcpPacket) ipV6Packet.getPayload();
                if(Direction.equals("inbound")){
                    current.addPortReceiveTraffic(tcp.getHeader().getDstPort().value(),size);
                }else if(Direction.equals("outbound")){
                    current.addPortSendTraffic(tcp.getHeader().getSrcPort().value(),size);
                }

            }else if(protocol.equals("UDP")){
                UdpPacket udp = (UdpPacket) ipV6Packet.getPayload();
                if(Direction.equals("inbound")){
                    current.addPortReceiveTraffic(udp.getHeader().getDstPort().value(),size);
                }else if(Direction.equals("outbound")){
                    current.addPortSendTraffic(udp.getHeader().getSrcPort().value(),size);
                }
            }

        }else {
            System.out.println("Unexpected type");
        }
    }

    public NetworkStatistics getCurrentStat(){
        return this.current;
    }

    public void Stop(){
        timer.cancel();
    }

    private void reset(){
        statisticsLog.add(this.current);
        current.resetStats();
    }

}
