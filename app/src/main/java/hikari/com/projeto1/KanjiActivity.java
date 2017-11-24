package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KanjiActivity extends AppCompatActivity {

    @BindView(R.id.recyclerKanjiId)
    RecyclerView recyclerView;
    ArrayList<ItemData> arrayKanji= new ArrayList<ItemData>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.kanjiImg)
    ImageView kanjiImg;
    @BindView(R.id.kanjiTraducao)
    TextView kanjiTraducao;
    @BindView(R.id.kunYomiTxt)
    TextView kunYomiTxt;
    @BindView(R.id.onYomiTxt)
    TextView onYomiTxt;
    @BindView(R.id.tracosTxt)
    TextView tracosTxt;
    @BindView(R.id.exemploTxt)
    TextView exemploTxt;
    @BindView(R.id.traducaoExemploTxt)
    TextView traducaoExemploTxt;

    TypedArray kanjiTyped;
    int[] kanjiList;
    int[] kanjiImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int position = intent.getIntExtra("kanjiClicado",0);
        int capPosition = intent.getIntExtra("capPosition",0);

        kanjiList = new int[]{R.array.KanjiCapitulo1,R.array.KanjiCapitulo2,R.array.KanjiCapitulo3,R.array.KanjiCapitulo4,R.array.KanjiCapitulo5};
        kanjiImgs = new int[]{R.array.KanjiCapitulo1Imgs,R.array.KanjiCapitulo2Imgs,R.array.KanjiCapitulo3Imgs,R.array.KanjiCapitulo4Imgs,R.array.KanjiCapitulo5Imgs};


        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        kanjiTyped= getResources().obtainTypedArray(kanjiList[capPosition]);
        TypedArray imgsTyped = getResources().obtainTypedArray(kanjiImgs[capPosition]);

        for (int i = 0; i < kanjiTyped.length(); i++) {
            String[] titulo = getResources().getStringArray(kanjiTyped.getResourceId(i,0))[0].split(",");
            arrayKanji.add(new KanjiItemData(titulo[0],imgsTyped.getResources().getDrawable(imgsTyped.getResourceId(i,0)),Integer.parseInt(getResources().getStringArray(kanjiTyped.getResourceId(i,0))[3])));;
        }
        mAdapter = new AdaptadorRecycler(arrayKanji,false);
        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        runLayoutAnimation(recyclerView);

        //Inicializando a tela com os valores do primeiro
        preencherTextos(position);


        //Adicionado a o onTouch ao recyclerView, pois o mesmo não o contém no padrão.
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Recebe os valores de acordo com a posição clicada, e envia para o metodo preencherTextos()
               preencherTextos(position);
            }

            //caso seja necessária alguma função de longClick.
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void preencherTextos(int position){
        String[] arrayString = getResources().getStringArray(kanjiTyped.getResourceId(position,0));
        kanjiImg.setImageDrawable(arrayKanji.get(position).getImage());
        String[] recebeTexto = arrayString[0].split(",");
        String textoTraduz="";
        for(int i = 0;i<recebeTexto.length;i++){
            textoTraduz += (i+1)+". "+recebeTexto[i]+"\n";
        }
        kanjiTraducao.setText(textoTraduz);
        onYomiTxt.setText(arrayString[1]);
        kunYomiTxt.setText(arrayString[2]);
        tracosTxt.setText(arrayString[3]);
        exemploTxt.setText(arrayString[4]);
        traducaoExemploTxt.setText(arrayString[5]);

        textoTraduz=null;
        recebeTexto=null;
        arrayString=null;
        Runtime.getRuntime().gc();
    }

    @Override
    public void onDestroy(){
        clearMemory();
        super.onDestroy();
    }

    public void clearMemory(){
        mAdapter=null;
        recyclerView.addOnItemTouchListener(null);
        recyclerView.setLayoutManager(null);
        recyclerView=null;
        kanjiTyped=null;
        kanjiList=null;
        kanjiImgs=null;
        kanjiImg=null;
        for(int i =0;i<arrayKanji.size();i++){
            arrayKanji.get(i).clearMemory();
        }
        //limpeza textos
        kanjiTraducao=null;
        kunYomiTxt=null;
        onYomiTxt=null;
        tracosTxt=null;
        exemploTxt=null;
        traducaoExemploTxt=null;

        Runtime.getRuntime().gc();

    }




}
