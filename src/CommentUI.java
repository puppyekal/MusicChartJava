import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
    private JLabel lblTitle,lblArtist;

    public CommentUI(){
        setPreferredSize(new Dimension(1000,1000));
        setBackground(new Color(0,0,0,30));
        setLayout(null);

        pnlMain = new JPanel();
        pnlMain.setBackground(Color.white);
        pnlMain.setBounds(150,250,700,500);
        pnlMain.setLayout(null);
        add(pnlMain);

        pnlCommentField = new JPanel();
        pnlCommentField.setBackground(Color.white);
        pnlCommentField.setBounds(20,20,480,400);
        pnlCommentField.setLayout(null);
        pnlMain.add(pnlCommentField);

        pnlMusicInfo = new JPanel();
        pnlMusicInfo.setBackground(new Color(200,200,200));
        pnlMusicInfo.setBounds(520,20,160,370);
        pnlMusicInfo.setLayout(null);
        pnlMain.add(pnlMusicInfo);

        txtComment = new JTextField();
        txtComment.setBounds(20,440,480,40);
        pnlMain.add(txtComment);

        txtPassword = new JTextField();
        txtPassword.setBounds(520,400,100,20);
        pnlMain.add(txtPassword);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(520,440,160,40);
        btnRegister.addActionListener(new ButtonListener());
        pnlMain.add(btnRegister);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(620,400,60,20);
        btnDelete.addActionListener(new ButtonListener());
        pnlMain.add(btnDelete);

        btnBack = new JButton("X");
        btnBack.setBounds(660,0,20,20);
        btnBack.addActionListener(new ButtonListener());
        pnlMain.add(btnBack);

        lblTitle = new JLabel();
        lblTitle.setBounds(0,40,160,30);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblTitle);

        lblArtist = new JLabel();
        lblArtist.setBounds(0,110,160,30);
        lblArtist.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblArtist);

        Font fnt1 = new Font("±¼¸²",Font.PLAIN,20);

        lblStrArtist = new JLabel("Artist");
        lblStrArtist.setFont(fnt1);
        lblStrArtist.setBounds(0,80,160,20);
        lblStrArtist.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblStrArtist);

        lblStrTitle = new JLabel("Title");
        lblStrTitle.setFont(fnt1);
        lblStrTitle.setBounds(0,10,160,20);
        lblStrTitle.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblStrTitle);

        arrComment = new ArrayList<>();
        arrPassword = new ArrayList<>();
        listComment = new JList();

        modelList = new DefaultListModel();
    }//Constructor

    private void addMusicInfo(){

        lblTitle.setText(strTitle);
        lblTitle.setFont(new Font("±¼¸²",Font.BOLD,160 / strTitle.length()));
        lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);

        lblArtist.setText(strArtist);
        lblArtist.setFont(new Font("±¼¸²",Font.BOLD,160 / strArtist.length()));
        //imgAlbumImage = clsMusicInfo.getImage();

    }//addMusicInfo

    private void addList(){
        for(String ptr:arrComment){
            modelList.addElement(ptr);
        }
        listComment.setModel(modelList);
        listComment.setBounds(0,0,480,400);
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
            if(obj == btnRegister){
                File file = new File("comments\\" + AppManager.getS_instance().getParser().getTitle(nRank) + ".txt");
                try {
                    FileWriter fw = new FileWriter(file,true);
                    fw.write(txtComment.getText() + "\r");
                    fw.write(txtPassword.getText() + "\r");
                    fw.flush();
                    fw.close();
                    modelList.addElement(txtComment.getText());

                    arrComment.add(txtComment.getText());
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

