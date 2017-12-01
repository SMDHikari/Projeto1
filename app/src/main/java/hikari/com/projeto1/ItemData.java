package hikari.com.projeto1;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Gustavo on 28/10/2017.
 */

abstract class ItemData implements Serializable, Comparable<ItemData>{

    abstract String getTitle();

    abstract Drawable getImage();

    abstract int getID();

    abstract int compareTo(KanItemData compareKanji);

    abstract void clearMemory();
}
