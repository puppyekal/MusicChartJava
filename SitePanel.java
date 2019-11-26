import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.*;

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
	//Music array with length of 100
	private Music[] arrMusic;
	//scroll bar next to the displayed list
	private JScrollPane scrollBar;
	//background/foreground colors
	private static Color TITLECOLOR = Color.blue;
	private static Color TEXTCOLOR = Color.black;
	private static Color LBLBACKGROUND = Color.white;
	private static Color LISTBACKGROUND = Color.white;
	//listener object
	private ClickListener clkListener;
	
	// - - - - - Constructor - - - - -
	public SitePanel(String name, Music[] musics) {
		strChartName = name;
		arrMusic = musics;
		
		clkListener = new ClickListener();
		
		setPreferredSize(new Dimension(1000, 660));
		setBounds(100, 140, 1000, 660);
		setBackground(LBLBACKGROUND);
		setLayout(null);
		setFont(new Font("굴림", Font.BOLD, 64));
		
		lblTitle = new JLabel(toString());
		lblTitle.setForeground(TITLECOLOR);
		lblTitle.setBounds(80, 30, 840, 80);
		lblTitle.setFont(new Font("굴림", Font.BOLD, 48));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
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
		for(int i = 0; i < 6; i++) {
			column = tableChart.getColumnModel().getColumn(i);
			switch(i) {
			case 0:
				column.setPreferredWidth(40);
				break;
			case 1:
				column.setPreferredWidth(60);
				break;
			case 2:
				column.setPreferredWidth(240);
				break;
			case 3:
				column.setPreferredWidth(180);
				break;
			case 4:
				column.setPreferredWidth(240);
				break;
			case 5:
				column.setPreferredWidth(80);
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
	public Music[] getMusics() {
		return arrMusic;
	}
	public Music getMusic(int index) {
		if(index < 0 || index > 99) return null;
		return arrMusic[index];
	}
	public void setMusics(Music[] arr) {
		tableSorter.setRowFilter(null);
		arrMusic = arr;
		for(int i = 0; i < 100; i++) {
			tableModel.setValueAt(arr[i].getnRank(), i, 0);
			tableModel.setValueAt(arr[i].getImage(), i, 1);
			tableModel.setValueAt(arr[i].getStrName(), i, 2);
			tableModel.setValueAt(arr[i].getStrSinger(), i, 3);
			tableModel.setValueAt(arr[i].getStrAlbum(), i, 4);
			tableModel.setValueAt("♥" + arr[i].getnLikes(), i, 5);
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
	class ChartModel extends AbstractTableModel {
		private String[] arrColumnName;
		private Object[][] chartData;
		
		public ChartModel(Music[] musics) {
			arrColumnName = new String[6];
			arrColumnName[0] = "순위";
			arrColumnName[1] = "이미지";
			arrColumnName[2] = "곡명";
			arrColumnName[3] = "가수";
			arrColumnName[4] = "앨범";
			arrColumnName[5] = "좋아요";
			
			chartData = new Object[100][6];
			for(int i = 0; i < 100; i++) {
				chartData[i] = new Object[6];
				chartData[i][0] = musics[i].getnRank();
				chartData[i][1] = musics[i].getImage();
				chartData[i][2] = musics[i].getStrName();
				chartData[i][3] = musics[i].getStrSinger();
				chartData[i][4] = musics[i].getStrAlbum();
				chartData[i][5] = "♥" + musics[i].getnLikes();
			}
		}
		
		public Object[][] getChartData() {
			return chartData;
		}
		public Object[] getMusicData(int index) {
			return chartData[index];
		}
		public void setChartData(Object[][] data) {
			for(int i = 0; i < 100; i++) {
				for(int j = 0; j < 6; j++) {
					setValueAt(data[i][j], i, j);
				}
			}
		}
		public void setMusicData(Object[] musicData, int index) {
			for(int i = 0; i < 6; i++) setValueAt(musicData[i], index, i);
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
			return getValueAt(0, column).getClass();
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
				Music music = arrMusic[table.convertRowIndexToModel(table.getSelectedRow())];
				//open community panel
				System.out.println(music); //for testing purpose only
			}
		}
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }
	}
}
