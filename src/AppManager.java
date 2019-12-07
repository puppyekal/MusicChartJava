import org.json.simple.JSONArray;

import javax.swing.*;
import java.awt.*;

public class AppManager {
    private static AppManager s_instance;
    private int Site_M_B_G;
    private MelonChartParser melon;
    private BugsChartParser bugs;
    private GenieChartParser genie;
    private JSONArray[] chartData;
    private CommentUI pnlComment;
    private ChartPrimaryPanel pnlChartPrimary;

    private AppManager(){
    	s_instance = this;
    	
        Site_M_B_G = 1;
        chartData = new JSONArray[3];
        chartData[0] = new JSONArray();
        chartData[1] = new JSONArray();
        chartData[2] = new JSONArray();

        melon = new MelonChartParser();
        bugs = new BugsChartParser();
        genie = new GenieChartParser();
        
        pnlChartPrimary = new ChartPrimaryPanel();
    }
    
    public CommentUI getPnlComment() {
        return pnlComment;
    }
    public ChartPrimaryPanel getPnlChartPrimary() {
        return pnlChartPrimary;
    }
    public void setSite_M_B_G(int M_B_G){
        Site_M_B_G = M_B_G;
    }
    public int getSite_M_B_G() {
        return Site_M_B_G;
    }
    public MelonChartParser getMelonChartParser() {
		return melon;
	}
    public BugsChartParser getBugsChartParser() {
		return bugs;
	}
	public GenieChartParser getGenieChartParser() {
		return genie;
	}
	public MusicChartParser getParser() {
        switch (Site_M_B_G) {
            case 1:
                return melon;
            case 2:
                return bugs;
            case 3:
                return genie;
            default:
            	return null;
        }
    }
    
    public void DataPassing() {
    	System.out.println("Parsing Data");
        switch (Site_M_B_G) {
            case 1:
                melon.chartDataParsing();
                break;
            case 2:
                bugs.chartDataParsing();
                break;
            case 3:
                genie.chartDataParsing();
                break;
        }
    }
    
    public void setJSONArray(JSONArray arr, int index) {
    	switch(index) {
    	case 0:
    		chartData[0] = arr;
    		return;
    	case 1:
    		chartData[1] = arr;
    		return;
    	case 2:
    		chartData[2] = arr;
    		return;
    	default:
    		throw new IndexOutOfBoundsException("the length of chartData is 3");
    	}
    }
    public JSONArray getJSONArray(int index) {
    	switch(index) {
    	case 0:
    		return chartData[0];
    	case 1:
    		return chartData[1];
    	case 2:
    		return chartData[2];
    	default:
    		throw new IndexOutOfBoundsException("the length of chartData is 3");
    	}
    }
    public JSONArray getDisplayedJSONArray() {
        return chartData[Site_M_B_G - 1];
    }
    
    public static AppManager getS_instance() {
        if(s_instance == null) s_instance = new AppManager();
        return s_instance;
    }
}
