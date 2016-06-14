package customRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import model.CastModel;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shituocheng.calcalculateapplication.com.douban.AppController;
import com.shituocheng.calcalculateapplication.com.douban.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shituocheng on 16/6/4.
 */

public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.ViewHolder> {

    private List<CastModel> castModels = new ArrayList<>();
    public static ClickListener sClickListener;
    public interface ClickListener{
        void onItemClick(int position, View v);
        void onLongItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private TextView cast_name;
        private NetworkImageView cast_networkImageView;
        private Button action_button;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cast_name = (TextView)itemView.findViewById(R.id.cast_textView);
            cast_networkImageView = (NetworkImageView)itemView.findViewById(R.id.cast_networkImageView);
            action_button = (Button)itemView.findViewById(R.id.cast_actionButton);
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

    public CastRecyclerViewAdapter(List<CastModel> castModels) {
        this.castModels = castModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cast_item,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return castModels.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CastModel castModel = castModels.get(position);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        holder.cast_networkImageView.setImageUrl(castModel.getCast_avatar(),imageLoader);
        holder.cast_name.setText(castModel.getCast_name());
    }

    public void setOnItemClickListener(ClickListener clickListener){
        CastRecyclerViewAdapter.sClickListener = clickListener;
    }

}
