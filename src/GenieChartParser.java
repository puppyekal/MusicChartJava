import java.awt.*;
import java.util.HashMap;

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

public class GenieChartParser extends MusicChartParser {

	/*
	 * GenieChartParser Description (KO_KR)
	 * 
	 **************************************************
	 * 
	 * ** 차트 100곡을 파싱할 시에 얻을 수 있는 것들 **
	 * 노래 아이디		(key : songId)
	 * 노래 순위		(key : rank)
	 * 노래 작은 이미지	(key : smallImageUrl)
	 * 노래 제목		(key : title)
	 * 가수 이름		(key : artist)
	 * 앨범 이름		(key : albumName)
	 * 
	 * ** 차트 100곡을 파싱할 시에 사용 가능한 메소드 **
	 * - 메소드 이름이 같은 메소드들은 반환형이 모두 같다.
	 * - [반환형] 메소드이름() 과 같이 표기했다.
	 * 
	 * <차트 100곡 파싱 관련 메소드>
	 * [void]		chartDataParsing(Component parentComponent)
	 * [boolean]	isParsed()
	 * 
	 * <차트 100곡 노래 정보 get 메소드>
	 * [JSONArray]	getChartList()
	 * [JSONObject]	getSongData(int rank)	getSongData(String title)
	 * [int]		getRank(String title)	getRank(JSONObject jObj)
	 * [String]		getTitle(int rank)		getTitle(JSONObject jObj)
	 * [String]		getArtistName(int rank)	getArtistName(String title)		getArtistName(JSONObject jObj)
	 * [String]		getAlbumName(int rank)	getAlbumName(String title)		getAlbumName(JSONObject jObj)
	 * [String]		getImageUrl(int rank)	getImageUrl(String title)		getImageUrl(JSONObject jObj)
	 * [String]		getSongId(int rank)		getSongId(String title)			getSongId(JSONObject jObj)
	 *
	 **************************************************
	 *
	 * ** 노래 1개에 대한 상세 정보를 파싱할 시에 얻을 수 있는 것들 **
	 * 노래 큰 이미지		(key : imageUrl)
	 * 노래 장르		(key : genre)
	 * 노래 재생시간		(key : songTime)
	 * 노래 좋아요 개수	(key : likeNum)
	 *
	 * ** 노래 1개에 대한 상세 정보를 파싱할 시에 사용 가능한 메소드 **
	 * - 메소드 이름이 같은 메소드들은 반환형이 모두 같다.
	 * - [반환형] 메소드이름() 과 같이 표기했다.
	 * 
	 * <노래 1개에 대한 상세 정보 파싱 관련 메소드>
	 * [void]		songDetailDataParsing(String songId, Component parentComponent)
	 * [void]		songDetailDataParsing(JSONObject jObj, Component parentComponent)
	 * [void]		songDetailDataParsing(int rank, JSONArray chartListData, Component parentComponent)
	 * [void]		songDetailDataParsing(String title, JSONArray chartListData, Component parentComponent)
	 * [boolean]	isParsed()
	 * 
	 * <노래 1개에 대한 상세 정보 get 메소드>
	 * [JSONObject]	getSongData()
	 * [String]		getImageUrl()		getImageUrl(JSONObject jObj)	getImageUrl(int rank)	getImageUrl(String title)
	 * [String]		getGenre()			getGenre(JSONObject jObj)
	 * [String]		getSongTime()		getSongTime(JSONObject jObj)
	 * [String]		getLikeNum()		getLikeNum(JSONObject jObj)
	 * 
	 **************************************************
	 *
	 */
	
	private String genieChartParsingTitle = "지니 차트 파싱중..";
	private String genieChartParsingMessage = "지니 차트 100곡에 대한 정보를 불러오는 중 입니다 :)";
	
	public GenieChartParser() {
		songCount = 0;
		chartList = null;
		songDetailInfo = null;
		url = null;
		chartThread = null;
		songDetailThread = null;
		progressMonitor = null;
	}
	
