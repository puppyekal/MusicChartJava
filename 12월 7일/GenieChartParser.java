import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author SejongUniv ��â��
 * @version 1.1
 *
 **/

public class GenieChartParser extends MusicChartParser {

	/*
	 * GenieChartParser Description (KO_KR)
	 * 
	 **************************************************
	 * 
	 * ** ��Ʈ 100���� �Ľ��� �ÿ� ���� �� �ִ� �͵� **
	 * �뷡 ���̵�		(key : songId)
	 * �뷡 ����		(key : rank)
	 * �뷡 ���� �̹���	(key : smallImageUrl)
	 * �뷡 ����		(key : title)
	 * ���� �̸�		(key : artist)
	 * �ٹ� �̸�		(key : albumName)
	 * 
	 * ** ��Ʈ 100���� �Ľ��� �ÿ� ��� ������ �޼ҵ� **
	 * - �޼ҵ� �̸��� ���� �޼ҵ���� ��ȯ���� ��� ����.
	 * - [��ȯ��] �޼ҵ��̸�() �� ���� ǥ���ߴ�.
	 * 
	 * <��Ʈ 100�� �Ľ� ���� �޼ҵ�>
	 * [void]		chartDataParsing()
	 * [boolean]	isParsed()
	 * 
	 * <��Ʈ 100�� �뷡 ���� get �޼ҵ�>
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
	 * ** �뷡 1���� ���� �� ������ �Ľ��� �ÿ� ���� �� �ִ� �͵� **
	 * �뷡 ū �̹���		(key : imageUrl)
	 * �뷡 �帣		(key : genre)
	 * �뷡 ����ð�		(key : songTime)
	 * �뷡 ���ƿ� ����	(key : likeNum)
	 *
	 * ** �뷡 1���� ���� �� ������ �Ľ��� �ÿ� ��� ������ �޼ҵ� **
	 * - �޼ҵ� �̸��� ���� �޼ҵ���� ��ȯ���� ��� ����.
	 * - [��ȯ��] �޼ҵ��̸�() �� ���� ǥ���ߴ�.
	 * 
	 * <�뷡 1���� ���� �� ���� �Ľ� ���� �޼ҵ�>
	 * [void]		songDetailDataParsing(String songId)
	 * [void]		songDetailDataParsing(JSONObject jObj)
	 * [void]		songDetailDataParsing(int rank, JSONArray chartListData)
	 * [void]		songDetailDataParsing(String title, JSONArray chartListData)
	 * [boolean]	isParsed()
	 * 
	 * <�뷡 1���� ���� �� ���� get �޼ҵ�>
	 * [JSONObject]	getSongData()
	 * [String]		getImageUrl()		getImageUrl(JSONObject jObj)	getImageUrl(int rank)	getImageUrl(String title)
	 * [String]		getGenre()			getGenre(JSONObject jObj)
	 * [String]		getSongTime()		getSongTime(JSONObject jObj)
	 * [String]		getLikeNum()		getLikeNum(JSONObject jObj)
	 * 
	 **************************************************
	 *
	 */
	
	public GenieChartParser() {
		songCount = 0;
		chartList = AppManager.getS_instance().getJSONArray(2);
		songDetailInfo = null;
		url = null;
	}
	
