package customRecyclerViewAdapter;

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

import model.MovieModel;

/**
 * Created by shituocheng on 16/6/8.
 */

public class WeeklyRecyclerViewAdapter extends RecyclerView.Adapter<WeeklyRecyclerViewAdapter.ViewHolder> {
    private List<MovieModel> mMovieModels = new ArrayList<>();
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public static ClickListener sClickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView title_textView;
        private TextView origenal_textView;
        private TextView rating_textView;
        private TextView year_textView;
        private TextView category_textView;
        private NetworkImageView mNetworkImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            title_textView = (TextView)itemView.findViewById(R.id.movie_title_textView);
            origenal_textView = (TextView)itemView.findViewById(R.id.subtitle_textView);
            rating_textView = (TextView)itemView.findViewById(R.id.rating_textView);
            year_textView = (TextView)itemView.findViewById(R.id.year_textView);
            category_textView = (TextView)itemView.findViewById(R.id.generes_textView);
            mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.cover_networkImageView);
        }

        @Override
        public void onClick(View v) {
            sClickListener.onItemClick(getAdapterPosition(),v);
        }

        @Override
        public boolean onLongClick(View v) {
            sClickListener.onLongItemClick(getAdapterPosition(),v);
            return false;
        }
    }

    public WeeklyRecyclerViewAdapter(List<MovieModel> movieModels) {
        mMovieModels = movieModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MovieModel movieModel = mMovieModels.get(position);

        holder.title_textView.setText(movieModel.getTitle());
        holder.origenal_textView.setText(movieModel.getOriginal_title());

        if (movieModel.getAvg_Rating() == 0){
            holder.rating_textView.setText("本电影还没有评分");
        }else {

            holder.rating_textView.setText("评分: "+movieModel.getAvg_Rating()+"/"+movieModel.getMax_Rating());
        }
        holder.year_textView.setText("年份: "+ String.valueOf(movieModel.getMovie_year()));
        holder.category_textView.setText(movieModel.getGeneres());
        holder.mNetworkImageView.setImageUrl(movieModel.getMovie_Image(),mImageLoader);

    }

    @Override
    public int getItemCount() {
        return mMovieModels.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WeeklyRecyclerViewAdapter.sClickListener = clickListener;
    }
}
