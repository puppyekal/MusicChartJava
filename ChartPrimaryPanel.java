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
    private JPanel SitePanel;
    private JPanel[] ChartPanel = new JPanel[50];
    private JButton RefreshBtn, SiteBtn_M, SiteBtn_B, SiteBtn_G, SearchBtn;
    private JLabel RefreshLabel, ChartLabel;
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

    private MusicInform[][] musicInfo = new MusicInform[3][50];
    private String[] albumName = new String[50],
            releaseDate = new String[50],
            imageUrl = new String[50],
            genre = new String[50],
            title = new String[50],
            rank = new String[50];

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

                    for (int i = 0; i < 50; i++) {
                        if(strCombo.getSelectedItem().toString() == "Name" && musicInfo[Site_M_B_G][i].getTitle().contains(SearchMusic)){
                            JPanel tempPanel = new JPanel();
                            tempPanel.setBounds(10, 10 + i * 30, 800,30);
                            tempPanel.setBackground(Color.white);
                            tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                            tempPanel.setLayout(null);
                            ChartLabel = new JLabel(musicInfo[0][i].showList());
                            ChartLabel.setBounds(0,0,80,30);
                            tempPanel.add(ChartLabel);
                            ChartPanel[i] = tempPanel;
                            SitePanel.add(ChartPanel[i]);

                            musicInfo[0][i].SysShow();
                        }
                        else if(strCombo.getSelectedItem().toString()=="Artist"){

                        }
                    }
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
        RefreshLabel = new JLabel("Renewal time : " + formatted);
        System.out.println("Renewal time : " + formatted);
        RefreshLabel.setBounds(800,830,200,40);
        RefreshLabel.setFont(new Font("Verdana", Font.BOLD + Font.PLAIN, 14));
        RefreshLabel.setBackground(Color.lightGray);
        RefreshLabel.setHorizontalAlignment(SwingConstants.CENTER);
        RefreshLabel.setOpaque(true);
        add(RefreshLabel);

        SiteBtn_M = new JButton(new ImageIcon("Image/logo_Melon.png"));
        SiteBtn_M.setBounds(100,100,150,40);
        SiteBtn_M.setBackground(Color.WHITE);
        SiteBtn_M.addActionListener(ButtonMelon);
        add(SiteBtn_M);

        SiteBtn_B = new JButton(new ImageIcon("Image/logo_Bugs.png"));
        SiteBtn_B.setBounds(250,100,150,40);
        SiteBtn_B.setBackground(Color.WHITE);
        SiteBtn_B.addActionListener(ButtonBugs);
        add(SiteBtn_B);

        SiteBtn_G = new JButton(new ImageIcon("Image/logo_Genie.png"));
        SiteBtn_G.setBounds(400,100,150,40);
        SiteBtn_G.setBackground(Color.WHITE);
        SiteBtn_G.addActionListener(ButtonGenie);
        add(SiteBtn_G);

        SitePanel = new JPanel();
        SitePanel.setBounds(100,140,1000,660);
        LineBorder SiteBorder = new LineBorder(Color.BLACK,3);
        SitePanel.setBorder(SiteBorder);
        SitePanel.setLayout(null);
        add(SitePanel);

