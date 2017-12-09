package hikari.com.projeto1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;



public class ButtonAdapter extends BaseAdapter {
    private Activity mContext;
    public Drawable[] filesImgs;
    private String[] tracos;

    // Gets the context so it can be used later
    public ButtonAdapter(Activity c, Drawable[] filesImgs, String[] tracos) {
        mContext = c;
        this.filesImgs=filesImgs;
        this.tracos=tracos;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return filesImgs.length;
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mContext.getLayoutInflater()
                .inflate(R.layout.grid_item_layout, parent, false);
        ImageView btn;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            btn =  view.findViewById(R.id.imageItemGrid);

            btn.setPadding(3, 3, 3, 3);
        }
        else {
            btn = (ImageView) convertView;
        }
        // filenames is an array of strings
        btn.setImageDrawable(filesImgs[position]);
        btn.setId(position);


        return btn;
    }
}