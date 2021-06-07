package Model;

import org.pcap4j.core.PcapNativeException;
import java.io.File;
import java.io.IOException;

//this model handles all the data in the app.
public class Model {
    public static final Model instance = new Model();

    private final StatisticsHandler statisticsHandler  = new StatisticsHandler();
    private final UserHandler userHandler;
    private final IpTable iptable;
    private final PcapMain pcap;


    private final SiteHandler siteHandler;

    private static final String CONFIG_FILE = "config.csv";
    private static final String SITES_BLOCKED_FILE = "blocked.csv";
    private static final String NETWORK_USERS_FILE = "userConfig.csv";

    private static void FileCheckUp(){ //making sure all the files exists, if not creates them
        File config = new File(CONFIG_FILE);
        File sites = new File(SITES_BLOCKED_FILE);
        File users = new File(NETWORK_USERS_FILE);

        try {
            if(config.createNewFile()){
                System.out.println("config file Created");
                //CreateConfig(config);  //creates default config file.
            }else{
                System.out.println("config file approved");
            }

            if(sites.createNewFile()){
                System.out.println("blocked sites file Created");
            }else{
                System.out.println("blocked sites file approved");
            }

            if(users.createNewFile()){
                System.out.println("users file Created");
            }else{
                System.out.println("users file approved");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Model(){
        FileCheckUp();
        userHandler = new UserHandler();
        iptable = new IpTable();
        pcap = new PcapMain();
        siteHandler = new SiteHandler();

        this.userHandler.loadFile(getUserFileString());
    }

    public void start(){
        try {
            pcap.Start();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    public StatisticsHandler getStatisticsHandler() {
        return statisticsHandler;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public IpTable getIptable() {
        return iptable;
    }

    public PcapMain getPcap() {
        return pcap;
    }

    public String getConfString(){
        return CONFIG_FILE;
    }

    public String getBlockedSiteFileString(){
        return SITES_BLOCKED_FILE;
    }

    public String getUserFileString(){
        return NETWORK_USERS_FILE;
    }

    public SiteHandler getSiteHandler() {
        return siteHandler;
    }
}