	private class ChartDataParsingThread implements Runnable {
		@Override
		public void run() {
			// 지니 차트 1~100위의 노래를 파싱함
			songCount = 0;
			url = "https://www.genie.co.kr/chart/top200";

			try {
				// 지니 차트 연결에 필요한 header 설정 및 연결
				Connection genieConnection1_50 = Jsoup.connect(url).header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
						.header("Upgrade-Insecure-Requests", "1")
						.header("User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
						.method(Connection.Method.GET);

				// 연결 후 웹페이지를 긁어옴
				Document genieDocument1_50 = genieConnection1_50.get();

				// 1~100위에 대한 정보를 불러옴
				Elements data1st50 = genieDocument1_50.select("table.list-wrap").first().select("tbody > tr.list");

				chartList = new JSONArray();

				for (Element elem : data1st50) { // 1~50위에 대한 내용 파싱
					// JSONObject에 데이터를 넣기 위한 작업
					HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

					// key : songId, value : 노래 아이디
					songAllInfo.put("songId", elem.attr("songId").toString());

					// key : rank, value : 순위
					songAllInfo.put("rank", elem.select("td.number").first().text().toString().split(" ")[0]);

					// key : smallImageUrl, value : 작은 이미지 url 링크
					songAllInfo.put("smallImageUrl",
							"https:" + elem.select("td").get(2).select("img").first().attr("src").toString());

					// key : title, value : 노래 제목
					songAllInfo.put("title", elem.select("td.info").first().select("a").first().text().toString());

					// key : artist, value : 가수 이름
					songAllInfo.put("artist", elem.select("td.info").first().select("a").get(1).text().toString());

					// key : albumName, value : 앨범 이름
					songAllInfo.put("albumName", elem.select("td.info").first().select("a").get(2).text().toString());

					// 값들을 JSONObject로 변환
					JSONObject jsonSongInfo = new JSONObject(songAllInfo);

					// JSONArray에 값 추가
					chartList.add(jsonSongInfo);
					songCount++;
					progressMonitor.setProgress(songCount);
				}
				
				String url51_100 = genieDocument1_50.select("div.page-nav.rank-page-nav").first().select("a").get(1).attr("href").toString();
				
				// 지니 차트 연결에 필요한 header 설정 및 연결
				Connection genieConnection51_100 = Jsoup.connect(url + url51_100).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
						.header("Sec-Fetch-User", "?1")
						.header("Upgrade-Insecure-Requests", "1")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
						.method(Connection.Method.GET);
				
				// 연결 후 웹페이지를 긁어옴
				Document genieDocument51_100 = genieConnection51_100.get();

				// 51~100위에 대한 정보를 불러옴
				Elements data51st100 = genieDocument51_100.select("table.list-wrap").first().select("tbody > tr.list");

				for (Element elem : data51st100) { // 51~100위에 대한 내용 파싱
					// JSONObject에 데이터를 넣기 위한 작업
					HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

					// key : songId, value : 노래 아이디
					songAllInfo.put("songId", elem.attr("songId").toString());

					// key : rank, value : 순위
					songAllInfo.put("rank", elem.select("td.number").first().text().toString().split(" ")[0]);

					// key : smallImageUrl, value : 작은 이미지 url 링크
					songAllInfo.put("smallImageUrl", "https:" + elem.select("td").get(2).select("img").first().attr("src").toString());

					// key : title, value : 노래 제목
					songAllInfo.put("title", elem.select("td.info").first().select("a").first().text().toString());

					// key : artist, value : 가수 이름
					songAllInfo.put("artist", elem.select("td.info").first().select("a").get(1).text().toString());

					// key : albumName, value : 앨범 이름
					songAllInfo.put("albumName", elem.select("td.info").first().select("a").get(2).text().toString());

					// 값들을 JSONObject로 변환
					JSONObject jsonSongInfo = new JSONObject(songAllInfo);

					// JSONArray에 값 추가
					chartList.add(jsonSongInfo);
					songCount++;
					progressMonitor.setProgress(songCount);
				}
				
				// 파싱 결과 출력(테스트용)
				/*
				for (Object o : chartList) {
					if (o instanceof JSONObject)
						System.out.println(((JSONObject) o));
				}
				*/

			} catch (HttpStatusException e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("많은 요청으로 인해 불러오기에 실패하였습니다.");
				songCount = 0;
				return;
			} catch (NullPointerException e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("Url 링크가 잘못되었거나, 웹 페이지 구조가 변경되어 파싱에 실패했습니다 :(");
				songCount = 0;
				return;
			} catch (Exception e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("파싱도중 에러가 발생했습니다 :(");
				songCount = 0;
				return;
			}
		}
	}
	
