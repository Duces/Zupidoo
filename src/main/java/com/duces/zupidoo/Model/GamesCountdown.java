package com.duces.zupidoo.Model;

import android.text.format.Time;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class GamesCountdown {
private long intervalMillis;
	
	public GamesCountdown(int monthday, int month, int year){
		Calendar releasedate = Calendar.getInstance();
		releasedate.set(Calendar.MONTH, month-1);
		releasedate.set(Calendar.YEAR, year);
		releasedate.set(Calendar.DAY_OF_MONTH, monthday);
		releasedate.set(Calendar.HOUR_OF_DAY, 0);
		releasedate.set(Calendar.MINUTE,0);

		long releaseMillis = releasedate.getTimeInMillis();
		
		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getDefault());

		long nowMillis = now.getTimeInMillis();
		
		intervalMillis = releaseMillis - nowMillis;
		
	}
	
	public long getIntervalMillis(){
		return intervalMillis;
	}
}
