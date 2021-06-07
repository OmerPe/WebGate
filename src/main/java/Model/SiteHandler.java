package Model;

import java.io.File;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.HashMap;

public class SiteHandler {
    HashMap<String, Site> siteList = new HashMap<>();

    public HashMap<String, Site> getList() {
        return this.siteList;
    }

    public Site getSite(String name) {
        return siteList.get(name);
    }

    public void addSite(Site site) {
        if (!siteList.containsKey(site.getDomain())) {
            siteList.put(site.getDomain(), site);
        }
    }

    public boolean containSite(String name) {
        return siteList.containsKey(name);
    }

    public void WriteToFile(String fileName) {
        if (siteList.isEmpty()) {
            return;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Error," + fileName + " does not exists");
            return;
        }
        try {
            FileWriter fwrtr = new FileWriter(file, false); //if you want to open in "append" mode change to true
            Formatter frmtr = new Formatter(fwrtr);

            for (Site site :
                    siteList.values()) {
                frmtr.format("%s", site.getFileString());
            }

            frmtr.close();
            fwrtr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadFile(String fileName){

    }

}
