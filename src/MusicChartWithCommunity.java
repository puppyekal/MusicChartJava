import javax.swing.*;

public class MusicChartWithCommunity {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Music Chart With Community");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        ChartPrimaryPanel primary = new ChartPrimaryPanel();
        frame.getContentPane().add(primary);


        frame.pack();
        frame.setVisible(true);
    }
}
