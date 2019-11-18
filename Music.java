import javax.swing.*;

public class Music {
	// - - - - - Field - - - - -
	//basic information of the song(name and singer)
	private String strName, strSinger;
	//additional information of the song(album and image)
	private String strAlbum;
	private ImageIcon image;
	//information shown in the chart(rank and likes)
	private int nRank, nLikes;
	
	// - - - - - Constructor - - - - -
	
	public Music(int rank, String album, String singer, String name, int likes, ImageIcon image) {
		strName = name;
		strSinger = singer;
		strAlbum = album;
		this.image = image;
		nRank = rank;
		nLikes = likes;
	}
	
	// - - - - - getter & setter - - - - -
	public String getStrName() {
		return strName;
	}
	public void setStrName(String name) {
		strName = name;
	}
	public String getStrSinger() {
		return strSinger;
	}
	public void setStrSinger(String singer) {
		strSinger = singer;
	}
	public String getStrAlbum() {
		return strAlbum;
	}
	public void setStrAlbum(String album) {
		strAlbum = album;
	}
	public ImageIcon getImage() {
		return image;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	public int getnRank() {
		return nRank;
	}
	public void setnRank(int rank) {
		nRank = rank;
	}
	public int getnLikes() {
		return nLikes;
	}
	public void setnLikes(int likes) {
		nLikes = likes;
	}
	
	// - - - - - functional method - - - - -
	/*
	Name: toString
	Parameter: -
	Returns: String representing this object
	Description: Returned string includes rank, singer and name of the song, plus the number of likes it has.
	*/
	@Override
	public String toString() {
		return nRank + ". " + strSinger + " - " + strName + " (♥ " + nLikes + ")";
	}
	/*
	Name: getMusicInfo
	Parameter: -
	Returns: String representing this object
	Description: Returned string includes only the basic information of the song.
	*/
	public String getMusicInfo() {
		return strSinger + " - " + strName;
	}
}
