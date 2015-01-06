package com.duces.zupidoo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class ApiClient {
 private static ZupidooGamesInterface sZupidooService;
 
 public static ZupidooGamesInterface getZupidooService(){
	 if (sZupidooService == null){
		 
		 RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.zupidoo.com/api").build();
//		 restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
		 
		 sZupidooService = restAdapter.create(ZupidooGamesInterface.class);
	 }
	 
	 
	 return sZupidooService;
 }
	
	
	
	
	
	
	public interface ZupidooGamesInterface {
		@GET("/popular.json")
		void getGames(Callback<List<GamesJSON>> callback);
		
		@GET("/game/{id}")
		void getGame(@Path("id") String id, Callback<GamesJSON> game);
	
		@GET("/game/{id}/screens")
		void getScreens(@Path("id") String id, Callback<GameScreens> screens);
		
		@GET("/game/{id}/related")
		void getRelated(@Path("id") String id, Callback<List<GamesJSON>> related);
		
		@GET("/favorites/add/{id}")
		void addFav(@Path("id") String id, Callback<String> st);
		
		@GET("/favorites/remove/{id}")
		void removeFav(@Path("id") String id, Callback<String> st);
	}
		
}
