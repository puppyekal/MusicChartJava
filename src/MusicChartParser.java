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
 * @version 1.5
 *
 **/

public abstract class MusicChartParser {
	
	protected int songCount = 0;
	protected String url;
	protected JSONArray chartList;
	protected JSONObject songDetailInfo;

	protected String isNotParsed = "파싱이 정상적으로 이루어지지 않았습니다 :(";
	protected String isOnlyChartParse = "해당 메소드는 차트 파싱에만 사용가능한 메소드 입니다 :(";
	protected String isOnlyDetailParse = "해당 메소드는 노래 1개의 상세 정보 파싱에만 사용가능한 메소드 입니다 :(";
	protected String jsonDontHaveKey = "JSONObject 내에 해당 키 값이 없습니다 :(";
	protected String plzUseRightJSONObject = "올바른 JSONObject 값을 사용해주세요 :(";
	protected String songDetailParsingTitle = "상세 정보 파싱중..";
	protected String songDetailParsingMessage = "해당 노래에 대한 상세 정보를 파싱하는 중입니다 :)";
	
	public abstract void chartDataParsing(Component parentComponent);
	public abstract void songDetailDataParsing(String songId, Component parentComponent);
	public abstract void songDetailDataParsing(JSONObject jObj, Component parentComponent);
	public abstract void songDetailDataParsing(int rank, JSONArray chartListData, Component parentComponent);
	public abstract void songDetailDataParsing(String title, JSONArray chartListData, Component parentComponent); // 비추천 하는 메소드 입니다. title에 맞는 데이터를 처음부터 찾아가야 하기 때문에 좀 더 비효율적입니다.
	
	protected Thread chartThread;
	protected Thread songDetailThread;
	protected ProgressMonitor progressMonitor;

	public boolean isParsed() {
		if (songCount == 0)
			return false;
		else
			return true;
	}
	
	public void progressMonitorManager(Component parentComponent, String title, String message) {
		progressMonitor = new ProgressMonitor(parentComponent, title, message, 0, 100);
		progressMonitor.setMaximum(100);
		progressMonitor.setMillisToDecideToPopup(100);
		progressMonitor.setMillisToPopup(100);
	}
	
	public JSONArray getChartList() {
		if (!isParsed())
			System.out.println(isNotParsed);

		if (songCount == 1) {
			System.out.println("getChartList() : " + isOnlyChartParse);
			return null;
		}

		return chartList;
	}
	
	public JSONObject getSongData() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 100) {
			System.out.println("getSongData() : " + isOnlyDetailParse);
			return null;
		}
		
		return songDetailInfo;
	}
	
	public JSONObject getSongData(int rank) {

		if (rank < 1 || rank > 100) {
			System.out.println("1 ~ 100위 이내의 순위를 입력해주세요 :)");
			return null;
		}
		
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) // 노래 상세 정보는 등수랑 상관 없이 불러 올 수 있음
			return songDetailInfo;
		else
			return (JSONObject) chartList.get(rank - 1);
	}
	public JSONObject getSongData(String title) {

		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}

		if (songCount == 1) // 노래 상세 정보는 등수랑 상관 없이 불러 올 수 있음
			return songDetailInfo;

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return (JSONObject) chartList.get(i);
		}
		// 반복문 내에서 못찾으면 정보가 없는 것 - null 반환
		return null;
	}
	
	public int getRank(String title) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return -1;
		}

		if (songCount == 1) {
			System.out.println("getRank(String title) :" + isOnlyChartParse);
			return -1;
		}

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return Integer.parseInt(((JSONObject) chartList.get(i)).get("rank").toString());
		}
		// 반복문 내에서 못찾으면 정보가 없는 것 - -1 반환
		return -1;
	}	
	
	public int getRank(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return -1;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return -1;
		}
		
		if (jObj.containsKey("rank"))		
			return Integer.parseInt(jObj.get("rank").toString());
		else {
			System.out.println(jsonDontHaveKey);
			return -1;
		}
	}
	// 앨범명, 좋아요, 가수명, 발매일, 장르로 getRank를 하면 겹칠 수 있기에 만들지 않음
	
	public String getTitle(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}

		if (songCount == 1) {
			System.out.println("getTitle(int rank) : " + isOnlyChartParse);
			return null;
		}
		else
			return ((JSONObject) chartList.get(rank - 1)).get("title").toString();
	}
	
	public String getTitle(JSONObject jObj) {
		
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("title"))
			return jObj.get("title").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	}
	// 앨범명, 좋아요, 가수명, 발매일, 장르로 getTitle을 하면 겹칠 수 있기에 만들지 않음
	
	public String getArtistName(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1 ~ 100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getArtistName(int rank) : " + isOnlyChartParse);
			return null;
		}
		return ((JSONObject) chartList.get(rank - 1)).get("artist").toString();
	}
	
	public String getArtistName(String title) {

		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getArtistName(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("artist").toString();
		}
		// 반복문 내에서 못찾으면 정보가 없는 것 - null 반환
		return null;
	}
	
	public String getArtistName(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("artist"))		
			return jObj.get("artist").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	}
	
	public String getAlbumName(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getAlbumName(int rank) : " + isOnlyChartParse);
			return null;
		}

		return ((JSONObject) chartList.get(rank - 1)).get("albumName").toString();
	}
	
	public String getAlbumName(String title) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getAlbumName(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("albumName").toString();
		}
		return null;
	}
	
	public String getAlbumName(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("albumName"))
			return jObj.get("albumName").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	}
	
	public String getSongId(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}

		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getSongId(int rank) : " + isOnlyChartParse);
			return null;
		}

		return ((JSONObject) chartList.get(rank - 1)).get("songId").toString();
	}
	
	public String getSongId(String title) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getSongId(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("songId").toString();
		}
		return null;
	}
	
	public String getSongId(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("songId"))
			return jObj.get("songId").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	}
	
	// getLikeNum()은 BugsChartParser와 GenieChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	// getLikeNum(int rank), getLikeNum(String title)는 MelonChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	
	public String getLikeNum(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (jObj.containsKey("likeNum"))
			return jObj.get("likeNum").toString();
		else {
			System.out.println(jsonDontHaveKey);
			return null;
		}
	}
	
	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getImageUrl() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1)
			return songDetailInfo.get("imageUrl").toString();
		
		System.out.println("getImageUrl() : " + isOnlyDetailParse);
		return null;
	}
	
	public String getImageUrl(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1~100위 이내의 순위를 입력해주세요");
			return null;
		}
		
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1)
			return songDetailInfo.get("imageUrl").toString(); // rank 값과 상관이 없음
		else
			return ((JSONObject) chartList.get(rank - 1)).get("smallImageUrl").toString();
	}
	
	public String getImageUrl(String title) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}		
		if (songCount == 1)
			return songDetailInfo.get("imageUrl").toString(); // title 값과 상관이 없음

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("smallImageUrl").toString();
		}
		return null;
	}
	
	public String getImageUrl(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (songCount == 1) {
			if (jObj.containsKey("imageUrl"))
				return jObj.get("imageUrl").toString();
		}
		else {
			if (jObj.containsKey("smallImageUrl"))
				return jObj.get("smallImageUrl").toString();
		}
		
		System.out.println(jsonDontHaveKey);
		return null;
	}
	
	// getGenre(), getGenre(JSONObject jObj)는 MelonChartParser와 GenieChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
	// getReleaseDate(), getReleaseDate(JSONObject jObj)는 MelonChartParser에서만 사용가능하므로 추상클래스에서는 제외됨
}
