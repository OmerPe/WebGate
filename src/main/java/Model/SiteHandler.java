package Model;

import java.util.HashMap;

public class SiteHandler {
    HashMap<String,Site> siteList = new HashMap<>();

    public HashMap<String,Site> getList (){
        return this.siteList;
    }

    public Site getSite(String name){
        return siteList.get(name);
    }

    public void addSite(Site site){
        if(!siteList.containsKey(site.getDomain())){
            siteList.put(site.getDomain(),site);
        }
    }

    public boolean containSite(String name){
        return siteList.containsKey(name);
    }


}
