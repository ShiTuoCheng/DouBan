package booksRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shituocheng.calcalculateapplication.com.douban.AppController;
import com.shituocheng.calcalculateapplication.com.douban.R;

import java.util.ArrayList;
import java.util.List;

import model.BooksModel;

/**
 * Created by shituocheng on 16/6/13.
 */
public class BooksRecyclerViewAdpater extends RecyclerView.Adapter<BooksRecyclerViewAdpater.ViewHolder> {

    private List<BooksModel> mBooksModels = new ArrayList<>();
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private NetworkImageView book_title_networkImageView;
        private TextView book_title_textView;
        private TextView book_author_textView;
        private TextView book_publisher_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            book_title_networkImageView = (NetworkImageView)itemView.findViewById(R.id.books_cover_networkImageView);
            book_title_textView = (TextView)itemView.findViewById(R.id.books_title);
            book_author_textView = (TextView)itemView.findViewById(R.id.book_author);
            book_publisher_textView = (TextView)itemView.findViewById(R.id.book_publisher);
        }
    }

    public BooksRecyclerViewAdpater(List<BooksModel> booksModels) {
        mBooksModels = booksModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BooksModel booksModel = mBooksModels.get(position);
        holder.book_title_networkImageView.setImageUrl(booksModel.getBook_image(),mImageLoader);
        holder.book_title_textView.setText(booksModel.getBook_name());
        holder.book_author_textView.setText(booksModel.getAuthor());
        holder.book_publisher_textView.setText(booksModel.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mBooksModels.size();
    }
}
