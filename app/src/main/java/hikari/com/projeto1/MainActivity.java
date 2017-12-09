package hikari.com.projeto1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    boolean botãoClicado=false;
    Intent intent;
    String posicaoClick;
    int posicaoDialogOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
    }

    //Metodo onClick, lida com os cliques de cada botão
    //utiliza a função @Bind e @OnClick da biblioteca Butterknife para uma melhor organização de códigos
    @OnClick({R.id.kanaBtn, R.id.kanjiBtn, R.id.vocabularioBtn, R.id.sobreBtn})
    public void onClicked(View v){
        Intent menuIntent;
        Intent intent;
        if(botãoClicado==false) {
            switch (v.getId()) {

                case R.id.kanaBtn:
                    menuIntent = new Intent(this, dialogActivity.class);
                    menuIntent.putExtra("botaoClicado", 0);
                    menuIntent.putExtra("titulo", "Kana");
                    posicaoClick = "kanaBtn";
                    startActivityForResult(menuIntent, 1);
                    break;

                case R.id.kanjiBtn:
                    menuIntent = new Intent(this, dialogActivity.class);
                    menuIntent.putExtra("botaoClicado", 1);
                    menuIntent.putExtra("titulo", "Kanji");
                    posicaoClick = "kanjiBtn";
                    startActivityForResult(menuIntent, 1);
                    break;

                case R.id.vocabularioBtn:
                    Toast.makeText(this,"Desativado",Toast.LENGTH_SHORT).show();
                    /*menuIntent = new Intent(this, dialogActivity.class);
                    menuIntent.putExtra("botaoClicado", 2);
                    menuIntent.putExtra("titulo", "Vocabulário");
                    posicaoClick = "vocabularioBtn";
                    startActivityForResult(menuIntent, 1);*/

                    break;
                case R.id.sobreBtn:
                    menuIntent = new Intent(this, SobreActivity.class);
                    changeActivity(menuIntent);
                    break;
            }
        }

    }

    //Metodo que lida com a mudança de atividades
    public void changeActivity(Intent intent){
        startActivity(intent);
    }

    //Metódo para criação de alert dialogs
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            int teste = data.getIntExtra("posicaoClicado",1);
            posicaoDialogOption = data.getIntExtra("clicadoOption", 0);
            intent = ((dialogItemData) data.getExtras().getParcelable("clicadoOption")).getIntent();
            if( data.hasExtra("QuizType")){
                Toast.makeText(this,data.getStringExtra("QuizType"),Toast.LENGTH_SHORT).show();
                intent.putExtra("QuizType",data.getStringExtra("QuizType"));
            }
            changeActivity(intent);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
public void onResume() {
    super.onResume();
    botãoClicado=false;
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
