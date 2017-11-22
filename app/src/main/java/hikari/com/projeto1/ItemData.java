package hikari.com.projeto1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Gustavo on 28/10/2017.
 */

abstract class ItemData {

    abstract String getTitle();

    abstract Drawable getImage();

    abstract void clearMemory();
}
