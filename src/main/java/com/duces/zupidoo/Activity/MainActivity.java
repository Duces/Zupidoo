package com.duces.zupidoo.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.duces.zupidoo.ApiClient;
import com.duces.zupidoo.Fragment.FavoritesFragment;
import com.duces.zupidoo.Fragment.GamesFragment;
import com.duces.zupidoo.Fragment.PopularFragment;
import com.duces.zupidoo.Model.GamesJSON;
import com.duces.zupidoo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

	ProgressBar pb;
	public static ArrayList<GamesJSON> games_pojo;

	ViewPager mainvp;
	TabLayout tabLayout;
	GamesFragment gamesfrag;
	PopularFragment popfrag;
	FavoritesFragment favsfrag;
	ImageView iv_refresh;
	Menu mMenu;
	SectionsPagerAdapter adapter;
	private AdView mAdView;
	boolean refresh;
	public interface FavsFragListener{
		void fragmentBecameVisible();
	}
	FavsFragListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(getDrawable(R.drawable.ic_ab_logo));
		getSupportActionBar().setTitle("");

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		games_pojo = new ArrayList<>();

		gamesfrag = new GamesFragment();
		popfrag = new PopularFragment();
		favsfrag = new FavoritesFragment();
		LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		refresh = false;
		tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

		iv_refresh = (ImageView)inflator.inflate(R.layout.iv_refresh, null);
		mAdView = (AdView) findViewById(R.id.adView);
//        mAdView.setAdListener(new ToastAdListener(this));
        mAdView.loadAd(new AdRequest.Builder().build());
        
		adapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
		mainvp = (ViewPager) findViewById(R.id.mainpager);
		getData();
		
			
	}

	public void setFavsFragListener(FavsFragListener l){
		listener = l;
	}

	private void getData(){
		ApiClient.getZupidooService().getGames(new Callback<List<GamesJSON>>() {
			
			@Override
			public void success(List<GamesJSON> arg0, Response arg1) {
				games_pojo.addAll(arg0);

				tabLayout.post(new Runnable() {
					@Override
					public void run() {
						mainvp.setAdapter(adapter);
//						tabLayout.setupWithViewPager(mainvp);

						mainvp.addOnPageChangeListener(new MyPageScrollListener(tabLayout));

						for (int i = 0; i < adapter.getCount(); i++) {
							TabLayout.Tab tab = tabLayout.newTab().setCustomView(adapter.getTabView(i));
							tabLayout.addTab(tab);
						}
						pb.setVisibility(View.INVISIBLE);
						tabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener());

						tabLayout.getTabAt(0).select();
					}
				});

				mMenu.getItem(0).setVisible(false);
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				Log.v("TAG", "Retrofit error: "+arg0);
			}
		});
	}
	private void Refresh(){
		ApiClient.getZupidooService().getGames(new Callback<List<GamesJSON>>() {
			
			@Override
			public void success(List<GamesJSON> arg0, Response arg1) {

				games_pojo.clear();
				games_pojo.addAll(arg0);
				adapter.notifyDataSetChanged();
				refresh = true;
				pb.setVisibility(View.INVISIBLE);
				mainvp.setVisibility(View.VISIBLE);
				disableAnimateRefresh();
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				Log.v("TAG", "Retrofit error: "+arg0);
			}
		});
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		
		SearchManager searchmanager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView)(menu.findItem(R.id.action_search)).getActionView();
		searchView.setSearchableInfo(searchmanager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				gamesfrag.performSearch(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.length() == 0) {
					gamesfrag.closeSearch();
				}
				return false;
			}
		});
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.action_refresh){
			pb.setVisibility(View.VISIBLE);
			mainvp.setVisibility(View.INVISIBLE);
			animateRefresh();
			Refresh();
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyPageScrollListener implements ViewPager.OnPageChangeListener{
		private TabLayout mTabLayout;

		public MyPageScrollListener(TabLayout tabLayout) {
			this.mTabLayout = tabLayout;
		}


		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			if(mTabLayout != null) {
				mTabLayout.getTabAt(position).select();
			}

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener{


		@Override
		public void onTabSelected(TabLayout.Tab tab) {
			TextView tv = (TextView) tab.getCustomView();
			if (tv!=null){
				tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
			}
			if (mainvp.getCurrentItem()!=tab.getPosition()){
				mainvp.setCurrentItem(tab.getPosition(), true);
			}

			if (tab.getPosition()==2){
				if (listener!=null){
					listener.fragmentBecameVisible();
				}
			}
			if (tab.getPosition()==1){
				mMenu.getItem(0).setVisible(true);
			}else{
				mMenu.getItem(0).setVisible(false);
			}
		}

		@Override
		public void onTabUnselected(TabLayout.Tab tab) {
			TextView tv = (TextView) tab.getCustomView();
			if (tv!=null){
				tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
			}

		}

		@Override
		public void onTabReselected(TabLayout.Tab tab) {

		}
	}
	private class SectionsPagerAdapter extends FragmentPagerAdapter {
		private String tabtitles[] = {"Popular","Games","Favorites"};
		Context ctx;

		public SectionsPagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			ctx = context;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabtitles[position];
		}

		@Override
		public Fragment getItem(int arg0) {

			switch(arg0){
			case 0:
				return popfrag;

			case 1:
				return gamesfrag;

			case 2:
				return favsfrag;

			}

			return null;
		}

		public View getTabView(int position){
			View v = LayoutInflater.from(ctx).inflate(R.layout.tab_textview, null);
			TextView tabtv = (TextView)v;
			tabtv.setText(tabtitles[position]);
			if (position==0){
				tabtv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
			}else{
				tabtv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
			}

			return v;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabtitles.length;
		}

	}
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

	public void animateRefresh(){

		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
		rotation.setRepeatCount(Animation.INFINITE);
		iv_refresh.startAnimation(rotation);
		(mMenu.getItem(1)).setActionView(iv_refresh);
	}
	public void disableAnimateRefresh(){
		(mMenu.getItem(1)).getActionView().clearAnimation();
		(mMenu.getItem(1)).setActionView(null);
	}
	public AdView getmAdView() {
		return mAdView;
	}

}
