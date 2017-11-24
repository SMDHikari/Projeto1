package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class KanjiListActivity extends AppCompatActivity implements Runnable{
    @BindView(R.id.kanjiCapSpn)
    Spinner kanjiCapSpn;
    @BindView(R.id.kanjiOrdemSpn)
    Spinner kanjiOrdemSpn;
    @BindView(R.id.recyclerKanjiId)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<KanjiItemData> arrayKanas;
    int OptionSpn1=0,OptionSpn2=0;
    Resources r;
    int[] kanjiList;
    int[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        r=getResources();

        kanjiList=new int[]{R.array.KanjiCapitulo1,R.array.KanjiCapitulo2,R.array.KanjiCapitulo3,R.array.KanjiCapitulo4,R.array.KanjiCapitulo5};
        //imgs kanji
        imgs = new int[]{R.array.KanjiCapitulo1Imgs,R.array.KanjiCapitulo2Imgs,R.array.KanjiCapitulo3Imgs,R.array.KanjiCapitulo4Imgs,R.array.KanjiCapitulo5Imgs};
        iniciarRecycler(imgs, kanjiList);


        /*Implementação do ClickListener (não existe naturalmente no RecyclerView)
        * Resto do código da implementação, estão na interface ClickListener e na innerClass
        * RecyclerTouchListener no MainActivity.*/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //On Click baseado na posição
                Intent intent = new Intent(getApplicationContext(), KanjiActivity.class);
                intent.putExtra("kanjiClicado",position);
                intent.putExtra("capPosition",OptionSpn1);
                startActivity(intent);
            }


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

    public void iniciarRecycler(int[] imgs, int[]kanjiList){
        mLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        atualizarLista(OptionSpn1,OptionSpn2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(   this,
                R.array.kanjiSpinner1, R.layout.spinner_item_top);
        ArrayAdapter<CharSequence> adapterSpn2 = ArrayAdapter.createFromResource(this,
                R.array.kanjiSpinner2, R.layout.spinner_item_top);
        // Specificando layout do dropdown
        adapterSpn1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterSpn2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        // Aplicando o adaptador no spinner
        kanjiCapSpn.setAdapter(adapterSpn1);
        kanjiOrdemSpn.setAdapter(adapterSpn2);
        runLayoutAnimation(recyclerView);
    }

    public void atualizarLista(int escolhaSpinner1, int escolhaSpinner2){
        //caso o ArrayKanas já exista, efetua-se uma limpeza dos dados e da memória antes de reinserir os valores neste.
        if(arrayKanas!=null){
            for(int i=0;i<arrayKanas.size();i++){
                arrayKanas.get(i).clearMemory();
            }
        }
        arrayKanas=null;
        Runtime.getRuntime().gc();

        arrayKanas=new ArrayList<KanjiItemData>();
        //Usar typedarray para os strings também.
        TypedArray kanjiTyped= getResources().obtainTypedArray(kanjiList[escolhaSpinner1]);
        TypedArray imgsTyped = getResources().obtainTypedArray(imgs[escolhaSpinner1]);
        for(int i=0;i<getResources().getStringArray(kanjiList[escolhaSpinner1]).length;i++){
            String[] titulo =getResources().getStringArray(kanjiTyped.getResourceId(i,0))[0].split(",");
            arrayKanas.add(new KanjiItemData(titulo[0],imgsTyped.getResources().getDrawable(imgsTyped.getResourceId(i,0)),Integer.parseInt(getResources().getStringArray(kanjiTyped.getResourceId(i,0))[3])));

        }
        //limpeza de memória do imgsTyped

        if(OptionSpn2==1){
            Collections.sort(arrayKanas);
        }
        mAdapter= new AdaptadorRecycler(cast(arrayKanas),true);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        imgsTyped.recycle();
        kanjiTyped.recycle();
        Runtime.getRuntime().gc();

    }

    public static <KanjiItemData> ArrayList<ItemData> cast(ArrayList list) {
        return list;
    }



    @Override
    public Intent getSupportParentActivityIntent() {
    /*String from = getIntent().getExtras().getString("from");
    Intent newIntent = null;
    if(from.equals("MAIN")){
        newIntent = new Intent(this, MainActivity.class);
    }else if(from.equals("FAV")){
        newIntent = new Intent(this, FavoriteActivity.class);
    }

    return newIntent;*/
        finish();
        return null;
    }


    @OnItemSelected({R.id.kanjiCapSpn,R.id.kanjiOrdemSpn})
    public void onItemSelected( Spinner spinner, int position) {
        switch(spinner.getId()){
            case R.id.kanjiCapSpn:
                OptionSpn1=position;
                atualizarLista(OptionSpn1,OptionSpn2);
                break;
            case R.id.kanjiOrdemSpn:
                OptionSpn2=position;
                atualizarLista(OptionSpn1,OptionSpn2);
                break;
        }
    }
    //Metodo chamado ao sair da atividade(nativo da classe Activity)
    @Override
    public void onDestroy(){
        clearMemory();
        super.onDestroy();
    }
    //Metodo chamado para limpar com o garbage Collector todas as váriaveis e objetos da atividade.
    public void clearMemory(){
        mAdapter=null;
        recyclerView.addOnItemTouchListener(null);
        recyclerView.setLayoutManager(null);
        recyclerView=null;
        kanjiCapSpn.setOnItemSelectedListener(null);
        kanjiOrdemSpn.setOnItemSelectedListener(null);
        kanjiOrdemSpn=null;
        kanjiCapSpn=null;
        for(int i=0;i<arrayKanas.size();i++){
            arrayKanas.get(i).clearMemory();
        }
        arrayKanas=null;
        Runtime.getRuntime().gc();
        Resources r;
    }

    //  Metodo runnable, mantem um controle da variavel arrayKanas para controlar a memória enquanto a atividade está rodando.
    @Override
    public void run(){

        for(int i=0;i<arrayKanas.size();i++){
            arrayKanas.get(i).clearMemory();
        }
        Runtime.getRuntime().gc();
    }

}
