package hikari.com.projeto1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 27/10/2017.
 */

public class AdaptadorRecycler extends RecyclerView.Adapter<AdaptadorRecycler.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private ArrayList<ItemData> mDataset;
    private boolean listaVertical;


    public AdaptadorRecycler(ArrayList<ItemData> mDataset,boolean listaVertical){
        this.mDataset=mDataset;
        this.listaVertical=listaVertical;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if(listaVertical==true){
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kana_list_item, null);
        }
        else{
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kanji_list_item, null);
        }
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listaVertical==true) {
            holder.mTextView.setText(mDataset.get(position).getTitle());
        }
        holder.imgViewIcon.setImageDrawable(mDataset.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public ImageView imgViewIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.itemTextID);
            imgViewIcon=(ImageView) itemView.findViewById(R.id.itemImageID);
            itemView.setOnClickListener(this);
            imgViewIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
