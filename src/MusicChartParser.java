import java.awt.Component;
import java.util.HashMap;

import javax.swing.ProgressMonitor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author SejongUniv 오창한
 * @version 1.7
 *
 **/

public abstract class MusicChartParser {
	
	protected int songCount = 0;	 		// 노래 개수를 담당
	protected String url;			 		// 파싱할 웹 사이트 url
	protected JSONArray chartList; 	 		// 차트 100곡에 대한 정보를 담을 JSONArray, JSONObject 100개로 이루어져있음
	protected JSONObject songDetailInfo;	// 노래 한 곡의 상세 정보를 담을 JSONObject, 차트 100곡에서는 얻을 수 없는 정보들만 포함함

	// 파싱 오류시 보여줄 문자열들
	protected String isNotParsed = "파싱이 정상적으로 이루어지지 않았습니다 :(";
	protected String isOnlyChartParse = "해당 메소드는 차트 파싱에만 사용가능한 메소드 입니다 :(";
	protected String isOnlyDetailParse = "해당 메소드는 노래 1개의 상세 정보 파싱에만 사용가능한 메소드 입니다 :(";
	protected String jsonDontHaveKey = "JSONObject 내에 해당 키 값이 없습니다 :(";
	protected String plzUseRightJSONObject = "올바른 JSONObject 값을 사용해주세요 :(";
	protected String songDetailParsingTitle = "상세 정보 파싱중..";
	protected String songDetailParsingMessage = "해당 노래에 대한 상세 정보를 파싱하는 중입니다 :)";
	
	// 차트 100곡을 파싱하는 abstract 메소드, 각 음원 사이트 파서별로 필수로 다르게 구현해야 함
	// parentComponent에 JPanel, JFrame 등을 넣으면 파싱을 하면서 해당 클래스에 로딩창을 띄워줌
	// ProgressMonitor를 사용했으나 이 것을 사용하면 버그가 있어 ProgressMonitor 부분만 주석처리 해두었음, 상세한 버그 내용은 아래에 기술했음
	public abstract void chartDataParsing(Component parentComponent);
	
	// 노래 한 곡의 상세 정보를 파싱하는 abstract 메소드, 여러 parameter들을 지원함
	// parentComponent에 JPanel, JFrame 등을 넣으면 파싱을 하면서 해당 클래스에 로딩창을 띄워줌
	// ProgressMonitor를 사용했으나 이 것을 사용하면 버그가 있어 ProgressMonitor 부분만 주석처리 해두었음, 상세한 버그 내용은 아래에 기술했음
	public abstract void songDetailDataParsing(String songId, Component parentComponent);
	public abstract void songDetailDataParsing(JSONObject jObj, Component parentComponent);
	public abstract void songDetailDataParsing(int rank, JSONArray chartListData, Component parentComponent);
	public abstract void songDetailDataParsing(String title, JSONArray chartListData, Component parentComponent); // 비추천 하는 메소드, title에 맞는 데이터를 처음부터 찾아가야 하기 때문에 좀 더 비효율적임
	
	protected Thread chartThread;				// 차트 100곡을 파싱할 때 사용할 Thread 
	protected Thread songDetailThread;			// 노래 한 곡에 대한 상세 정보를 파싱할 때 사용할 Thread
	
	/*
	 * @deprecated
	 * 파싱 중에 로딩창으로 사용할 ProgressMonitor
	 * 근데 이걸 사용하면 Thread가 종료되지 않는 오류와 원하는 대로 ProgressMonitor가 나오지 않는 버그가 있어 Deprecated 처리해놓았음
	 * ProgressBar 등을 커스텀해서 사용해야 할 듯, 현재 파서에서는 주석처리 해두었음
	 */
	@Deprecated
	protected ProgressMonitor progressMonitor;

	public boolean isParsed() { // 파싱이 이루어졌는지 판단하는 메소드
		// chartDataParsing()이나 songDetailDataParsing()을 한 번이라도 호출했으면 songCount는 1 이상임
		if (songCount == 0)
			return false;
		else
			return true;
	} // boolean isParsed()
	
	public JSONArray getChartList() { // 차트 100곡에 대한 정보를 담는 JSONArray인 chartList를 반환하는 메소드
		if (!isParsed()) {// 파싱을 한 적이 없으면
			System.out.println(isNotParsed);
			return null;
		}

		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱을 했다면
			System.out.println("getChartList() : " + isOnlyChartParse);
			return null;
		}

