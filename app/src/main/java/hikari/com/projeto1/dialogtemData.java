package hikari.com.projeto1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Gustavo on 25/11/2017.
 */

public class dialogtemData extends ItemData implements Parcelable {
    private String title;
    private Drawable image;
    private Intent intent;

    public dialogtemData(String title, Drawable image,Intent intent){
        this.title=title;
        this.image=image;
        this.intent=intent;
    }

    protected dialogtemData(Parcel in) {
        title = in.readString();
        intent = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<dialogtemData> CREATOR = new Creator<dialogtemData>() {
        @Override
        public dialogtemData createFromParcel(Parcel in) {
            return new dialogtemData(in);
        }

        @Override
        public dialogtemData[] newArray(int size) {
            return new dialogtemData[size];
        }
    };

    Intent getIntent(){
        return this.intent;
    }
    @Override
    String getTitle() {
        return this.title;
    }

    @Override
    Drawable getImage() {
        return this.image;
    }

    @Override
    int getID() {
        return 0;
    }

    @Override
    int compareTo(KanItemData compareKanji) {
        return 0;
    }

    @Override
    void clearMemory() {
        this.title=null;
        this.image=null;
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
