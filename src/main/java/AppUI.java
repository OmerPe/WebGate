import Model.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppUI extends JFrame{
    private JPanel MainPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JButton firewallButton;
    private JButton networkUsersButton;
    private JButton networkStatisticButton;
    private JPanel fwPanel;
    private JList blockedSitesList;
    private JTextField domainTxt;
    private JButton siteBlockButton;
    private JButton siteUnblockButton;
    private JLabel blockedSitesLabel;
    private JPanel usersPanel;
    private JPanel statisticsPanel;
    private JPanel welcomePanel;
    private JButton homeButton;
    private JLabel userLable;
    private JList usersList;
    private JPanel userMenuPanel;
    private JPanel userListPanel;
    private JButton editButton;
    private JButton parentalControlButton;
    private JButton blockUserButton;
    private JButton unblockUserButton;
    private DefaultListModel<String> siteDefaultListModel;
    private DefaultListModel<String> UserListModel;

    private String currentPanel;

    public AppUI(){
        super("WebGate");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeIrrelevant();
                Model.instance.getPcap().Stop();
                super.windowClosing(e);
            }
        });
        this.setContentPane(MainPanel);
        //this.pack();
        this.setSize(600,480);

        String blocked_site_file = Model.instance.getBlockedSiteFileString();
        String config_file = Model.instance.getConfString();
        String user_file = Model.instance.getUserHandlerFileString();

        currentPanel = "welcome";
        siteDefaultListModel = new DefaultListModel<>();
        blockedSitesList.setModel(siteDefaultListModel);
        UserHandler uh = Model.instance.getUserHandler();
        UserListModel = new DefaultListModel<>();
        usersList.setModel(UserListModel);

        IpTable iptable = Model.instance.getIptable();

        CardLayout cl = new CardLayout();
        rightPanel.setLayout(cl);
        rightPanel.add(fwPanel,"firewall");
        rightPanel.add(usersPanel,"users");
        rightPanel.add(statisticsPanel,"statistics");
        rightPanel.add(welcomePanel,"welcome");

        cl.show(rightPanel,"welcome");

        //--------------------------firewall section --------------------------
        firewallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeIrrelevant();
                currentPanel = "firewall";
                iptable.ReadFromFile(blocked_site_file);
                cl.show(rightPanel,"firewall");
                refreshSiteList(siteDefaultListModel);
            }
        });

        siteBlockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String txt = domainTxt.getText();
                if (!(txt.equals(""))) {
                    Site site;
                    if (iptable.siteExists(txt)) {
                        site = iptable.getSite(txt);
                    } else {
                        site = new Site(domainTxt.getText());
                        ArrayList<String> tmp = DNSLookUp.lookup(site);
                        if (tmp == null) {
                            JOptionPane.showMessageDialog(MainPanel, txt + " is not a site");
                            return;
                        }
                    }
                    if (iptable.isBlocked(site)) {
                        JOptionPane.showMessageDialog(MainPanel, site.getDomain() + " already Blocked!");
                    } else {
                        iptable.BlockIP(site);
                        JOptionPane.showMessageDialog(MainPanel, site.getDomain() + " got Blocked!");
                    }
                    refreshSiteList(siteDefaultListModel);
                }
            }
        });

        siteUnblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String txt = domainTxt.getText();
                if (txt.equals("")) {
                    int idx = blockedSitesList.getSelectedIndex();
                    if(idx >= 0){
                        txt = siteDefaultListModel.getElementAt(idx);
                    }else{
                        return;
                    }
                }
                Site site;
                if (iptable.siteExists(txt)) {
                    site = iptable.getSite(domainTxt.getText());
                    if (iptable.isBlocked(site)) {
                        iptable.unBlockIP(site);
                        JOptionPane.showMessageDialog(MainPanel, site.getDomain() + " is unBlocked");
                        refreshSiteList(siteDefaultListModel);
                    } else {
                        JOptionPane.showMessageDialog(MainPanel, site.getDomain() + " is not blocked!");
                    }

                } else {
                    JOptionPane.showMessageDialog(MainPanel, txt + " does not exist in the blocked sites");
                }
            }
        });

        blockedSitesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int siteNum = blockedSitesList.getSelectedIndex();
                if(siteNum >= 0){
                    String domain = siteDefaultListModel.getElementAt(siteNum);
                    domainTxt.setText(domain);
                }
            }
        });
        //----------------------end firewall section ----------------------

        //-----------------------user handler section ----------------------
        networkUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeIrrelevant();
                currentPanel = "users";
                cl.show(rightPanel,"users");
                refreshUserList(UserListModel);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int idx = usersList.getSelectedIndex();
                if(idx >= 0){
                    String name = UserListModel.getElementAt(idx);
                    NetworkUser usr = uh.findByName(name);
                    new UserEditWindow(usr);
                }
            }
        });

        parentalControlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int idx = usersList.getSelectedIndex();
                if(idx >= 0){
                    String name = UserListModel.getElementAt(idx);
                    NetworkUser usr = uh.findByName(name);
                    new UserPCWindow(usr);
                }

            }
        });

        blockUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int idx = usersList.getSelectedIndex();
                if(idx >= 0){
                    String name = UserListModel.getElementAt(idx);
                    NetworkUser usr = uh.findByName(name);
                    usr.BlockUser();
                    JOptionPane.showMessageDialog(MainPanel, "User " + usr.getName() + " is Blocked");
                }
            }
        });

        unblockUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int idx = usersList.getSelectedIndex();
                if(idx >= 0){
                    String name = UserListModel.getElementAt(idx);
                    NetworkUser usr = uh.findByName(name);
                    usr.unBlockUser();
                    JOptionPane.showMessageDialog(MainPanel,"User " + usr.getName() + " is unBlocked");
                }
            }
        });
        //-----------------------end user handler section -------------------

        networkStatisticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeIrrelevant();
                currentPanel = "statistics";
                cl.show(rightPanel,"statistics");
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeIrrelevant();
                currentPanel = "welcome";
                cl.show(rightPanel,"welcome");
            }
        });

        this.setVisible(true);
    }

    private void closeIrrelevant(){
        switch (currentPanel){
            case "firewall":
                Model.instance.getIptable().WriteToFile(Model.instance.getBlockedSiteFileString());
                break;
            case "users":
                Model.instance.getUserHandler().saveFile(Model.instance.getUserHandlerFileString());
                break;
            case "statistics":
                break;
            default:
                break;
        }
    }

    private void refreshSiteList(DefaultListModel<String> list){
        list.removeAllElements();

        Model.instance.getIptable().getSiteList().forEach((d)->{
            if(Model.instance.getIptable().isBlocked(d)){
                list.addElement(d.getDomain());
            }
        });
    }

    private void refreshUserList(DefaultListModel<String> list){
        list.removeAllElements();

        Model.instance.getUserHandler().getUserMacList().forEach(mac -> {
            list.addElement(Model.instance.getUserHandler().getUserByMac(mac).getName());
        });
    }


}
