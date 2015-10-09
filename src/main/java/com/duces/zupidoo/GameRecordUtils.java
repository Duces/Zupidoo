package com.duces.zupidoo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.duces.zupidoo.Adapter.GamesGridAdapter;
import com.duces.zupidoo.Model.GamesCountdown;
import com.duces.zupidoo.Model.GamesJSON;

public class GameRecordUtils {
	SharedPreferences prefs;
	OnSharedPreferenceChangeListener changedlistener;
	Context c;
	
	public GameRecordUtils(Context c){
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		this.c = c;
	}
	public boolean isInFavs(String key){
		boolean fav = false;
		Set<String> tempfavs = prefs.getStringSet("favs", null);
		if (!(tempfavs==null)){
			ArrayList<String> tempfavsarray = new ArrayList<String>(tempfavs);
			for (int z = 0;z<tempfavsarray.size();z++){
				if (key.equals(tempfavsarray.get(z))){
					fav = true;
				}
			}
		}
		return fav;
	}
	public void addToFavs(String key){
		Set<String> tempfavs = prefs.getStringSet("favs", new HashSet<String>());
			tempfavs.add(key);
			prefs.edit().remove("favs").commit();
			prefs.edit().putStringSet("favs", tempfavs).commit();
			ApiClient.getZupidooService().addFav(key, new Callback<String>() {
				
				@Override
				public void success(String arg0, Response arg1) {
					
					
				}
				
				@Override
				public void failure(RetrofitError arg0) {
					
					
				}
			});
	}
	
	public void removeFromFavs(String key){
		Set<String> tempfavs = prefs.getStringSet("favs", null);
		if (!(tempfavs==null)){
			tempfavs.remove(key);
			prefs.edit().remove("favs").commit();
			prefs.edit().putStringSet("favs", tempfavs).commit();
			ApiClient.getZupidooService().removeFav(key, new Callback<String>() {

				@Override
				public void failure(RetrofitError arg0) {
					
					
				}

				@Override
				public void success(String arg0, Response arg1) {
					
					
				}
				
			});
		}
	}
	public Integer populatePlatformImage(String platform){
		int returnres;
		
		if (platform.contains("Vita")){
			returnres = R.drawable.ic_platform_vita;
		}else if (platform.toLowerCase().contains("playstation")){
			returnres = R.drawable.ic_platform_psx;
		}else if (platform.contains("Xbox")){
			returnres = R.drawable.ic_platform_xbox;
		}else if (platform.contains("Wii")){
			returnres = R.drawable.ic_platform_wii_wiiu;
		}else if (platform.equals("PC Games")){
			returnres = R.drawable.ic_platform_win;
		}else{
			returnres = R.drawable.ic_platform_3ds_temp;
		}
		return returnres;
		
	}
	public void setPopupMenu(View ib, final String key, final String purchase){
		ib.setOnClickListener(new OnClickListener() {
		boolean isfav = false;	
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(c, v);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.context, popup.getMenu());
				if (isInFavs(key)){
					popup.getMenu().getItem(1).setTitle(R.string.remove_favorites);
					isfav = true;
				}else{
					popup.getMenu().getItem(1).setTitle(R.string.add_favorites);
					isfav = false;
				}
				popup.show();
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()){
						case R.id.pre_order:
							//pre order game
							Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(purchase));
							c.startActivity(webintent);
							return true;
						case R.id.favorite:
							//favorite game
							if (isfav){
								removeFromFavs(key);
								return true;
							}else{
								addToFavs(key);
								return true;	
							}
						}
						return false;
					}
				});
			}
		});
	}
	public void setPopupMenu(View ib, final String key, final String purchase, final GamesGridAdapter adapter, final boolean isfavsfragment, final GamesJSON obj){
		ib.setOnClickListener(new OnClickListener() {
		boolean isfav = false;	
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(c, v);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.context, popup.getMenu());
				if (isInFavs(key)){
					popup.getMenu().getItem(1).setTitle(R.string.remove_favorites);
					isfav = true;
				}else{
					popup.getMenu().getItem(1).setTitle(R.string.add_favorites);
					isfav = false;
				}
				popup.show();
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()){
						case R.id.pre_order:
							//pre order game
							Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(purchase));
							c.startActivity(webintent);
							return true;
						case R.id.favorite:
							//favorite game
							if (isfav){
								removeFromFavs(key);
								if (isfavsfragment){
									adapter.remove(obj);
									adapter.notifyDataSetChanged();
								}
								return true;
							}else{
								addToFavs(key);
								return true;	
							}
						}
						return false;
					}
				});
			}
		});
	}
	public CountDownTimer setCountdown(final TextView tv, int day, int month, int year, final boolean sec){
		GamesCountdown timer = new GamesCountdown(day, month, year);
		CountDownTimer cdt = new CountDownTimer(timer.getIntervalMillis(), 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				int daysint = (int) (millisUntilFinished/(1000*60*60*24));
		        int hoursint = (int) (((millisUntilFinished / 1000)
		                - (daysint * 86400)) / 3600);
		        int minutesint = (int) (((millisUntilFinished / 1000)
		                - (daysint * 86400) - (hoursint * 3600)) / 60);
		        int secondsint = (int) ((millisUntilFinished / 1000) % 60);
		        String countdown;
		        if (sec){
		        	countdown = daysint+"d "+hoursint+"h "+minutesint+"m "+secondsint+"s";
		        }else{
		        	countdown = daysint+"d "+hoursint+"h "+minutesint+"m" ;
		        }
		        
				
		        tv.setText(countdown);
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
			}
		};
	return cdt;
	}
}
