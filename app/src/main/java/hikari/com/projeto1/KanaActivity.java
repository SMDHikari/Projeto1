package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class KanaActivity extends AppCompatActivity{
    @BindView(R.id.kataAndHiraSpn)
    Spinner kataAndHiraSpn;
    @BindView(R.id.levelKanaSpn)
    Spinner levelKanaSpn;
    @BindView(R.id.recyclerKanaId)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemData> arrayKanas=null;
    int OptionSpn1=0,OptionSpn2=0;

    private int[][] kanaList={{R.array.placeholder,R.array.hiraBasico,R.array.hiraVar,R.array.hiraJun},{R.array.placeholder,R.array.kataBasico,R.array.kataVar,R.array.kataJun}};

    private int[]/*[]*/ imgs ={R.array.placeholder,R.array.hiraBasicoImg,R.array.hiraVarImg,R.array.hiraJunImg};/*,{R.array.placeholder,R.array.kataBasicoImg,R.array.kataVarImg,R.array.kataJunImg}};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kana);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        mLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(true);
        atualizarLista(OptionSpn1,OptionSpn2);
        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(this,
                R.array.kanaSpinner1, R.layout.spinner_item_top);
        ArrayAdapter<CharSequence> adapterSpn2 = ArrayAdapter.createFromResource(this,
                R.array.kanaSpinner2, R.layout.spinner_item_top);
        // Specificando layout do dropdown
        adapterSpn1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterSpn2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        // Aplicando o adaptador no spinner
        kataAndHiraSpn.setAdapter(adapterSpn1);
        levelKanaSpn.setAdapter(adapterSpn2);
        runLayoutAnimation(recyclerView);



        /*Implementação do ClickListener (não existe naturalmente no RecyclerView)
        * Resto do código da implementação, estão na interface ClickListener e na innerClass
        * RecyclerTouchListener no MainActivity.*/

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //On Click baseado na posição
                Toast.makeText(getApplicationContext(), "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        //limpa as variaveis que não serão mais utilizadas após a criação
        adapterSpn1=null;
        adapterSpn2=null;
        Runtime.getRuntime().gc();
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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

        arrayKanas=new ArrayList<ItemData>();
        TypedArray imgsTyped;

        if(escolhaSpinner2==0){
            for(int i=0;i< imgs/*[escolhaSpinner1]*/.length;i++){
                imgsTyped = getResources().obtainTypedArray(imgs[i]);
                for(int x=0;x<getResources().getIntArray(imgs/*[escolhaSpinner1]*/[i]).length;x++){
                    //.getResourceId(x,0)
                    arrayKanas.add(new KanaItemData(getResources().getStringArray(kanaList[escolhaSpinner1][i])[x],imgsTyped.getResources().getDrawable(imgsTyped.getResourceId(x,-1))));
                }

                imgsTyped.recycle();
                imgsTyped=null;
            }
        }
        else{
            imgsTyped = getResources().obtainTypedArray(imgs[escolhaSpinner2]);
            for(int i=0;i<getResources().getStringArray(kanaList[escolhaSpinner1][escolhaSpinner2]).length;i++){
                arrayKanas.add(new KanaItemData(getResources().getStringArray(kanaList[escolhaSpinner1][escolhaSpinner2])[i],imgsTyped.getResources().getDrawable(imgsTyped.getResourceId(i,-1))));
            }
            //
            imgsTyped.recycle();
            imgsTyped=null;
        }

        mAdapter= new AdaptadorRecycler(arrayKanas,true);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        Runtime.getRuntime().gc();

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


    @OnItemSelected({R.id.kataAndHiraSpn,R.id.levelKanaSpn})
    public void onItemSelected( Spinner spinner, int position) {
        switch(spinner.getId()){
            case R.id.kataAndHiraSpn:
                OptionSpn1=position;
                atualizarLista(OptionSpn1,OptionSpn2);
                break;
            case R.id.levelKanaSpn:
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
        kataAndHiraSpn.setOnItemSelectedListener(null);
        levelKanaSpn.setOnItemSelectedListener(null);
        for(int i=0;i<arrayKanas.size();i++){
            arrayKanas.get(i).clearMemory();
        }
        arrayKanas=null;
        Runtime.getRuntime().gc();
    }

}
