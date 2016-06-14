package com.shituocheng.calcalculateapplication.com.douban;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

import model.MovieModel;
import utilities.API;
import customRecyclerViewAdapter.customRecyclerViewAdapter;
import utilities.NetworkState;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowMovieFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog mProgressDialog;
    private List<MovieModel> mMovieModels = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    public NowMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_now, container, false);

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity()
                .getApplicationContext()
                .getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            mProgressDialog.dismiss();
            dialog.setTitle("网络连接出错");
            dialog.setMessage("请检查网络连接");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            dialog.show();
        }
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

        mProgressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        //mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        return view;
    }

    public void fetchData(){
        new Thread(new Runnable() {

            HttpURLConnection mConnection;
            InputStream mInputStream;

            @Override
            public void run() {

                try {
                    mConnection = (HttpURLConnection)new URL(API.NOW_MOVIE_API).openConnection();
                    mConnection.setConnectTimeout(5000);
                    mConnection.setReadTimeout(5000);
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
                            mProgressDialog.dismiss();
                            mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.universal_recyclerView);

                            customRecyclerViewAdapter customRecyclerViewAdapter = new customRecyclerViewAdapter(mMovieModels);

                            mLinearLayoutManager = new LinearLayoutManager(getActivity());

                            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                            mRecyclerView.setLayoutManager(mLinearLayoutManager);

                            mRecyclerView.setAdapter(customRecyclerViewAdapter);

                            customRecyclerViewAdapter.notifyDataSetChanged();

                            mSwipeRefreshLayout.setRefreshing(false);

                            customRecyclerViewAdapter.setOnItemClickListener(new customRecyclerViewAdapter.ClickListener() {
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

    public void showNoNetwork(View view){
        Snackbar.make(view,R.string.no_network_connected,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.go_to_set, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).show();
    }

}
