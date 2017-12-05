package hikari.com.projeto1;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 25/11/2017.
 */

public class AdaptadorDialog extends ArrayAdapter<dialogtemData> {
    private final ArrayList<dialogtemData> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public AdaptadorDialog(Activity context, ArrayList<dialogtemData> list) {
        super(context, R.layout.dialog_item, list);
        this.context = context;

        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem=null;

        if (convertView == null) {
            LayoutInflater inflatorItem = context.getLayoutInflater();
            viewItem = inflatorItem.inflate(R.layout.dialog_item, null);


            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) viewItem.findViewById(R.id.title);
            viewHolder.flag = (ImageView) viewItem.findViewById(R.id.icon);
            viewItem.setTag(viewHolder);

        } else {
            viewItem = convertView;
        }

        ViewHolder holder = (ViewHolder) viewItem.getTag();
        holder.name.setText(list.get(position).getTitle());
        holder.flag.setImageResource(list.get(position).getImage());
        return viewItem;
    }
}
