package hikari.com.projeto1;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;

/**
 * Created by Gustavo on 28/10/2017.
 */

public class KanItemData extends ItemData {
    private String title;
    private int image;
    private VectorDrawable imageV;
    private int tracos;
    private int idTabela;

    public KanItemData(String title, int image, int tracos, int idTabela){
        this.title=title;
        this.image=image;
        this.tracos=tracos;
        this.idTabela=idTabela;
    }
    public KanItemData(String title, VectorDrawable imageV, int tracos, int idTabela){
        this.title=title;
        this.imageV=imageV;
        this.tracos=tracos;
        this.idTabela=idTabela;
    }


    public int getTracos(){
        return tracos;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    @Override
    int getID() {
        return idTabela;
    }

    @Override
    int compareTo(KanItemData compareKanji) {
        int compareTracos=(compareKanji).getTracos();
        /* For Ascending order*/
        return this.tracos-compareTracos;
    }

    public void clearMemory(){
        this.title=null;
    }

    @Override
    public int compareTo(@NonNull ItemData compareKanji) {
        return 0;
    }
}
