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
 * @author SejongUniv ��â��
 * @version 1.1
 *
 **/

public class MelonChartParser extends MusicChartParser {

	/*
	 * MelonChartParser Description (KO_KR)
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
	 * �뷡 ���ƿ� ����	(key : likeNum)
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
	 * [String]		getLikeNum(int rank)	getLikeNum(String title)		getLikeNum(JSONObject jObj)
	 * [String]		getImageUrl(int rank)	getImageUrl(String title)		getImageUrl(JSONObject jObj)
	 * [String]		getSongId(int rank)		getSongId(String title)			getSongId(JSONObject jObj)
	 *
	 **************************************************
	 *
	 * ** �뷡 1���� ���� �� ������ �Ľ��� �ÿ� ���� �� �ִ� �͵� **
	 * �뷡 ū �̹���		(key : imageUrl)
	 * �뷡 �߸���		(key : releaseDate)
	 * �뷡 �帣		(key : genre)
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
	 * [String]		getReleaseDate()	getReleaseDate(JSONObject jObj)
	 * [String]		getGenre()			getGenre(JSONObject jObj)
	 * 
	 **************************************************
	 *
	 */
	
	public MelonChartParser() {
		songCount = 0;
		chartList = AppManager.getS_instance().getJSONArray(1);
		songDetailInfo = null;
		url = null;
	}
	
