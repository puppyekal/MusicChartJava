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

@SuppressWarnings("serial")
public class SitePanel extends JPanel {
	// - - - - - Field - - - - -
	//title displayed at top of the panel
	private JLabel lblTitle;
	//provider of the chart
	private String strChartName;
	//JTable displaying music chart
	private JTable tableChart;
	//model(format) of the chart
	private ChartModel tableModel;
	//sorter/filter of the chart
	private TableRowSorter<ChartModel> tableSorter;
	//JSONArray which stores information of musics
	private JSONArray arrMusic;
	//tableChart + a scroll bar
	private JScrollPane scrollBar;
	//background/foreground colors
	private static Color TITLECOLOR = new Color(525484);
	private static Color TEXTCOLOR = Color.black;
	private static Color LBLBACKGROUND = new Color(234, 234, 234);
	private static Color LISTBACKGROUND = Color.white;
	//listener object
	private ClickListener clkListener;
	
	// - - - - - Constructor - - - - -
	public SitePanel(String name, MusicChartParser parser) {
		strChartName = name;
		
		clkListener = new ClickListener();
		
		setPreferredSize(new Dimension(1000, 660));
		setBounds(100, 140, 1000, 660);
		setBackground(LBLBACKGROUND);
		setLayout(null);
		setFont(new Font("굴림", Font.BOLD, 64));
		
		lblTitle = new JLabel(toString());
		lblTitle.setBackground(Color.white);
		lblTitle.setForeground(TITLECOLOR);
		lblTitle.setBounds(80, 30, 840, 80);
		lblTitle.setFont(new Font("굴림", Font.BOLD, 48));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		if(!parser.isParsed()) parser.chartDataParsing();
		arrMusic = parser.getChartList();
		
		tableModel = new ChartModel(arrMusic);
		
		tableSorter = new TableRowSorter<ChartModel>(tableModel);
		tableSorter.setComparator(0, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		
		tableChart = new JTable(tableModel);
		tableChart.setBackground(LISTBACKGROUND);
		tableChart.setForeground(TEXTCOLOR);
		tableChart.setFont(new Font("굴림", Font.PLAIN, 18));
		tableChart.setRowHeight(60);
		TableColumn column;
		for(int i = 0; i < 5; i++) {
			column = tableChart.getColumnModel().getColumn(i);
			switch(i) {
			case 0:
				column.setPreferredWidth(40);
				break;
			case 1:
				column.setPreferredWidth(60);
				break;
			case 2:
				column.setPreferredWidth(280);
				break;
			case 3:
				column.setPreferredWidth(180);
				break;
			case 4:
				column.setPreferredWidth(280);
				break;
			}
			column.setResizable(false);
		}
		tableChart.setRowSorter(tableSorter);
		tableChart.addMouseListener(clkListener);
		add(tableChart);
		
		scrollBar = new JScrollPane(tableChart, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setBounds(80, 130, 840, 500);
		add(scrollBar);
	}
	
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
	public JSONArray getMusics() {
		return arrMusic;
	}
	public JSONObject getMusic(int index) {
		if(arrMusic == null) return null;
		if(index < 0 || index >= arrMusic.size()) return null;
		return (JSONObject) arrMusic.get(index);
	}
	public void setMusics(JSONArray arr) {
		tableSorter.setRowFilter(null);
		arrMusic = arr;
		
		JSONObject obj;
		for(int i = 0; i < arr.size(); i++) {
			obj = (JSONObject) arr.get(i);
			tableModel.setValueAt(Integer.parseInt((String) (obj.get("rank"))), i, 0);
			try {
				tableModel.setValueAt(new ImageIcon(new URL((String) obj.get("smallImageUrl"))) , i, 1);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			tableModel.setValueAt((String) (obj.get("title")), i, 2);
			tableModel.setValueAt((String) (obj.get("artist")), i, 3);
			tableModel.setValueAt((String) (obj.get("albumName")), i, 4);
		}
	}
	public JScrollPane getScrollBar() {
		return scrollBar;
	}
	
	// - - - - - functional method - - - - -
	@Override
	public String toString() {
		return strChartName + " 음악 차트 TOP 100";
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
			for(int i = 0; i < musics.size(); i++) {
				chartData[i] = new Object[5];
				JSONObject obj = (JSONObject) musics.get(i);
				chartData[i][0] = Integer.parseInt((String) (obj.get("rank")));
				try {
					chartData[i][1] = new ImageIcon(new URL((String) obj.get("smallImageUrl")));
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
			case 0:
				return Integer.class;
			case 1:
				return ImageIcon.class;
			case 2:
			case 3:
			case 4:
				return String.class;
			default:
				return Object.class;
			}
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}
	
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
				Object[] music = tableModel.getMusicData(table.convertRowIndexToModel(table.getSelectedRow()));
				//open community panel
				System.out.println(music[0]); //for testing purpose
			}
		}
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }
	}
}
