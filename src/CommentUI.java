import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommentUI extends JPanel {

    private JPanel pnlMain,pnlCommentField,pnlMusicInfo;
    private JTextField txtComment,txtPassword;
    private JButton btnRegister,btnDelete,btnBack;
    private ArrayList<String> arrComment;
    private ArrayList<String> arrPassword;
    private JList listComment;
    private DefaultListModel modelList;
    private int nRank;
    private String strTitle,strArtist,strAlbum,strReadTitle;
    private JLabel lblStrTitle,lblStrArtist;
    private JLabel lblTitle,lblArtist,lblImage;
    private ImageIcon imgAlbum;


    public CommentUI(){
        setPreferredSize(new Dimension(1024,768));
        setBackground(new Color(0,0,0,25));
        setLayout(null);

        setBounds(128,96,1024,768);
        setLayout(null);

        pnlMusicInfo = new JPanel();
        pnlMusicInfo.setBackground(new Color(200,200,200));
        pnlMusicInfo.setBounds(32,32,960,80);
        pnlMusicInfo.setLayout(null);
        add(pnlMusicInfo);

        pnlCommentField = new JPanel();
        pnlCommentField.setBackground(Color.white);
        pnlCommentField.setBounds(32,144,960,520);
        pnlCommentField.setLayout(null);
        add(pnlCommentField);

        txtComment = new JTextField();
        txtComment.setBounds(32,696,800,40);
        add(txtComment);

        txtPassword = new JTextField();
        txtPassword.setBounds(832,676,80,20);
        add(txtPassword);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(832,696,160,40);
        btnRegister.setBackground(Color.WHITE);
        btnRegister.addActionListener(new ButtonListener());
        add(btnRegister);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(912,676,80,20);
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setFont(new Font("한강남산체 M",Font.PLAIN,13));
        btnDelete.addActionListener(new ButtonListener());
        add(btnDelete);

        btnBack = new JButton("Back");
        btnBack.setBounds(964,0,60,30);
        btnBack.setFont(new Font("배달의민족 을지로체 TTF",Font.PLAIN,12));
        btnBack.setBackground(Color.WHITE);
        btnBack.addActionListener(new ButtonListener());
        add(btnBack);


        lblTitle = new JLabel();
        lblTitle.setBounds(110,10,400,60);
        lblTitle.setHorizontalAlignment(JLabel.LEFT);
        lblTitle.setFont(new Font("서울남산체 B",Font.PLAIN,40));
        pnlMusicInfo.add(lblTitle);

        lblArtist = new JLabel();
        lblArtist.setBounds(670,10,220,60);
        lblArtist.setFont(new Font("서울남산체 B",Font.PLAIN,40));
        lblArtist.setHorizontalAlignment(JLabel.LEFT);
        pnlMusicInfo.add(lblArtist);

        Font fnt1 = new Font("한강남산체 M",Font.BOLD,30);

        lblStrArtist = new JLabel("Artist : ");
        lblStrArtist.setFont(fnt1);
        lblStrArtist.setBounds(510,10,160,60);
        lblStrArtist.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblStrArtist);

        lblStrTitle = new JLabel("Title : ");
        lblStrTitle.setFont(fnt1);
        lblStrTitle.setBounds(10,10,100,60);
        pnlMusicInfo.add(lblStrTitle);

        imgAlbum = new ImageIcon();
        lblImage = new JLabel(imgAlbum);
        lblImage.setBounds(900,10,50,50);
        pnlMusicInfo.add(lblImage);

        arrComment = new ArrayList<>();
        arrPassword = new ArrayList<>();
        listComment = new JList();

        modelList = new DefaultListModel();
    }//Constructor

    private void addMusicInfo(){
        String strRefinedTitle = strTitle;
        if(strRefinedTitle.indexOf("(") != -1){
            strRefinedTitle = strRefinedTitle.substring(0,strRefinedTitle.indexOf("("));
        }
        lblTitle.setText(strRefinedTitle);

        String strRefinedArtist = strArtist;
        if(strRefinedArtist.indexOf("(") != -1){
            strRefinedArtist = strRefinedArtist.substring(0,strRefinedArtist.indexOf("("));
        }
        lblArtist.setText(strRefinedArtist);



    }//addMusicInfo

    private void addList(){
        for(String ptr:arrComment){
            modelList.addElement(ptr);
        }
        listComment.setModel(modelList);
        listComment.setFont(new Font("서울한강체 M",Font.PLAIN,20));
        listComment.setBounds(0,0,960,400);
        pnlCommentField.add(listComment);
    }

    public void reNewalInfo(int rank){
        nRank = rank;
        this.setVisible(true);
        strTitle = AppManager.getS_instance().getParser().getTitle(rank);
        strArtist = AppManager.getS_instance().getParser().getArtistName(rank);
        strAlbum = AppManager.getS_instance().getParser().getAlbumName(rank);



        strReadTitle = strTitle;
        strReadTitle = strReadTitle.replace("\'", "");
        if (strReadTitle.indexOf("(") != -1)
            strReadTitle = strReadTitle.substring(0, strReadTitle.indexOf("("));
        strReadTitle = strReadTitle.replace(" ", "");
        strReadTitle = strReadTitle.replace("\'", "");

        readComment();
        addList();
        addMusicInfo();
    }

    private void readComment(){
        File file;
        System.out.println("Read " + strTitle + ".txt File");
        file = new File("comments\\" + strReadTitle + ".txt");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader inFiles = new BufferedReader(fr);
            String strline = "";
            while( (strline = inFiles.readLine()) != null) {
                arrComment.add(strline);
                arrPassword.add(inFiles.readLine());
            }
            fr.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }//readComment

    private void removeAtTxt(int index){
        System.out.println(index);
        File file = new File("comments\\" + strReadTitle + ".txt");
        ArrayList<String> dummy = new ArrayList<String>();
        try{
            FileReader fr = new FileReader(file);
            BufferedReader inFile = new BufferedReader(fr);
            for(int i = 0 ; i < index * 2 ; i++){
                dummy.add(inFile.readLine() + "\r");
            }
            inFile.readLine();
            inFile.readLine();
            String strTemp;
            while( (strTemp = inFile.readLine()) != null){
                dummy.add(strTemp + "\r");
            }
            FileWriter fw = new FileWriter(file,false);
            for(String str : dummy){
                fw.write(str);
            }
            fw.flush();

            fw.close();
            fr.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearAll(){
        txtPassword.setText("");
        txtComment.setText("");
        lblArtist.setText("");
        lblTitle.setText("");

        modelList.clear();
        arrComment.clear();
        arrPassword.clear();
    }

    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if(obj == btnRegister && !txtComment.getText().equals("") ){
                File file = new File("comments\\" + strReadTitle + ".txt");
                try {
                    FileWriter fw = new FileWriter(file,true);
                    fw.write(txtComment.getText() + "\r");
                    if( txtPassword.getText().equals("") )
                        fw.write("0000\r");
                    else
                        fw.write(txtPassword.getText() + "\r");
                    fw.flush();
                    fw.close();
                    modelList.addElement(txtComment.getText());

                    arrComment.add(txtComment.getText());
                    if( txtPassword.getText().equals("") )
                        arrPassword.add("0000");
                    else
                        arrPassword.add(txtPassword.getText());

                    txtComment.setText("");
                    txtPassword.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }//obj == btnRegister
            if(obj == btnDelete){
                if(Integer.parseInt(txtPassword.getText()) == Integer.parseInt(arrPassword.get(listComment.getSelectedIndex()))){
                    System.out.println("Same Password! At : " + String.valueOf(listComment.getSelectedIndex()));
                    arrPassword.remove(listComment.getSelectedIndex());
                    arrComment.remove(listComment.getSelectedIndex());
                    removeAtTxt(listComment.getSelectedIndex());
                    modelList.removeElementAt(listComment.getSelectedIndex());
                }
                txtPassword.setText("");
            }
            if(obj == btnBack){
                clearAll();
                AppManager.getS_instance().BackToChartPrimaryPanel();
                System.out.println("Back To ChartPrimary");
            }
        }//actionPerfomed
    }//ButtonRegister


}//CommentUI

