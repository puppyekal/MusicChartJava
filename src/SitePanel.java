import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//encoding: UTF-8
@SuppressWarnings("serial")
public class SitePanel extends JPanel {
	// - - - - - Field - - - - -
	//title displayed above the chart
	private JLabel lblTitle;
	
	//provider of the chart(either "Melon", "Bugs!", or "Genie")
	private String strChartName;
	
	//JTable displaying music chart
	private JTable tableChart;
	
	//model(format) of the chart
	private ChartModel tableModel;
	
	//manages sorting & filtering of the chart
	private TableRowSorter<ChartModel> tableSorter;
	
	//tableChart + a scroll bar
	private JScrollPane scrollBar;
	
	//background/foreground colors
	private static Color TITLECOLOR = new Color(52,54,84);
	private static Color TEXTCOLOR = Color.black;
	private static Color LBLBACKGROUND = new Color(234, 234, 234);
	private static Color LISTBACKGROUND = Color.white;
	
	//listener object
	private ClickListener clkListener;
	
	// - - - - - Constructor - - - - -
	public SitePanel() {
		strChartName = "Melon";
		clkListener = new ClickListener();
		
		setPreferredSize(new Dimension(1000, 660));
		setBounds(100, 140, 1000, 660);
		setBackground(LBLBACKGROUND);
		setLayout(null);
		setFont(new Font("맑은 고딕", Font.BOLD, 64));
		
		lblTitle = new JLabel(strChartName + " TOP 100");
		lblTitle.setBackground(Color.white);
		lblTitle.setForeground(TITLECOLOR);
		lblTitle.setBounds(80, 30, 840, 80);
		lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 48));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		MusicChartParser parser = AppManager.getS_instance().getParser();
		if(!parser.isParsed()) parser.chartDataParsing();
		
		tableModel = new ChartModel(parser.getChartList());
		
		tableSorter = new TableRowSorter<ChartModel>(tableModel);
		tableSorter.setComparator(0, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		}); //sort by rank
		
		tableChart = new JTable(tableModel);
		tableChart.setBackground(LISTBACKGROUND);
		tableChart.setForeground(TEXTCOLOR);
		tableChart.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		tableChart.setRowHeight(60);
		buildTable();
		tableChart.setRowSorter(tableSorter);
		tableChart.addMouseListener(clkListener);
		add(tableChart);
		
		scrollBar = new JScrollPane(tableChart, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBar.setBounds(80, 130, 840, 500);
		add(scrollBar);
	} //constructor
	
	/*
	Name: buildTable
	Parameter: -
	Returns: -
	Description: designs cells before putting something to display 
	*/
	private void buildTable() {
		TableColumn column;
		for(int i = 0; i < 5; i++) {
			column = tableChart.getColumnModel().getColumn(i);
			switch(i) {
				case 0: //rank
					column.setResizable(false);
					column.setPreferredWidth(40);
					break;
				case 1: //image
					column.setResizable(false);
					column.setPreferredWidth(60);
					break;
				case 2: //title
				case 4: //album
					column.setResizable(true);
					column.setPreferredWidth(280);
					column.setMinWidth(100);
					break;
				case 3: //artist
					column.setResizable(true);
					column.setPreferredWidth(180);
					column.setMinWidth(80);
					break;
			}
		} //set form of the columns
	} //method used in constructor & dataChange
	
	// - - - - - getter & setter - - - - -
	public JLabel getLblTitle() {
		return lblTitle;
	}
	public String getStrChartName() {
		return strChartName;
	}
	public void setStrChartName(String name) {
		strChartName = name;
	}
	public JTable getTableChart() {
		return tableChart;
	}
	public ChartModel getTableModel() {
		return tableModel;
	}
	
	// - - - - - functional method - - - - -
	
	/*
	Name: changeData
	Parameter: -
	Returns: -
	Description: change contents to show different chart or to refresh
	*/
	public void changeData() {
		switch(AppManager.getS_instance().getSite_M_B_G()){
			case 1:
				strChartName = "Melon";
				break;
			case 2:
				strChartName = "Bugs";
				break;
			case 3:
				strChartName = "Genie";
				break;
		}
		lblTitle.setText(strChartName + " TOP 100");
		tableModel.setContents(AppManager.getS_instance().getParser().getChartList());
		buildTable();
		tableChart.repaint();
	}

	/*
	Name: filter
	Parameter: (String) Text to search, (int) Name(2) or singer(3)
	Returns: -
	Description: search and display only matched results
	*/
	public void filter(String text, int criteria) {
		if(text == null) tableSorter.setRowFilter(null);
		else tableSorter.setRowFilter(RowFilter.regexFilter(text, criteria));
	}
	
	//table model class
	public class ChartModel extends AbstractTableModel {
		private String[] arrColumnName;
		private Object[][] chartData;
		
		public ChartModel(JSONArray musics) {
			arrColumnName = new String[5];
			arrColumnName[0] = "순위";
			arrColumnName[1] = "이미지";
			arrColumnName[2] = "곡명";
			arrColumnName[3] = "가수";
			arrColumnName[4] = "앨범";
			
			chartData = new Object[musics.size()][5];
			for(int i = 0; i < musics.size(); i++) chartData[i] = new Object[5];
			setContents(musics);
		}//Constructor ChartModel
		
		/*
		Name: setContents
		Parameter: (JSONArray) has contents inside
		Returns: -
		Description: setting things to display within the table
		*/
		private void setContents(JSONArray musics) {
			for(int i = 0; i < musics.size(); i++) {
				JSONObject obj = (JSONObject) musics.get(i);
				chartData[i][0] = Integer.parseInt((String) (obj.get("rank")));
				try {
					ImageIcon loadedImage = new ImageIcon(new URL((String) obj.get("smallImageUrl")));
					chartData[i][1] = new ImageIcon(loadedImage.getImage().getScaledInstance(50, 50, Image.SCALE_FAST));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				chartData[i][2] = (String) obj.get("title");
				chartData[i][3] = (String) obj.get("artist");
				chartData[i][4] = (String) obj.get("albumName");
			}
		}
		
		public Object[][] getChartData() {
			return chartData;
		}
		public Object[] getMusicData(int index) {
			return chartData[index];
		}
		@Override
		public int getColumnCount() {
			return arrColumnName.length;
		}
		@Override
		public int getRowCount() {
			return chartData.length;
		}
		@Override
		public Object getValueAt(int row, int column) {
			return chartData[row][column];
		}
		@Override
		public void setValueAt(Object value, int row, int column) {
			chartData[row][column] = value;
		}
		@Override
		public String getColumnName(int column) {
			return arrColumnName[column];
		}
		@Override
		public Class<?> getColumnClass(int column) {
			switch(column) {
			case 0: //rank
				return Integer.class;
			case 1: //image
				return ImageIcon.class;
			case 2: //title
			case 3: //artist
			case 4: //album
				return String.class;
			default:
				return Object.class;
			}
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	} //ChartModel class
	
	//listener class
	private class ClickListener implements MouseListener {
		@Override
		public void mouseEntered(MouseEvent e) { }
		@Override
		public void mouseExited(MouseEvent e) { }
		@Override
		public void mouseClicked(MouseEvent e) {
			Object obj = e.getSource();
			if(obj == tableChart) {
				JTable table = (JTable) obj;
				
				//OPEN COMMUNITY HERE
				//use table.convertRowIndexToModel(table.getSelectedRow()) to get index of selected music
				//you get actual position regardless of active filter
				
				//for testing purpose only
				Object[] music = tableModel.getMusicData(table.convertRowIndexToModel(table.getSelectedRow()));
				System.out.println(music[2]);

			}
		}
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }
	}
}
