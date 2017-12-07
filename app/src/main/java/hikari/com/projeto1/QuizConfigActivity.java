package hikari.com.projeto1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Optional;

public class QuizConfigActivity extends AppCompatActivity {
    @Nullable @BindView(R.id.spnQuantidade)
    Spinner spnQuantidade;
    @Nullable @BindView(R.id.capituloSpn)
    Spinner capituloSpn;
    @Nullable @BindView(R.id.hiraKataSpn)
    Spinner hiraKataSpn;
    @Nullable @BindView(R.id.basVarJunSpn)
    Spinner basVarJunSpn;
    @Nullable @BindView(R.id.vocabPartSpn)
    Spinner vocabPartSpn;



    @BindView(R.id.startQuizBtn)
    Button startQuizBtn;
    Intent proximaIntent;
    String QuizType;

    private String hiraKata,basVarJun;
    private int quantidadeQuiz,capituloKanji,parteVocab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        QuizType = intent.getStringExtra("QuizType");
        if(QuizType.equals("Kana")){
            setContentView(R.layout.activity_quiz_kana);
            hiraKata="hiragana";
            basVarJun="basico";
        }else if(QuizType.equals("Kanji")){
            setContentView(R.layout.activity_quiz_kanji);
            capituloKanji=1;
        }else{
            setContentView(R.layout.activity_quiz_vocab);
        }

        ButterKnife.bind(this);
        adapters(QuizType);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(QuizType+ " Quiz");


    }

    public void adapters(String quizType){
        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpnQuantidade = ArrayAdapter.createFromResource(this,
                R.array.quantidadeQuiz , R.layout.spinner_item_top);
        adapterSpnQuantidade.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spnQuantidade.setAdapter(adapterSpnQuantidade);

        if(quizType.equals("Kana")){
            ArrayAdapter<CharSequence> adapterSpnKana1 = ArrayAdapter.createFromResource(   this,
                    R.array.kanaSpinner1, R.layout.spinner_item_top);
            ArrayAdapter<CharSequence> adapterSpnKana2 = ArrayAdapter.createFromResource(this,
                    R.array.kanaSpinner2, R.layout.spinner_item_top);
            adapterSpnKana1.setDropDownViewResource(R.layout.spinner_item_dropdown);
            adapterSpnKana2.setDropDownViewResource(R.layout.spinner_item_dropdown);
            hiraKataSpn.setAdapter(adapterSpnKana1);
            basVarJunSpn.setAdapter(adapterSpnKana2);
        }else if(quizType.equals("Kanji")){
            ArrayAdapter<CharSequence> adapterSpnKanji1 = ArrayAdapter.createFromResource(this,
                    R.array.kanjiSpinner1, R.layout.spinner_item_top);
            adapterSpnKanji1.setDropDownViewResource(R.layout.spinner_item_dropdown);
            capituloSpn.setAdapter(adapterSpnKanji1);
        }else{
            ArrayAdapter<CharSequence> adapterSpnVocab = ArrayAdapter.createFromResource(this,
                    R.array.parteVocab, R.layout.spinner_item_top);
            adapterSpnVocab.setDropDownViewResource(R.layout.spinner_item_dropdown);
            vocabPartSpn.setAdapter(adapterSpnVocab);
        }





        // Aplicando o adaptador no spinner




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Optional @OnItemSelected({R.id.capituloSpn,R.id.spnQuantidade,R.id.hiraKataSpn,R.id.basVarJunSpn,R.id.vocabPartSpn})
    public void onItemSelected(Spinner spn, int position){
        switch(spn.getId()){
            case R.id.capituloSpn:
                capituloKanji= spn.getSelectedItemPosition()+1;
            break;

            case R.id.spnQuantidade:
                switch (spn.getSelectedItemPosition()){
                    case 0:
                        quantidadeQuiz=10;
                        break;
                    case 1:
                        quantidadeQuiz=15;
                        break;
                    case 2:
                        quantidadeQuiz=20;
                        break;
                }


            break;

            case R.id.hiraKataSpn:
                if(spn.getSelectedItemPosition()==0){
                hiraKata="hiragana";
                }
                else{
                    hiraKata="katakana";
                }
            break;

            case R.id.basVarJunSpn:
                basVarJun=spn.getSelectedItem().toString();
                if(spn.getSelectedItemPosition()==0){
                    basVarJun="basico";
                }else if(spn.getSelectedItemPosition()==1){
                    basVarJun="variavel";
                }else if(spn.getSelectedItemPosition()==2){
                    basVarJun="juncao";
                }else{
                    basVarJun="todos";
                }
            break;

            case R.id.vocabPartSpn:
                parteVocab=spn.getSelectedItemPosition()+1;
            break;

        }

    }


    @OnClick(R.id.startQuizBtn)
    public void onClick(View view){
        proximaIntent= new Intent(this, QuizActivity.class);
        proximaIntent.putExtra("quantidade", quantidadeQuiz);
        proximaIntent.putExtra("QuizType",QuizType);
        if(QuizType.equals("Kana")){
            proximaIntent.putExtra("hiraOuKata",hiraKata);
            proximaIntent.putExtra("basVarJun",basVarJun);
        }else if(QuizType.equals("Kanji")){
            proximaIntent.putExtra("capitulo",capituloKanji);
        } else{
            proximaIntent.putExtra("parteVocab",parteVocab);
        }
        startActivity(proximaIntent);


    }
}
