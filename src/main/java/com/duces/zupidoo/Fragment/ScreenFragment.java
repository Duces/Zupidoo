package com.duces.zupidoo.Fragment;

import java.util.Calendar;
import java.util.Locale;

import com.duces.zupidoo.GameRecordUtils;
import com.duces.zupidoo.R;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenFragment extends Fragment{
String key, img, platform, day, month, purchase, daynotation;
ImageView screeniv, faviv, plativ;
TextView datetv;
Button po_but;
boolean hide;
GameRecordUtils utils;
View mShadingView;

	public ScreenFragment(){
	super();
	}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
	
	return inflater.inflate(R.layout.screenshotview, container, false);
}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle bundle = getArguments();
		img = bundle.getString("img");
		day = bundle.getString("day");
		month = bundle.getString("month");
		platform = bundle.getString("platform");
		hide = bundle.getBoolean("hide");
		key = bundle.getString("key");
		purchase = bundle.getString("purchase");
		screeniv = (ImageView) view.findViewById(R.id.ssiv);
		faviv = (ImageView) view.findViewById(R.id.fav_iv);
		plativ = (ImageView) view.findViewById(R.id.platform_iv);
		po_but = (Button) view.findViewById(R.id.preorder_but);
		datetv = (TextView) view.findViewById(R.id.monthday_tv);
		mShadingView = view.findViewById(R.id.shadingview);
		Picasso.with(getActivity()).load(img).placeholder(R.drawable.no_cover).fit().centerCrop().into(screeniv);
		utils = new GameRecordUtils(getActivity());
		plativ.setImageResource(utils.populatePlatformImage(platform));
		po_but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(purchase));
				startActivity(webintent);
			}
		});
		Calendar mCalendar = Calendar.getInstance();
		if (month!=null){
			mCalendar.set(2000, Integer.valueOf(month)-1, 1);
			String monthword = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

			switch(Integer.valueOf(day.substring(day.length()-1))){
				case 1:
					daynotation = "st";
					break;
				case 2:
					daynotation = "nd";
					break;
				case 3:
					daynotation = "rd";
					break;
				default:
					daynotation = "th";
					break;
			}
			String monthday = monthword +" "+Integer.valueOf(day)+daynotation;
			datetv.setText(monthday);
		}



		if (hide){
			hideOverlay();
		}else{
			showOverlay();
		}


	}

	private void hideOverlay(){
	faviv.setVisibility(View.INVISIBLE);
	plativ.setVisibility(View.INVISIBLE);
	po_but.setVisibility(View.INVISIBLE);
	datetv.setVisibility(View.INVISIBLE);
	mShadingView.setVisibility(View.INVISIBLE);
	
}
private void showOverlay(){
	if (!utils.isInFavs(key)){
		faviv.setVisibility(View.INVISIBLE);
	}else{
		faviv.setVisibility(View.VISIBLE);
	}
	plativ.setVisibility(View.VISIBLE);
	po_but.setVisibility(View.VISIBLE);
	datetv.setVisibility(View.VISIBLE);
	mShadingView.setVisibility(View.VISIBLE);
}
}
