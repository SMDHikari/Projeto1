package hikari.com.projeto1;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 07/12/2017.
 */

public class AdaptadorSimpleListView extends BaseAdapter {
    private final ArrayList<quizQuestions> questoes;
    Activity context;
        AdaptadorSimpleListView(ArrayList<quizQuestions> questoes, Activity context){
            this.questoes=questoes;
            this.context=context;

        }
    @Override
    public int getCount() {
        return questoes.size();
    }

    @Override
    public Object getItem(int position) {
        return questoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = context.getLayoutInflater()
                .inflate(R.layout.resultado_list_item, parent, false);
        quizQuestions quizQ = questoes.get(position);

        ImageView imagemItem =
                view.findViewById(R.id.imagemItem);
        TextView nome =
                view.findViewById(R.id.textoItem);
        ImageView imagemBool =
                view.findViewById(R.id.certoErradoItem);
        imagemItem.setImageResource(quizQ.getQuestaoImg());
        nome.setText(quizQ.getresposta());
        if(quizQ.getAcertouOuErrou()==true){
            nome.setTextColor(context.getResources().getColor(R.color.colorGreen));
            imagemBool.setImageResource(R.drawable.icone_acerto);
        }else{
            nome.setTextColor(context.getResources().getColor(R.color.colorRed));
            imagemBool.setImageResource(R.drawable.icone_erro);
        }
            return view;
    }


}