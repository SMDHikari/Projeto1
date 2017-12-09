package hikari.com.projeto1;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormeKanjiActivity extends AppCompatActivity {
    @BindView(R.id.formeGrid) GridView formeGrid;
    @BindView(R.id.formeImage) ImageView formeImage;
    @BindView(R.id.TituloForme) TextView TituloForme;
    @BindView(R.id.quantidadeTracosText) TextView quantidadeTracosText;
    @BindView(R.id.dicaBtn) Button dicaBtn;

    Cursor cursor;
    private SQLiteDatabase bancoDados;
    ItemDataFactory factory = new ItemDataFactory();

    boolean teste=false;
    int[] imagesIds;
    int posicaoAtual=0,posicaoDrag=-1;
    int[] sequenciaCorretaIds,sequenciaImgsFormeIds;
    Drawable[] tracosImgs;
    String[] listaTracos= {"traco01","traco02","traco03","traco04","traco05","traco06","traco07","traco08","traco09","traco10","traco11","traco12","traco13","traco14","traco15","traco16","traco17","traco18","traco19","traco20","traco21","traco22","traco23","traco24","traco25","traco26","traco27","traco28","traco29","traco30","traco31","traco32","traco33","traco34","traco35"};
    String[] sequenciaImgsForme,sequenciaCorreta;
    View decorView;
    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forme_kanji);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        String Traducao;
        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        cursor= bancoDados.rawQuery("select traducao,sequencia_formekanji,sequencia_correta_forme from kanji ORDER BY RANDOM() LIMIT 1",null);

        int idSequenciaCorreta=cursor.getColumnIndex("sequencia_correta_forme");
        int idSequenciaForm=cursor.getColumnIndex("sequencia_formekanji");
        int idTrad= cursor.getColumnIndex("traducao");


        cursor.moveToFirst();
        Traducao= cursor.getString(idTrad);
        TituloForme.setText(Traducao);
        sequenciaCorreta=cursor.getString(idSequenciaCorreta).split(",");
        sequenciaImgsForme=cursor.getString(idSequenciaForm).split(",");
        sequenciaCorretaIds = new int[sequenciaCorreta.length];
        sequenciaImgsFormeIds= new int[sequenciaImgsForme.length];
        atualizarText();
        for(int i =0;i<sequenciaCorreta.length;i++){
            sequenciaCorretaIds[i]=getResources().getIdentifier(sequenciaCorreta[i],"drawable",this.getPackageName());
        }
        for(int i=0;i<sequenciaImgsForme.length;i++){
            sequenciaImgsFormeIds[i]=getResources().getIdentifier(sequenciaImgsForme[i],"drawable",this.getPackageName());
        }
        tracosImgs = new Drawable[listaTracos.length];
        for(int i=0;i<listaTracos.length;i++) {
            int idTracoAtual =getResources().getIdentifier(listaTracos[i],"drawable",this.getPackageName());
            tracosImgs[i]=getResources().getDrawable(idTracoAtual);
        }

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext(),140);
        formeGrid.setNumColumns(mNoOfColumns);
        //formeGrid.setNumColumns(4);
        formeGrid.setAdapter(new ButtonAdapter(this,tracosImgs,listaTracos));


        formeGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                posicaoDrag=position;
                view.startDrag(data, shadow, null, 0);
                return  false;
            }
        });




        formeImage.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
// TODO Auto-generated method stub
                final int action = event.getAction();

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        teste=false;
// Executed after startDrag() is called.
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        Log.e("teste","entrou");
                        // Executed after the Drag Shadow enters the drop area
                        break;
                    case DragEvent.ACTION_DROP: {
                        //Executed when user drops the data
                        return (true);
                    }

                    case DragEvent.ACTION_DRAG_ENDED: {
                            if (listaTracos[posicaoDrag].equals(sequenciaCorreta[posicaoAtual])) {
                                formeImage.setImageResource(sequenciaImgsFormeIds[posicaoAtual]);
                                teste = true;
                                posicaoAtual++;
                                atualizarText();
                            }else{
                                if(teste==false){
                                formeImage.setBackgroundColor(getResources().getColor(R.color.teste));
                                new CountDownTimer(150, 100) {

                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        formeImage.setBackground(getResources().getDrawable(R.drawable.quadrado));
                                    }
                                }.start();
                                }
                            }



                        return (true);
                    }
                    default:
                        break;
                }
                return true;
            }
        });



    }
    public void atualizarText(){
        quantidadeTracosText.setText("traço "+(posicaoAtual)+" de "+sequenciaCorreta.length);
    }
    @Override
    public void onResume() {

        super.onResume();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @OnClick(R.id.dicaBtn)
    public void onClick(View view){
        if(posicaoAtual<sequenciaImgsForme.length){
        formeImage.setImageResource(sequenciaImgsFormeIds[posicaoAtual]);
        posicaoAtual++;
        atualizarText();}
    }
}
