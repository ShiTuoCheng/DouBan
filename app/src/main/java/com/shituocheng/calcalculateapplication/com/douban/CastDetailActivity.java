package com.shituocheng.calcalculateapplication.com.douban;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import customRecyclerViewAdapter.CastTopRecyclerViewAdapter;
import model.CastTopModel;
import utilities.API;

public class CastDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<CastTopModel> castTopModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView name_textView;
    private TextView en_name_textView;
    private TextView born_place_textView;
    private NetworkImageView cast_networkImageView;
    private NetworkImageView cast_top_networkImageView;
    private ProgressDialog progressDialog;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        initView();
        fetchData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在载入");
        progressDialog.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("cast_name"));
        cast_networkImageView.setImageUrl(getIntent().getStringExtra("cast_avatar"),imageLoader);
        cast_top_networkImageView.setImageUrl(getIntent().getStringExtra("cast_avatar"),imageLoader);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cast_networkImageView = (NetworkImageView)findViewById(R.id.backdrop);
        cast_top_networkImageView = (NetworkImageView)findViewById(R.id.title_networkImageView);
        name_textView = (TextView)findViewById(R.id.name_title);
        en_name_textView = (TextView)findViewById(R.id.en_name_title);
        born_place_textView = (TextView)findViewById(R.id.born_place_title);
        recyclerView = (RecyclerView)findViewById(R.id.cast_top_recyclerView);
    }
    private void fetchData(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            @Override
            public void run() {

                int cast_id = getIntent().getIntExtra("cast_id",0);
                String api = API.MOVIE_CAST_DETAIL_API+cast_id;
                try {
                    URL url = new URL(api);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine())!= null){
                        stringBuilder.append(line);
                    }

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    String cast_share_url = jsonObject.getString("mobile_url");
                    final JSONArray en_nameArray = jsonObject.getJSONArray("aka_en");
                    StringBuilder stringBuilder1 = new StringBuilder();
                    for (int i = 0; i < en_nameArray.length(); i++){
                        String piece = en_nameArray.getString(i);
                        stringBuilder1.append(piece+" ");
                    }
                    final String cast_en_name = stringBuilder1.toString();
                    final String cast_name = jsonObject.getString("name");
                    final String born_place = jsonObject.getString("born_place");
                    JSONArray worksJsonArray = jsonObject.getJSONArray("works");

                    for (int i = 0; i < worksJsonArray.length(); i++){

                        CastTopModel castTopModel = new CastTopModel();
                        JSONObject eachJsonObj = worksJsonArray.getJSONObject(i);
                        JSONObject subjectJsonObj = eachJsonObj.getJSONObject("subject");
                        JSONObject ratingJson = subjectJsonObj.getJSONObject("rating");
                        castTopModel.setMax(ratingJson.getInt("max"));
                        castTopModel.setAvg(ratingJson.getInt("average"));
                        castTopModel.setTitle(subjectJsonObj.getString("title"));
                        castTopModel.setYear(subjectJsonObj.getInt("year"));
                        castTopModel.setTop_cast_id(subjectJsonObj.getInt("id"));
                        JSONObject imageJsonObj = subjectJsonObj.getJSONObject("images");
                        String imageURL = imageJsonObj.getString("large");
                        castTopModel.setCast_top_image(imageURL);

                        castTopModels.add(castTopModel);

                    }

                    Log.d("result_size",String.valueOf(castTopModels.size()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            name_textView.setText(cast_name);
                            en_name_textView.setText(cast_en_name);
                            born_place_textView.setText(born_place);
                            CastTopRecyclerViewAdapter castTopRecyclerViewAdapter = new CastTopRecyclerViewAdapter(castTopModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            castTopRecyclerViewAdapter.notifyDataSetChanged();
                            castTopRecyclerViewAdapter.setOnItemClickListener(new CastTopRecyclerViewAdapter.ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {

                                    CastTopModel castTopModel = castTopModels.get(position);
                                    Intent intent = new Intent(CastDetailActivity.this, MovieDetailActivity.class);
                                    intent.putExtra("movie_name",castTopModel.getTitle());
                                    intent.putExtra("movie_id",castTopModel.getTop_cast_id());
                                    intent.putExtra("movie_title_image",castTopModel.getCast_top_image());
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(int position, View v) {

                                }
                            });
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(castTopRecyclerViewAdapter);

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
}
