package Model;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PcapMain {

    static HashMap<String,Thread> threadMap = null; // the threads saved by names

    private static final String READ_TIMEOUT_KEY = PcapMain.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

    private static final String SNAPLEN_KEY = PcapMain.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

    private static final String BUFFER_SIZE_KEY = PcapMain.class.getName() + ".bufferSize";
    private static final int BUFFER_SIZE =
            Integer.getInteger(BUFFER_SIZE_KEY, 1 * 1024 * 1024); // [bytes]

    private static final String TIMESTAMP_PRECISION_NANO_KEY =
            PcapMain.class.getName() + ".timestampPrecision.nano";
    private static final boolean TIMESTAMP_PRECISION_NANO =
            Boolean.getBoolean(TIMESTAMP_PRECISION_NANO_KEY);

    private static final String NIF_NAME_KEY = PcapMain.class.getName() +".nifName";
    private static final String NIF_NAME = System.getProperty(NIF_NAME_KEY);

    PScanner scanner;
    PReader printer;


    public void Start()throws PcapNativeException {

        threadMap = new HashMap<>();

        PcapNetworkInterface nif;
        nif = Pcaps.findAllDevs().get(0);

        System.out.println(nif.getName() + " (" + nif.getDescription() + ")");
        for (PcapAddress addr : nif.getAddresses()) {
            if (addr.getAddress() != null) {
                System.out.println("IP address: " + addr.getAddress());
            }
        }
        System.out.println("");

        try {
            NetworkUser tmpusr = new NetworkUser(MacAddress.getByAddress(NetworkInterface.getByName(nif.getName()).getHardwareAddress()) ,
                    (Inet4Address) nif.getAddresses().get(0).getAddress());
            tmpusr.setName("WebGate");
            Model.instance.getUserHandler().addUser(tmpusr);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        PcapHandle.Builder phb =
                new PcapHandle.Builder(nif.getName())
                        .snaplen(SNAPLEN)
                        .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                        .timeoutMillis(READ_TIMEOUT)
                        .bufferSize(BUFFER_SIZE);
        if (TIMESTAMP_PRECISION_NANO) {
            phb.timestampPrecision(PcapHandle.TimestampPrecision.NANO);
        }
        PcapHandle handle = phb.build();

        BlockingQueue<Packet> packetList = new LinkedBlockingQueue<>();

        scanner = new PScanner(handle,packetList);
        printer = new PReader(packetList,getDefaultGateway(),getSubnet());

        threadMap.put("Scanner",new Thread(scanner));
        threadMap.put("Reader",new Thread(printer));
        threadMap.get("Scanner").start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadMap.get("Reader").start();
    }

    public static String getDefaultGateway(){
        Process p;
        String defaultGateway;
        try {
            p = Runtime.getRuntime().exec("ip r");

            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(p.getInputStream())));
            s.next();
            s.next();
            defaultGateway = s.next();
            p.waitFor();

            return defaultGateway;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    public static String getSubnet(){
        Process p;
        String subnet;
        try {
            p = Runtime.getRuntime().exec("ip r");

            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(p.getInputStream())));
            s.nextLine();
            subnet = s.next();
            System.out.println(subnet);
            p.waitFor();

            return subnet;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    public void Stop(){
        scanner.stop();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printer.stop();
        Model.instance.getStatisticsHandler().Stop();
    }
}
