package hikari.com.projeto1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //@BindView é um comando da Biblioteca ButterKnife que ajuda a organizar os metodos de click, scroll, select e etc...
    // Link para o site biblioteca ButterKnife: http://jakewharton.github.io/butterknife/
    @BindView(R.id.kanaBtn)
    Button kanaBtn;
    @BindView(R.id.kanjiBtn)
    Button kanjiBtn;
    @BindView(R.id.vocabularioBtn)
    Button vocabularioBtn;
    @BindView(R.id.sobreBtn)
    Button sobreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
    }

    //Metodo onClick, lida com os cliques de cada botão
    //utiliza a função @Bind e @OnClick da biblioteca Butterknife para uma melhor organização de códigos
    @OnClick({R.id.kanaBtn, R.id.kanjiBtn, R.id.vocabularioBtn, R.id.sobreBtn})
    public void onClicked(View v){
    Intent intent;
        switch (v.getId()){
            case R.id.kanaBtn:
                dialogOptions(this,"kanaBtn").show();;

                break;
            case R.id.kanjiBtn:
                dialogOptions(this,"kanjiBtn").show();;
                break;
            case R.id.vocabularioBtn:
                dialogOptions(this,"vocabulario").show();
                break;
            case R.id.sobreBtn:
                intent = new Intent(this, SobreActivity.class);
                changeActivity(intent);
                break;
        }
    }

    //Metodo que lida com a mudança de atividades
    public void changeActivity(Intent intent){
        startActivity(intent);
    }

    //Metódo para criação de alert dialogs
    AlertDialog dialogOptions(Context context,String string){

        switch(string){
            case "kanaBtn":
                return new AlertDialog.Builder(context)
                        .setTitle("Kana")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(R.array.kanaMenu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch(which){
                                    case 0:
                                        intent = new Intent(getApplicationContext(), QuizActivity.class);
                                        intent.putExtra("quizType","Kana");
                                        changeActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getApplicationContext(), KanaActivity.class);
                                        intent.setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        changeActivity(intent);
                                        break;
                                }
                            }
                        })
                        .create();
            case "kanjiBtn":
                return new AlertDialog.Builder(context)
                        .setTitle("Quantas questões deseja?")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(R.array.kanjiMenu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch(which){
                                    case 0:
                                        intent = new Intent(getApplicationContext(), QuizActivity.class);
                                        intent.putExtra("quizType","Kanji");
                                        changeActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getApplicationContext(), KanjiListActivity.class);
                                        changeActivity(intent);
                                        break;
                                    case 2:
                                        intent = new Intent(getApplicationContext(), FormeKanjiActivity.class);
                                        changeActivity(intent);
                                        break;

                                }
                            }
                        })
                        .create();
            case "vocabulario":
                return new AlertDialog.Builder(context)
                        .setTitle("Vocabulário")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(R.array.vocabularioMenu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch(which){
                                    case 0:
                                        intent = new Intent(getApplicationContext(), QuizActivity.class);
                                        intent.putExtra("quizType","Vocabulario");
                                        changeActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getApplicationContext(), VocabularioActivity.class);
                                        changeActivity(intent);
                                        break;
                                }
                            }
                        })
                        .create();

            default:
                return new AlertDialog.Builder(this).create();
        }
    }
}

//SubClasse criada para lidar com o OnItemTouch de todos os RecyclerViews do aplicativo
class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
        return true;
        }

            @Override
            public void onLongPress(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null){
                clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
