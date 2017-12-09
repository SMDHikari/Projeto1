package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.PorterDuff;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuizActivity extends AppCompatActivity {
    int contador=0,quantidadeQuiz;
    String QuizType;
    @BindView(R.id.QuantidadeText)
    TextView QuantidadeText;
    @BindView(R.id.BtnQuiz1)
    Button BtnQuiz1;
    @BindView(R.id.BtnQuiz2)
    Button BtnQuiz2;
    @BindView(R.id.BtnQuiz3)
    Button BtnQuiz3;
    @BindView(R.id.BtnQuiz4)
    Button BtnQuiz4;
    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;
    @BindView(R.id.ImagemPergunta)
    ImageView ImagemPergunta;



    ArrayList<quizQuestions> arrayQuestoes;
    Boolean[] acertouErrou;
    String[] respostasDadas,respostasCertas,nomesImagems;
    int[] imageId;

    boolean botaoClicado=false;



    int CapituloKanji;
    String hiraOuKata,basVarJun,parteVocabulario;
    private SQLiteDatabase bancoDados;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        Intent intent = getIntent();
        QuizType = intent.getStringExtra("QuizType");
        quantidadeQuiz= intent.getIntExtra("quantidade",10);
        arrayQuestoes= new ArrayList<quizQuestions>(quantidadeQuiz);
        acertouErrou = new Boolean[quantidadeQuiz];
        respostasDadas= new String[quantidadeQuiz];
        respostasCertas= new String[quantidadeQuiz];
        nomesImagems= new String[quantidadeQuiz];
        imageId=new int[quantidadeQuiz];
        ButterKnife.bind(this);
        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        if(QuizType.equals("Kana")){
            hiraOuKata = intent.getStringExtra("hiraOuKata");
            basVarJun = intent.getStringExtra("basVarJun");
            iniciarKanaQuiz();
        }else if(QuizType.equals("Kanji")){
            CapituloKanji = intent.getIntExtra("capitulo",1);
            iniciarKanjiQuiz();
        }else{
            parteVocabulario = intent.getStringExtra("parteVocab");
            iniciarVocabQuiz();
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        TextView toolbar_title =  findViewById(R.id.toolbar_title);
        toolbar_title.setText(QuizType+ " Quiz");
        atualizarTexto();

    }

    public void atualizarTexto(){
        QuantidadeText.setText((contador+1)+"/"+quantidadeQuiz);
    }

    public void iniciarKanjiQuiz(){
        cursor= bancoDados.rawQuery("SELECT leitura_on,leitura_kun,kanji_imagem FROM kanji where capitulo_kanji= "+CapituloKanji+" ORDER BY RANDOM() LIMIT 1",null);
        int indiceColunaLeituraOn=cursor.getColumnIndex("leitura_on");
        int indiceColunaleituraKun=cursor.getColumnIndex("leitura_kun");
        int indiceColunaImagem=cursor.getColumnIndex("kanji_imagem");
        cursor.moveToFirst();

        String[] listaLeituraCertaOn = cursor.getString(indiceColunaLeituraOn).split(",");
        String[] listaLeituraCertaKun = cursor.getString(indiceColunaleituraKun).split(",");

        nomesImagems[contador]= cursor.getString(indiceColunaImagem);
        imageId[contador] = getResources().getIdentifier(cursor.getString(indiceColunaImagem), "drawable", this.getPackageName());
        cursor.close();
        String[] respostas= new String[4];
        boolean randomizar = (Math.random() < 0.5);
        if(randomizar=true){
            respostasCertas[contador] = shufleArray(listaLeituraCertaOn)[0];
        }else{
            respostasCertas[contador] = shufleArray(listaLeituraCertaKun)[0];
        }
        for(int x=0;x<3;x++){
            boolean escape = false;
            Log.e("teste","foraWhile");
            while(escape==false){
                cursor= bancoDados.rawQuery("SELECT leitura_on,leitura_kun FROM kanji ORDER BY RANDOM() LIMIT 1",null);
                int indiceLeituraErradaOn = cursor.getColumnIndex("leitura_on");
                int indiceLeituraErradaKun = cursor.getColumnIndex("leitura_kun");
                cursor.moveToFirst();

                String[] leiturasOn = cursor.getString(indiceLeituraErradaOn).split(",");
                String[] leiturasKun = cursor.getString(indiceLeituraErradaKun).split(",");
                String[] leituras;
                randomizar = (Math.random() < 0.5);
                if(randomizar==true){
                    leituras=shufleArray(leiturasOn);
                }else{
                    leituras=shufleArray(leiturasKun);
                }

                for(int y=0;y<leituras.length;y++){
                    if(!(Arrays.asList(listaLeituraCertaOn).contains(leituras[y]))&&!(Arrays.asList(listaLeituraCertaKun).contains(leituras[y]))&&
                            !(Arrays.asList(respostas).contains(leituras[y]))){
                        escape=true;
                        respostas[x]=leituras[y];
                        Log.e("teste","forLeituras");
                        break;
                    }
                }
                cursor.close();
            }
        }
        respostas[3]=respostasCertas[contador];
        iniciarQuestaoQuiz(respostas,imageId[contador]);
        cursor.close();
    }

    public void iniciarQuestaoQuiz(String[] respostas, int idImagem){
        resetBtnColors();
        respostas = shufleArray(respostas);
        ImagemPergunta.setImageResource(idImagem);
        BtnQuiz1.setText(respostas[0]);
        BtnQuiz2.setText(respostas[1]);
        BtnQuiz3.setText(respostas[2]);
        BtnQuiz4.setText(respostas[3]);
    }



    public String[] shufleArray(String[] respostas){

        List<String> strList = Arrays.asList(respostas);
        Collections.shuffle(strList);
        return strList.toArray(new String[strList.size()]);
    }


    public void iniciarKanaQuiz(){
        if(!(basVarJun.equals("todos"))) {
            cursor = bancoDados.rawQuery("SELECT nome,nome_imagem FROM " + hiraOuKata + " where basico_var_jun= \"" + basVarJun + "\" ORDER BY RANDOM() LIMIT 1;", null);
        }else{cursor = bancoDados.rawQuery("SELECT nome,nome_imagem FROM " + hiraOuKata + " ORDER BY RANDOM() LIMIT 1;", null);


        }
        int indiceColunaNome=cursor.getColumnIndex("nome");
        int indiceColunaImagem=cursor.getColumnIndex("nome_imagem");
        cursor.moveToFirst();
        nomesImagems[contador]= cursor.getString(indiceColunaImagem);
        respostasCertas[contador] = cursor.getString(indiceColunaNome);
        imageId[contador] = getResources().getIdentifier(cursor.getString(indiceColunaImagem), "drawable", this.getPackageName());
        String[] respostas= new String[4];
        cursor.close();
        for(int x=0;x<3;x++){

            boolean escape = false;
            while(escape==false){
                if(!(basVarJun.equals("todos"))) {
                    cursor = bancoDados.rawQuery("SELECT nome from " + hiraOuKata + " where basico_var_jun= \""+basVarJun+"\" ORDER BY RANDOM() LIMIT 1;", null);
                }else{cursor = bancoDados.rawQuery("SELECT nome FROM " + hiraOuKata + " ORDER BY RANDOM() LIMIT 1;", null);


                    }
                int indiceLeituraErradaNome = cursor.getColumnIndex("nome");
                cursor.moveToFirst();
                String leituraNome = cursor.getString(indiceLeituraErradaNome);

                if(!(leituraNome.equals(respostasCertas[contador]))&&!(Arrays.asList(respostas).contains(leituraNome))){
                    escape=true;
                    respostas[x]= leituraNome;
                }

                cursor.close();
            }
        }
        respostas[3]=respostasCertas[contador];
        iniciarQuestaoQuiz(respostas,imageId[contador]);
    }

    public void iniciarVocabQuiz(){

    }

    public void resetBtnColors(){
        BtnQuiz1.getBackground().setColorFilter(getResources().getColor(R.color.colorGrayBtnsQuiz), PorterDuff.Mode.MULTIPLY);
        BtnQuiz2.getBackground().setColorFilter(getResources().getColor(R.color.colorGrayBtnsQuiz), PorterDuff.Mode.MULTIPLY);
        BtnQuiz3.getBackground().setColorFilter(getResources().getColor(R.color.colorGrayBtnsQuiz), PorterDuff.Mode.MULTIPLY);
        BtnQuiz4.getBackground().setColorFilter(getResources().getColor(R.color.colorGrayBtnsQuiz), PorterDuff.Mode.MULTIPLY);
    }

    @OnClick({R.id.BtnQuiz1,R.id.BtnQuiz2,R.id.BtnQuiz3,R.id.BtnQuiz4,R.id.fabNext})
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.fabNext:
                contador++;
                fabNext.setVisibility(View.INVISIBLE);
                resetBtnColors();
                botaoClicado=false;
                if(contador==quantidadeQuiz){
                    Intent intent = new Intent(this,ResultadoQuizActivity.class);
                    intent.putExtra("respostasDadas",respostasDadas);
                    intent.putExtra("respostasCertas",respostasCertas);
                    intent.putExtra("acertouOuErrou",acertouErrou);
                    intent.putExtra("nomesImagems",nomesImagems);
                    intent.putExtra("quantidadeQuiz",quantidadeQuiz);
                    startActivity(intent);
                    finish();
                }else {
                    if (QuizType.equals("Kana")) {
                        iniciarKanaQuiz();
                    } else if (QuizType.equals("Kanji")) {
                        iniciarKanjiQuiz();
                    } else {
                        iniciarVocabQuiz();
                    }
                    atualizarTexto();
                }

                    break;
                default:
                    if(botaoClicado==false){
                        fabNext.setVisibility(View.VISIBLE);
                        botaoClicado=true;
                        respostasDadas[contador] = ((Button) view).getText().toString();
                        if (respostasDadas[contador].equals(respostasCertas[contador])) {
                            acertouErrou[contador] = true;
                            view.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
                        } else {
                                acertouErrou[contador] = false;
                                view.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.MULTIPLY);
                            if(BtnQuiz4.getText().toString().equals(respostasCertas[contador])){
                                BtnQuiz4.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
                            }
                            else if(BtnQuiz3.getText().toString().equals(respostasCertas[contador])){
                                BtnQuiz3.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
                            }
                            else if(BtnQuiz2.getText().toString().equals(respostasCertas[contador])){
                                BtnQuiz2.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
                            }
                            else if(BtnQuiz1.getText().toString().equals(respostasCertas[contador])){
                                BtnQuiz1.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                    break;



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
