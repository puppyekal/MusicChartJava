public class MusicInform {
    private String albumName;
    private String releaseDate;
    private String imageUrl;
    private String genre;
    private String title;
    private int rank;

    public String getAlbumName() {return albumName;}
    public String getReleaseDate() {return releaseDate;}
    public String getImageUrl() {return imageUrl;}
    public String getGenre() {return genre;}
    public String getTitle() {return title;}
    public int getRank() {return rank;}

    public void setAlbumName(String albumName) {this.albumName = albumName;}
    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate; }
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setTitle(String title) {this.title = title;}
    public void setRank(int rank) {this.rank = rank;}

    public MusicInform(String albumName, String releaseDate, String imageUrl, String genre, String title, int rank) {
        this.albumName = albumName;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.genre = genre;
        this.title = title;
        this.rank = rank;
    }

    public void SysShow(){
        System.out.println("rank="+getRank()+", title="+getTitle()+", albumName="+getAlbumName()+", releaseDate="+getReleaseDate()+", genre="+getGenre() + ", imageURL=" + getImageUrl());
    }//to debug

    public String showList(){
        String Musicchart;
        Musicchart = "rank= "+getRank()+", title= "+getTitle()+", albumName= "+getAlbumName()+", releaseDate= "+getReleaseDate()+", genre= "+getGenre() + ", imageURL= " + getImageUrl();
        return Musicchart;
    }
}//MusicInfo
