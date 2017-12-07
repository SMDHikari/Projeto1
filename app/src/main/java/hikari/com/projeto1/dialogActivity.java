package hikari.com.projeto1;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class dialogActivity extends ListActivity {

    private String[] dialogTitles;
    private TypedArray imgs;
    boolean clicado = false;
    private ArrayList<dialogtemData> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(clicado==false){
        Intent intent = getIntent();
        clicado=true;
        int botaoClicado = intent.getIntExtra("botaoClicado",0);
        String tituloClicado= intent.getStringExtra("titulo");
        populateDialogList(botaoClicado);
        ArrayAdapter<dialogtemData> adapter = new AdaptadorDialog(this,items);
        this.setTitle(tituloClicado);
        int dividerId = this.getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = this.findViewById(dividerId);
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

                finish();
            }
        });
        clicado=false;
        }
    }

    private void populateDialogList(int botaoClicado) {
        items = new ArrayList<dialogtemData>();
        Intent[] dialogIntent= new Intent[0];


        switch(botaoClicado){
            case 0:
                dialogTitles = getResources().getStringArray(R.array.kanaMenu);
                imgs = getResources().obtainTypedArray(R.array.kanaMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanaListActivity.class)
                        ,new Intent(this,QuizConfigActivity.class).putExtra("QuizType","Kana")};

            break;
            case 1:
                dialogTitles = getResources().getStringArray(R.array.kanjiMenu);
                imgs = getResources().obtainTypedArray(R.array.kanjiMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanjiListActivity.class)
                        ,new Intent(this,QuizConfigActivity.class).putExtra("QuizType","Kanji"),
                        new Intent(this,FormeKanjiActivity.class)};

                break;
            case 2:
                dialogTitles = getResources().getStringArray(R.array.vocabularioMenu);
                imgs = getResources().obtainTypedArray(R.array.vocabularioMenuImgs);
                dialogIntent=new Intent[]{new Intent(this,KanaListActivity.class)
                        ,new Intent(this,QuizConfigActivity.class).putExtra("QuizType","Vocabulario")};

                break;
        }
        for(int i = 0; i < dialogTitles.length; i++){
            items.add(new dialogtemData(dialogTitles[i],  imgs.getResourceId(i,0),dialogIntent[i]));
        }
    }
}
