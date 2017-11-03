package hikari.com.projeto1;

import android.graphics.Bitmap;

/**
 * Created by Gustavo on 27/10/2017.
 */

public class KanaItemData extends ItemData {

    private String title = "default";
    private Bitmap image;
    private String leitura;

    public KanaItemData(String title, Bitmap image){
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
