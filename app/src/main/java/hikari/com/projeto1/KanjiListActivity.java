package hikari.com.projeto1;

import android.content.res.Resources;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KanjiListActivity extends AppCompatActivity{
    @BindView(R.id.kataAndHiraSpn)
    Spinner kataAndHiraSpn;
    @BindView(R.id.levelKanaSpn)
    Spinner levelKanaSpn;
    @BindView(R.id.recyclerKanaId)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemData> arrayKanas;
    int OptionSpn1=0,OptionSpn2=0;
    Resources r=getResources();
    private int[][] kanjiList={r.getIntArray(R.array.KanjiCapitulo1),r.getIntArray(R.array.KanjiCapitulo2),r.getIntArray(R.array.KanjiCapitulo3),r.getIntArray(R.array.KanjiCapitulo4),r.getIntArray(R.array.KanjiCapitulo5)};
    //imgs kanji
    private int[]/*[]*/ imgs ={R.array.placeholder,R.array.hiraBasicoImg,R.array.hiraVarImg,R.array.hiraJunImg};/*,{R.array.placeholder,R.array.kataBasicoImg,R.array.kataVarImg,R.array.kataJunImg}};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        iniciarRecycler();

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
    }

    public void iniciarRecycler(){
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
        kataAndHiraSpn.setAdapter(adapterSpn1);
        levelKanaSpn.setAdapter(adapterSpn2);
    }

    public void atualizarLista(int escolhaSpinner1, int escolhaSpinner2){
        arrayKanas=new ArrayList<ItemData>();
        ArrayList<int[]> todos= new ArrayList<int[]>();
        TypedArray imgsTyped;
        if(escolhaSpinner2==0){
            for(int i=0;i< imgs/*[escolhaSpinner1]*/.length;i++){
                imgsTyped=getResources().obtainTypedArray(imgs/*[escolhaSpinner1]*/[i]);
                for(int x=0;x<getResources().getIntArray(imgs/*[escolhaSpinner1]*/[i]).length;x++){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imgsTyped.getResourceId(x,-1));
                    //arrayKanas.add(new KanaItemData(getResources().getStringArray(kanjiList[escolhaSpinner1][i])[x],bitmap));
                }
            }
        }
        else{
            imgsTyped = getResources().obtainTypedArray(imgs/*[escolhaSpinner1]*/[escolhaSpinner2]);
            for(int i=0;i<getResources().getStringArray(kanjiList[escolhaSpinner1][escolhaSpinner2]).length;i++){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imgsTyped.getResourceId(i,-1));
                //arrayKanas.add(new KanaItemData(getResources().getStringArray(kanjiList[escolhaSpinner1][escolhaSpinner2])[i],bitmap));
            }
        }

        mAdapter= new AdaptadorRecycler(arrayKanas);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);

    }

}
