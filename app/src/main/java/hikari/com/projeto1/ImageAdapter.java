package hikari.com.projeto1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by Gustavo on 29/11/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Drawable[] drawables;
    private static final float PADDING_IN_DP = 10.0f; // 1 dip = 1 pixel on an MDPI device
    private final int mPaddingInPixels;


    public ImageAdapter(Context c, Drawable[] drawables) {
        mContext = c;
        final float scale = c.getResources().getDisplayMetrics().density;
        mPaddingInPixels = (int) (PADDING_IN_DP * scale + 0.6f);
        this.drawables=drawables;
    }

    public int getCount() {
        return drawables.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(mPaddingInPixels, mPaddingInPixels, mPaddingInPixels, mPaddingInPixels);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(drawables[position]);
        return imageView;
    }
}