	private class SongDetailDataParsingThread implements Runnable {
		@Override
		public void run() {
			songCount = 0;
			HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

			try {
				// songId를 통해 곡에 대한 상세한 정보를 얻기 위한 접근
				Connection songDetailConnection = Jsoup.connect(url).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
						.header("Sec-Fetch-User", "?1")
						.header("Upgrade-Insecure-Requests", "1")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
						.method(Connection.Method.GET);
				
				progressMonitor.setProgress(50);

				// 곡에 대한 상세한 정보 웹 페이지를 긁어옴
				Document songDetailDocument = songDetailConnection.get();
				Element songDetailInfo = songDetailDocument.select("div.song-main-infos").first();
				
				Element songDetailAlbumInfo = songDetailInfo.select("div.info-zone").first();
				
				progressMonitor.setProgress(60);
				
				// key : imageUrl, value : 큰 이미지 url 링크
				songAllInfo.put("imageUrl", "https:" + songDetailInfo.select("div.photo-zone > a").first().attr("href").toString());

				progressMonitor.setProgress(70);
				
				// key : genre, value : 노래 장르
				songAllInfo.put("genre", songDetailAlbumInfo.select("ul.info-data > li").get(2).select("span.value").first().text().toString());
				
				progressMonitor.setProgress(80);
				
				// key : songTime, value : 재생 시간
				songAllInfo.put("songTime", songDetailAlbumInfo.select("ul.info-data > li").get(3).select("span.value").first().text().toString());

				progressMonitor.setProgress(90);
				
				// key : likeNum, value : 좋아요 개수
				songAllInfo.put("likeNum", songDetailAlbumInfo.select("p.song-button-zone > span.sns-like > a.like.radius > em#emLikeCount").first().text().toString());
				
				progressMonitor.setProgress(100);
				
			}
			catch (HttpStatusException e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("많은 요청으로 인해 불러오기에 실패하였습니다.");
				songCount = 0;
				return;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("Url 링크가 잘못되었거나, 웹 페이지 구조가 변경되어 파싱에 실패했습니다 :(");
				songCount = 0;
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
				chartList = null;
				songDetailInfo = null;
				System.out.println("파싱도중 에러가 발생했습니다 :(");
				songCount = 0;
				return;
			}
			songDetailInfo = new JSONObject(songAllInfo);
			songCount++;
			System.out.println(songDetailInfo);
		}
	}
	
