import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ChartPrimaryPanel extends JPanel{
    private JPanel SitePanel;
    private JButton RefreshBtn, SiteBtn_M, SiteBtn_B, SiteBtn_G;
    private JLabel RefreshLabel;

    private int RefreshTime;
    private int Site_M_B_G = 1;

    private ButtonListener ButtonRefresh, ButtonMelon, ButtonBugs, ButtonGenie;

    private String SearchArtist, SearchMusic, SearchWindow;
    private String MelonSite, BugsSite, GenieSite;


    private JTextField Searchtxt;


    //get/set
    public String getMelonSite()        {return MelonSite;}
    public String getSearchArtist()     {return SearchArtist;}
    public String getSearchMusic()      {return SearchMusic;}
    public String getSearchWindow()     {return SearchWindow;}
    public String getBugsSite()         {return BugsSite;}
    public String getGenieSite()        {return GenieSite;}

    public void setMelonSite(String melonSite)          {MelonSite = melonSite;}
    public void setSearchArtist(String searchArtist)    {SearchArtist = searchArtist;}
    public void setSearchMusic(String searchMusic)      {SearchMusic = searchMusic;}
    public void setSearchWindow(String searchWindow)    {SearchWindow = searchWindow;}
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

        Searchtxt = new JTextField(1000);
        Searchtxt.setBounds(100,30,1000,40);
        add(Searchtxt);

        RefreshBtn = new JButton(new ImageIcon("Image/Refresh1.png"));
        RefreshBtn.setBounds(30,30,40,40);
        RefreshBtn.setForeground(Color.DARK_GRAY);
        RefreshBtn.setBackground(Color.lightGray);
        RefreshBtn.addActionListener(ButtonRefresh);
        add(RefreshBtn);

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatted = current.format(formatter);
        RefreshLabel = new JLabel("Renewal time : " + formatted);
        System.out.println("Renewal time : " + formatted);
        RefreshLabel.setBounds(800,830,200,40);
        RefreshLabel.setFont(new Font("Verdana", Font.BOLD + Font.PLAIN, 14));
        RefreshLabel.setBackground(Color.lightGray);
        RefreshLabel.setHorizontalAlignment(SwingConstants.CENTER);
        RefreshLabel.setOpaque(true);
        add(RefreshLabel);

        SiteBtn_M = new JButton("Melon");
        SiteBtn_M.setBounds(100,100,150,40);
        SiteBtn_M.addActionListener(ButtonMelon);
        add(SiteBtn_M);

        SiteBtn_B = new JButton("Bugs");
        SiteBtn_B.setBounds(250,100,150,40);
        SiteBtn_B.addActionListener(ButtonBugs);
        add(SiteBtn_B);

        SiteBtn_G = new JButton("Genie");
        SiteBtn_G.setBounds(400,100,150,40);
        SiteBtn_G.addActionListener(ButtonGenie);
        add(SiteBtn_G);

        SitePanel = new JPanel();
        SitePanel.setBounds(100,140,1000,660);
        LineBorder SiteBorder = new LineBorder(Color.BLACK,3);
        SitePanel.setBorder(SiteBorder);
        add(SitePanel);

    }//constructor

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();

            if (obj == RefreshBtn) {
                LocalDateTime current = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formatted = current.format(formatter);
                System.out.println("Renewal time : " + formatted);
                RefreshLabel.setText("Renewal time : " + formatted);

            } else if (obj == SiteBtn_M) {
                Site_M_B_G = 1;
            } else if (obj == SiteBtn_B) {
                Site_M_B_G = 2;
            } else if (obj == SiteBtn_G) {
                Site_M_B_G = 3;
            }
        }
    }//ButtonListener
}
