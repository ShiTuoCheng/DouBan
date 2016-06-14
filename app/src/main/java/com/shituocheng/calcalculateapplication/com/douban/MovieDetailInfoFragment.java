package com.shituocheng.calcalculateapplication.com.douban;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customRecyclerViewAdapter.CastRecyclerViewAdapter;
import customRecyclerViewAdapter.DirRecyclerViewAdapter;
import model.CastModel;
import utilities.API;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailInfoFragment extends Fragment {
    private ProgressDialog progressDialog;
    private NetworkImageView networkImageView;
    private TextView alertTextView;
    private TextView ratingTextView;
    private TextView titleTextView;
    private TextView genresTextView;
    private TextView contentTextView;
    private TextView countryTextView;
    private RecyclerView castRecyclerView;
    private RecyclerView dirRecyclerView;

    private static final int MSG_WHAT_RATING=0;
    private static final int MSG_WHAT_CAST=1;
    private static final int MSG_WHAT_CONTENT=2;
    private static final int MSG_WHAT_DIR=3;
    private static final int MSG_WHAT_OTHER=4;
    private static final int MSG_WHAT_SHARE=5;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_WHAT_RATING:

                    Map<String,String> ratingMap = (Map<String, String>)msg.obj;
                    String maxValue = ratingMap.get("max");
                    String avgValue = ratingMap.get("avg");
                    if (avgValue.equals("0")){
                        ratingTextView.setText("还没有评分");
                    }else {
                        ratingTextView.setText("评分:"+avgValue+"/"+maxValue);
                    }
                    genresTextView.setText(ratingMap.get("genres"));
                    break;
                case MSG_WHAT_CAST:

                    final List<CastModel> castModels = (List<CastModel>) msg.obj;
                    if (castModels.size() == 0){
                        alertTextView.setText("还没有相关演员=.=");
                    }
                    CastRecyclerViewAdapter castRecyclerViewAdapter = new CastRecyclerViewAdapter(castModels);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    castRecyclerViewAdapter.notifyDataSetChanged();
                    castRecyclerViewAdapter.setOnItemClickListener(new CastRecyclerViewAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            CastModel castModel = castModels.get(position);
                            Intent intent = new Intent(getActivity(),CastDetailActivity.class);
                            intent.putExtra("cast_avatar",castModel.getCast_avatar());
                            intent.putExtra("cast_id",castModel.getCast_id());
                            intent.putExtra("cast_name",castModel.getCast_name());
                            startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(int position, View v) {

                        }
                    });
                    castRecyclerView.setLayoutManager(linearLayoutManager);
                    castRecyclerView.setAdapter(castRecyclerViewAdapter);
                    break;

                case MSG_WHAT_CONTENT:

                    String content = (String)msg.obj;
                    contentTextView.setText(content);

                    break;
                case MSG_WHAT_DIR:

                    List<CastModel> dirModels = (List<CastModel>) msg.obj;
                    DirRecyclerViewAdapter dirRecyclerViewAdapter = new DirRecyclerViewAdapter(dirModels);
                    LinearLayoutManager dirLinearLayoutManager = new LinearLayoutManager(getActivity());
                    dirLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    dirRecyclerViewAdapter.notifyDataSetChanged();
                    dirRecyclerView.setLayoutManager(dirLinearLayoutManager);
                    dirRecyclerView.setAdapter(dirRecyclerViewAdapter);
                    break;
                case MSG_WHAT_OTHER:

                    Map<String,String> otherMap = (Map<String, String>)msg.obj;
                    String country = otherMap.get("country");
                    Log.d("map",country);
                    String want = otherMap.get("want");
                    String collect = otherMap.get("collect");
                    countryTextView.setText(country);
                    break;

                case MSG_WHAT_SHARE:

                    Map<String,String> shareMap = (Map<String, String>) msg.obj;
                    String desktop_url = shareMap.get("desktop_url");
                    String mobile_url = shareMap.get("mobile_url");
                    final String shared_url = "看看这部电影^^\n"+"手机版链接:"+mobile_url+"\n"+"电脑版链接:"+desktop_url;
                    FloatingActionButton floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.fab);
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,shared_url);
                            startActivity(Intent.createChooser(shareIntent,"分享至"));
                        }
                    });

            }
        }
    };


    public MovieDetailInfoFragment() {
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

        View view = inflater.inflate(R.layout.fragment_movie_detail_info, container, false);
        initView(view);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        titleTextView.setText(getActivity().getIntent().getStringExtra("movie_name"));
        networkImageView.setImageUrl(getActivity().getIntent().getStringExtra("movie_title_image"),imageLoader);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在载入");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return view;
    }

    private void fetchData(){

        new Thread(new Runnable() {
            HttpURLConnection connection;
            InputStream inputStream;
            @Override
            public void run() {
                try {

                    int movie_id = getActivity().getIntent().getIntExtra("movie_id",0);
                    Log.d("result_id",String.valueOf(movie_id));
                    connection = (HttpURLConnection)new URL(API.MOVIE_DETAIL_API+movie_id).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();

                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine())!=null){
                        stringBuilder.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    JSONObject ratingJson = jsonObject.getJSONObject("rating");
                    int rating_max = ratingJson.getInt("max");
                    int rating_avg = ratingJson.getInt("average");
                    JSONArray genresArray = jsonObject.getJSONArray("genres");
                    StringBuilder genresStringBuilder = new StringBuilder();
                    for (int i = 0; i < genresArray.length();i++){
                        String piece = genresArray.getString(i);
                        genresStringBuilder.append(piece+" ");
                    }
                    Map<String,String> ratingMap = new HashMap<>();
                    String maxValue = String.valueOf(rating_max);
                    String avgValue = String.valueOf(rating_avg);
                    ratingMap.put("max",maxValue);
                    ratingMap.put("avg",avgValue);
                    ratingMap.put("genres",genresStringBuilder.toString());
                    Message ratingMessage = new Message();
                    ratingMessage.what = MSG_WHAT_RATING;
                    ratingMessage.obj = ratingMap;
                    handler.sendMessage(ratingMessage);

                    List<CastModel> castModels = new ArrayList<>();
                    JSONArray castJsonArray = jsonObject.getJSONArray("casts");
                    for (int i=0; i<castJsonArray.length();i++){

                        CastModel castModel = new CastModel();
                        JSONObject eachCastJson = castJsonArray.getJSONObject(i);
                        castModel.setCast_alt(eachCastJson.getString("alt"));
                        JSONObject imageJsonObj = eachCastJson.getJSONObject("avatars");
                        castModel.setCast_avatar(imageJsonObj.getString("large"));
                        castModel.setCast_id(eachCastJson.getInt("id"));
                        castModel.setCast_name(eachCastJson.getString("name"));
                        castModels.add(castModel);
                    }

                    Log.d("cast",String.valueOf(castModels.size()));

                    Message castMessage = new Message();
                    castMessage.what = MSG_WHAT_CAST;
                    castMessage.obj = castModels;
                    handler.sendMessage(castMessage);

                    String content = jsonObject.getString("summary");
                    Message contentMessage = new Message();
                    contentMessage.what = MSG_WHAT_CONTENT;
                    contentMessage.obj = content;
                    handler.sendMessage(contentMessage);

                    JSONArray dirJsonArray = jsonObject.getJSONArray("directors");

                    List<CastModel> dirModels = new ArrayList<>();
                    for (int i=0; i<dirJsonArray.length();i++){
                        CastModel castModel = new CastModel();
                        JSONObject eachCastJson = dirJsonArray.getJSONObject(i);
                        castModel.setCast_alt(eachCastJson.getString("alt"));
                        JSONObject imageJsonObj = eachCastJson.getJSONObject("avatars");
                        castModel.setCast_avatar(imageJsonObj.getString("large"));
                        castModel.setCast_id(eachCastJson.getInt("id"));
                        castModel.setCast_name(eachCastJson.getString("name"));
                        dirModels.add(castModel);
                    }
                    Log.d("dir",String.valueOf(dirModels.size()));
                    Message dirMessage = new Message();
                    dirMessage.what = MSG_WHAT_DIR;
                    dirMessage.obj = dirModels;
                    handler.sendMessage(dirMessage);

                    JSONArray countries = jsonObject.getJSONArray("countries");
                    StringBuilder countryStringBuilder = new StringBuilder();
                    for (int i = 0; i < countries.length();i++){

                        String piece = countries.getString(i);
                        countryStringBuilder.append(piece+" ");

                    }
                    Log.d("countries",countryStringBuilder.toString());
                    String country = countryStringBuilder.toString();
                    int want_num = jsonObject.getInt("wish_count");
                    int collect_num = jsonObject.getInt("collect_count");
                    String want = String.valueOf(want_num);
                    String collect = String.valueOf(collect_num);

                    Message otherMessage = new Message();
                    Map<String,String> otherMap = new HashMap<String, String>();
                    otherMap.put("country",country);
                    otherMap.put("want",want);
                    otherMap.put("collect",collect);
                    otherMessage.obj = otherMap;
                    otherMessage.what = MSG_WHAT_OTHER;
                    handler.sendMessage(otherMessage);

                    Message shareMessage = new Message();
                    Map<String,String> shareMap = new HashMap<>();
                    String desktopShareUrl = jsonObject.getString("alt");
                    String mobile = jsonObject.getString("mobile_url");
                    shareMap.put("desktop_url",desktopShareUrl);
                    shareMap.put("mobile_url",mobile);
                    shareMessage.obj = shareMap;
                    shareMessage.what = MSG_WHAT_SHARE;
                    handler.sendMessage(shareMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

     private void initView(View view){
         ratingTextView = (TextView)view.findViewById(R.id.rating_title);
         titleTextView = (TextView)view.findViewById(R.id.name_title);
         genresTextView = (TextView)view.findViewById(R.id.dir_title);
         networkImageView = (NetworkImageView)view.findViewById(R.id.title_networkImageView);
         castRecyclerView = (RecyclerView)view.findViewById(R.id.cast_recyclerView);
         contentTextView = (TextView)view.findViewById(R.id.content_textView);
         dirRecyclerView = (RecyclerView)view.findViewById(R.id.preview_recyclerView);
         alertTextView = (TextView)view.findViewById(R.id.alarm_title);
         countryTextView = (TextView)view.findViewById(R.id.src_title);
     }

}
