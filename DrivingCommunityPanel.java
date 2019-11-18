import javax.swing.*;

public class DrivingCommunityPanel {
    public static void main(String[] args){
        JFrame frame = new JFrame("Driving Class");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CommunityPanel primary = new CommunityPanel();
        primary.setVisible(true);
        frame.getContentPane().add(primary);

        frame.pack();
        frame.setVisible(true);
    }
}
