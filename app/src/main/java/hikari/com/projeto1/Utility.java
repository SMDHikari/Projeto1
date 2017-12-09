package hikari.com.projeto1;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Gustavo on 08/12/2017.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 70);
        return noOfColumns;
    }
    public static int calculateNoOfColumns(Context context,int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / width);
        return noOfColumns;
    }
}
