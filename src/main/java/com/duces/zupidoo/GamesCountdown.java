package com.duces.zupidoo;

import android.text.format.Time;
import android.util.Log;

public class GamesCountdown {
private long intervalMillis;
	
	public GamesCountdown(int monthday, int month, int year){
		Time releasedate = new Time();
		releasedate.set(monthday, month-1, year);
		releasedate.normalize(true);
		long releaseMillis = releasedate.toMillis(true);
		
		Time now = new Time(Time.getCurrentTimezone());
		
		now.setToNow();
		now.normalize(true);
		long nowMillis = now.toMillis(true);
		
		intervalMillis = releaseMillis - nowMillis;
		
		
	}
	
	public long getIntervalMillis(){
		return intervalMillis;
	}
}
