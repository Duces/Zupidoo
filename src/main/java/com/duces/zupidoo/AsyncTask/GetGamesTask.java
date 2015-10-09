package com.duces.zupidoo.AsyncTask;

import android.os.AsyncTask;

import com.duces.zupidoo.ApiClient;
import com.duces.zupidoo.Model.GamesJSON;

import java.util.List;

/**
 * Created by David Doose on 9/16/2015.
 */
public class GetGamesTask extends AsyncTask<Void, Void, List<GamesJSON>> {

    @Override
    protected List<GamesJSON> doInBackground(Void... params) {
        return ApiClient.getZupidooService().getGames();
    }
}
