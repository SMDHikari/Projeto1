package hikari.com.projeto1;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class dialogActivity extends ListActivity {

    private String[] dialogTitles;
    private TypedArray imgs;
    boolean clicado = false;
    private ArrayList<ItemData> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(clicado==false){
        Intent intent = getIntent();
        clicado=true;
        int botaoClicado = intent.getIntExtra("botaoClicado",0);
        String tituloClicado= intent.getStringExtra("titulo");
        populateDialogList(botaoClicado);
        ArrayAdapter<ItemData> adapter = new AdaptadorDialog(this,items);
        this.setTitle(tituloClicado);
        int divierId = this.getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = this.findViewById(divierId);
        divider.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setListAdapter(adapter);




        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemData c = items.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("posicaoClicado",position);
                returnIntent.putExtra("clicadoOption", c);
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images;
                finish();
            }
        });
        clicado=false;
        }
    }

    private void populateDialogList(int botaoClicado) {
        items = new ArrayList<ItemData>();
        Intent[] dialogIntent= new Intent[0];


        switch(botaoClicado){
            case 0:
                dialogTitles = getResources().getStringArray(R.array.kanaMenu);
                imgs = getResources().obtainTypedArray(R.array.kanaMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanaListActivity.class)
                        ,new Intent(this,QuizActivity.class).putExtra("QuizType","kana")};

            break;
            case 1:
                dialogTitles = getResources().getStringArray(R.array.kanjiMenu);
                imgs = getResources().obtainTypedArray(R.array.kanjiMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanjiListActivity.class)
                        ,new Intent(this,QuizActivity.class).putExtra("QuizType","kanji"),
                        new Intent(this,FormeKanjiActivity.class)};

                break;
            case 2:
                dialogTitles = getResources().getStringArray(R.array.vocabularioMenu);
                imgs = getResources().obtainTypedArray(R.array.vocabularioMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanaListActivity.class)
                        ,new Intent(this,QuizActivity.class).putExtra("QuizType","vocabulario")};

                break;
        }
        for(int i = 0; i < dialogTitles.length; i++){
            items.add(new dialogtemData(dialogTitles[i],  imgs.getDrawable(i),dialogIntent[i]));
        }
    }
}
