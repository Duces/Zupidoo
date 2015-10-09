package com.duces.zupidoo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duces.zupidoo.ApiClient;
import com.duces.zupidoo.Fragment.ScreenFragment;
import com.duces.zupidoo.GameRecordUtils;
import com.duces.zupidoo.Model.GameScreens;
import com.duces.zupidoo.Model.GamesJSON;
import com.duces.zupidoo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends AppCompatActivity{
ViewPager screensvp;
TextView timertv, norelatedtv;
String amazonkey;
GamesJSON game;
ScreensFragmentAdapter adapter;
FrameLayout fl;
CountDownTimer timer;
CirclePageIndicator cpi;
GameRecordUtils utils;
ListView related_lv;
private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(getDrawable(R.drawable.ic_ab_logo));
		if (getSupportActionBar()!=null){
			getSupportActionBar().setTitle("");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		screensvp = (ViewPager) findViewById(R.id.pager);
		screensvp.setPageTransformer(true, new ZoomOutPageTransformer());
		timertv = (TextView) findViewById(R.id.countdown);
		norelatedtv = (TextView) findViewById(R.id.norelated_tv);
		cpi = (CirclePageIndicator) findViewById(R.id.circles);
		fl = (FrameLayout) findViewById(R.id.fl);
		related_lv = (ListView) findViewById(R.id.feed_lv);
		mAdView = (AdView) findViewById(R.id.adView2);
		utils = new GameRecordUtils(this);
//        mAdView.setAdListener(new ToastAdListener(this));
		mAdView.loadAd(new AdRequest.Builder().build());
        final float density = getResources().getDisplayMetrics().density;
	      cpi.setRadius(5 * density);
		cpi.setPageColor(0x88E6E6E6);
	      cpi.setFillColor(0xFFFFFFFF);
	      amazonkey = getIntent().getStringExtra("key");
		getGameData(amazonkey);



	}
	
	@Override
		protected void onStop() {
            if (timer!=null){
                timer.cancel();
            }

			super.onStop();
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
	
public void getGameData(final String key){
	ApiClient.getZupidooService().getGame(key, new Callback<GamesJSON>() {
		@Override
		public void success(GamesJSON arg0, Response arg1) {
			game = arg0;
			getSupportActionBar().setTitle(game.getGamename().replace("\\", ""));
			GameRecordUtils utils = new GameRecordUtils(GameActivity.this);
            if (game.getDay()!=null){
                timer = utils.setCountdown(timertv, Integer.valueOf(game.getDay()), Integer.valueOf(game.getMonth()), Integer.valueOf(game.getYear()), true);
                timer.start();
            }

			ApiClient.getZupidooService().getScreens(key, new Callback<GameScreens>() {

				@Override
				public void failure(RetrofitError arg0) {
					Log.v("TAG", "Failed getting screenshots");
					Log.v("TAG", "Amazonkey: "+game.getAmazonkey());
					screensvp.setVisibility(View.GONE);
					cpi.setVisibility(View.GONE);
					fl.setVisibility(View.VISIBLE);
					android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
					Bundle bundle = new Bundle();
					ScreenFragment fragment = new ScreenFragment();
					if (game.getBanner().equals("")){
						bundle.putString("img", game.getImagelarge());
					}else{
						bundle.putString("img", game.getBanner());
					}
					bundle.putString("day", game.getDay());
					bundle.putString("month", game.getMonth());
					bundle.putString("platform", game.getPlatform());
					bundle.putString("purchase", game.getPurchase());
					bundle.putString("key", game.getAmazonkey());
					bundle.putBoolean("hide", false);
					fragment.setArguments(bundle);
					trans.add(R.id.fl, fragment).commit();
				}

				@Override
				public void success(GameScreens arg0, Response arg1) {
					screensvp.setVisibility(View.VISIBLE);
					cpi.setVisibility(View.VISIBLE);
					fl.setVisibility(View.INVISIBLE);
					Log.v("TAG","number of screens: "+arg0.getScreen().size());
					adapter = new ScreensFragmentAdapter(getSupportFragmentManager(), game.getDay(), game.getMonth(), game.getAmazonkey(), game.getPlatform(), game.getPurchase(), arg0.getScreen());
					screensvp.setAdapter(adapter);
					cpi.setViewPager(screensvp);
				}
			});
		}
		
		@Override
		public void failure(RetrofitError arg0) {
			Log.v("TAG", "Failed getting game data");
			
		}
	});
	ApiClient.getZupidooService().getRelated(key, new Callback<List<GamesJSON>>() {

		@Override
		public void failure(RetrofitError arg0) {
			Log.v("TAG", "Failed getting related game data");
			related_lv.setVisibility(View.INVISIBLE);
			norelatedtv.setVisibility(View.VISIBLE);
			
		}

		@Override
		public void success(final List<GamesJSON> arg0, Response arg1) {
			related_lv.setVisibility(View.VISIBLE);
			norelatedtv.setVisibility(View.INVISIBLE);
					
			RelatedGamesAdapter adapter = new RelatedGamesAdapter(GameActivity.this, R.layout.related_item, arg0);
			related_lv.setAdapter(adapter);
			related_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					timer.cancel();
					amazonkey = arg0.get(position).getAmazonkey();
					getGameData(amazonkey);
					GameActivity.this.invalidateOptionsMenu();
					
				}
			});
	
		}
	
	});
}
@Override
public void onBackPressed() {
	Intent returnIntent = new Intent();
	setResult(RESULT_OK, returnIntent);
	super.onBackPressed();
}

