package com.duces.zupidoo.Fragment;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.duces.zupidoo.FragmentInterface;
import com.duces.zupidoo.GameRecordUtils;
import com.duces.zupidoo.Adapter.GamesGridAdapter;
import com.duces.zupidoo.Model.GamesJSON;
import com.duces.zupidoo.Model.ListParcelable;
import com.duces.zupidoo.Activity.MainActivity;
import com.duces.zupidoo.R;

public class FavoritesFragment extends Fragment implements MainActivity.FavsFragListener {
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

	return inflater.inflate(R.layout.fragment_favorites, container, false);
}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		gamesdata = new ArrayList<>();
		gamesdata = MainActivity.games_pojo;
		utils = new GameRecordUtils(getActivity());
		favs_gv = (GridView) view.findViewById(R.id.favs_gv);
		favs_tv = (TextView) view.findViewById(R.id.favs_tv);
		favs_tv.setText("No Favorites");

		MainActivity activity = (MainActivity)getActivity();
		activity.setFavsFragListener(this);
		refreshFavs();

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
