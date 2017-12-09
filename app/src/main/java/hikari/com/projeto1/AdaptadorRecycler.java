package hikari.com.projeto1;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustavo on 27/10/2017.
 */

public class AdaptadorRecycler extends RecyclerView.Adapter<AdaptadorRecycler.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private ArrayList<ItemData> mDataset;
    private ArrayList<VocabularioItem> vDataSet;
    private boolean listaVertical;
    private String basVarJun;
    private boolean vocab=false;
    static private int selectedPosition=-1;

    static public void setSelectedPosition(int recebido){
        selectedPosition=recebido;
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }


    public AdaptadorRecycler(ArrayList<ItemData> mDataset,boolean listaVertical){
        this.mDataset=mDataset;
        this.listaVertical=listaVertical;
        basVarJun="";
    }
    public AdaptadorRecycler(ArrayList<VocabularioItem> vDataSet,boolean listaVertical,boolean vocab){
        this.vDataSet=vDataSet;
        this.listaVertical=listaVertical;
        this.vocab=vocab;
        basVarJun="";
    }
    public AdaptadorRecycler(ArrayList<ItemData> mDataset,boolean listaVertical,String basVarJun){
        this.mDataset=mDataset;
        this.listaVertical=listaVertical;
        this.basVarJun=basVarJun;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if(listaVertical==true){
            if(basVarJun.equals("juncao")) {
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_item_jun, null);
            }
            else{
                if(vocab==true){
                    itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocab_list_item, null);
                }
                else{
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_item, null);
            }}
        }
        else{
            if(basVarJun.equals("juncao")) {
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item_jun, null);
            }
            else{
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, null);
            }
        }
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        viewHolder.linearLayout.setPadding(0,0,0, 0);
        viewHolder.linearLayout.setGravity(Gravity.CENTER);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listaVertical==true) {
            if(vocab==true){
                holder.mTextView.setText(vDataSet.get(position).getTitulo());
                holder.mTextView2.setText(vDataSet.get(position).getTraducao());
            }
            else{
            holder.mTextView.setText(mDataset.get(position).getTitle());
        }}
        holder.imgViewIcon.setImageResource(mDataset.get(position).getImage());
        if(listaVertical==false){
            if(selectedPosition==position)
                holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"));
            else{
                holder.itemView.setBackgroundColor(Color.alpha(100));
            }
        }


    }


    @Override
    public int getItemCount() {
        if(vocab==false){
        return mDataset.size();}
        else{
            return vDataSet.size();
        }
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
        public TextView mTextView2;
        public ImageView imgViewIcon;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            if(vocab==false){
            mTextView= itemView.findViewById(R.id.itemTextID);
            imgViewIcon= itemView.findViewById(R.id.itemImageID);
            linearLayout= itemView.findViewById(R.id.linearHorizList);
                itemView.setOnClickListener(this);
                imgViewIcon.setOnClickListener(this);
            }
            else{
                mTextView= itemView.findViewById(R.id.itemTitleID);
                mTextView2= itemView.findViewById(R.id.itemTradID);
                linearLayout= itemView.findViewById(R.id.linearHorizList);
            }


        }

        @Override
        public void onClick(View v) {

        }
    }
}
