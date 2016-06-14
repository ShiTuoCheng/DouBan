package booksRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shituocheng.calcalculateapplication.com.douban.R;

import java.util.ArrayList;
import java.util.List;

import model.BooksTagModel;

/**
 * Created by shituocheng on 16/6/10.
 */

public class BooksTagRecyclerViewAdapter extends RecyclerView.Adapter<BooksTagRecyclerViewAdapter.ViewHolder> {

    private List<BooksTagModel>booksTagModels = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tag_textView;
        public ViewHolder(View itemView) {
            super(itemView);
            tag_textView = (TextView)itemView.findViewById(R.id.tag_textView);
        }
    }

    public BooksTagRecyclerViewAdapter(List<BooksTagModel> booksTagModels) {
        this.booksTagModels = booksTagModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_books_tags,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BooksTagModel booksTagModel = booksTagModels.get(position);
        holder.tag_textView.setText(booksTagModel.getBookName());
    }

    @Override
    public int getItemCount() {
        return booksTagModels.size();
    }
}
