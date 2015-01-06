package com.duces.zupidoo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GamesGridAdapter extends ArrayAdapter<GamesJSON>{
Context c;
int res;
boolean isfavs;
FavoritesFragment frag;
	public GamesGridAdapter(Context context, int resource,List<GamesJSON> objects, boolean isfavs) {
		super(context, resource, objects);
		c = context;
		res = resource;
		this.isfavs = isfavs;
	}
	public GamesGridAdapter(Context context, int resource,List<GamesJSON> objects, boolean isfavs, FavoritesFragment frag) {
		super(context, resource, objects);
		c = context;
		res = resource;
		this.isfavs = isfavs;
		this.frag = frag;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View item = convertView;
		ItemHolder holder = null;
		GameRecordUtils utils = new GameRecordUtils(c);
		int day = Integer.valueOf(getItem(position).getDay());
		int month = Integer.valueOf(getItem(position).getMonth());
		int year = Integer.valueOf(getItem(position).getYear());
		if(item==null){
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			item = inflater.inflate(res, parent, false);
			holder = new ItemHolder();
			holder.context_hit = item.findViewById(R.id.context_hit);
			holder.gameimg = (ImageView) item.findViewById(R.id.gameimg);
			holder.gamename = (TextView) item.findViewById(R.id.name_tv);
			holder.timertv = (TextView) item.findViewById(R.id.timer_tv);
			holder.platformiv = (ImageView) item.findViewById(R.id.platform_iv);
			holder.overlayview = item.findViewById(R.id.blankview);
			holder.timer = utils.setCountdown(holder.timertv, day, month, year, false);
			item.setTag(holder);
		}else{
			holder = (ItemHolder)item.getTag();
			holder.timer.cancel();
		}
		holder.timer = utils.setCountdown(holder.timertv, day, month, year, false);
		holder.timer.start();
		holder.overlayview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.v("TAG","Game clicked: "+getItem(position).getGamename());
				Intent intent = new Intent(c, GameActivity.class);
				intent.putExtra("key", getItem(position).getAmazonkey());
				((Activity)c).startActivityForResult(intent, 1);
			}
		});
		utils.setPopupMenu(holder.context_hit, getItem(position).getAmazonkey(), getItem(position).getPurchase(), this, isfavs, getItem(position));
		holder.gamename.setText(getItem(position).getGamename());
		if (!(getItem(position).getBanner()=="")){
			
			Picasso.with(c).load(getItem(position).getBanner()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg);
		}else{
			Picasso.with(c).load(getItem(position).getImagelarge()).placeholder(R.drawable.no_cover).fit().centerCrop().into(holder.gameimg);
		}
		holder.platformiv.setImageResource(utils.populatePlatformImage(getItem(position).getPlatform()));
		
		return item;
	}
	private class ItemHolder{
		ImageView gameimg;
		TextView gamename;
		View overlayview;
		TextView timertv;
		ImageView platformiv;
		CountDownTimer timer;
		View context_hit;
	}
	@Override
	public void notifyDataSetChanged() {
		if (isfavs){
			frag.refreshFavs();
		}
		super.notifyDataSetChanged();
	}
	
}
