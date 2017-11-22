package hikari.com.projeto1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Gustavo on 28/10/2017.
 */

public class KanjiItemData extends ItemData {
    private String title;
    private Drawable image;

    public KanjiItemData(Drawable image,String title){
        this.title=title;
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getImage() {
        return image;
    }

    public void clearMemory(){
        this.title=null;
        this.image=null;
    }

}
