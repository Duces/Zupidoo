package com.duces.zupidoo;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.SearchManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
ListView gamelist;
ProgressBar pb, pb_refresh;
SpinnerAdapter filteradapter;
String[] filter_items;
ActionBar.OnNavigationListener navlistener;
ActionBar actionbar;
SharedPreferences prefs;
Tab gamestab, populartab, favstab;
public static ArrayList<GamesJSON> games_pojo;
boolean filtered;
ViewPager mainvp;
GamesFragment gamesfrag;
PopularFragment popfrag;
FavoritesFragment favsfrag;
ImageView iv_refresh;
Menu mMenu;
SectionsPagerAdapter adapter;
private AdView mAdView;
boolean refresh;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		actionbar = getActionBar();
		actionbar.setLogo(R.drawable.ic_ab_logo);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		games_pojo = new ArrayList<GamesJSON>();
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099cc")));
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		gamesfrag = new GamesFragment();
		popfrag = new PopularFragment();
		favsfrag = new FavoritesFragment();
		LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView tab_tv_games = (TextView) inflator.inflate(R.layout.tab_textview, null);
		TextView tab_tv_pop = (TextView) inflator.inflate(R.layout.tab_textview, null);
		TextView tab_tv_favs = (TextView) inflator.inflate(R.layout.tab_textview, null);
		refresh = false;
		tab_tv_games.setText("Games");
		tab_tv_games.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
		tab_tv_pop.setText("Popular");
		tab_tv_pop.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
		tab_tv_favs.setText("Favorites");
		tab_tv_favs.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		iv_refresh = (ImageView)inflater.inflate(R.layout.iv_refresh, null);
//		pb_refresh = (ProgressBar)inflater.inflate(R.layout.pb_refresh, null);
		gamestab = actionbar.newTab().setCustomView(tab_tv_games);
		populartab = actionbar.newTab().setCustomView(tab_tv_pop);
		favstab = actionbar.newTab().setCustomView(tab_tv_favs);
		mAdView = (AdView) findViewById(R.id.adView);
//        mAdView.setAdListener(new ToastAdListener(this));
        mAdView.loadAd(new AdRequest.Builder().build());
        
		adapter = new SectionsPagerAdapter(getSupportFragmentManager(), games_pojo);
		mainvp = (ViewPager) findViewById(R.id.mainpager);
		mainvp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				actionbar.setSelectedNavigationItem(arg0);
				if (arg0==2){
					FragmentInterface fragment = (FragmentInterface) adapter.instantiateItem(mainvp, arg0);
					if (fragment!=null && !refresh){
						fragment.fragmentBecameVisible();
					}
					refresh = false;
				}
				if (arg0==1){
					mMenu.getItem(0).setVisible(true);
				}else{
					mMenu.getItem(0).setVisible(false);
				}
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				TextView tv = (TextView) tab.getCustomView();
				tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				TextView tv = (TextView) tab.getCustomView();
				tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"));
				int selectedVal = 0;
				
				if (tv.getText().toString().equals("Popular")){
					selectedVal = 0;
				}else if (tv.getText().toString().equals("Games")){
					selectedVal = 1;
				}else if (tv.getText().toString().equals("Favorites")){
					selectedVal = 2;
				}
				
				mainvp.setCurrentItem(selectedVal);
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
		gamestab.setTabListener(tabListener);
		populartab.setTabListener(tabListener);
		favstab.setTabListener(tabListener);
		getData();
		
			
	}
	private void getData(){
		ApiClient.getZupidooService().getGames(new Callback<List<GamesJSON>>() {
			
			@Override
			public void success(List<GamesJSON> arg0, Response arg1) {
				games_pojo.addAll(arg0);
				actionbar.addTab(populartab);
				actionbar.addTab(gamestab);
				actionbar.addTab(favstab);
				mainvp.setAdapter(adapter);
				pb.setVisibility(View.INVISIBLE);
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
				
				int selectedtab = actionbar.getSelectedNavigationIndex();
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
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchmanager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				gamesfrag.performSearch(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.length()==0){
					gamesfrag.closeSearch();
				}
				return false;
			}
		});
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
	private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
//		ArrayList<GamesJSON> data;
		public SectionsPagerAdapter(FragmentManager fm, ArrayList<GamesJSON> data) {
			super(fm);
//			this.data = data;
		}


		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = new Fragment();
			
			
			
			switch(arg0){
			case 0:
				fragment = popfrag;
				break;
			case 1:
				fragment = gamesfrag;
				break;
			case 2:
				fragment = favsfrag;
				break;
			
			}
//			Bundle args = new Bundle();
//			ListParcelable listparc = new ListParcelable();
////			Log.v("TAG", "first game pageradapter: "+data.get(0).getGamename());
//			listparc.setList(data);
//			args.putParcelable("GamesData", listparc);
//			fragment.setArguments(args);
			
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
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
