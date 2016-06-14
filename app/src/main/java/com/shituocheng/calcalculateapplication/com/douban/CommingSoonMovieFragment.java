package com.shituocheng.calcalculateapplication.com.douban;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import customRecyclerViewAdapter.customRecyclerViewAdapter;
import customRecyclerViewAdapter.customcommingsoonViewPager;
import model.MovieModel;
import utilities.API;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommingSoonMovieFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<MovieModel> mMovieModels = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comming_soon_movie, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        return view;
    }

    public void fetchData(){
        new Thread(new Runnable() {

            HttpURLConnection mConnection;
            InputStream mInputStream;

            @Override
            public void run() {

                try {
                    mConnection = (HttpURLConnection)new URL(API.COMMING_SOON_MOVIE_API).openConnection();
                    mConnection.setRequestMethod("GET");
                    mConnection.connect();

                    mInputStream = mConnection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream));

                    String line;

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null){
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
                            movieModel.setMovie_year(eachJson.getInt("year"));

                            mMovieModels.add(movieModel);
                        }

                    }

                    Log.d("result",jsonObject.toString());

                    Log.d("size",String.valueOf( mMovieModels.size()));

                    Log.d("test",mMovieModels.get(1).getMovie_Image());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.comming_soon_movie_fragment);

                            customcommingsoonViewPager commingSoonViewPager = new customcommingsoonViewPager(mMovieModels);

                            mLinearLayoutManager = new LinearLayoutManager(getActivity());

                            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                            mRecyclerView.setLayoutManager(mLinearLayoutManager);

                            mRecyclerView.setAdapter(commingSoonViewPager);

                            commingSoonViewPager.notifyDataSetChanged();

                            mSwipeRefreshLayout.setRefreshing(false);

                            commingSoonViewPager.setOnItemClickListener(new customcommingsoonViewPager.ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    MovieModel movieModel = mMovieModels.get(position);
                                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
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

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
