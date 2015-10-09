package com.duces.zupidoo.Model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ListParcelable implements Parcelable{
private ArrayList<GamesJSON> list;

	public ListParcelable(Parcel in) {
//	in.readTypedList(list, GamesJSON.CREATOR);
	}
	
	 ListParcelable(){
		list = new ArrayList<GamesJSON>();
	}

	public ArrayList<GamesJSON> getList() {
	return list;
	}

	public void setList(ArrayList<GamesJSON> list) {
		this.list = list;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeTypedList(list);
		
	}
	public static final Parcelable.Creator<ListParcelable> CREATOR = new Parcelable.Creator<ListParcelable>() {

	@Override
	public ListParcelable createFromParcel(Parcel source) {
		// TODO Auto-generated method stub
		return new ListParcelable(source);
	}

	@Override
	public ListParcelable[] newArray(int size) {
		// TODO Auto-generated method stub
		return new ListParcelable[size];
	}
};


}
