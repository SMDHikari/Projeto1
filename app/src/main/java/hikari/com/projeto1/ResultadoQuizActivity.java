package hikari.com.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoQuizActivity extends AppCompatActivity {
    @BindView(R.id.resultadoList)
    ListView resultadoList;
    ArrayList<quizQuestions> questoes;
    @BindView(R.id.textAcertos)
    TextView textAcertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_quiz);
        Intent intent= this.getIntent();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        ButterKnife.bind(this);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbar_title =  findViewById(R.id.toolbar_title);
        toolbar_title.setText("Resultado Quiz");









        int quantidadeQuiz= intent.getIntExtra("quantidadeQuiz",0);
        String[] respostasCertas = intent.getStringArrayExtra("respostasCertas");
        String[] respostasDadas = intent.getStringArrayExtra("respostasDadas");
        String[] nomesImagems=intent.getStringArrayExtra("nomesImagems");

        int contadorAcertos=0;
        for(int i=0;i<respostasDadas.length;i++){
            if(respostasDadas[i].equals(respostasCertas[i])){
                contadorAcertos++;
            }
        }
        textAcertos.setText("Acertos: "+contadorAcertos+"\nErros: "+(quantidadeQuiz-contadorAcertos));
        questoes= new ArrayList<quizQuestions>();
        int idImagem;



        for(int i = 0;i<quantidadeQuiz;i++){
            idImagem= getResources().getIdentifier(nomesImagems[i], "drawable", this.getPackageName());
            questoes.add(new quizQuestions(respostasDadas[i],respostasDadas[i].equals(respostasCertas[i]),idImagem));
        }

        AdaptadorSimpleListView adapter =
                new AdaptadorSimpleListView(questoes, this);
        resultadoList.setAdapter(adapter);

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