/////////////////////////////////
        //실행시 차트 긁어옴
        MelonChartParser MelonChart = new MelonChartParser();
        MelonChart.htmlDataParsing();
        JSONArray MusicChart = new JSONArray();
        MusicChart = MelonChart.getChartList();

        String[] musicArr = MusicChart.toString().replace("\\/","/").split("\"},\\{\"");
        //역슬래시 다 바꾸고 배열 한칸에 노래정보 하나씩 집어넣음

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 6; j++) {
                String[] tempStr = musicArr[i].toString().split("\",\"");
                switch (j){
                    case 0:
                        if(i == 0){
                            albumName[i] = tempStr[j].replace("[{\"albumName\":\"","");
                        }
                        else{
                            albumName[i] = tempStr[j].replace("albumName\":\"","");
                        }
                        break;
                    case 1:
                        releaseDate[i] = tempStr[j].replace("releaseDate\":\"","");
                        break;
                    case 2:
                        imageUrl[i] = tempStr[j].replace("imageUrl\":\"","");
                        break;
                    case 3:
                        genre[i] = tempStr[j].replace("genre\":\"","");
                        break;
                    case 4:
                        rank[i] = tempStr[j].replace("rank\":\"","");
                        break;
                    case 5:
                        if(i == 49){
                            title[i] = tempStr[j].replace("title\":\"","").replace("\"}]","");
                        }
                        else{
                            title[i] = tempStr[j].replace("title\":\"","");
                        }
                        break;
                }
            }//각각 할당된 태그 지우고 그 키값만 따로 저장
            musicInfo[Site_M_B_G][i] = new MusicInform(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i],Integer.parseInt(rank[i]));

            JPanel tempPanel = new JPanel();
            tempPanel.setBounds(10, 10 + i * 30, 800,30);
            tempPanel.setBackground(Color.white);
            tempPanel.setBorder(new LineBorder(Color.BLACK,1));
            tempPanel.setLayout(null);
            ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
            ChartLabel.setBounds(0,0,800,30);
            tempPanel.add(ChartLabel);
            ChartPanel[i] = tempPanel;
            SitePanel.add(ChartPanel[i]);

            musicInfo[Site_M_B_G][i].SysShow();
        }//SitePanel 에 한줄씩 띄움
    }//constructor



    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();

            if (obj == RefreshBtn) {
                current = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                switch (Site_M_B_G){
                    case 0:
                        formatted_Melon = current.format(formatter);
                        RefreshLabel.setText("Renewal time : " + formatted_Melon);
                        MelonChartParser MelonChart = new MelonChartParser();
                        MelonChart.htmlDataParsing();
                        JSONArray MusicChart = new JSONArray();
                        MusicChart = MelonChart.getChartList();

                        String[] arr = MusicChart.toString().replace("\\/","/").split("\"},\\{\"");

                        for (int i = 0; i < 50; i++) {

                            for (int j = 0; j < 6; j++) {
                                String[] tempStr = arr[i].toString().split("\",\"");
                                switch (j){
                                    case 0:
                                        if(i == 0){
                                            albumName[i] = tempStr[j].replace("[{\"albumName\":\"","");
                                        }
                                        else{
                                            albumName[i] = tempStr[j].replace("albumName\":\"","");
                                        }
                                        break;
                                    case 1:
                                        releaseDate[i] = tempStr[j].replace("releaseDate\":\"","");
                                        break;
                                    case 2:
                                        imageUrl[i] = tempStr[j].replace("imageUrl\":\"","");
                                        break;
                                    case 3:
                                        genre[i] = tempStr[j].replace("genre\":\"","");
                                        break;
                                    case 4:
                                        rank[i] = tempStr[j].replace("rank\":\"","");
                                        break;
                                    case 5:
                                        if(i == 49){
                                            title[i] = tempStr[j].replace("title\":\"","").replace("\"}]","");
                                        }
                                        else{
                                            title[i] = tempStr[j].replace("title\":\"","");
                                        }
                                        break;
                                }
                            }
                            musicInfo[Site_M_B_G][i] = new MusicInform(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i],Integer.parseInt(rank[i]));

                            JPanel tempPanel = new JPanel();
                            tempPanel.setBounds(10, 10 + i * 30, 800,30);
                            tempPanel.setBackground(Color.white);
                            tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                            tempPanel.setLayout(null);
                            ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
                            ChartLabel.setBounds(0,0,80,30);
                            tempPanel.add(ChartLabel);
                            ChartPanel[i] = tempPanel;
                            SitePanel.add(ChartPanel[i]);

                            musicInfo[Site_M_B_G][i].SysShow();
                        }
                        break;
                    case 1:
                        formatted_Bugs = current.format(formatter);
                        RefreshLabel.setText("Renewal time : " + formatted_Bugs);
//                        BugsChartParser BugsChart = new BugsChartParser();
//                        BugsChart.htmlDataParsing();
//                        JSONArray MusicChart = new JSONArray();
//                        MusicChart = BugsChart.getChartList();
//
//                        String[] arr = MusicChart.toString().replace("\\/","/").split("\"},\\{\"");
//                        for (int i = 0; i < 50; i++) {
//
//                            for (int j = 0; j < 6; j++) {
//                                String[] tempStr = arr[i].toString().split("\",\"");
//                                switch (j) {
//                                    case 0:
//                                        if (i == 0) {
//                                            albumName[i] = tempStr[j].replace("[{\"albumName\":\"", "");
//                                        } else {
//                                            albumName[i] = tempStr[j].replace("albumName\":\"", "");
//                                        }
//                                        break;
//                                    case 1:
//                                        releaseDate[i] = tempStr[j].replace("releaseDate\":\"", "");
//                                        break;
//                                    case 2:
//                                        imageUrl[i] = tempStr[j].replace("imageUrl\":\"", "");
//                                        break;
//                                    case 3:
//                                        genre[i] = tempStr[j].replace("genre\":\"", "");
//                                        break;
//                                    case 4:
//                                        rank[i] = tempStr[j].replace("rank\":\"", "");
//                                        break;
//                                    case 5:
//                                        if (i == 49) {
//                                            title[i] = tempStr[j].replace("title\":\"", "").replace("\"}]", "");
//                                        } else {
//                                            title[i] = tempStr[j].replace("title\":\"", "");
//                                        }
//                                        break;
//                                }
//                            }
//                            musicInfo[Site_M_B_G][i] = new MusicInfo(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i], Integer.parseInt(rank[i]));
//
//                            JPanel tempPanel = new JPanel();
//                            tempPanel.setBounds(10, 10 + i * 30, 800, 30);
//                            tempPanel.setBackground(Color.white);
//                            tempPanel.setBorder(new LineBorder(Color.BLACK, 1));
//                            tempPanel.setLayout(null);
//                            ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
//                            ChartLabel.setBounds(0, 0, 80, 30);
//                            tempPanel.add(ChartLabel);
//                            ChartPanel[i] = tempPanel;
//                            SitePanel.add(ChartPanel[i]);
//
//                            musicInfo[Site_M_B_G1][i].SysShow();
//                        }


                        break;
                    case 2:
                        formatted_Genie = current.format(formatter);
                        RefreshLabel.setText("Renewal time : " + formatted_Genie);
//                        GenieChartParser GenieChart = new GenieChartParser();
//                        GenieChart.htmlDataParsing();
//                        JSONArray MusicChart = new JSONArray();
//                        MusicChart = GenieChart.getChartList();
//
//                        String[] arr = MusicChart.toString().replace("\\/","/").split("\"},\\{\"");
//                        for (int i = 0; i < 50; i++) {
//
//                            for (int j = 0; j < 6; j++) {
//                                String[] tempStr = arr[i].toString().split("\",\"");
//                                switch (j) {
//                                    case 0:
//                                        if (i == 0) {
//                                            albumName[i] = tempStr[j].replace("[{\"albumName\":\"", "");
//                                        } else {
//                                            albumName[i] = tempStr[j].replace("albumName\":\"", "");
//                                        }
//                                        break;
//                                    case 1:
//                                        releaseDate[i] = tempStr[j].replace("releaseDate\":\"", "");
//                                        break;
//                                    case 2:
//                                        imageUrl[i] = tempStr[j].replace("imageUrl\":\"", "");
//                                        break;
//                                    case 3:
//                                        genre[i] = tempStr[j].replace("genre\":\"", "");
//                                        break;
//                                    case 4:
//                                        rank[i] = tempStr[j].replace("rank\":\"", "");
//                                        break;
//                                    case 5:
//                                        if (i == 49) {
//                                            title[i] = tempStr[j].replace("title\":\"", "").replace("\"}]", "");
//                                        } else {
//                                            title[i] = tempStr[j].replace("title\":\"", "");
//                                        }
//                                        break;
//                                }
//                            }
//                            musicInfo[Site_M_B_G][i] = new MusicInfo(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i], Integer.parseInt(rank[i]));
//
//                            JPanel tempPanel = new JPanel();
//                            tempPanel.setBounds(10, 10 + i * 30, 800, 30);
//                            tempPanel.setBackground(Color.white);
//                            tempPanel.setBorder(new LineBorder(Color.BLACK, 1));
//                            tempPanel.setLayout(null);
//                            ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
//                            ChartLabel.setBounds(0, 0, 80, 30);
//                            tempPanel.add(ChartLabel);
//                            ChartPanel[i] = tempPanel;
//                            SitePanel.add(ChartPanel[i]);
//
//                            musicInfo[Site_M_B_G][i].SysShow();
//                        }
                        break;
                }
            }//refresh 새로 파싱해옴
            else if (obj == SiteBtn_M) {
                //////////////////////////////////////////////
                ///Need code to erase and repaint SitePanel///
                //////////////////////////////////////////////

                Site_M_B_G = 0;
                for (int i = 0; i < 50; i++) {
                    musicInfo[Site_M_B_G][i] = new MusicInform(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i],Integer.parseInt(rank[i]));

                    JPanel tempPanel = new JPanel();
                    tempPanel.setBounds(10, 10 + i * 30, 800,30);
                    tempPanel.setBackground(Color.white);
                    tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                    tempPanel.setLayout(null);
                    ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
                    ChartLabel.setBounds(0,0,800,30);
                    tempPanel.add(ChartLabel);
                    ChartPanel[i] = tempPanel;
                    SitePanel.add(ChartPanel[i]);

                    musicInfo[Site_M_B_G][i].SysShow();
                }
                RefreshLabel.setText("Renewal time : " + formatted_Melon);
            }
            else if (obj == SiteBtn_B) {
                //////////////////////////////////////////////
                ///Need code to erase and repaint SitePanel///
                //////////////////////////////////////////////

                Site_M_B_G = 1;
                for (int i = 0; i < 50; i++) {
                    musicInfo[Site_M_B_G][i] = new MusicInform(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i],Integer.parseInt(rank[i]));

                    JPanel tempPanel = new JPanel();
                    tempPanel.setBounds(10, 10 + i * 30, 800,30);
                    tempPanel.setBackground(Color.white);
                    tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                    tempPanel.setLayout(null);
                    ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
                    ChartLabel.setBounds(0,0,800,30);
                    tempPanel.add(ChartLabel);
                    ChartPanel[i] = tempPanel;
                    SitePanel.add(ChartPanel[i]);

                    musicInfo[Site_M_B_G][i].SysShow();
                }
                RefreshLabel.setText("Renewal time : " + formatted_Bugs);
            }
            else if (obj == SiteBtn_G) {
                //////////////////////////////////////////////
                ///Need code to erase and repaint SitePanel///
                //////////////////////////////////////////////

                Site_M_B_G = 2;
                for (int i = 0; i < 50; i++) {
                    musicInfo[Site_M_B_G][i] = new MusicInform(albumName[i], releaseDate[i], imageUrl[i], genre[i], title[i],Integer.parseInt(rank[i]));

                    JPanel tempPanel = new JPanel();
                    tempPanel.setBounds(10, 10 + i * 30, 800,30);
                    tempPanel.setBackground(Color.white);
                    tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                    tempPanel.setLayout(null);
                    ChartLabel = new JLabel(musicInfo[Site_M_B_G][i].showList());
                    ChartLabel.setBounds(0,0,800,30);
                    tempPanel.add(ChartLabel);
                    ChartPanel[i] = tempPanel;
                    SitePanel.add(ChartPanel[i]);

                    musicInfo[Site_M_B_G][i].SysShow();
                }
                RefreshLabel.setText("Renewal time : " + formatted_Genie);
            }
            else if (obj == SearchBtn) {
                SearchMusic = Searchtxt.getText();
                System.out.println(SearchMusic);
                Searchtxt.setText("");
                //////////////////////////////////////////////
                ///Need code to erase and repaint SitePanel///
                //////////////////////////////////////////////

                for (int i = 0; i < 50; i++) {
                    if(strCombo.getSelectedItem().toString() == "Name" && musicInfo[Site_M_B_G][i].getTitle().contains(SearchMusic)){
                        JPanel tempPanel = new JPanel();
                        tempPanel.setBounds(10, 10 + i * 30, 800,30);
                        tempPanel.setBackground(Color.white);
                        tempPanel.setBorder(new LineBorder(Color.BLACK,1));
                        tempPanel.setLayout(null);
                        ChartLabel = new JLabel(musicInfo[0][i].showList());
                        ChartLabel.setBounds(0,0,80,30);
                        tempPanel.add(ChartLabel);
                        ChartPanel[i] = tempPanel;
                        SitePanel.add(ChartPanel[i]);

                        musicInfo[0][i].SysShow();
                    }
                    else if(strCombo.getSelectedItem().toString()=="Artist"){

                    }
                }
            }//Search
        }
    }//ButtonListener
}
