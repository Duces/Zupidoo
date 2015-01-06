package com.duces.zupidoo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
		View rootView = inflater.inflate(R.layout.fragment_games, container, false);
		gamesdata = new ArrayList<GamesJSON>();
		alphadata = new ArrayList<GamesJSON>();
//		Bundle bundle = getArguments();
//		listparc = bundle.getParcelable("GamesData");
//		gamesdata = listparc.getList();
		gamesdata = MainActivity.games_pojo;
		alphadata = alphabetizedData(gamesdata);
	    imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		games_gv = (GridView) rootView.findViewById(R.id.games_gv);
		adapter = new GamesGridAdapter(getActivity(), R.layout.gamerecord_grid, alphadata, false);
		games_gv.setAdapter(adapter);
		
//		search_et = (EditText) rootView.findViewById(R.id.search_et);
//		search_et.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
//		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//						imm.hideSoftInputFromWindow(search_et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			            performSearch();
//			            return true;
//			        }
//				return false;
//			}
//		});
		
	
		
		return rootView;
	}
	public void closeSearch(){
//		imm.hideSoftInputFromWindow(search_et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		games_gv.setAdapter(adapter);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void performSearch(String search){
		searchdata = new ArrayList<GamesJSON>();
//		String searchterm = search_et.getText().toString();
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
//		ordereddata.clear();
//		ordereddata = data;
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
