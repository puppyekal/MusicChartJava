import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private String strSelectPassword;

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
        pnlMain.add(btnBack);

        arrComment = new ArrayList<>();
        arrPassword = new ArrayList<>();
        listComment = new JList();

        modelList = new DefaultListModel();
        readComment();
        addMusicInfo();
        addList();
    }//Constructor

    private void addMusicInfo(/*Music clsMusicInfo*/){
        /*
        strSinger = clsMusicInfo.getStrSinger();
        strTitle = clsMusicInfo.getStrName();
        strAlbumTitle = clsMusicInfo.getStrAlbum();
        */
        JLabel lblTitle,lblSinger;
        Font fnt2 = new Font("±¼¸²",Font.BOLD,30);

        lblTitle = new JLabel("»ß»ß"/*clsMusicInfo.getStrName()*/);
        lblTitle.setFont(fnt2);
        lblTitle.setBounds(0,40,160,30);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblTitle);

        lblSinger = new JLabel("IU"/*clsMusicInfo.getStrSinger()*/);
        lblSinger.setFont(fnt2);
        lblSinger.setBounds(0,110,160,30);
        lblSinger.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblSinger);
        //imgAlbumImage = clsMusicInfo.getImage();

        Font fnt1 = new Font("±¼¸²",Font.PLAIN,20);

        JLabel lblStrTitle = new JLabel("Title");
        lblStrTitle.setFont(fnt1);
        lblStrTitle.setBounds(0,10,160,20);
        lblStrTitle.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblStrTitle);

        JLabel lblStrSinger = new JLabel("Singer");
        lblStrSinger.setFont(fnt1);
        lblStrSinger.setBounds(0,80,160,20);
        lblStrSinger.setHorizontalAlignment(JLabel.CENTER);
        pnlMusicInfo.add(lblStrSinger);



    }//addMusicInfo

    private void addList(){
        for(String ptr:arrComment){
            modelList.addElement(ptr);
        }
        listComment.setModel(modelList);
        listComment.setBounds(0,0,480,400);
        pnlCommentField.add(listComment);
    }


    private void readComment(/*Music clsMusicInfo*/){
        File file;
        //file = new File("comments\\" + clsMusicInfo.getStrName()+ ".txt");
        try {
            file = new File("comments\\ºñµµ ¿À°í ±×·¡¼­.txt");
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

    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if(obj == btnRegister){
                File file = new File("comments\\" + "ºñµµ ¿À°í ±×·¡¼­" + ".txt");
                try {
                    FileWriter fw = new FileWriter(file,true);
                    fw.write(txtComment.getText() + "\r");
                    fw.write(txtPassword.getText() + "\r");
                    fw.flush();
                    fw.close();
                    modelList.addElement(txtComment.getText());
                    txtComment.setText("");
                    txtPassword.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }//obj == btnRegister
            if(obj == btnDelete){
                if(txtPassword.getText() == arrPassword.get(listComment.getSelectedIndex())){
                    modelList.removeElementAt(listComment.getSelectedIndex());
                }
            }
            if(obj == btnBack){

            }
        }//actionPerfomed
    }//ButtonRegister

}//CommentUI