private class RelatedGamesAdapter extends ArrayAdapter<GamesJSON>{
Context c;
int res;
	public RelatedGamesAdapter(Context context, int resource, List<GamesJSON> objects) {
		super(context, resource, objects);
		c = context;
		res = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ItemHolder holder = null;
		if (row==null){
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			row = inflater.inflate(res, parent, false);
			holder = new ItemHolder();
			holder.gamedateTV = (TextView) row.findViewById(R.id.related_date);
			holder.gamesmallimg = (ImageView) row.findViewById(R.id.gameimage);
			holder.gametitleTV = (TextView) row.findViewById(R.id.related_title);
			row.setTag(holder);
		}else{
			holder = (ItemHolder) row.getTag();
		}
		
		holder.gametitleTV.setText(getItem(position).getGamename().replace("\\", ""));
		holder.gamedateTV.setText(getItem(position).getRelease());
        if (!getItem(position).getImagesmall().equals("")){
            Picasso.with(c).load(getItem(position).getImagesmall()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gamesmallimg);
        }

		return row;
	}
private class ItemHolder{
	ImageView gamesmallimg;
	TextView gametitleTV;
	TextView gamedateTV;
}
	
}
private class ScreensFragmentAdapter extends FragmentStatePagerAdapter{
List<String> screens;
String day, month, key, platform, purchase;
	public ScreensFragmentAdapter(FragmentManager fm, String day, String month, String key, String platform, String purchase, List<String> screens) {
		super(fm);
		this.screens = screens;
		this.day = day;
		this.month = month;
		this.platform = platform;
		this.purchase = purchase;
		this.key = key;
		Log.v("TAG","amazonkey: "+key);
		Log.v("TAG","number of screens inside adapter: "+screens.size());
		
	}

	@Override
	public Fragment getItem(int arg0) {
		ScreenFragment fragment = new ScreenFragment();
		Bundle bundle = new Bundle();
		if (arg0==0){
			bundle.putBoolean("hide", false);
		}else{
			bundle.putBoolean("hide", true);
		}
		bundle.putString("img", screens.get(arg0));
		bundle.putString("day", day);
		bundle.putString("month", month);
		bundle.putString("platform", platform);
		bundle.putString("purchase", purchase);
		bundle.putString("key", key);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return screens.size()<6 ? screens.size() : 5;
	}
	
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()){
	case android.R.id.home:
		onBackPressed();
		break;
	case R.id.action_favorite:
		utils.addToFavs(amazonkey);
		Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
		item.setVisible(false);
		break;
	}
	return true;
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.game, menu);

	return true;
}

@Override
public boolean onPrepareOptionsMenu(Menu menu) {
	if (utils.isInFavs(amazonkey)){
		menu.getItem(0).setVisible(false);
	}else{
		menu.getItem(0).setVisible(true);
	}
	return true;
}

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
}
