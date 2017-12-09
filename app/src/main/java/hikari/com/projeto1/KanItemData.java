package hikari.com.projeto1;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gustavo on 28/10/2017.
 */

public class KanItemData extends ItemData implements  Serializable, Comparable<ItemData> {
    private String title;
    private int image;
    private VectorDrawable imageV;
    private int tracos;
    private int idTabela;
    private String bas_var_jun = "";

    public void iniciar(String title, int image, int tracos, int idTabela){
        this.title=title;
        this.image=image;
        this.tracos=tracos;
        this.idTabela=idTabela;
    }
    public void iniciar(int image){
        this.image = image;
    }
    public void iniciar(String title, int image, int tracos, int idTabela,String bas_var_jun){
        this.title=title;
        this.image=image;
        this.tracos=tracos;
        this.idTabela=idTabela;
        this.bas_var_jun=bas_var_jun;
    }

    public String getBas_var_jun(){
        return bas_var_jun;
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
    public int getID() {
        return idTabela;
    }

    @Override
    public int compareTo(KanItemData compareKanji) {
        int compareTracos=(compareKanji).getTracos();
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
