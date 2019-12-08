import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("음악의 민족");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new MakeComment();

        frame.getContentPane().add(AppManager.getS_instance().getPrimaryPanel());
        AppManager.getS_instance().addToPrimaryPanel(AppManager.getS_instance().getPnlChartPrimary());

        frame.pack();
        frame.setVisible(true);
    }
}
