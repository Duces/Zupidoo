package com.duces.zupidoo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopularFragment extends Fragment{
ArrayList<GamesJSON> gamesdata;
ArrayList<GamesJSON> bottomdata;
ArrayList<GamesJSON> topdata;
ArrayList<Integer> parallelarray;
ArrayList<Integer> positionarray;
ListParcelable listparc;
TopGamesFragmentAdapter adapter;
ListView lv;
ViewPager vp;
CirclePageIndicator cpi;
PopularAdapter bottomadapter;

	public PopularFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		gamesdata = new ArrayList<GamesJSON>();
		parallelarray = new ArrayList<Integer>();
		positionarray = new ArrayList<Integer>();
//		Bundle bundle = getArguments();
//		listparc = bundle.getParcelable("GamesData");
//		gamesdata = listparc.getList();
		gamesdata = MainActivity.games_pojo;
		lv = (ListView) rootView.findViewById(R.id.popular_lv);
		bottomdata = filterBottomGames(gamesdata);
		topdata = filterTopGames(gamesdata);
		populateParallelArray();
		bottomadapter = new PopularAdapter(getActivity(), R.layout.gamesrecord_popular, positionarray, bottomdata);
		adapter = new TopGamesFragmentAdapter(getActivity().getSupportFragmentManager(), topdata);
		View pagerview = View.inflate(getActivity(),R.layout.topgames_pager, null);
		vp = (ViewPager) pagerview.findViewById(R.id.pager);
		vp.setPageTransformer(true, new DepthPageTransformer());
		cpi = (CirclePageIndicator) pagerview.findViewById(R.id.circles);
        vp.setAdapter(adapter);
        cpi.setViewPager(vp);
        lv.addHeaderView(pagerview);
        lv.setAdapter(bottomadapter);
        final float density = getResources().getDisplayMetrics().density;
//        cpi.setBackgroundColor(0x80B8B8B8);
        cpi.setRadius(5 * density);
        cpi.setPageColor(0x88E6E6E6);
        cpi.setFillColor(0xFFFFFFFF);
      
        
        
		return rootView;
	}
