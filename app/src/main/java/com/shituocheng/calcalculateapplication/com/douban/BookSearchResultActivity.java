package com.shituocheng.calcalculateapplication.com.douban;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import java.util.logging.LogRecord;

import utilities.API;

public class BookSearchResultActivity extends AppCompatActivity {

    private static final int BOOK_TITLE_MESSAGE_WHAT=0;

    private NetworkImageView background_ImageView;
    private NetworkImageView book_title_networkImageView;
    private TextView book_category_textView;
    private ProgressDialog mProgressDialog;
    private  TextView book_content_textView;
    private TextView book_title_textView;
    private TextView book_publisher_textView;
    private TextView book_author_textView;
    private TextView book_price_textView;
    private TextView book_rating_textView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BOOK_TITLE_MESSAGE_WHAT:
                    Map<String, String> titleMap = (Map<String, String>) msg.obj;
                    String bookTilte = titleMap.get("book_title");
                    getSupportActionBar().setTitle(bookTilte);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在载入");
        mProgressDialog.show();
        setSupportActionBar(toolbar);
        initView();
        fetchData();

    }

    private void fetchData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                InputStream inputStream;
                String book_isbn = getIntent().getStringExtra("book_isbn");

                String api = API.BOOK_ISBN_API+book_isbn;

                try {
                    connection = (HttpURLConnection) new URL(api).openConnection();
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
                    JSONObject imageJson = jsonObject.getJSONObject("images");
                    final String imageString = imageJson.getString("large");
                    final String categoryString = jsonObject.getString("catalog");
                    final String contentString = jsonObject.getString("summary");
                    final String bookTitleString = jsonObject.getString("title");

                    Map<String, String> titleMap = new HashMap();
                    titleMap.put("book_title",bookTitleString);
                    Message titleMessage = new Message();
                    titleMessage.obj = titleMap;
                    titleMessage.what = BOOK_TITLE_MESSAGE_WHAT;
                    handler.sendMessage(titleMessage);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            getSupportActionBar().setTitle(bookTitleString);
                            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                            background_ImageView.setImageUrl(imageString,imageLoader);
                            book_title_networkImageView.setImageUrl(imageString,imageLoader);
                            book_title_textView.setText(bookTitleString);
                            book_category_textView.setText(categoryString);
                            book_content_textView.setText(contentString);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return true;
    }



    private void initView(){
        book_content_textView = (TextView)findViewById(R.id.book_content_textView);
        book_category_textView = (TextView)findViewById(R.id.book_category_textView);
        book_title_textView = (TextView)findViewById(R.id.book_name_title);
        book_author_textView = (TextView)findViewById(R.id.book_author_title);
        book_publisher_textView = (TextView)findViewById(R.id.book_publisher_title);
        book_price_textView = (TextView)findViewById(R.id.book_price_title);
        book_title_networkImageView = (NetworkImageView)findViewById(R.id.book_title_networkImageView);
        background_ImageView = (NetworkImageView)findViewById(R.id.backdrop);
    }

}
