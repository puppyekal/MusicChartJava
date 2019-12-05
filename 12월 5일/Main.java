import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("������ ����");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(AppManager.getS_instance().getPnlChartPrimary());


        frame.pack();
        frame.setVisible(true);
    }
}
