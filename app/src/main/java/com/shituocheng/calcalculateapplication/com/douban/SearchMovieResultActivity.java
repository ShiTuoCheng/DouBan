package com.shituocheng.calcalculateapplication.com.douban;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import customRecyclerViewAdapter.SearchResultRecyclerViewAdapter;
import model.MovieModel;
import utilities.API;

public class SearchMovieResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MovieModel> mMovieModels = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("搜索"+getIntent().getStringExtra("movie_search")+"的结果");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        fetchData();
    }

    private void fetchData(){
        new Thread(new Runnable() {

            HttpURLConnection connection = null;
            InputStream inputStream = null;

            @Override
            public void run() {


                try {
                    String searchString = getIntent().getStringExtra("movie_search");
                    //String data = URLEncoder.encode(searchString,"utf-8");
                    String api = API.MOVIE_SEARCH_API+searchString;
                    Log.d("api",api);
                    URL url = new URL(api);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.connect();

                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine())!=null){
                        stringBuilder.append(line);
                    }

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("subjects");

                    if (mMovieModels.size() == 0){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject eachJson = jsonArray.getJSONObject(i);
                            MovieModel movieModel = new MovieModel();

                            JSONObject ratingJson = eachJson.getJSONObject("rating");
                            movieModel.setMax_Rating(ratingJson.getInt("max"));
                            movieModel.setAvg_Rating(ratingJson.getInt("average"));
                            movieModel.setTitle(eachJson.getString("title"));
                            movieModel.setMovie_id(eachJson.getInt("id"));
                            movieModel.setOriginal_title(eachJson.getString("original_title"));
                            JSONArray genresJsonArray = eachJson.getJSONArray("genres");
                            StringBuilder genresStringBuilder = new StringBuilder();
                            for (int g =0; g<genresJsonArray.length();g++){
                                String genresPiece = genresJsonArray.getString(g);
                                genresStringBuilder.append(genresPiece+" ");
                            }
                            movieModel.setGeneres(genresStringBuilder.toString());
                            JSONObject imageJsonObj = eachJson.getJSONObject("images");
                            movieModel.setMovie_Image(imageJsonObj.getString("large"));

                            mMovieModels.add(movieModel);
                        }

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            recyclerView = (RecyclerView)findViewById(R.id.search_result_recyclerView);
                            mLinearLayoutManager = new LinearLayoutManager(SearchMovieResultActivity.this);
                            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            SearchResultRecyclerViewAdapter searchResultRecyclerViewAdapter = new SearchResultRecyclerViewAdapter(mMovieModels);
                            recyclerView.setAdapter(searchResultRecyclerViewAdapter);
                            searchResultRecyclerViewAdapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(mLinearLayoutManager);
                            searchResultRecyclerViewAdapter.setOnItemClickListener(new SearchResultRecyclerViewAdapter.ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {

                                    MovieModel movieModel = mMovieModels.get(position);
                                    Intent intent = new Intent(SearchMovieResultActivity.this, MovieDetailActivity.class);
                                    intent.putExtra("movie_name",movieModel.getTitle());
                                    intent.putExtra("movie_id",movieModel.getMovie_id());
                                    intent.putExtra("movie_title_image",movieModel.getMovie_Image());
                                    intent.putExtra("movie_share_url",movieModel.getMovie_url());
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongItemClick(int position, View v) {

                                }
                            });
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private void initView(){

        recyclerView = (RecyclerView)findViewById(R.id.search_result_recyclerView);
    }

}
