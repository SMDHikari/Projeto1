package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class KanjiListActivity extends AppCompatActivity implements Runnable{
    @BindView(R.id.kanjiCapSpn)
    Spinner kanjiCapSpn;
    @BindView(R.id.recyclerKanjiId)
    RecyclerView recyclerView;
    @BindView(R.id.searchTextKanji)
    EditText searchTextKanji;
    private AdaptadorRecyclerViewSection mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemData> arrayKanji;
    ArrayList<ArrayList<ItemData>> arrayKanjisSectioner=new ArrayList<ArrayList<ItemData>>();

    ItemDataFactory factory = new ItemDataFactory();
    Boolean textChanged;
    int OptionSpn1=0;
    private SQLiteDatabase bancoDados;
    Cursor cursor;
    Resources r;
    //int[] kanjiList;
    //int[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_list);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        r=getResources();

        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }

        //kanjiList=new int[]{R.array.KanjiCapitulo1,R.array.KanjiCapitulo2,R.array.KanjiCapitulo3,R.array.KanjiCapitulo4,R.array.KanjiCapitulo5};
        //imgs kanji
        //imgs = new int[]{R.array.KanjiCapitulo1Imgs,R.array.KanjiCapitulo2Imgs,R.array.KanjiCapitulo3Imgs,R.array.KanjiCapitulo4Imgs,R.array.KanjiCapitulo5Imgs};
        iniciarRecycler();


        /*Implementação do ClickListener (não existe naturalmente no RecyclerView)
        * Resto do código da implementação, estão na interface ClickListener e na innerClass
        * RecyclerTouchListener no MainActivity.*/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //On Click recebe id do view clicado
                    int teste = ((AdaptadorRecyclerViewSection)recyclerView.getAdapter()).getSectionCount();
                    int contador =0;
                    int idClicado=0;
                    for(int x=0;x<teste;x++){
                        for(int y=0;y<arrayKanjisSectioner.get(x).size();y++){
                            contador++;
                            if(contador==position-x){
                                idClicado=arrayKanjisSectioner.get(x).get(y).getID();
                                break;
                            }
                        }

                    }

                    Intent intent = new Intent(getApplicationContext(), KanjiActivity.class);
                    //intent.putExtra("capPosition",OptionSpn1);
                    intent.putExtra("kanjiClicado",idClicado);
                    startActivity(intent);

            }


            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        searchTextKanji.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(searchTextKanji.getText().toString().equals(""))){
                    textChanged=true;
                    searchFromDB(searchTextKanji.getText().toString());
                }else{
                    textChanged=false;
                    atualizarLista();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void iniciarRecycler(){
        mLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        atualizarLista();
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //Definindo Spinners e inicializando valores do spinner
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(   this,
                R.array.kanjiSpinner1, R.layout.spinner_item_top);
        // Specificando layout do dropdown
        adapterSpn1.setDropDownViewResource(R.layout.spinner_item_dropdown);


        // Aplicando o adaptador no spinner
        kanjiCapSpn.setAdapter(adapterSpn1);
        runLayoutAnimation(recyclerView);


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

    public void atualizarLista(){
        //caso o ArrayKanji já exista, efetua-se uma limpeza dos dados e da memória antes de reinserir os valores neste.
        String[] StringDivisao=new String[0];
        int[] tracosDivisao=new int[0];
        int capitulo = OptionSpn1+1;
        for(int contador=0;contador<arrayKanjisSectioner.size();contador++) {
            arrayKanjisSectioner.get(contador).clear();
        }
        arrayKanjisSectioner.clear();
        int indiceColunaTrad,indiceColunaTracos,indiceColunaID,indiceColunaImg;
                arrayKanji=null;
        Runtime.getRuntime().gc();
        //Usar typedarray para os strings também.
        //TypedArray kanjiTyped= getResources().obtainTypedArray(kanjiList[escolhaSpinner1]);
        //TypedArray imgsTyped = getResources().obtainTypedArray(getResources().obtainTypedArray(imgs[escolhaSpinner1]).getResourceId(0,0));

            cursor = bancoDados.rawQuery("SELECT DISTINCT tracos FROM kanji where capitulo_kanji ="+capitulo, null);
            cursor.moveToFirst();
            int indiceContagemTracos=cursor.getColumnIndex("tracos");

            cursor.moveToFirst();
            tracosDivisao = new int[cursor.getCount()];
            cursor.moveToFirst();

            try{
            while(cursor!=null)
            {
                tracosDivisao[cursor.getPosition()]=cursor.getInt(indiceContagemTracos);
                cursor.moveToNext();
            }
            }catch(Exception e){
                e.printStackTrace();

            }

            cursor.moveToFirst();
            Arrays.sort(tracosDivisao);
            for(int i=0;i<tracosDivisao.length;i++){

                cursor=bancoDados.rawQuery("SELECT id_kanji,traducao, tracos,kanji_imagem FROM kanji WHERE tracos ="+tracosDivisao[i]+" AND capitulo_kanji ="+capitulo,null);


                //int indiceCapitulo = cursor.getColumnIndex("capitulo_kanji");
                indiceColunaTrad=cursor.getColumnIndex("traducao");
                indiceColunaTracos=cursor.getColumnIndex("tracos");
                indiceColunaID=cursor.getColumnIndex("id_kanji");
                indiceColunaImg=cursor.getColumnIndex("kanji_imagem");
                cursor.moveToFirst();

                try{
                    arrayKanji=new ArrayList<ItemData>();

                  for(int x=0;x<cursor.getCount();x++){
                      int imageId = getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
                      String[] titulo =cursor.getString(indiceColunaTrad).split(",");
                      ItemData itemData= factory.getItemData("Kan");
                      ((KanItemData)itemData).iniciar(titulo[0]
                              ,imageId
                              ,cursor.getInt(indiceColunaTracos)
                              ,cursor.getInt(indiceColunaID));
                      arrayKanji.add(itemData);
                      cursor.moveToNext();
                  }
                }catch (Exception e){
                 e.printStackTrace();



                }
                arrayKanjisSectioner.add(new ArrayList<ItemData>(arrayKanji));
                arrayKanji=new ArrayList<ItemData>();
                cursor.close();
            }

        StringDivisao= new String[tracosDivisao.length];
        for(int i=0;i<tracosDivisao.length;i++){
            StringDivisao[i]=String.valueOf(tracosDivisao[i])+" Traços:";
        }
        mAdapter= new AdaptadorRecyclerViewSection(arrayKanjisSectioner,StringDivisao,getApplicationContext());
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        //imgsTyped.recycle();
        //kanjiTyped.recycle();
        Runtime.getRuntime().gc();

    }

    public void searchFromDB(String busca){
        if(arrayKanji!=null){
            for(int i=0;i<arrayKanji.size();i++){
                arrayKanji.get(i).clearMemory();
            }
        }
        if(arrayKanjisSectioner!=null){
            for(int contador =0;contador<arrayKanjisSectioner.size();contador++){
                arrayKanjisSectioner.get(contador).clear();
            }
        }
        arrayKanjisSectioner.clear();
        arrayKanji.clear();


            //Inicializa o cursor para se fazer uma busca com o valor da caixa de texto
            String buscaRecebida="SELECT id_kanji,traducao, tracos,kanji_imagem FROM kanji WHERE traducao LIKE \""+busca+"%\" order by traducao";
            cursor=bancoDados.rawQuery(buscaRecebida,null);

            cursor.moveToFirst();

            int indiceColunaTrad=cursor.getColumnIndex("traducao");
            int indiceColunaTracos=cursor.getColumnIndex("tracos");
            int indiceColunaID=cursor.getColumnIndex("id_kanji");
            int indiceColunaImg=cursor.getColumnIndex("kanji_imagem");
            cursor.moveToFirst();

        try{
            arrayKanji=new ArrayList<ItemData>();

            for(int x=0;x<cursor.getCount();x++){
                int imageId = getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
                String[] titulo =cursor.getString(indiceColunaTrad).split(",");
                ItemData itemData= factory.getItemData("Kan");
                ((KanItemData)itemData).iniciar(titulo[0]
                        ,imageId
                        ,cursor.getInt(indiceColunaTracos)
                        ,cursor.getInt(indiceColunaID));
                arrayKanji.add(itemData);
                cursor.moveToNext();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
            arrayKanjisSectioner.add(new ArrayList<ItemData>(arrayKanji));
            arrayKanji=new ArrayList<ItemData>();
            cursor.close();

            arrayKanjisSectioner.add(arrayKanji);
            mAdapter= new AdaptadorRecyclerViewSection(arrayKanjisSectioner);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            runLayoutAnimation(recyclerView);
            //limpa a variavel imgsTyped no fim da atualização da lista
            //imgsTyped.recycle();
            //kanjiTyped.recycle();
            Runtime.getRuntime().gc();
    }



    @OnItemSelected(R.id.kanjiCapSpn)
    public void onItemSelected( Spinner spinner, int thisPosition) {
        OptionSpn1=thisPosition;
        searchTextKanji.setText("");
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
        kanjiCapSpn=null;
        Runtime.getRuntime().gc();
        Resources r;
    }

    //  Metodo runnable, mantem um controle da variavel arrayKanji para controlar a memória enquanto a atividade está rodando.
    @Override
    public void run(){

       /* for(int i=0;i<arrayKanji.size();i++){
            arrayKanji.get(i).clearMemory();
        }
        Runtime.getRuntime().gc();*/
    }

}