	@Override
	public void chartDataParsing() {
		// ���� ��Ʈ 1~100���� �뷡�� �Ľ���
		songCount = 0;
		url = "https://www.genie.co.kr/chart/top200";

		try {
			// ���� ��Ʈ ���ῡ �ʿ��� header ���� �� ����
			Connection genieConnection1_50 = Jsoup.connect(url).header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
					.header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
					.method(Connection.Method.GET);

			// ���� �� ���������� �ܾ��
			Document genieDocument1_50 = genieConnection1_50.get();

			// 1~100���� ���� ������ �ҷ���
			Elements data1st50 = genieDocument1_50.select("table.list-wrap").first().select("tbody > tr.list");

			chartList = new JSONArray();

			for (Element elem : data1st50) { // 1~50���� ���� ���� �Ľ�
				// JSONObject�� �����͸� �ֱ� ���� �۾�
				HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

				// key : songId, value : �뷡 ���̵�
				songAllInfo.put("songId", elem.attr("songId").toString());

				// key : rank, value : ����
				songAllInfo.put("rank", elem.select("td.number").first().text().toString().split(" ")[0]);

				// key : smallImageUrl, value : ���� �̹��� url ��ũ
				songAllInfo.put("smallImageUrl",
						"https:" + elem.select("td").get(2).select("img").first().attr("src").toString());

				// key : title, value : �뷡 ����
				songAllInfo.put("title", elem.select("td.info").first().select("a").first().text().toString());

				// key : artist, value : ���� �̸�
				songAllInfo.put("artist", elem.select("td.info").first().select("a").get(1).text().toString());

				// key : albumName, value : �ٹ� �̸�
				songAllInfo.put("albumName", elem.select("td.info").first().select("a").get(2).text().toString());

				// ������ JSONObject�� ��ȯ
				JSONObject jsonSongInfo = new JSONObject(songAllInfo);

				// JSONArray�� �� �߰�
				chartList.add(jsonSongInfo);
				songCount++;
			}
			
			String url51_100 = genieDocument1_50.select("div.page-nav.rank-page-nav").first().select("a").get(1).attr("href").toString();
			
			// ���� ��Ʈ ���ῡ �ʿ��� header ���� �� ����
			Connection genieConnection51_100 = Jsoup.connect(url + url51_100).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
					.header("Sec-Fetch-User", "?1")
					.header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
					.method(Connection.Method.GET);
			
			// ���� �� ���������� �ܾ��
			Document genieDocument51_100 = genieConnection51_100.get();

			// 51~100���� ���� ������ �ҷ���
			Elements data51st100 = genieDocument51_100.select("table.list-wrap").first().select("tbody > tr.list");

			for (Element elem : data51st100) { // 51~100���� ���� ���� �Ľ�
				// JSONObject�� �����͸� �ֱ� ���� �۾�
				HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

				// key : songId, value : �뷡 ���̵�
				songAllInfo.put("songId", elem.attr("songId").toString());

				// key : rank, value : ����
				songAllInfo.put("rank", elem.select("td.number").first().text().toString().split(" ")[0]);

				// key : smallImageUrl, value : ���� �̹��� url ��ũ
				songAllInfo.put("smallImageUrl", "https:" + elem.select("td").get(2).select("img").first().attr("src").toString());

				// key : title, value : �뷡 ����
				songAllInfo.put("title", elem.select("td.info").first().select("a").first().text().toString());

				// key : artist, value : ���� �̸�
				songAllInfo.put("artist", elem.select("td.info").first().select("a").get(1).text().toString());

				// key : albumName, value : �ٹ� �̸�
				songAllInfo.put("albumName", elem.select("td.info").first().select("a").get(2).text().toString());

				// ������ JSONObject�� ��ȯ
				JSONObject jsonSongInfo = new JSONObject(songAllInfo);

				// JSONArray�� �� �߰�
				chartList.add(jsonSongInfo);
				songCount++;
			}
			
			// �Ľ� ��� ���(�׽�Ʈ��)
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
			System.out.println("���� ��û���� ���� �ҷ����⿡ �����Ͽ����ϴ�.");
			songCount = 0;
			return;
		} catch (NullPointerException e) {
			e.printStackTrace();
			chartList = null;
			songDetailInfo = null;
			System.out.println("Url ��ũ�� �߸��Ǿ��ų�, �� ������ ������ ����Ǿ� �Ľ̿� �����߽��ϴ� :(");
			songCount = 0;
			return;
		} catch (Exception e) {
			e.printStackTrace();
			chartList = null;
			songDetailInfo = null;
			System.out.println("�Ľ̵��� ������ �߻��߽��ϴ� :(");
			songCount = 0;
			return;
		}
	}
	
