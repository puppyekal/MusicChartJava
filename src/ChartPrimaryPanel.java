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
    private JComboBox<String> strCombo;

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

        new MakeComment(this);

        setBackground(new Color(255, 255, 255, 0));
        setBounds(1,0,1278,960);
        setLayout(null);

        ButtonRefresh = new ButtonListener(this);
        ButtonMelon = new ButtonListener(this);
        ButtonBugs = new ButtonListener(this);
        ButtonGenie = new ButtonListener(this);
        ButtonSearch = new ButtonListener();

        btnRefresh = new JButton(new ImageIcon("Image/Refresh.png"));
        btnRefresh.setBounds(30,30,40,40);
        btnRefresh.setForeground(Color.DARK_GRAY);
        btnRefresh.setBackground(Color.lightGray);
        btnRefresh.addActionListener(ButtonRefresh);
        add(btnRefresh);

        strCombo = new JComboBox<String>(strSearchCategory);
        strCombo.setBounds(100, 30, 150,40);
        strCombo.setEditable(false);
        add(strCombo);

        txtSearch = new JTextField();
        txtSearch.setBounds(250,30,840,40);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 25));
        txtSearch.addKeyListener(new KeyActionListener());
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(1090,30,150,40);
        btnSearch.setForeground(Color.DARK_GRAY);
        btnSearch.setBackground(Color.lightGray);
        btnSearch.addActionListener(ButtonSearch);
        add(btnSearch);

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

        pnlSitePanel = new SitePanel();
        pnlSitePanel.setBounds(100,140,1080,700);
        LineBorder SiteBorder = new LineBorder(Color.BLACK,3);
        pnlSitePanel.setBorder(SiteBorder);
        pnlSitePanel.setLayout(null);
        add(pnlSitePanel);

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatted = current.format(formatter);
        //현재시간을 정해진 표기데로 출력해주는 메소드
        lblTime = new JLabel("Renewal time : " + formatted);
        System.out.println("Renewal time : " + formatted);

        lblTime.setBounds(900,870,200,40);
        lblTime.setFont(new Font("Verdana", Font.BOLD + Font.PLAIN, 14));
        lblTime.setBackground(Color.lightGray);
        lblTime.setHorizontalAlignment(SwingConstants.CENTER);
        lblTime.setOpaque(true);
        add(lblTime);

    }//constructor



    private class ButtonListener implements ActionListener {
        private Component _viewLoading;
        public ButtonListener() { }
        public ButtonListener(Component parentComponent){
            _viewLoading = parentComponent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj == btnRefresh) {
                current = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                switch (AppManager.getS_instance().getSite_M_B_G()){
                    case 1:
                        formatted_Melon = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Melon);
                        AppManager.getS_instance().setSite_M_B_G(1);
                        AppManager.getS_instance().DataPassing(_viewLoading);
                        System.out.println("why?");
                        break;
                    case 2:
                        formatted_Bugs = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Bugs);
                        AppManager.getS_instance().setSite_M_B_G(2);
                        AppManager.getS_instance().DataPassing(_viewLoading);
                        break;
                    case 3:
                        formatted_Genie = current.format(formatter);
                        lblTime.setText("Renewal time : " + formatted_Genie);
                        AppManager.getS_instance().setSite_M_B_G(3);
                        AppManager.getS_instance().DataPassing(_viewLoading);
                        break;
                }
            }//refresh 새로 파싱해옴//파싱시간도 갱신
            if (obj == btnSite_M) {
                if(AppManager.getS_instance().getSite_M_B_G() == 1) return;
                AppManager.getS_instance().setSite_M_B_G(1);
                if(!AppManager.getS_instance().getParser().isParsed()) AppManager.getS_instance().DataPassing(_viewLoading);
                System.out.println("Melon");
                pnlSitePanel.changeData();
                lblTime.setText("Renewal time : " + formatted_Melon);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
            if (obj == btnSite_B) {
                if(AppManager.getS_instance().getSite_M_B_G() == 2) return;
                AppManager.getS_instance().setSite_M_B_G(2);
                if(!AppManager.getS_instance().getParser().isParsed()) AppManager.getS_instance().DataPassing(_viewLoading);
                System.out.println("Bugs");
                pnlSitePanel.changeData();
                lblTime.setText("Renewal time : " + formatted_Bugs);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
            if (obj == btnSite_G) {
                if(AppManager.getS_instance().getSite_M_B_G() == 3) return;
                AppManager.getS_instance().setSite_M_B_G(3);
                if(!AppManager.getS_instance().getParser().isParsed()) AppManager.getS_instance().DataPassing(_viewLoading);
                System.out.println("Genie");
                pnlSitePanel.changeData();
                lblTime.setText("Renewal time : " + formatted_Genie);
                txtSearch.setText("");
                pnlSitePanel.filter(null,2);
            }
            //멜론, 벅스, 지니 버튼에 따라 저장해 놓은 데이터를 새로 갖고옴
            //저장되있던 불러온 시간도 같이 갖고옴
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
                if(0 == strCombo.getSelectedIndex())//Name
                    pnlSitePanel.filter(txtSearch.getText(),2);
                if(1 == strCombo.getSelectedIndex())//Artist
                    pnlSitePanel.filter(txtSearch.getText(),3);
            }//comboBox 0, 1일때 sitepanel의 filter에서 검색

        }//KeyReleased

    }//KeyListener
}