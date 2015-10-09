package com.duces.zupidoo.Fragment;

import com.duces.zupidoo.Activity.GameActivity;
import com.duces.zupidoo.GameRecordUtils;
import com.duces.zupidoo.R;
import com.squareup.picasso.Picasso;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TopGamesFragment extends Fragment{
String img, name, day, month, year, key, platform, banner;

	public TopGamesFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {

		return inflater.inflate(R.layout.topgamesview, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		GameRecordUtils utils = new GameRecordUtils(getActivity());
		Bundle bundle = getArguments();
		img = bundle.getString("imgurl");
		name = bundle.getString("name");
		day = bundle.getString("day");
		month = bundle.getString("month");
		year = bundle.getString("year");
		key = bundle.getString("key");
		platform = bundle.getString("platform");
		banner = bundle.getString("banner");

		TextView mGamenameTV = (TextView) view.findViewById(R.id.game_name_textview);
		final TextView mCountdownTV = (TextView) view.findViewById(R.id.countdown_textview);
		ImageView mGameimage = (ImageView) view.findViewById(R.id.game_imageview);
		ImageView mFavsimage = (ImageView) view.findViewById(R.id.fav_iv);
		ImageView mPlatformimage = (ImageView) view.findViewById(R.id.platform_iv);
		View shading = view.findViewById(R.id.blankview);
		shading.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), GameActivity.class);
				intent.putExtra("key", key);
				getActivity().startActivityForResult(intent, 1);

			}
		});
		if (!utils.isInFavs(key)){
			mFavsimage.setVisibility(View.INVISIBLE);
		}else{
			mFavsimage.setVisibility(View.VISIBLE);
		}
		mGamenameTV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf"));
		mCountdownTV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
		mGamenameTV.setText(name);
		mPlatformimage.setImageResource(utils.populatePlatformImage(platform));
		if (banner!=""){
			Picasso.with(getActivity()).load(banner).placeholder(R.drawable.no_cover).fit().centerCrop().into(mGameimage);
		}else{
			Picasso.with(getActivity()).load(img).placeholder(R.drawable.no_cover).fit().centerCrop().into(mGameimage);
		}

		utils.setCountdown(mCountdownTV, Integer.valueOf(day), Integer.valueOf(month), Integer.valueOf(year), false).start();

	}
}
