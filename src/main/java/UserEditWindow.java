import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.*;

public class UserEditWindow extends JFrame{
    private JTextField userNameTxt;
    private JLabel userMacLable;
    private JLabel userIPLable;
    private JButton updateButton;
    private JPanel MainPanel;

    public UserEditWindow(NetworkUser usr){
        super("Edit");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(MainPanel);
        this.pack();
        this.setSize(600,480);

        this.userNameTxt.setText(usr.getName());
        this.userMacLable.setText(usr.getMac().toString());
        this.userIPLable.setText(usr.getIpAddr().toString());


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String txt = userNameTxt.getText();
                usr.setName(txt);
                UserEditWindow.super.getDefaultCloseOperation();
            }
        });
        this.setVisible(true);
    }
}
