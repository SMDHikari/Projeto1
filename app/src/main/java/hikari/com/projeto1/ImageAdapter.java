package hikari.com.projeto1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
    final float scale;
    private static final float PADDING_IN_DP = 10.0f; // 1 dip = 1 pixel on an MDPI device
    private final int mPaddingInPixels;


    public ImageAdapter(Context c, Drawable[] drawables) {
        mContext = c;
         scale= c.getResources().getDisplayMetrics().density;
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(Math.round(60*scale+0.6f), Math.round(60*scale+0.6f)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setCropToPadding(true);
            imageView.setPadding(0, 0,0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(drawables[position]);
        return imageView;
    }
}
