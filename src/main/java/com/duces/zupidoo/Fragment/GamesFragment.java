package com.duces.zupidoo.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.duces.zupidoo.Adapter.GamesGridAdapter;
import com.duces.zupidoo.Model.GamesJSON;
import com.duces.zupidoo.Model.ListParcelable;
import com.duces.zupidoo.Activity.MainActivity;
import com.duces.zupidoo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;

public class GamesFragment extends Fragment{
ArrayList<GamesJSON> gamesdata;
ArrayList<GamesJSON> searchdata;
ArrayList<GamesJSON> alphadata;

ListParcelable listparc;
GridView games_gv;
GamesGridAdapter adapter;
EditText search_et;
InputMethodManager imm;
	public GamesFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_games, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		gamesdata = new ArrayList<>();
		alphadata = new ArrayList<>();
		gamesdata = MainActivity.games_pojo;
		alphadata = alphabetizedData(gamesdata);
		imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		games_gv = (GridView) view.findViewById(R.id.games_gv);
		adapter = new GamesGridAdapter(getActivity(), R.layout.gamerecord_grid, alphadata, false);
		games_gv.setAdapter(adapter);

	}

	public void closeSearch(){
		games_gv.setAdapter(adapter);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void performSearch(String search){
		searchdata = new ArrayList<>();
		for (int x = 0;x<gamesdata.size();x++){
			if (gamesdata.get(x).getGamename().toLowerCase().contains(search.toLowerCase())){
				searchdata.add(gamesdata.get(x));
			}
		}
		GamesGridAdapter searchadapter = new GamesGridAdapter(getActivity(), R.layout.gamerecord_grid, searchdata, false);
		games_gv.setAdapter(searchadapter);
	}
	private ArrayList<GamesJSON> alphabetizedData(ArrayList<GamesJSON> data){
		ArrayList<GamesJSON> ordereddata  = new ArrayList<GamesJSON>(data);
		Collections.sort(ordereddata, new Comparator<GamesJSON>() {

			@Override
			public int compare(GamesJSON lhs, GamesJSON rhs) {
				return lhs.getGamename().toLowerCase().compareTo(rhs.getGamename().toLowerCase());
			}
		});
		
		return ordereddata;
	}
	@Override
	public void onStart() {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onStart();
	}
	
}
