package hikari.com.projeto1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;


/**
 * Created by Gustavo on 27/11/2017.
 */

public class AdaptadorRecyclerViewSection  extends SectionedRecyclerViewAdapter<AdaptadorRecyclerViewSection.CountHeaderViewHolder, AdaptadorRecyclerViewSection.CountItemViewHolder, AdaptadorRecyclerViewSection.CountFooterViewHolder> {
    private ArrayList<ArrayList<ItemData>> mDataset;
    private String[] sectionsString;


    public AdaptadorRecyclerViewSection(ArrayList<ArrayList<ItemData>> mDataset,String[] sectionsString){
        this.mDataset=mDataset;
        this.sectionsString=sectionsString;

    }
    public AdaptadorRecyclerViewSection(ArrayList<ArrayList<ItemData>> mDataset){
        this.mDataset=mDataset;
        this.sectionsString= new String[]{""};
    }


    @Override
    protected int getSectionCount() {
        return this.sectionsString.length;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return this.mDataset.get(section).size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    protected LayoutInflater getLayoutInflater(ViewGroup parent){
        return LayoutInflater.from(parent.getContext());
    }

    @Override
    protected CountHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if(!(sectionsString==null)){
        View view = getLayoutInflater(parent).inflate(R.layout.header_lists, parent, false);
        return new CountHeaderViewHolder(view);}
        else{
            return null;
        }
    }

    @Override
    protected CountFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected CountItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater(parent).inflate(R.layout.kana_list_item, parent, false);
        return new CountItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CountHeaderViewHolder holder, int section) {
        holder.mTextView.setText(sectionsString[section]);
    }

    @Override
    protected void onBindSectionFooterViewHolder(CountFooterViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(CountItemViewHolder holder, int section, int position) {
        holder.mTextView.setText(mDataset.get(section).get(position).getTitle());
        holder.imgViewIcon.setImageResource(mDataset.get(section).get(position).getImage());
    }



    public class CountHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public CountHeaderViewHolder(View itemView) {
            super(itemView);
            mTextView= itemView.findViewById(R.id.sectionHeaderId);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class CountFooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CountFooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public class CountItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public ImageView imgViewIcon;
        public CountItemViewHolder(View itemView) {
            super(itemView);
            mTextView= itemView.findViewById(R.id.itemTextID);
            imgViewIcon= itemView.findViewById(R.id.itemImageID);
            itemView.setOnClickListener(this);
            imgViewIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }

}