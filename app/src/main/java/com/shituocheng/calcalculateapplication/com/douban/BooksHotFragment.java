package com.shituocheng.calcalculateapplication.com.douban;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import booksRecyclerViewAdapter.BooksRecyclerViewAdpater;
import model.BooksModel;
import utilities.API;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksHotFragment extends Fragment {

    private List<BooksModel> mBooksModels = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;


    public BooksHotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books_hot, container, false);

        initView(view);
        fetchData();
        return view;
    }

    private void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                InputStream inputStream;

                try {
                    connection = (HttpURLConnection)new URL(API.BOOK_HOT_API).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray booksJsonArray = jsonObject.getJSONArray("books");
                    int num_booksJsonArray = booksJsonArray.length();
                    for (int i = 0; i < num_booksJsonArray; i++){

                        BooksModel booksModel = new BooksModel();
                        JSONObject eachBookJsonObj = booksJsonArray.getJSONObject(i);
                        JSONObject ratingJsonObj = eachBookJsonObj.getJSONObject("rating");
                        int book_max = ratingJsonObj.getInt("max");
                        int book_avg = ratingJsonObj.getInt("average");
                        JSONObject imageJsonObj = eachBookJsonObj.getJSONObject("images");
                        String book_image = imageJsonObj.getString("large");
                        String book_publisher = eachBookJsonObj.getString("publisher");
                        String book_title = eachBookJsonObj.getString("title");
                        booksModel.setBook_avg(book_avg);
                        booksModel.setBook_max(book_max);
                        booksModel.setBook_image(book_image);
                        booksModel.setPublisher(book_publisher);
                        booksModel.setBook_name(book_title);
                        mBooksModels.add(booksModel);

                    }

                    Log.d("num_array",String.valueOf(mBooksModels.size()));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGridLayoutManager = new GridLayoutManager(getActivity(),2);
                            mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setLayoutManager(mGridLayoutManager);
                            BooksRecyclerViewAdpater booksRecyclerViewAdpater = new BooksRecyclerViewAdpater(mBooksModels);
                            booksRecyclerViewAdpater.notifyDataSetChanged();
                            mRecyclerView.setAdapter(booksRecyclerViewAdpater);

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

    private void initView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.books_hot_recyclerView);
    }

}
