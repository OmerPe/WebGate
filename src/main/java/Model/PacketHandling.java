package Model;

import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;

import java.util.ArrayList;

public class PacketHandling {
    public static ArrayList<Float> AiPrep2(Packet pkt)
    {

        ArrayList<Float> input=new ArrayList<>();
        EthernetPacket epkt = (EthernetPacket) pkt;
        if(Model.instance.getUserHandler().containUserByMac(epkt.getHeader().getSrcAddr())){
            input.add(Model.instance.getUserHandler().getUserByMac(epkt.getHeader().getSrcAddr()).getTrustFactor());
        }else{
            input.add((float)5);
        }
        if(Model.instance.getUserHandler().containUserByMac(epkt.getHeader().getDstAddr())){
            input.add(Model.instance.getUserHandler().getUserByMac(epkt.getHeader().getDstAddr()).getTrustFactor());
        }else{
            input.add((float)5);
        }
        EtherType type = epkt.getHeader().getType();
        input.add(type.value().floatValue());// ai would determine the course of action by the type he recieve since every type take different actions into consideration
        if(type == EtherType.ARP){
            ArpPacket.ArpHeader hdr = (ArpPacket.ArpHeader)epkt.getPayload().getHeader();
            input.add(Float.parseFloat(hdr.getSrcProtocolAddr().getHostAddress()));
            input.add(Float.parseFloat(hdr.getDstProtocolAddr().getHostAddress()));


        }else if(type == EtherType.IPV4){
            IpV4Packet.IpV4Header v4hdr = (IpV4Packet.IpV4Header)epkt.getPayload().getHeader();
            input.add(Float.parseFloat(v4hdr.getSrcAddr().getHostAddress()));
            input.add(Float.parseFloat(v4hdr.getDstAddr().getHostAddress()));
            input.add(Float.parseFloat(v4hdr.getProtocol().value().toString()));


        }else if(type == EtherType.IPV6) {
            IpV6Packet.IpV6Header v6hdr = (IpV6Packet.IpV6Header) epkt.getPayload().getHeader();
            input.add(Float.parseFloat(v6hdr.getSrcAddr().getHostAddress()));
            input.add(Float.parseFloat(v6hdr.getDstAddr().getHostAddress()));
            input.add(Float.parseFloat(v6hdr.getProtocol().value().toString()));
        }else{
            System.out.println("Unexpected type");
        }


        return input;
    }
}
