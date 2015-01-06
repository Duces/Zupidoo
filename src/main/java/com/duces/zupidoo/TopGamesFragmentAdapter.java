package com.duces.zupidoo;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class TopGamesFragmentAdapter extends FragmentStatePagerAdapter{
ArrayList<GamesJSON> gamedata;
	public TopGamesFragmentAdapter(FragmentManager fragmentManager, ArrayList<GamesJSON> gamesdata) {
		super(fragmentManager);
		this.gamedata = gamesdata;
	}


	@Override
	public Fragment getItem(int arg0) {
		TopGamesFragment fragment = new TopGamesFragment();
		Bundle bundle = new Bundle();
		bundle.putString("imgurl", gamedata.get(arg0).getImagelarge());
		bundle.putString("name", gamedata.get(arg0).getGamename());
		bundle.putString("day", gamedata.get(arg0).getDay());
		bundle.putString("month", gamedata.get(arg0).getMonth());
		bundle.putString("year", gamedata.get(arg0).getYear());
		bundle.putString("key", gamedata.get(arg0).getAmazonkey());
		bundle.putString("platform", gamedata.get(arg0).getPlatform());
		bundle.putString("banner", gamedata.get(arg0).getBanner());
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

}
