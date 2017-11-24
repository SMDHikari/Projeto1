package hikari.com.projeto1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {
    @BindView(R.id.questionQuizImg)
    ImageView questionQuizImg;
    @BindView(R.id.optionsLst)
    ListView optionsLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        String QuizType = intent.getStringExtra("quizType");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        askHowManyQuestions(this).show();




    }

    AlertDialog askHowManyQuestions(Context context){
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        return new AlertDialog.Builder(context)
                .setTitle("Quantas questões deseja?")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(input)
                .setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(String.valueOf(input.getText())!=""){
                            int numero = Integer.parseInt(input.getText().toString());
                            if(numero==0){

                            }
                        }
                    }
                })
                .create();

    }
}
