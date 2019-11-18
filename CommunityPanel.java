import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CommunityPanel extends JPanel {
    private JPanel _pnlMusicInfo;
    private JPanel _pnlCommentArea;
    private JButton _btnAddComment;
    private JTextField _txtpassword;
    private JTextField _txtComment;
    private File _fileComment;
    //constructor
    public CommunityPanel(){
        setBackground(Color.white);
        setForeground(Color.black);
        setPreferredSize(new Dimension(1000,700));
        setLayout(null);

        _pnlMusicInfo = new MusicInfo();
        _pnlMusicInfo.setBounds(30,30,940,50);
        add(_pnlMusicInfo);

        _pnlCommentArea = new JPanel();
        _pnlCommentArea.setBounds(30,30,940,540);
        _pnlCommentArea.setBackground(Color.DARK_GRAY);
        add(_pnlCommentArea);

        _btnAddComment = new JButton("등록");
        _btnAddComment.setForeground(Color.black);
        _btnAddComment.setBackground(Color.white);
        _btnAddComment.setBounds(830,610,140,70);
        _btnAddComment.addActionListener(new RegistrationListener());
        add(_btnAddComment);

        _txtComment = new JTextField();
        _txtComment.setBounds(30,580,800,100);
        add(_txtComment);

       _txtpassword = new JTextField();
        _txtpassword.setBounds(830,580,140,30);
        add(_txtpassword);


    }

    public void makeRandomComment(){

    }

    private class RegistrationListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if(obj == _btnAddComment){
                //버튼 눌렀을 시 작동 할 것들
            }
        }//actionPerformed
    }//ActionListener

}//CommunityPanel
