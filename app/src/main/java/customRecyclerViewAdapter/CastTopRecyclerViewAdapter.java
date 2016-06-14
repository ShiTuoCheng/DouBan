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

import model.CastTopModel;

/**
 * Created by shituocheng on 16/6/6.
 */

public class CastTopRecyclerViewAdapter extends RecyclerView.Adapter<CastTopRecyclerViewAdapter.ViewHolder> {

    List<CastTopModel> castTopModels = new ArrayList<>();
    public static ClickListener sClickListener;
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private NetworkImageView top_networkImageView;
        private TextView top_title_textView;
        private TextView top_rating_textView;
        private TextView top_year_textView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            top_networkImageView = (NetworkImageView)itemView.findViewById(R.id.top_networkImageView);
            top_title_textView = (TextView)itemView.findViewById(R.id.top_textView);
            top_rating_textView = (TextView)itemView.findViewById(R.id.rating_textView);
            top_year_textView = (TextView)itemView.findViewById(R.id.year_textView);
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

    public CastTopRecyclerViewAdapter(List<CastTopModel> castTopModels) {
        this.castTopModels = castTopModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cast_top_item,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return castTopModels.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CastTopModel castTopModel = castTopModels.get(position);

        holder.top_year_textView.setText("年份"+String.valueOf(castTopModel.getYear()));
        holder.top_rating_textView.setText("评分"+String.valueOf(castTopModel.getAvg())+"/"+String.valueOf(castTopModel.getMax()));
        holder.top_title_textView.setText(castTopModel.getTitle());
        holder.top_networkImageView.setImageUrl(castTopModel.getCast_top_image(),imageLoader);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        CastTopRecyclerViewAdapter.sClickListener = clickListener;
    }
}
