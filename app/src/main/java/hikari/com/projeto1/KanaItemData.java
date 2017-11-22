package hikari.com.projeto1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Gustavo on 27/10/2017.
 */

public class KanaItemData extends ItemData {

    private String title = "default";
    private Drawable image;
    private String leitura;

    public KanaItemData(String title, Drawable image){
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
