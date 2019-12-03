import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ChartPrimaryPanel extends JPanel{
    private SitePanel pnlSitePanel;
    private JButton btnRefresh, btnSite_M, btnSite_B, btnSite_G, btnSearch;
    private JLabel lblTime, lblChart;
    private JComboBox strCombo;

    private int Site_M_B_G = 0;

    private ButtonListener  ButtonRefresh, ButtonSearch,
                            ButtonMelon, ButtonBugs, ButtonGenie;

    private String strSearchMusic;
    private String strMelonSite, strBugsSite, strGenieSite;

    private String[] strSearchCategory = {"Name", "Artist"};

    private JTextField txtSearch;


    LocalDateTime current = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    String formatted_Melon = current.format(formatter);
    String formatted_Bugs = current.format(formatter);
    String formatted_Genie = current.format(formatter);
    //refreshTime

    //get/set
    public String getStrMelonSite()        {return strMelonSite;}
    public String getStrSearchMusic()      {return strSearchMusic;}
    public String getStrBugsSite()         {return strBugsSite;}
    public String getStrGenieSite()        {return strGenieSite;}

    public void setStrMelonSite(String strMelonSite)          {
        this.strMelonSite = strMelonSite;}
    public void setStrSearchMusic(String strSearchMusic)      {
        this.strSearchMusic = strSearchMusic;}
    public void setStrBugsSite(String strBugsSite)            {
        this.strBugsSite = strBugsSite;}
    public void setStrGenieSite(String strGenieSite)          {
        this.strGenieSite = strGenieSite;}

    public ChartPrimaryPanel(){

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200,900));
        setLayout(null);

        ButtonRefresh = new ButtonListener();
        ButtonMelon = new ButtonListener();
        ButtonBugs = new ButtonListener();
        ButtonGenie = new ButtonListener();
        ButtonSearch = new ButtonListener();

        strCombo = new JComboBox(strSearchCategory);
        strCombo.setBounds(100, 30, 150,40);
        add(strCombo);

        txtSearch = new JTextField();
        txtSearch.setBounds(250,30,700,40);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 25));
        txtSearch.addKeyListener(new KeyActionListener());
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(950,30,150,40);
        btnSearch.setForeground(Color.DARK_GRAY);
        btnSearch.setBackground(Color.lightGray);
        btnSearch.addActionListener(ButtonRefresh);
        add(btnSearch);

        btnRefresh = new JButton(new ImageIcon("Image/Refresh.png"));
        btnRefresh.setBounds(30,30,40,40);
        btnRefresh.setForeground(Color.DARK_GRAY);
        btnRefresh.setBackground(Color.lightGray);
        btnRefresh.addActionListener(ButtonSearch);
        add(btnRefresh);

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatted = current.format(formatter);
        lblTime = new JLabel("Renewal time : " + formatted);
        System.out.println("Renewal time : " + formatted);
        lblTime.setBounds(800,830,200,40);
        lblTime.setFont(new Font("Verdana", Font.BOLD + Font.PLAIN, 14));
        lblTime.setBackground(Color.lightGray);
        lblTime.setHorizontalAlignment(SwingConstants.CENTER);
        lblTime.setOpaque(true);
        add(lblTime);

        btnSite_M = new JButton(new ImageIcon("Image/logo_Melon.png"));
        btnSite_M.setBounds(100,100,150,40);
        btnSite_M.setBackground(Color.WHITE);
        btnSite_M.addActionListener(ButtonMelon);
        add(btnSite_M);

        btnSite_B = new JButton(new ImageIcon("Image/logo_Bugs.png"));
        btnSite_B.setBounds(250,100,150,40);
        btnSite_B.setBackground(Color.WHITE);
        btnSite_B.addActionListener(ButtonBugs);
        add(btnSite_B);

        btnSite_G = new JButton(new ImageIcon("Image/logo_Genie.png"));
        btnSite_G.setBounds(400,100,150,40);
        btnSite_G.setBackground(Color.WHITE);
        btnSite_G.addActionListener(ButtonGenie);
        add(btnSite_G);


        pnlSitePanel = new SitePanel(AppManger.getS_instance().getParser());
        pnlSitePanel.setBounds(100,140,1000,660);
        LineBorder SiteBorder = new LineBorder(Color.BLACK,3);
        pnlSitePanel.setBorder(SiteBorder);
        pnlSitePanel.setLayout(null);
        add(pnlSitePanel);

    }//constructor



    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj == btnRefresh) {
                current = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                switch (AppManger.getS_instance().getSite_M_B_G()){
                    case 0:
                        formatted_Melon = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Melon);
                        AppManger.getS_instance().setSite_M_B_G(1);
                        AppManger.getS_instance().DataPassing();
                        break;
                    case 1:
                        formatted_Bugs = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Bugs);
                        AppManger.getS_instance().setSite_M_B_G(2);
                        AppManger.getS_instance().DataPassing();
                        break;
                    case 2:
                        formatted_Genie = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Genie);
                        AppManger.getS_instance().setSite_M_B_G(3);
                        AppManger.getS_instance().DataPassing();
                        break;
                }
            }//refresh 새로 파싱해옴
            if (obj == btnSite_M) {
                AppManger.getS_instance().setSite_M_B_G(1);
                AppManger.getS_instance().DataPassing();
                System.out.println("Melon");
                pnlSitePanel.dataChange(AppManger.getS_instance().getParser());
                lblTime.setText("Renewal time : " + formatted_Melon);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
            if (obj == btnSite_B) {
                AppManger.getS_instance().setSite_M_B_G(2);
                AppManger.getS_instance().DataPassing();
                System.out.println("Bugs");
                pnlSitePanel.dataChange(AppManger.getS_instance().getParser());
                lblTime.setText("Renewal time : " + formatted_Bugs);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
            if (obj == btnSite_G) {
                AppManger.getS_instance().setSite_M_B_G(3);
                AppManger.getS_instance().DataPassing();
                System.out.println("Genie");
                pnlSitePanel.dataChange(AppManger.getS_instance().getParser());
                lblTime.setText("Renewal time : " + formatted_Genie);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
        }
    }//ButtonListener

    private class KeyActionListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyPressed(KeyEvent e) { }

        @Override
        public void keyReleased(KeyEvent e) {
            Object obj = e.getSource();

            if(obj == txtSearch){
                //strSearchCategory = {"Name", "Artist"};
                if(0 == strCombo.getSelectedIndex())
                    pnlSitePanel.filter(txtSearch.getText(),2);
                if(1 == strCombo.getSelectedIndex())
                    pnlSitePanel.filter(txtSearch.getText(),3);
            }
        }//KeyReleased

    }//KeyListener
}