	@Override
	public void chartDataParsing() {
		// ��� ��Ʈ 1~100���� �뷡�� �Ľ���
		songCount = 0;
		url = "https://www.melon.com/chart/index.htm";

		try {
			// ��� ��Ʈ ���ῡ �ʿ��� header ���� �� ����
			Connection melonConnection = Jsoup.connect(url).header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
					.header("Sec-Fetch-User", "?1").header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
					.method(Connection.Method.GET);

			// ���� �� ���������� �ܾ��
			Document melonDocument = melonConnection.get();

			// 1~50���� ���� ������ �ҷ���, ������ ���� ���� ������ �̱� ���� ��ũ�� �̴� �뵵�� ���
			Elements data1st50 = melonDocument.select("tr.lst50");
			
			// 51~100���� ���� ������ �ҷ���, ������ ���� ���� ������ �̱� ���� ��ũ�� �̴� �뵵�� ���
			Elements data51st100 = melonDocument.select("tr.lst100");

			if(!chartList.isEmpty()) chartList.clear();

			for (Element elem : data1st50) { // 1~50���� ���� ���� �Ľ�
				// JSONObject�� �����͸� �ֱ� ���� �۾�
				HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

				// key : songId, value : �뷡 ���̵� - �� ������ �Ľ��� �� �ʿ���
				songAllInfo.put("songId", elem.attr("data-song-no").toString());

				// key : rank, value : ����
				songAllInfo.put("rank", elem.select("span.rank").first().text());

				// key : smallImageUrl, value : �뷡 �̹���(������ ����) ��ũ (ū ������ �̹����� detailDataParsing���� �ٷ�)
				songAllInfo.put("smallImageUrl", elem.select("a > img").first().attr("src").toString());

				// key : title, value : �뷡 ����
				songAllInfo.put("title", elem.select("div.ellipsis > span > a").first().text().toString());

				// key : artist, value : ���� �̸�
				songAllInfo.put("artist", elem.select("div.ellipsis").get(1).select("a").first().text().toString());

				// key : albumName, value : �ٹ� �̸�
				songAllInfo.put("albumName", elem.select("div.ellipsis").get(2).select("a").text().toString());

				String likeNumUrl = "https://www.melon.com/commonlike/getSongLike.json?contsIds="
						+ songAllInfo.get("songId").toString();

				Document likeNumDocument = Jsoup.connect(likeNumUrl).header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
						.header("Sec-Fetch-User", "?1").header("Upgrade-Insecure-Requests", "1")
						.header("User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
						.ignoreContentType(true).get();

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(likeNumDocument.text());
				songAllInfo.put("likeNum",
						((JSONObject) (((JSONArray) obj.get("contsLike")).get(0))).get("SUMMCNT").toString());

				// ������ JSONObject�� ��ȯ
				JSONObject jsonSongInfo = new JSONObject(songAllInfo);

				// JSONArray�� �� �߰�
				chartList.add(jsonSongInfo);
				songCount++;
			}

			for (Element elem : data51st100) { // 51~100���� ���� ���� �Ľ�
				// JSONObject�� �����͸� �ֱ� ���� �۾�
				HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

				// key : songId, value : �뷡 ���̵� - �� ������ �Ľ��� �� �ʿ���
				songAllInfo.put("songId", elem.attr("data-song-no").toString());

				// key : rank, value : ����
				songAllInfo.put("rank", elem.select("span.rank").first().text());

				// key : smallImageUrl, value : �뷡 �̹���(������ ����) ��ũ (ū ������ �̹�����
				// detailDataParsing���� �ٷ�)
				songAllInfo.put("smallImageUrl", elem.select("a > img").first().attr("src").toString());

				// key : title, value : �뷡 ����
				songAllInfo.put("title", elem.select("div.ellipsis > span > a").first().text().toString());

				// key : artist, value : ���� �̸�
				songAllInfo.put("artist", elem.select("div.ellipsis").get(1).select("a").first().text().toString());

				// key : albumName, value : �ٹ� �̸�
				songAllInfo.put("albumName", elem.select("div.ellipsis").get(2).select("a").text().toString());

				String likeNumUrl = "https://www.melon.com/commonlike/getSongLike.json?contsIds=" + songAllInfo.get("songId").toString();

				Document likeNumDocument = Jsoup.connect(likeNumUrl).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
						.header("Sec-Fetch-User", "?1")
						.header("Upgrade-Insecure-Requests", "1")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
						.ignoreContentType(true).get();

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(likeNumDocument.text());
				songAllInfo.put("likeNum", ((JSONObject) (((JSONArray) obj.get("contsLike")).get(0))).get("SUMMCNT").toString());

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
			AppManager.getS_instance().setJSONArray(chartList, 0);
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
	}

	private void songDetailDataParse(String url) {
		
		songCount = 0;
		HashMap<String, Object> songAllInfo = new HashMap<String, Object>();

		try {
			// songId�� ���� � ���� ���� ������ ��� ���� ����
			Connection songDetailConnection = Jsoup.connect(url).header("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
					.header("Sec-Fetch-User", "?1").header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
					.method(Connection.Method.GET);

			// � ���� ���� ���� �� �������� �ܾ��
			Document songDetailDocument = songDetailConnection.get();
			Element songDetailInfo = songDetailDocument.select(".wrap_info").first();

			String songImageUrl = songDetailInfo.getElementsByTag("img").first().attr("src");
			songAllInfo.put("imageUrl", songImageUrl);

			Element songDetailEtcInfo = songDetailInfo.select("dl.list").first();

			String songReleaseDate = songDetailEtcInfo.getElementsByTag("dd").get(1).text();
			songAllInfo.put("releaseDate", songReleaseDate);

			String songGenre = songDetailEtcInfo.getElementsByTag("dd").get(2).text();
			songAllInfo.put("genre", songGenre);
			
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
	}

	@Override
	public void songDetailDataParsing(String songId) {
		url = "https://www.melon.com/song/detail.htm?songId=" + songId;
		songDetailDataParse(url);
	}

	@Override
	public void songDetailDataParsing(JSONObject jObj) {
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return;
		}
		
		if (!jObj.containsKey("songId")) {
			System.out.println(jsonDontHaveKey);
			return;
		}
		url = "https://www.melon.com/song/detail.htm?songId=" + jObj.get("songId").toString();
		songDetailDataParse(url);
	}
	
	@Override
	public void songDetailDataParsing(int rank, JSONArray chartListData) {
		if (chartListData == null) {
			System.out.println("��Ʈ �Ľ̵� �����Ͱ� ���� �޼ҵ� ������ �����մϴ� :(");
			return;
		}
		url = "https://www.melon.com/song/detail.htm?songId="
				+ ((JSONObject) chartListData.get(rank - 1)).get("songId").toString();
		songDetailDataParse(url);
	}
	
	@Override
	public void songDetailDataParsing(String title, JSONArray chartListData) {
		/* ����õ �ϴ� �޼ҵ� �Դϴ�. title�� �´� �����͸� ó������ ã�ư��� �ϱ� ������ �� �� ��ȿ�����Դϴ�. */
		String tmpSongId = null;
		
		if (chartListData == null) {
			System.out.println("��Ʈ �Ľ̵� �����Ͱ� ���� �޼ҵ� ������ �����մϴ� :(");
			return;
		}

		for (int i = 0; i < 100; i++) {
			if (((JSONObject) chartListData.get(i)).get("title").toString() == title) {
				url = "https://www.melon.com/song/detail.htm?songId=" + ((JSONObject) chartListData.get(i)).get("songId").toString();
				tmpSongId = ((JSONObject) chartListData.get(i)).get("songId").toString();
				break;
			}
		}
		if (tmpSongId == null) {
			System.out.println("���� �ش��ϴ� �뷡�� ��Ʈ �����Ϳ� ���� �ҷ��� �� �����ϴ� :(");
			return;
		}
		else
			songDetailDataParse(url);
	}
	
	public String getLikeNum(int rank) {
		if (rank < 1 || rank > 100) {
			System.out.println("1~100�� �̳��� ������ �Է����ּ���");
			return null;
		}
		
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getLikeNum(int rank) : " + isOnlyChartParse);
			return null;
		}
		
		return ((JSONObject)chartList.get(rank - 1)).get("likeNum").toString();
	}

	public String getLikeNum(String title) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (songCount == 1) {
			System.out.println("getLikeNum(String title) : " + isOnlyChartParse);
			return null;
		}

		for (int i = 0; i < songCount; i++) {
			if (((JSONObject) chartList.get(i)).get("title") == title)
				return ((JSONObject) chartList.get(i)).get("likeNum").toString();
		}
		
		return null;
	}

	@Override
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

	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
	public String getReleaseDate() {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		if (songCount == 1)
			return songDetailInfo.get("releaseDate").toString();
		
		System.out.println("getReleaseDate() : " + isOnlyDetailParse);
		return null;
	}
	
	// songDetailDataParsing �Ŀ��� ��밡���� �޼ҵ�
	public String getReleaseDate(JSONObject jObj) {
		if (!isParsed()) {
			System.out.println(isNotParsed);
			return null;
		}
		
		if (jObj == null) {
			System.out.println(plzUseRightJSONObject);
			return null;
		}
		
		if (songCount == 1) {
			if (jObj.containsKey("releaseDate"))
				return jObj.get("releaseDate").toString();
			else {
				System.out.println(jsonDontHaveKey);
				return null;
			}
		}
		
		System.out.println("getReleaseDate(JSONObject jObj) : " + isOnlyDetailParse);
		return null;
	}
	
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
}
