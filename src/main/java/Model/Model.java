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
    private static final String SITES_BLOCKED_FILE = "blockedSites.csv";
    private static final String USER_HANDLER_FILE = "userHandler.csv";
    private static final String SITES_FILE = "sites.csv";

    private static void FileCheckUp(){ //making sure all the files exists, if not creates them
        File config = new File(CONFIG_FILE);
        File siteHandlerFile = new File(SITES_BLOCKED_FILE);
        File userHandlerFile = new File(USER_HANDLER_FILE);
        File sites = new File(SITES_FILE);

        try {
            if(config.createNewFile()){
                System.out.println("config file Created");
                //CreateConfig(config);  //creates default config file.
            }else{
                System.out.println("config file approved");
            }

            if(siteHandlerFile.createNewFile()){
                System.out.println("blocked siteHandlerFile file Created");
            }else{
                System.out.println("blocked siteHandlerFile file approved");
            }

            if(userHandlerFile.createNewFile()){
                System.out.println("userHandlerFile file Created");
            }else{
                System.out.println("userHandlerFile file approved");
            }

            if(sites.createNewFile()){
                System.out.println("sites file Created");
            }else{
                System.out.println("sites file approved");
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

        this.userHandler.loadFile(getUserHandlerFileString());
    }

    public void start(){
        try {
            siteHandler.loadFile(SITES_FILE);
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

    public String getUserHandlerFileString(){
        return USER_HANDLER_FILE;
    }

    public String getSitesFile() {return SITES_FILE;}

    public SiteHandler getSiteHandler() {
        return siteHandler;
    }
}
