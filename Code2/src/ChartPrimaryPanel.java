import org.json.simple.JSONArray;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ChartPrimaryPanel extends JPanel{
    private SitePanel pnlsitePanel;
    private JPanel[] ChartPanel = new JPanel[50];
    private JButton RefreshBtn, btnSiteBtn_M, btnSiteBtn_B, btnSiteBtn_G, SearchBtn;
    private JLabel TimeLabel, ChartLabel;
    private JComboBox strCombo;

    private int Site_M_B_G = 0;

    private ButtonListener ButtonRefresh, ButtonMelon, ButtonBugs, ButtonGenie, ButtonSearch;

    private String SearchMusic;
    private String MelonSite, BugsSite, GenieSite;

    private String[] SearchCategory = {"Name", "Artist"};

    private JTextField Searchtxt;


    LocalDateTime current = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    String formatted_Melon = current.format(formatter);
    String formatted_Bugs = current.format(formatter);
    String formatted_Genie = current.format(formatter);
    //refreshTime

    //get/set
    public String getMelonSite()        {return MelonSite;}
    public String getSearchMusic()      {return SearchMusic;}
    public String getBugsSite()         {return BugsSite;}
    public String getGenieSite()        {return GenieSite;}

    public void setMelonSite(String melonSite)          {MelonSite = melonSite;}
    public void setSearchMusic(String searchMusic)      {SearchMusic = searchMusic;}
    public void setBugsSite(String bugsSite)            {BugsSite = bugsSite;}
    public void setGenieSite(String genieSite)          {GenieSite = genieSite;}

    public ChartPrimaryPanel(){

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200,900));
        setLayout(null);

        ButtonRefresh = new ButtonListener();
        ButtonMelon = new ButtonListener();
        ButtonBugs = new ButtonListener();
        ButtonGenie = new ButtonListener();
        ButtonSearch = new ButtonListener();

        strCombo = new JComboBox(SearchCategory);
        strCombo.setBounds(100, 30, 150,40);
        add(strCombo);

        Searchtxt = new JTextField();
        Searchtxt.setBounds(250,30,700,40);
        Searchtxt.setFont(new Font("SansSerif", Font.PLAIN, 25));

        Searchtxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SearchMusic = Searchtxt.getText();
                    System.out.println(SearchMusic);
                    Searchtxt.setText("");
                    //////////////////////////////////////////////
                    ///Need code to erase and repaint SitePanel///
                    //////////////////////////////////////////////
                }
            }
        });//엔터키 입력
        add(Searchtxt);

        SearchBtn = new JButton("Search");
        SearchBtn.setBounds(950,30,150,40);
        SearchBtn.setForeground(Color.DARK_GRAY);
        SearchBtn.setBackground(Color.lightGray);
        SearchBtn.addActionListener(ButtonRefresh);
        add(SearchBtn);

        RefreshBtn = new JButton(new ImageIcon("Image/Refresh.png"));
        RefreshBtn.setBounds(30,30,40,40);
        RefreshBtn.setForeground(Color.DARK_GRAY);
        RefreshBtn.setBackground(Color.lightGray);
        RefreshBtn.addActionListener(ButtonSearch);
        add(RefreshBtn);

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatted = current.format(formatter);
        TimeLabel = new JLabel("Renewal time : " + formatted);
        System.out.println("Renewal time : " + formatted);
        TimeLabel.setBounds(800,830,200,40);
        TimeLabel.setFont(new Font("Verdana", Font.BOLD + Font.PLAIN, 14));
        TimeLabel.setBackground(Color.lightGray);
        TimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TimeLabel.setOpaque(true);
        add(TimeLabel);

        btnSiteBtn_M = new JButton(new ImageIcon("Image/logo_Melon.png"));
        btnSiteBtn_M.setBounds(100,100,150,40);
        btnSiteBtn_M.setBackground(Color.WHITE);
        btnSiteBtn_M.addActionListener(ButtonMelon);
        add(btnSiteBtn_M);

        btnSiteBtn_B = new JButton(new ImageIcon("Image/logo_Bugs.png"));
        btnSiteBtn_B.setBounds(250,100,150,40);
        btnSiteBtn_B.setBackground(Color.WHITE);
        btnSiteBtn_B.addActionListener(ButtonBugs);
        add(btnSiteBtn_B);

        btnSiteBtn_G = new JButton(new ImageIcon("Image/logo_Genie.png"));
        btnSiteBtn_G.setBounds(400,100,150,40);
        btnSiteBtn_G.setBackground(Color.WHITE);
        btnSiteBtn_G.addActionListener(ButtonGenie);
        add(btnSiteBtn_G);


        pnlsitePanel = new SitePanel(AppManger.getS_instance().getParser());
        pnlsitePanel.setBounds(100,140,1000,660);
        LineBorder SiteBorder = new LineBorder(Color.BLACK,3);
        pnlsitePanel.setBorder(SiteBorder);
        pnlsitePanel.setLayout(null);
        add(pnlsitePanel);

    }//constructor



    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj == RefreshBtn) {
                current = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                switch (AppManger.getS_instance().getSite_M_B_G()){
                    case 0:
                        formatted_Melon = current.format(formatter);
                        TimeLabel.setText("Renewal time : " + formatted_Melon);
                        AppManger.getS_instance().setSite_M_B_G(1);
                        AppManger.getS_instance().DataPassing();
                        break;
                    case 1:
                        formatted_Bugs = current.format(formatter);
                        TimeLabel.setText("Renewal time : " + formatted_Bugs);
                        AppManger.getS_instance().setSite_M_B_G(2);
                        AppManger.getS_instance().DataPassing();
                        break;
                    case 2:
                        formatted_Genie = current.format(formatter);
                        TimeLabel.setText("Renewal time : " + formatted_Genie);
                        AppManger.getS_instance().setSite_M_B_G(3);
                        AppManger.getS_instance().DataPassing();
                        break;
                }
            }//refresh 새로 파싱해옴
            if (obj == btnSiteBtn_M) {
                AppManger.getS_instance().setSite_M_B_G(1);
                AppManger.getS_instance().DataPassing();
                System.out.println("Melon");
                pnlsitePanel.dataChange(AppManger.getS_instance().getParser());
                TimeLabel.setText("Renewal time : " + formatted_Melon);
            }
            if (obj == btnSiteBtn_B) {
                AppManger.getS_instance().setSite_M_B_G(2);
                AppManger.getS_instance().DataPassing();
                System.out.println("Bugs");
                pnlsitePanel.dataChange(AppManger.getS_instance().getParser());
                TimeLabel.setText("Renewal time : " + formatted_Bugs);
            }
            if (obj == btnSiteBtn_G) {
                AppManger.getS_instance().setSite_M_B_G(3);
                AppManger.getS_instance().DataPassing();
                System.out.println("Genie");
                pnlsitePanel.dataChange(AppManger.getS_instance().getParser());
                TimeLabel.setText("Renewal time : " + formatted_Genie);
            }
            if (obj == SearchBtn) {
                SearchMusic = Searchtxt.getText();
                System.out.println(SearchMusic);
                Searchtxt.setText("");
                //////////////////////////////////////////////
                ///Need code to erase and repaint SitePanel///
                //////////////////////////////////////////////
            }//Search
        }
    }//ButtonListener
}
