package com.duces.zupidoo.Model;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;


public class GamesJSON  {

private String gameid;
private String gamename;
private String gameurl;
private String publisher;
private String month;
private String day;
private String year;
private String unixrelease;
private String amazonkey;
private String amazonparent;
private String rank;
private String banner;
private String imagesmall;
private String imagelarge;
private String platform;
private String purchase;
private String release;

private Map<String, Object> additionalProperties = new HashMap<String, Object>();


public String getRelease() {
	return release;
}

public void setRelease(String release) {
	this.release = release;
}

public String getPurchase() {
	return purchase;
}

public void setPurchase(String purchase) {
	this.purchase = purchase;
}

public String getGameid() {
return gameid;
}

public void setGameid(String gameid) {
this.gameid = gameid;
}

public String getGamename() {
return gamename;
}

public void setGamename(String gamename) {
this.gamename = gamename;
}

public String getGameurl() {
return gameurl;
}

public void setGameurl(String gameurl) {
this.gameurl = gameurl;
}

public String getPublisher() {
return publisher;
}

public void setPublisher(String publisher) {
this.publisher = publisher;
}

public String getMonth() {
return month;
}

public void setMonth(String month) {
this.month = month;
}

public String getDay() {
return day;
}

public void setDay(String day) {
this.day = day;
}

public String getYear() {
return year;
}

public void setYear(String year) {
this.year = year;
}

public String getUnixrelease() {
return unixrelease;
}

public void setUnixrelease(String unixrelease) {
this.unixrelease = unixrelease;
}


public String getAmazonkey() {
return amazonkey;
}

public void setAmazonkey(String amazonkey) {
this.amazonkey = amazonkey;
}

public String getAmazonparent() {
return amazonparent;
}

public void setAmazonparent(String amazonparent) {
this.amazonparent = amazonparent;
}

public String getRank() {
return rank;
}

public void setRank(String rank) {
this.rank = rank;
}

public String getBanner() {
return banner;
}

public void setBanner(String banner) {
this.banner = banner;
}


public String getImagesmall() {
return imagesmall;
}

public void setImagesmall(String imagesmall) {
this.imagesmall = imagesmall;
}

public String getImagelarge() {
return imagelarge;
}

public void setImagelarge(String imagelarge) {
this.imagelarge = imagelarge;
}

public String getPlatform() {
return platform;
}

public void setPlatform(String platform) {
this.platform = platform;
}


public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}


//    protected GamesJSON(Parcel in) {
//        gameid = in.readString();
//        gamename = in.readString();
//        gameurl = in.readString();
//        publisher = in.readString();
//        month = in.readString();
//        day = in.readString();
//        year = in.readString();
//        unixrelease = in.readString();
//        amazonkey = in.readString();
//        amazonparent = in.readString();
//        rank = in.readString();
//        banner = in.readString();
//        imagesmall = in.readString();
//        imagelarge = in.readString();
//        platform = in.readString();
//        release = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(gameid);
//        dest.writeString(gamename);
//        dest.writeString(gameurl);
//        dest.writeString(publisher);
//        dest.writeString(month);
//        dest.writeString(day);
//        dest.writeString(year);
//        dest.writeString(unixrelease);
//        dest.writeString(amazonkey);
//        dest.writeString(amazonparent);
//        dest.writeString(rank);
//        dest.writeString(banner);
//        dest.writeString(imagesmall);
//        dest.writeString(imagelarge);
//        dest.writeString(platform);
//        dest.writeString(release);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<GamesJSON> CREATOR = new Parcelable.Creator<GamesJSON>() {
//        @Override
//        public GamesJSON createFromParcel(Parcel in) {
//            return new GamesJSON(in);
//        }
//
//        @Override
//        public GamesJSON[] newArray(int size) {
//            return new GamesJSON[size];
//        }
//    };
}