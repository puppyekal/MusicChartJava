import javax.swing.*;
import java.awt.*;

public class MusicInfo extends JPanel {
    private JLabel _lblSinger;
    private JLabel _lblTitle;
    private JLabel _lblLike;
    private int _nLike;
    private Font _fnt;

    public MusicInfo(){
        setBackground(Color.cyan);
        setForeground(Color.black);
        setPreferredSize(new Dimension(940,50));
        setLayout(null);

        _fnt = new Font("veranda", Font.BOLD,20);

        _lblSinger = new JLabel("IU");
        _lblSinger.setForeground(Color.black);
        _lblSinger.setFont(_fnt);
        _lblSinger.setBounds(300,10,310,30);
        add(_lblSinger);

        _lblTitle = new JLabel("Love Poem");
        _lblTitle.setForeground(Color.black);
        _lblTitle.setBounds(10,10,300,30);
        _lblTitle.setFont(_fnt);
        add(_lblTitle);

        _nLike = 0;
        _lblLike = new JLabel("Like : " + String.valueOf(_nLike));
        _lblLike.setForeground(Color.black);
        _lblLike.setBounds(610,10,200,30);
        _lblLike.setFont(_fnt);
        add(_lblLike);
    }

    public String get_strSinger(){return _lblSinger.getText();}
    public String get_strTitle(){return _lblTitle.getText();}

    public void set_lblSinger(JLabel _lblSinger) {
        this._lblSinger = _lblSinger;
    }
}
