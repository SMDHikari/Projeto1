package hikari.com.projeto1;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Gustavo on 25/11/2017.
 */

public class dialogItemData extends ItemData implements Parcelable {private String title;
    private int image;
   private Intent intent;

    public void iniciar(String title, int image,Intent intent){
        this.title=title;
        this.image=image;
        this.intent=intent;
    }

    protected void iniciar(Parcel in) {
        title = in.readString();
        intent = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<dialogItemData> CREATOR = new Creator<dialogItemData>() {
        @Override
        public dialogItemData createFromParcel(Parcel in) {
            dialogItemData dialogo = new dialogItemData();
            dialogo.iniciar(in);
            return dialogo;
        }

        @Override
        public dialogItemData[] newArray(int size) {
            return new dialogItemData[size];
        }
    };

    //    @Override
    public int getTracos(){
        return 0;
    }

    Intent getIntent(){
        return this.intent;
    }

       @Override
    public String getTitle() {
        return this.title;
    }

        @Override
    public int getImage() {
        return this.image;
    }

        @Override
    public int getID() {
        return 0;
    }

        @Override
    public int compareTo(KanItemData compareKanji) {
        return 0;
    }

       @Override
    public void clearMemory() {
        this.title=null;
    }

        @Override
    public int describeContents() {
        return 0;
    }

        @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(intent, flags);
    }

        @Override
    public int compareTo(@NonNull ItemData o) {
        return 0;
    }
}