	@Override
	public void chartDataParsing(Component parentComponent) {
		if (chartThread == null) chartThread = new Thread(new ChartDataParsingThread());
		if (chartThread.isAlive()) chartThread.stop();
		progressMonitorManager(parentComponent, genieChartParsingTitle, genieChartParsingMessage);
		chartThread.start();
		try {
			chartThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void songDetailDataParsing(String songId, Component parentComponent) {
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + songId;
		if (songDetailThread == null) songDetailThread = new Thread(new SongDetailDataParsingThread());
		if (songDetailThread.isAlive()) songDetailThread.stop();
		progressMonitorManager(parentComponent, songDetailParsingTitle, songDetailParsingMessage);
		songDetailThread.start();
		try {
			songDetailThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void songDetailDataParsing(JSONObject obj, Component parentComponent) {
		if (obj == null) {
			System.out.println(plzUseRightJSONObject);
			return;
		}

		if (!obj.containsKey("songId")) {
			System.out.println(jsonDontHaveKey);
			return;
		}
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + obj.get("songId").toString();
		if (songDetailThread == null) songDetailThread = new Thread(new SongDetailDataParsingThread());
		if (songDetailThread.isAlive()) songDetailThread.stop();
		progressMonitorManager(parentComponent, songDetailParsingTitle, songDetailParsingMessage);
		songDetailThread.start();
		try {
			songDetailThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void songDetailDataParsing(int rank, JSONArray chartListData, Component parentComponent) {
		if (chartListData == null) {
			System.out.println("차트 파싱된 데이터가 없어 메소드 실행을 종료합니다 :(");
			return;
		}
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + ((JSONObject) chartListData.get(rank - 1)).get("songId").toString();
		if (songDetailThread == null) songDetailThread = new Thread(new SongDetailDataParsingThread());
		if (songDetailThread.isAlive()) songDetailThread.stop();
		progressMonitorManager(parentComponent, songDetailParsingTitle, songDetailParsingMessage);
		songDetailThread.start();
		try {
			songDetailThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void songDetailDataParsing(String title, JSONArray chartListData, Component parentComponent) {
		/*
		 * 비추천 하는 메소드 입니다. title에 맞는 데이터를 처음부터 찾아가야 하기 때문에 좀 더 비효율적입니다.
		 */
		String tmpSongId = null;

		if (chartListData == null) {
			System.out.println("차트 파싱된 데이터가 없어 메소드 실행을 종료합니다 :(");
			return;
		}

		for (int i = 0; i < 100; i++) {
			if (((JSONObject) chartListData.get(i)).get("title").toString() == title) {
				url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + ((JSONObject) chartListData.get(i)).get("songId").toString();
				tmpSongId = ((JSONObject) chartListData.get(i)).get("songId").toString();
				break;
			}
		}
		if (tmpSongId == null) {
			System.out.println("제목에 해당하는 노래가 차트 데이터에 없어 불러올 수 없습니다 :(");
			return;
		}
		else {
			if (songDetailThread == null) songDetailThread = new Thread(new SongDetailDataParsingThread());
			if (songDetailThread.isAlive()) songDetailThread.stop();
			progressMonitorManager(parentComponent, songDetailParsingTitle, songDetailParsingMessage);
			songDetailThread.start();
			try {
				songDetailThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	// 지니는 발매일(releaseDate)를 웹 페이지에서 보여주지 않아 getReleaseDate 메소드가 없음
	
	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getGenre() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		if (songCount == 1)
			return songDetailInfo.get("genre").toString();
		
		System.out.println("getGenre() : " + isOnlyDetailParse);
		return null;
	}

	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getGenre(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
			
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
			
		if (songCount == 1) {
			if (jObj.containsKey("genre"))
				return jObj.get("genre").toString();
			else {
				System.out.println(jsonDontHaveKey);
				return null;
			}
		}
			
		System.out.println("getGenre(JSONObject jObj) : " + isOnlyDetailParse);
		return null;
	}

	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getSongTime() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		if (songCount == 1)
			return songDetailInfo.get("songTime").toString();
			
		System.out.println("getSongTime() : " + isOnlyDetailParse);
		return null;
	}
		
	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getSongTime(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
			
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
			
		if (songCount == 1)
			return jObj.get("songTime").toString();
			
		System.out.println(jsonDontHaveKey);
		return null;
	}

	// songDetailDataParsing 후에만 사용가능한 메소드
	public String getLikeNum() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		if (songCount == 1)
			return songDetailInfo.get("likeNum").toString();
				
		System.out.println("getLikeNum() : " + isOnlyDetailParse);
		return null;
	}
	
}