		return chartList;
	} // JSONArray getChartList()
	
	public JSONObject getSongData() { // 노래 한 곡에 대한 상세 정보를 담는 JSONObject인 songDetailInfo를 반환하는 메소드
		if (!isParsed()) { // 파싱을 한 적이 없으면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 100) { // 차트 100곡에 대한 파싱을 했다면
			System.out.println("getSongData() : " + isOnlyDetailParse);
			return null;
		}
		
		return songDetailInfo;
	} // JSONObject getSongData()
	
	public JSONObject getSongData(int rank) { // 노래 한 곡에 대한 상세 정보 또는 차트 100위의 노래 1곡에 대한 정보를 반환하는 메소드

		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1 ~ 100위 이내의 순위를 입력해주세요 :)");
			return null;
		}
		
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) 		// 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 상세 정보 반환(상세 정보는 순위에 상관이 없음)
			return songDetailInfo;
		else						// 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 rank 순위에 맞는 원소를 반환
			return (JSONObject) chartList.get(rank - 1);
	} // JSONObject getSongData(int rank)
	
	public JSONObject getSongData(String title) { // 노래 한 곡에 대한 상세 정보 또는 차트 100위의 노래 1곡에 대한 정보를 반환하는 메소드

		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}

		if (songCount == 1) // 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 상세 정보 반환(상세 정보는 순위에 상관이 없음)
			return songDetailInfo;

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소를 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return (JSONObject) chartList.get(i);
		}
		
		return null; // 반복문 내에서 못찾으면 정보가 없는 것 = null 반환
	} // JSONObject getSongData(String title)
	
	public int getRank(String title) { // 노래 제목을 통해 해당 노래의 순위를 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return -1;
		}

		if (songCount == 1) { // 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 -1 반환(상세 정보에는 순위가 없기 때문)
			System.out.println("getRank(String title) :" + isOnlyChartParse);
			return -1;
		}

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소의 순위를 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return Integer.parseInt(((JSONObject) chartList.get(i)).get("rank").toString());
		}
		return -1; // 반복문 내에서 못찾으면 정보가 없는 것 = -1 반환
	} // int getRank(String title)
	
	public int getRank(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 순위를 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return -1;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return -1;
		}
		
		if (jObj.containsKey("rank")) // rank key값 유효성 검사
			return Integer.parseInt(jObj.get("rank").toString());
		else {
			System.out.println(jsonDontHaveKey);
			return -1;
		}
	} // int getRank(JSONObject jObj)
	
	public String getTitle(int rank) { // 노래 순위를 통해 해당 노래의 제목을 반환하는 메소드
		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}

		if (songCount == 1) { // 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 null 반환(상세 정보에는 제목이 없기 때문)
			System.out.println("getTitle(int rank) : " + isOnlyChartParse);
			return null;
		}
		else
			return ((JSONObject) chartList.get(rank - 1)).get("title").toString();
	} // String getTitle(int rank)
	
	public String getTitle(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 제목을 반환하는 메소드
		
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("title")) // title key값 유효성 검사
			return jObj.get("title").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	} // String getTitle(JSONObject jObj)
	
	public String getArtistName(int rank) { // 노래 순위를 통해 해당 노래의 가수 이름을 반환하는 메소드
		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1 ~ 100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getArtistName(int rank) : " + isOnlyChartParse);
			return null;
		}
		return ((JSONObject) chartList.get(rank - 1)).get("artist").toString();
	} // String getArtistName(int rank)
	
	public String getArtistName(String title) { // 노래 제목을 통해 해당 노래의 가수 이름을 반환하는 메소드

		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래 한곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getArtistName(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소의 가수 이름을 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("artist").toString();
		}
		return null; // 반복문 내에서 못찾으면 정보가 없는 것 = null 반환
	} // String getArtistName(String title)
	
	public String getArtistName(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 가수 이름을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("artist")) // artist key값 유효성 검사
			return jObj.get("artist").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	} // String getArtistName(JSONObject jObj)
	
	public String getAlbumName(int rank) { // 노래 순위를 통해 해당 노래의 앨범 이름을 반환하는 메소드
		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getAlbumName(int rank) : " + isOnlyChartParse);
			return null;
		}

		return ((JSONObject) chartList.get(rank - 1)).get("albumName").toString();
	} // String getArtistName(int rank)
	
	public String getAlbumName(String title) { // 노래 제목을 통해 해당 노래의 앨범 이름을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getAlbumName(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소의 앨범 이름을 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("albumName").toString();
		}
		return null;
	} // String getAlbumName(String title)
	
	public String getAlbumName(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 앨범 이름을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("albumName")) // albumName key값 유효성 검사
			return jObj.get("albumName").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	} // String getAlbumName(JSONObject jObj)
	
	public String getSongId(int rank) { // 노래 순위를 통해 해당 노래의 앨범 이름을 반환하는 메소드
		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getSongId(int rank) : " + isOnlyChartParse);
			return null;
		}

		return ((JSONObject) chartList.get(rank - 1)).get("songId").toString();
	} // String getSongId(int rank)
	
	public String getSongId(String title) { // 노래 제목을 통해 해당 노래의 노래 아이디를 반환하는 메소드, 노래 아이디는 상세 페이지 url을 얻을 때 사용 됨
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) { // 노래  한 곡에 대한 상세 파싱이 이루어졌다면
			System.out.println("getSongId(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소의 노래 아이디를 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("songId").toString();
		}
		return null;
	} // String getSongId(String title)
	
	public String getSongId(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 앨범 이름을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("songId")) // songId key값 유효성 검사
			return jObj.get("songId").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	} // String getSongId(JSONObject jObj)
	
	// getLikeNum()은 BugsChartParser와 GenieChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	// getLikeNum(int rank), getLikeNum(String title)는 MelonChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	
	public String getLikeNum(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 좋아요 개수를 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("likeNum")) // likeNum key값 유효성 검사
			return jObj.get("likeNum").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	} // String getLikeNum(JSONObject jObj)
	
	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getImageUrl() { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면 그 곡의 큰 이미지 url을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			return songDetailInfo.get("imageUrl").toString();
		
		System.out.println("getImageUrl() : " + isOnlyDetailParse);
		return null;
	} // String getImageUrl()
	
	public String getImageUrl(int rank) { // 노래 순위를 통해 해당 노래의 이미지 url을 반환하는 메소드
		if (rank < 1 || rank > 100) { // 1 <= rank <= 100을 벗어나는 범위라면
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}
		
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1)
			return songDetailInfo.get("imageUrl").toString(); // 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 그 곡의 큰 이미지 url을 반환(상세 정보는 순위에 상관이 없음)
		else
			return ((JSONObject) chartList.get(rank - 1)).get("smallImageUrl").toString(); // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 순위에 맞는 원소의 작은 이미지 url을 반환
	} // String getImageUrl(int rank)
	
	public String getImageUrl(String title) { // 노래 제목을 통해 해당 노래의 이미지 url을 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}		
		if (songCount == 1) // 노래 한 곡에 대한 상세 파싱이 이루어졌다면
			return songDetailInfo.get("imageUrl").toString(); // 노래 한 곡에 대한 상세 정보 파싱이 이루어졌다면 그 곡의 큰 이미지 url을 반환(상세 정보는 제목과 상관이 없음)

		for (int i = 0; i < songCount; i++) { // 차트 100곡에 대한 파싱이 이루어졌다면 JSONArray에 있는 노래들 중 title 제목에 맞는 원소의 작은 이미지 url을 반환
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("smallImageUrl").toString();
		}
		return null;
	} // String getImageUrl(String title)
	
	public String getImageUrl(JSONObject jObj) { // JSONArray의 원소 중 하나를 이용하여 해당 노래의 좋아요 개수를 반환하는 메소드
		if (!isParsed()) { // 파싱이 이루어지지 않았다면
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (songCount == 1) { // 노래 한 곡에 대한 상세 파싱이 이루어졌다면 큰 이미지 url 반환
			if (jObj.containsKey("imageUrl"))
				return jObj.get("imageUrl").toString();
		}
		else { // 차트 100곡에 대한 파싱이 이루어졌다면 작은 이미지 url 반환
			if (jObj.containsKey("smallImageUrl"))
				return jObj.get("smallImageUrl").toString();
		}
		
		System.out.println(jsonDontHaveKey);
		return null;
	} // String getImageUrl(JSONObject jObj)
	
	// getGenre(), getGenre(JSONObject jObj)는 MelonChartParser와 GenieChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	// getReleaseDate(), getReleaseDate(JSONObject jObj)는 MelonChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
} // MusicChartParser class