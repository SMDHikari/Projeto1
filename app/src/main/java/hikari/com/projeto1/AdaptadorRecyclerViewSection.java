package hikari.com.projeto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;


/**
 * Created by Gustavo on 27/11/2017.
 */

public class AdaptadorRecyclerViewSection  extends SectionedRecyclerViewAdapter<AdaptadorRecyclerViewSection.CountHeaderViewHolder, AdaptadorRecyclerViewSection.CountItemViewHolder, AdaptadorRecyclerViewSection.CountFooterViewHolder> {
    private ArrayList<ArrayList<ItemData>> mDataset;
    private String[] sectionsString;
    private Context context;
    private int[] sectionForPosition = null;
    private int[] positionWithinSection = null;
    private boolean[] isHeader = null;
    private boolean[] isFooter = null;
    private int count = 0;
    protected static final int TYPE_SECTION_HEADER = -1;
    protected static final int TYPE_SECTION_FOOTER = -2;
    protected static final int type_kan=-3;
    protected static final int type_jun=-4;



    public AdaptadorRecyclerViewSection(ArrayList<ArrayList<ItemData>> mDataset,String[] sectionsString,Context context){
        this.mDataset=mDataset;
        this.sectionsString=sectionsString;
        this.context=context;

    }

    public AdaptadorRecyclerViewSection(ArrayList<ArrayList<ItemData>> mDataset){
        this.mDataset=mDataset;
        this.sectionsString= new String[]{""};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if(isSectionHeaderViewType(viewType)){
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType);
        }else if(isSectionFooterViewType(viewType)){
            viewHolder = onCreateSectionFooterViewHolder(parent, viewType);
        }else{
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }

        return viewHolder;
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
    public int getItemViewType(int position) {

        if(sectionForPosition == null){
            setupIndices();
        }

        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if(isSectionHeaderPosition(position)){
            return getSectionHeaderViewType(section);
        }else if(isSectionFooterPosition(position)){
            return getSectionFooterViewType(section);
        }else{
            return getSectionItemViewType(section, index);
        }
    }

    private void setupIndices(){
        count = countItems();
        allocateAuxiliaryArrays(count);
        precomputeIndices();
    }

    private int countItems() {
        int count = 0;
        int sections = getSectionCount();

        for(int i = 0; i < sections; i++){
            count += 1 + getItemCountForSection(i) + (hasFooterInSection(i) ? 1 : 0);
        }
        return count;
    }
    @Override
    protected int getSectionItemViewType(int section, int position){
        if(((KanItemData)mDataset.get(section).get(position)).getBas_var_jun().equals("juncao")){
            return type_jun;
        }
        else{
            return type_kan;
        }

    }

    private void precomputeIndices(){
        int sections = getSectionCount();
        int index = 0;

        for(int i = 0; i < sections; i++){
            setPrecomputedItem(index, true, false, i, 0);
            index++;

            for(int j = 0; j < getItemCountForSection(i); j++){
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }

            if(hasFooterInSection(i)){
                setPrecomputedItem(index, false, true, i, 0);
                index++;
            }
        }
    }

    private void setPrecomputedItem(int index, boolean isHeader, boolean isFooter, int section, int position) {
        this.isHeader[index] = isHeader;
        this.isFooter[index] = isFooter;
        sectionForPosition[index] = section;
        positionWithinSection[index] = position;
    }

    private void allocateAuxiliaryArrays(int count) {
        sectionForPosition = new int[count];
        positionWithinSection = new int[count];
        isHeader = new boolean[count];
        isFooter = new boolean[count];
    }

    @Override
    protected CountFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected CountItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case type_jun:
                Toast.makeText(parent.getContext(),"teste",Toast.LENGTH_SHORT).show();
                view = getLayoutInflater(parent).inflate(R.layout.vertical_list_item_jun, parent, false);
                break;
            default:
                 view = getLayoutInflater(parent).inflate(R.layout.vertical_list_item, parent, false);
                break;
        }

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
        public LinearLayout linearLayout;
        public CountItemViewHolder(View itemView) {
            super(itemView);
            mTextView= itemView.findViewById(R.id.itemTextID);
            imgViewIcon= itemView.findViewById(R.id.itemImageID);
            linearLayout= itemView.findViewById(R.id.linearVertList);
            itemView.setOnClickListener(this);
            imgViewIcon.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

        }
    }

}