package com.duces.zupidoo;

import java.util.ArrayList;






import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FavoritesFragment extends Fragment implements FragmentInterface{
	ArrayList<GamesJSON> gamesdata;
	ListParcelable listparc;
	GridView favs_gv;
	TextView favs_tv;
	GamesGridAdapter adapter;
	GameRecordUtils utils;
	
	public FavoritesFragment(){
	}




@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
	gamesdata = new ArrayList<GamesJSON>();
//	Bundle bundle = getArguments();
//	listparc = bundle.getParcelable("GamesData");
//	gamesdata = listparc.getList();
	gamesdata = MainActivity.games_pojo;
	utils = new GameRecordUtils(getActivity());
	favs_gv = (GridView) rootView.findViewById(R.id.favs_gv);
	favs_tv = (TextView) rootView.findViewById(R.id.favs_tv);
	favs_tv.setText("No Favorites");
	
	refreshFavs();
	return rootView;
}
public void refreshFavs(){
	adapter = new GamesGridAdapter(getActivity(), R.layout.gamerecord_grid, filterFavs(), true, this);
	favs_gv.setAdapter(adapter);
	if (filterFavs().isEmpty()){
		favs_gv.setVisibility(View.GONE);
		favs_tv.setVisibility(View.VISIBLE);
	}else{
		favs_gv.setVisibility(View.VISIBLE);
		favs_tv.setVisibility(View.GONE);
	}
	
}
private ArrayList<GamesJSON> filterFavs(){
	ArrayList<GamesJSON> favsdata = new ArrayList<GamesJSON>();
	favsdata.clear();
	for (int z = 0;z<gamesdata.size();z++){
		if (utils.isInFavs(gamesdata.get(z).getAmazonkey())){
		favsdata.add(gamesdata.get(z));
		}
	}
	
	return favsdata;
}


@Override
public void fragmentBecameVisible() {
	refreshFavs();
	
}

}
