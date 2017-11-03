package hikari.com.projeto1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustavo on 27/10/2017.
 */

public class AdaptadorRecycler extends RecyclerView.Adapter<AdaptadorRecycler.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private ArrayList<ItemData> mDataset;


    public AdaptadorRecycler(ArrayList<ItemData> mDataset){
        this.mDataset=mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if(KanaItemData.class.isInstance(mDataset.get(1))){
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
        if(KanaItemData.class.isInstance(mDataset.get(position))){
            holder.mTextView.setText(mDataset.get(position).getTitle());
        }
        holder.imgViewIcon.setImageBitmap(mDataset.get(position).getImageUrl());
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
