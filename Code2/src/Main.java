import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("¿Ωæ«¿« πŒ¡∑");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChartPrimaryPanel primaryPanel = new ChartPrimaryPanel();
//        MelonChartParser melon = new MelonChartParser();
  //      melon.chartDataParsing();
    //    SitePanel sitePanel = new SitePanel("MELON",melon);
      //  primaryPanel.add(sitePanel);

        frame.getContentPane().add(primaryPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
