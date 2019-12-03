import org.json.simple.JSONArray;

import javax.swing.*;
import java.awt.*;

public class AppManger {
    private static AppManger s_instance;
    private int Site_M_B_G;
    private MelonChartParser melon;
    private BugsChartParser bugs;
    private GenieChartParser genie;
    private JSONArray[] chartData;
    private CommentUI pnlComment;
    private ChartPrimaryPanel pnlChartPrimary;
    private JPanel primary;

    public AppManger(){
        Site_M_B_G = 1;
        chartData = new JSONArray[3];

        melon = new MelonChartParser();
        bugs = new BugsChartParser();
        genie = new GenieChartParser();

    }

    public JPanel getPrimary() {
        if(primary == null)
            primary = new JPanel();
        primary.setPreferredSize(new Dimension(1000,1000));
        primary.setLayout(null);
        primary.setVisible(true);
        return primary;
    }

    public CommentUI getPnlComment() {
        return pnlComment;
    }
    public ChartPrimaryPanel getPnlChartPrimary() {
        if(pnlChartPrimary == null)
            pnlChartPrimary = new ChartPrimaryPanel();
        return pnlChartPrimary;
    }


    public void setSite_M_B_G(int M_B_G){
        Site_M_B_G = M_B_G;
    }

    public int getSite_M_B_G() {
        return Site_M_B_G;
    }

    public MusicChartParser getParser(){
        System.out.println("Parsing Data");
        switch (Site_M_B_G){
            case 1:
                return melon;
            case 2:
                return bugs;
            case 3:
                return genie;
        }
        return null;
    }

    public void DataPassing(){
        switch (Site_M_B_G){
            case 1:{
                melon.chartDataParsing();
                break;
            }
            case 2:{
                bugs.chartDataParsing();
                break;
            }
            case 3:{
                genie.chartDataParsing();
                break;
            }
        }
    }

    public JSONArray getJSONArrayData(){
        return chartData[Site_M_B_G];
    }

    public static AppManger getS_instance() {
        if(s_instance == null)
            s_instance = new AppManger();
    return s_instance;
    }

}
