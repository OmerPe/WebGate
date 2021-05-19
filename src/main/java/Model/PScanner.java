package Model;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PScanner implements Runnable {
    private final AtomicBoolean stp = new AtomicBoolean(false);
    private final PcapHandle handle;
    private final BlockingQueue<Packet> packetList; // the packet list we capture

    public PScanner(PcapHandle h, BlockingQueue<Packet> packetList){
        this.handle = h;
        this.packetList = packetList;
    }

    public void stop() {
        stp.set(true);
    }


    @Override
    public void run() {
        if(handle == null || packetList == null){
            System.out.println("you did not set the pcap correctly, set handler/list");
        }
        try {
            while (!stp.get()){
                assert handle != null;
                Packet p = handle.getNextPacket();
                if(p != null){
                    assert packetList != null;
                    packetList.add(p);
                }
            }
        }catch (NotOpenException e) {
            System.out.println(e.toString());
        }
        System.out.println("Scanner stopped");
    }
}