//	public void refresh(){
//		gamesdata.clear();
//		gamesdata = MainActivity.games_pojo;
//		bottomadapter = new PopularAdapter(getActivity(), R.layout.gamesrecord_popular, positionarray, filterBottomGames());
//		adapter = new TopGamesFragmentAdapter(getActivity().getSupportFragmentManager(), filterTopGames());
//		vp.setAdapter(adapter);
//	}
	private class PopularAdapter extends ArrayAdapter<Integer>{
		Context c;
		ArrayList<GamesJSON> gamesdata;
		ArrayList<Integer> para;
		int res;
		Intent intent;
		public PopularAdapter(Context context, int resource,ArrayList<Integer> objects, ArrayList<GamesJSON> gamesdata) {
			super(context, resource, objects);
			c = context;
			para = objects;
			this.gamesdata = gamesdata; 
			res = resource;
			intent = new Intent(c, GameActivity.class);
		}

				
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
				View row = convertView;
				ItemHolder holder = null;
				GameRecordUtils utils = new GameRecordUtils(c);
				int day = Integer.valueOf(gamesdata.get(para.get(position)).getDay());
				int month = Integer.valueOf(gamesdata.get(para.get(position)).getMonth());
				int year = Integer.valueOf(gamesdata.get(para.get(position)).getYear());
				if (row==null){
					LayoutInflater inflater = ((Activity)c).getLayoutInflater();
					row = inflater.inflate(res, parent, false);
					holder = new ItemHolder();
					holder.record_one = (RelativeLayout) row.findViewById(R.id.record_one);
					holder.record_two = (LinearLayout) row.findViewById(R.id.record_two);
					holder.context_hit = row.findViewById(R.id.context_hit);
					holder.context_hit1 = row.findViewById(R.id.context_hit1);
					holder.context_hit2 = row.findViewById(R.id.context_but2);
					holder.viewoverlay1 = row.findViewById(R.id.blankview);
					holder.viewoverlay2 = row.findViewById(R.id.view2);
					holder.viewoverlay3 = row.findViewById(R.id.view3);
					holder.gameimg = (ImageView) row.findViewById(R.id.gameimg);
					holder.gameimg1 = (ImageView) row.findViewById(R.id.gameimg1);
					holder.gameimg2 = (ImageView) row.findViewById(R.id.gameimg2);
					holder.gamename = (TextView) row.findViewById(R.id.name_tv);
					holder.gamename1 = (TextView) row.findViewById(R.id.name_tv1);
					holder.gamename2 = (TextView) row.findViewById(R.id.name_tv2);
					holder.timertv = (TextView) row.findViewById(R.id.timer_tv);
					holder.timertv1 = (TextView) row.findViewById(R.id.timer_tv1);
					holder.timertv2 = (TextView) row.findViewById(R.id.timer_tv2);
					holder.platformiv = (ImageView) row.findViewById(R.id.platform_iv);
					holder.platformiv1 = (ImageView) row.findViewById(R.id.platform_iv1);
					holder.platformiv2 = (ImageView) row.findViewById(R.id.platform_iv2);
					row.setTag(holder);
				}else{
					holder = (ItemHolder) row.getTag();
				}
				
				if (para.get(position)%5==0 && para.get(position)!=0){
						if (holder.timer!=null){
							holder.timer.cancel();
						}
					holder.record_one.setVisibility(View.VISIBLE);
					holder.record_two.setVisibility(View.GONE);
					holder.timer = utils.setCountdown(holder.timertv, day, month, year, false);
					holder.timer.start();
					utils.setPopupMenu(holder.context_hit, gamesdata.get(para.get(position)).getAmazonkey(), gamesdata.get(para.get(position)).getPurchase());
					holder.gamename.setText(gamesdata.get(para.get(position)).getGamename());
					if (!(gamesdata.get(para.get(position)).getBanner()=="")){
						Picasso.with(c).load(gamesdata.get(para.get(position)).getBanner()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg);
					}else{
						Picasso.with(c).load(gamesdata.get(para.get(position)).getImagelarge()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg);
					}
					holder.viewoverlay3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							Log.v("TAG", "game clicked: "+gamesdata.get(para.get(position)).getGamename());
							intent.putExtra("key", gamesdata.get(para.get(position)).getAmazonkey());
							c.startActivity(intent);
						}
					});
					
					holder.platformiv.setImageResource(utils.populatePlatformImage(gamesdata.get(para.get(position)).getPlatform()));
				}else{
					if (holder.timer1!=null && holder.timer2!=null){
						holder.timer1.cancel();
						holder.timer2.cancel();
					}
					int day2 = Integer.valueOf(gamesdata.get(para.get(position)+1).getDay());
					int month2 = Integer.valueOf(gamesdata.get(para.get(position)+1).getMonth());
					int year2 = Integer.valueOf(gamesdata.get(para.get(position)+1).getYear());
					holder.record_one.setVisibility(View.GONE);
					holder.record_two.setVisibility(View.VISIBLE);
					holder.timer1 = utils.setCountdown(holder.timertv1, day, month, year, false);
					holder.timer2 = utils.setCountdown(holder.timertv2, day2, month2, year2, false);
					holder.timer1.start();
					holder.timer2.start();
					utils.setPopupMenu(holder.context_hit1, gamesdata.get(para.get(position)).getAmazonkey(), gamesdata.get(para.get(position)).getPurchase());
					utils.setPopupMenu(holder.context_hit2, gamesdata.get(para.get(position)+1).getAmazonkey(), gamesdata.get(para.get(position)+1).getPurchase());
					holder.gamename1.setText(gamesdata.get(para.get(position)).getGamename());
					holder.gamename2.setText(gamesdata.get(para.get(position)+1).getGamename());
					if (!(gamesdata.get(para.get(position)).getBanner()=="")){
						Picasso.with(c).load(gamesdata.get(para.get(position)).getBanner()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg1);
					}else{
						Picasso.with(c).load(gamesdata.get(para.get(position)).getImagelarge()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg1);
					}
					holder.viewoverlay1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							Log.v("TAG", "game clicked: "+gamesdata.get(para.get(position)).getGamename());
							intent.putExtra("key", gamesdata.get(para.get(position)).getAmazonkey());
							c.startActivity(intent);
						}
					});
					if (!(gamesdata.get(para.get(position)+1).getBanner()=="")){
						Picasso.with(c).load(gamesdata.get(para.get(position)+1).getBanner()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg2);
					}else{
						Picasso.with(c).load(gamesdata.get(para.get(position)+1).getImagelarge()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg2);
					}
					holder.viewoverlay2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							Log.v("TAG", "game clicked: "+gamesdata.get(para.get(position)+1).getGamename());
							intent.putExtra("key", gamesdata.get(para.get(position)+1).getAmazonkey());
							c.startActivity(intent);
							
						}
					});
					
					holder.platformiv1.setImageResource(utils.populatePlatformImage(gamesdata.get(para.get(position)).getPlatform()));
					holder.platformiv2.setImageResource(utils.populatePlatformImage(gamesdata.get(para.get(position)+1).getPlatform()));
				}
				
			
			return row;
		}
		
	public class ItemHolder{
		LinearLayout record_two;
		RelativeLayout record_one;
		View viewoverlay1;
		View viewoverlay2;
		View viewoverlay3;
		ImageView gameimg;
		TextView gamename;
		TextView timertv;
		ImageView platformiv;
		CountDownTimer timer;
		View context_hit;
		ImageView gameimg1;
		TextView gamename1;
		TextView timertv1;
		ImageView platformiv1;
		CountDownTimer timer1;
		View context_hit1;
		ImageView gameimg2;
		TextView gamename2;
		TextView timertv2;
		ImageView platformiv2;
		CountDownTimer timer2;
		View context_hit2;
	}

		
	}
	
	private ArrayList<GamesJSON> filterTopGames(ArrayList<GamesJSON> data){
		ArrayList<GamesJSON> sortedgames = new ArrayList<GamesJSON>();
		sortedgames.clear();
		for (int z = 0;z<5;z++){
				sortedgames.add(data.get(z));
		}
		
		return sortedgames;
		
	}
	private ArrayList<GamesJSON> filterBottomGames(ArrayList<GamesJSON> data){
		ArrayList<GamesJSON> sortedgames = new ArrayList<GamesJSON>();
		sortedgames.clear();
		for (int x = 0;x<21;x++){
			if (x>4){
				sortedgames.add(data.get(x));
			}
			
		}
		return sortedgames;
	}
	private void populateParallelArray(){
		
		for (int z = 0;z<bottomdata.size();z++){
			parallelarray.add(z);
		}
		for (int y = 0;y<parallelarray.size();y++){
			if (parallelarray.get(y)%5==0){
				positionarray.add(parallelarray.get(y));
			}else{
				positionarray.add(parallelarray.get(y));
				parallelarray.remove(y+1);
			}
		}
		positionarray.remove(0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	public class DepthPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.75f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) { // (0,1]
	            // Fade the page out.
	            view.setAlpha(1 - position);

	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
}
