import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class SitePanel extends JPanel {
	// - - - - - Field - - - - -
	//title displayed at top of the panel
	private JLabel lblTitle;
	//provider of the chart
	private String strChartName;
	//List of musics from the chart
	private JList<Music> listChart;
	//Music array with length of 100
	private Music[] arrMusic;
	//scroll bar next to the displayed list
	private JScrollPane scrollBar;
	//background/foreground colors
	private static Color TITLECOLOR = Color.blue;
	private static Color TEXTCOLOR = Color.black;
	private static Color LBLBACKGROUND = new Color(240, 240, 240);
	private static Color LISTBACKGROUND = Color.white;
	//listener object
	private ClickListener clkListener;
	//this temporary variable will be removed after the exact size of the panel is revealed
	private static final int BUTTONHEIGHT = 60;
	
	// - - - - - Constructor - - - - -
	public SitePanel(String name, Music[] musics) {
		strChartName = name;
		setMusics(musics);
		
		clkListener = new ClickListener();
		
		setPreferredSize(new Dimension(1000, 700 - BUTTONHEIGHT));
		setBounds(100, 100 + BUTTONHEIGHT, 1000, 700 - BUTTONHEIGHT);
		setBackground(LBLBACKGROUND);
		setLayout(null);
		setFont(new Font("굴림", Font.BOLD, 64));
		
		lblTitle = new JLabel(toString());
		lblTitle.setForeground(TITLECOLOR);
		lblTitle.setBounds(80, 40, 840, 100);
		lblTitle.setFont(new Font("굴림", Font.BOLD, 48));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		listChart = new JList<Music>(arrMusic);
		listChart.setBackground(LISTBACKGROUND);
		listChart.setForeground(TEXTCOLOR);
		listChart.setFont(new Font("굴림", Font.PLAIN, 28));
		listChart.addMouseListener(clkListener);
		add(listChart);
		
		scrollBar = new JScrollPane(listChart, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setBounds(80, 180, 840, 480 - BUTTONHEIGHT);
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
	public JList<Music> getListChart() {
		return listChart;
	}
	public Music[] getMusics() {
		return arrMusic;
	}
	public Music getMusic(int index) {
		if(index < 0 || index > 99) return null;
		return arrMusic[index];
	}
	public void setMusics(Music[] arr) {
		if(arr.length == 100) arrMusic = arr;
		else throw new IndexOutOfBoundsException("length of the array must be exactly 100");
	}
	public JScrollPane getScrollBar() {
		return scrollBar;
	}
	
	// - - - - - functional method - - - - -
	@Override
	public String toString() {
		return strChartName + " 음악 차트 TOP 100";
	}
	
	private class ClickListener implements MouseListener {
		@Override
		public void mouseEntered(MouseEvent e) { }
		@Override
		public void mouseExited(MouseEvent e) { }
		@Override
		public void mouseClicked(MouseEvent e) {
			Object obj = e.getSource();
			if(obj == listChart) {
				JList<Music> list = (JList<Music>) obj;
				Music music = list.getSelectedValue();
				//open community panel
			}
		}
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }
	}
}
