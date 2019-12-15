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
	// - - - - - 인스턴스 데이터 - - - - -
	//차트 위에 표시되는 제목
	private JLabel lblTitle;
	
	//차트 제공자를 나타내는 문자열("Melon" 또는 "Bugs" 또는 "Genie")
	private String strChartName;
	
	//음악 차트를 담는 표
	private JTable tableChart;
	
	//표의 모델(셀의 크기, 개수, 표시 자료형 등을 결정)
	private ChartModel tableModel;
	
	//표에서 정렬 및 필터링 기능을 담당
	private TableRowSorter<ChartModel> tableSorter;
	
	//스크롤바 제공, 스크롤 가능한 요소(여기서는 tableChart)를 가진다
	private JScrollPane scrollBar;
	
	//배경색과 전경색
	private static Color TITLECOLOR = new Color(52,54,84);
	private static Color TEXTCOLOR = Color.black;
	private static Color LBLBACKGROUND = new Color(234, 234, 234);
	private static Color LISTBACKGROUND = Color.white;
	
	//이벤트 리스너 객체
	private ClickListener clkListener;
	
	// - - - - - 생성자 - - - - -
	public SitePanel() {
		strChartName = "Melon"; //프로그램 실행 직후 Melon 차트를 표시하기 위함
		clkListener = new ClickListener();
		
		setBackground(LBLBACKGROUND);
		setLayout(null);
		setFont(new Font("맑은 고딕", Font.BOLD, 64));
		
		lblTitle = new JLabel(strChartName + " TOP 100");
		lblTitle.setBackground(Color.white);
		lblTitle.setForeground(TITLECOLOR);
		lblTitle.setBounds(80, 30, 920, 80);
		lblTitle.setFont(new Font("배달의민족 도현", Font.BOLD, 48));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		MusicChartParser parser = AppManager.getS_instance().getParser();
		if(!parser.isParsed()) parser.chartDataParsing(this); //Melon 차트 정보 받아옴
		
		tableModel = new ChartModel(parser.getChartList());
		
		tableSorter = new TableRowSorter<ChartModel>(tableModel);
		tableSorter.setComparator(0, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		}); //표에서 순위를 기준으로 정렬되도록 설정(값이 작을수록 위에 있음)
		
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
		scrollBar.setBounds(40, 130, 1000, 540);
		add(scrollBar);
	} //생성자 끝
	
	/*
	Name: buildTable
	Parameter: -
	Returns: -
	Description: 셀에 항목을 표시하기 전에 기본적인 셀 속성 설정
	*/
	private void buildTable() {
		TableColumn column;
		for(int i = 0; i < 5; i++) {
			column = tableChart.getColumnModel().getColumn(i);
			switch(i) {
				case 0: //순위
					column.setResizable(false);
					column.setPreferredWidth(40);
					break;
				case 1: //이미지
					column.setResizable(false);
					column.setPreferredWidth(60);
					break;
				case 2: //제목
				case 4: //앨범
					column.setResizable(true);
					column.setPreferredWidth(280);
					column.setMinWidth(100);
					break;
				case 3: //가수
					column.setResizable(true);
					column.setPreferredWidth(180);
					column.setMinWidth(80);
					break;
			}
		} //셀의 속성을 행 별로 설정
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
	} //getter & setter 끝
	
	/*
	Name: changeData
	Parameter: -
	Returns: -
	Description: 다른 사이트의 차트를 표시하거나 새로고침할 때 표시되는 내용을 변경 
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
	Parameter: (String) 검색할 내용, (int) 검색 요소로 제목(2) 또는 가수(3)
	Returns: -
	Description: 검색 결과에 맞는 곡만 표시되도록 설정
	*/
	public void filter(String text, int criteria) {
		if(text == null) tableSorter.setRowFilter(null); //검색 중이 아닌 경우 모든 항목을 표시
		else tableSorter.setRowFilter(RowFilter.regexFilter(text, criteria)); //실제 검색 실행하여 결과 표시
	}
	
	// - - - - - ChartModel 클래스 - - - - -
	public class ChartModel extends AbstractTableModel {
		// - - - - - 인스턴스 데이터 - - - - -
		//각각의 행 이름을 저장하는 배열
		private String[] arrColumnName;
		//각각의 셀의 항목을 저장하는 배열
		private Object[][] chartData;
		
		// - - - - - 생성자 - - - - -
		public ChartModel(JSONArray musics) {
			arrColumnName = new String[5];
			arrColumnName[0] = "Rank";
			arrColumnName[1] = "Album Image";
			arrColumnName[2] = "Title";
			arrColumnName[3] = "Singer";
			arrColumnName[4] = "Album Title";
			
			chartData = new Object[musics.size()][5];
			for(int i = 0; i < musics.size(); i++) chartData[i] = new Object[5];
			setContents(musics);
		} //생성자 끝
		
		/*
		Name: setContents
		Parameter: (JSONArray) 항목이 들어있음
		Returns: -
		Description: 표에서 표시할 항목 설정
		*/
		private void setContents(JSONArray musics) {
			for(int i = 0; i < musics.size(); i++) {
				JSONObject obj = (JSONObject) musics.get(i);
				chartData[i][0] = Integer.parseInt((String) (obj.get("rank")));
				try {
					ImageIcon loadedImage = new ImageIcon(new URL((String) obj.get("smallImageUrl"))); //지정된 URL로부터 이미지를 받아옴
					chartData[i][1] = new ImageIcon(loadedImage.getImage().getScaledInstance(50, 50, Image.SCALE_FAST)); //받은 이미지를 50 * 50 크기로 변환하여 사용
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
			case 0: //순위
				return Integer.class;
			case 1: //이미지
				return ImageIcon.class;
			case 2: //제목
			case 3: //가수
			case 4: //앨범
				return String.class;
			default:
				return Object.class;
			}
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	} //ChartModel 클래스 끝
	
	// - - - - - ClickListener 클래스 - - - - -
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
				Object[] music = tableModel.getMusicData(table.convertRowIndexToModel(table.getSelectedRow())); //클릭된 열의 위치(숨겨진 항목이 있어도 바뀌지 않는 절대적인 위치)에 있는 곡 선택
				System.out.println(music[2] + music[0].toString()); //테스트
				AppManager.getS_instance().PopUpCommentUI(Integer.parseInt(music[0].toString())); //선택된 곡에 대한 커뮤니티 표시
			}
		}
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }
	} //ClickListener 클래스 끝
}
