package hikari.com.projeto1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by Gustavo on 28/10/2017.
 */

public class KanjiItemData extends ItemData implements Comparable<KanjiItemData> {
    private String title;
    private Drawable image;
    private int tracos;

    public KanjiItemData(String title,Drawable image,int tracos){
        this.title=title;
        this.image=image;
        this.tracos=tracos;
    }

    public int getTracos(){
        return tracos;
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

    @Override
    public int compareTo(KanjiItemData compareKanji) {
        int compareTracos=(compareKanji).getTracos();
        /* For Ascending order*/
        return this.tracos-compareTracos;
    }

}
