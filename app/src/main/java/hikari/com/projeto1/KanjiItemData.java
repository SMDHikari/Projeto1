package hikari.com.projeto1;

import android.graphics.Bitmap;

/**
 * Created by Gustavo on 28/10/2017.
 */

public class KanjiItemData extends ItemData {
    private String title;
    private Bitmap image;

    public KanjiItemData(Bitmap image,String title){
        this.title=title;
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImageUrl() {
        return image;
    }

}
