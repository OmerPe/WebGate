import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Model.*;

public class UserPCWindow extends JFrame{
    private JList BlockedSiteList;
    private JLabel UseNameLable;
    private JLabel titleLable;
    private JTextField siteDomainTxt;
    private JButton blockSiteButton;
    private JButton unblockSiteButton;
    private JPanel MainPanel;
    private DefaultListModel<String> blockedDefaultListModel;

    private NetworkUser user;

    public UserPCWindow(NetworkUser usr){
        super("Parental control");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(MainPanel);
        this.pack();
        this.setSize(600,480);

        this.user = usr;
        UseNameLable.setText(user.getName());
        blockedDefaultListModel = new DefaultListModel<>();
        BlockedSiteList.setModel(blockedDefaultListModel);
        refreshList();

        blockSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String txt = siteDomainTxt.getText();
                if (!(txt.equals(""))) {
                    if (user.siteExists(txt)) {
                        JOptionPane.showMessageDialog(MainPanel, txt + " already blocked");
                    } else {
                        Site site = new Site(txt);
                        ArrayList<String> tmp = DNSLookUp.lookup(site);
                        if (tmp == null) {
                            JOptionPane.showMessageDialog(MainPanel, txt + " is not a site");
                            return;
                        }
                        user.BlockSite(site);
                        JOptionPane.showMessageDialog(MainPanel, txt + " blocked");
                    }
                    refreshList();
                }
            }
        });

        unblockSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String txt = siteDomainTxt.getText();

                if(txt.equals(""))
                {
                    int idx = BlockedSiteList.getSelectedIndex();
                    if (idx>=0)
                    {
                       txt=blockedDefaultListModel.getElementAt(idx);
                    }else{
                        return;
                    }
                }
                if (user.siteExists(txt)) {
                    Site site = user.getSiteByName(txt);
                    user.UnblockSite(site);
                    JOptionPane.showMessageDialog(MainPanel, txt + " successfully unblocked");
                } else {
                    JOptionPane.showMessageDialog(MainPanel, txt + " isn't blocked");
                }
                refreshList();
            }
        });


        this.setVisible(true);
    }

    private void refreshList(){
        blockedDefaultListModel.removeAllElements();

        user.getBlockedSites().forEach((d)->{
            blockedDefaultListModel.addElement(d.getDomain());
        });
    }

}