	private void songDetailDataParse(String url) {
		songCount = 0;
		HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

		try {
			// songId�� ���� � ���� ���� ������ ��� ���� ����
			Connection songDetailConnection = Jsoup.connect(url).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
					.header("Sec-Fetch-User", "?1")
					.header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
					.method(Connection.Method.GET);

			// � ���� ���� ���� �� �������� �ܾ��
			Document songDetailDocument = songDetailConnection.get();
			Element songDetailInfo = songDetailDocument.select("div.song-main-infos").first();
			
			Element songDetailAlbumInfo = songDetailInfo.select("div.info-zone").first();
			
			// key : imageUrl, value : ū �̹��� url ��ũ
			songAllInfo.put("imageUrl", "https:" + songDetailInfo.select("div.photo-zone > a").first().attr("href").toString());

			// key : genre, value : �뷡 �帣
			songAllInfo.put("genre", songDetailAlbumInfo.select("ul.info-data > li").get(2).select("span.value").first().text().toString());
			
			// key : songTime, value : ��� �ð�
			songAllInfo.put("songTime", songDetailAlbumInfo.select("ul.info-data > li").get(3).select("span.value").first().text().toString());

			// key : likeNum, value : ���ƿ� ����
			songAllInfo.put("likeNum", songDetailAlbumInfo.select("p.song-button-zone > span.sns-like > a.like.radius > em#emLikeCount").first().text().toString());
			
		}
		catch (HttpStatusException e) {
			e.printStackTrace();
			chartList = null;
			songDetailInfo = null;
			System.out.println("���� ��û���� ���� �ҷ����⿡ �����Ͽ����ϴ�.");
			songCount = 0;
			return;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			chartList = null;
			songDetailInfo = null;
			System.out.println("Url ��ũ�� �߸��Ǿ��ų�, �� ������ ������ ����Ǿ� �Ľ̿� �����߽��ϴ� :(");
			songCount = 0;
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
			chartList = null;
			songDetailInfo = null;
			System.out.println("�Ľ̵��� ������ �߻��߽��ϴ� :(");
			songCount = 0;
			return;
		}
		songDetailInfo = new JSONObject(songAllInfo);
		songCount++;
		System.out.println(songDetailInfo);
	}

	public void songDetailDataParsing(String songId) {
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + songId;
		songDetailDataParse(url);
	}

	public void songDetailDataParsing(JSONObject obj) {
		if (obj == null) {
			System.out.println(plzUseRightJSONObject);
			return;
		}

		if (!obj.containsKey("songId")) {
			System.out.println(jsonDontHaveKey);
			return;
		}
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + obj.get("songId").toString();
		songDetailDataParse(url);
	}

	public void songDetailDataParsing(int rank, JSONArray chartListData) {
		if (chartListData == null) {
			System.out.println("��Ʈ �Ľ̵� �����Ͱ� ���� �޼ҵ� ������ �����մϴ� :(");
			return;
		}
		url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + ((JSONObject) chartListData.get(rank - 1)).get("songId").toString();
		songDetailDataParse(url);
	}

	public void songDetailDataParsing(String title, JSONArray chartListData) {
		/*
		 * ����õ �ϴ� �޼ҵ� �Դϴ�. title�� �´� �����͸� ó������ ã�ư��� �ϱ� ������ �� �� ��ȿ�����Դϴ�.
		 */
		String tmpSongId = null;

		if (chartListData == null) {
			System.out.println("��Ʈ �Ľ̵� �����Ͱ� ���� �޼ҵ� ������ �����մϴ� :(");
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
			System.out.println("���� �ش��ϴ� �뷡�� ��Ʈ �����Ϳ� ���� �ҷ��� �� �����ϴ� :(");
			return;
		} else
			songDetailDataParse(url);
	}

	
	// ���ϴ� �߸���(releaseDate)�� �� ���������� �������� �ʾ� getReleaseDate �޼ҵ尡 ����
	
	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
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

	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
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

	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
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
		
	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
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

	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
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
