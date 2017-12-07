package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class KanaListActivity extends AppCompatActivity{
    @BindView(R.id.kanaSearchText)
    EditText kanaSearchText;
    @BindView(R.id.kataAndHiraSpn)
    Spinner kataAndHiraSpn;
    @BindView(R.id.levelKanaSpn)
    Spinner levelKanaSpn;
    @BindView(R.id.recyclerKanaId)
    RecyclerView recyclerView;
    private AdaptadorRecyclerViewSection mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemData> arrayKanas;
    ArrayList<ArrayList<ItemData>> arrayKanaSectioner=new ArrayList<ArrayList<ItemData>>();
    private SQLiteDatabase bancoDados;
    Cursor cursor;
    int OptionSpn1=0,OptionSpn2=0;


    //private int[][] kanaList;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    //private int[][] imgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kana_list);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        //imgs = new int[][]{{R.array.hiraBasicoImg,R.array.hiraVarImg,R.array.hiraJunImg},{R.array.kataBasicoImg,R.array.kataVarImg,R.array.kataJunImg}};
        //kanaList = new int[][]{{R.array.hiraBasico,R.array.hiraVar,R.array.hiraJun},{R.array.kataBasico,R.array.kataVar,R.array.kataJun}};
        iniciarRecycler();


        /*Implementação do ClickListener (não existe naturalmente no RecyclerView)
        * Resto do código da implementação, estão na interface ClickListener e na innerClass
        * RecyclerTouchListener no MainActivity.*/

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //On Click baseado na posição
                if(view.isClickable()){
                    int teste = ((AdaptadorRecyclerViewSection)recyclerView.getAdapter()).getSectionCount();
                    int contador =0;
                    int idClicado=0;
                    for(int x=0;x<teste;x++){
                        for(int y=0;y<arrayKanaSectioner.get(x).size();y++){
                            contador++;
                            if(contador==position-x){
                                idClicado=arrayKanaSectioner.get(x).get(y).getID();
                                break;
                            }
                        }

                    }
                    Intent intent = new Intent(getApplicationContext(), KanaActivity.class);
                    //intent.putExtra("capPosition",OptionSpn1);
                    intent.putExtra("kanaSpn1",OptionSpn1);
                    intent.putExtra("kanaClicado",idClicado);
                    intent.putExtra("kanaSpn2",OptionSpn2);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),String.valueOf(idClicado),Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        //limpa as variaveis que não serão mais utilizadas após a criação
        Runtime.getRuntime().gc();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    public void iniciarRecycler(){
        mLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        atualizarLista(OptionSpn1,OptionSpn2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.getRecycledViewPool().clear();



        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(   this,
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

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    public void atualizarLista(int escolhaSpinner1, int escolhaSpinner2){
        String[] divisorContagem,StringDivisao=new String[]{"basico","variavel","juncao"};
        String hiraOuKata="";
        switch(escolhaSpinner1){
            case 0:
                hiraOuKata="hiragana";
                break;
            case 1:
                hiraOuKata="katakana";
                break;
        }
        String tiposKana="";
        switch(escolhaSpinner2){
            case 0:
                tiposKana="\"basico\"";
                break;
            case 1:
                tiposKana="\"variavel\"";
                break;
            case 2:
                tiposKana="\"juncao\"";
                break;
            case 3:
                tiposKana="";
                break;
                default:
                    tiposKana="basico";
                    break;
        }
        for(int contador=0;contador<arrayKanaSectioner.size();contador++) {
            arrayKanaSectioner.get(contador).clear();
        }
        arrayKanaSectioner.clear();
        int indiceColunaNome,indiceColunaTracos,indiceColunaID,indiceColunaImg;
        arrayKanas=null;
        Runtime.getRuntime().gc();

        for(int i=0;i<StringDivisao.length;i++){
            if(escolhaSpinner2<3) {
                cursor = bancoDados.rawQuery("SELECT id_kana,nome, tracos,nome_imagem FROM " + hiraOuKata + " WHERE basico_var_jun= " + tiposKana, null);
            }
            else{
                cursor = bancoDados.rawQuery("SELECT id_kana,nome, tracos,nome_imagem FROM " + hiraOuKata +" where basico_var_jun=\""+StringDivisao[i]+"\"", null);
            }

            indiceColunaNome=cursor.getColumnIndex("nome");
            indiceColunaTracos=cursor.getColumnIndex("tracos");
            indiceColunaID=cursor.getColumnIndex("id_kana");
            indiceColunaImg=cursor.getColumnIndex("nome_imagem");
            cursor.moveToFirst();

            try{
                arrayKanas=new ArrayList<ItemData>();

                for(int x=0;x<cursor.getCount();x++){
                    int imageId = getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
                    String titulo =cursor.getString(indiceColunaNome);
                    arrayKanas.add(new KanItemData(titulo
                            ,imageId
                            ,cursor.getInt(indiceColunaTracos)
                            ,cursor.getInt(indiceColunaID)
                    ));
                    cursor.moveToNext();
                }
            }catch (Exception e){
                e.printStackTrace();

            }
            arrayKanaSectioner.add(new ArrayList<ItemData>(arrayKanas));
            arrayKanas=new ArrayList<ItemData>();
            cursor.close();

        }

        if(escolhaSpinner2==3){
            divisorContagem= new String[StringDivisao.length];
            for(int i=0;i<StringDivisao.length;i++){
                divisorContagem[i]="Traços "+StringDivisao[i]+":";
            }

            mAdapter= new AdaptadorRecyclerViewSection(arrayKanaSectioner,divisorContagem,getApplicationContext());
        }
        else{
            mAdapter= new AdaptadorRecyclerViewSection(arrayKanaSectioner);
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        //imgsTyped.recycle();
        //kanjiTyped.recycle();
        Runtime.getRuntime().gc();

    }

    public void searchFromDB(String busca, int escolhaSpn1){
        String hiraOuKata="";
        switch(escolhaSpn1){
            case 0:
                hiraOuKata="hiragana";
                break;
            case 1:
                hiraOuKata="katakana";
                break;
        }
        if(arrayKanas!=null){
            for(int i=0;i<arrayKanas.size();i++){
                arrayKanas.get(i).clearMemory();
            }
        }
        if(arrayKanaSectioner!=null){
            for(int contador =0;contador<arrayKanaSectioner.size();contador++){
                arrayKanaSectioner.get(contador).clear();
            }
        }
        arrayKanaSectioner.clear();
        arrayKanas.clear();


        //Inicializa o cursor para se fazer uma busca com o valor da caixa de texto
        String buscaRecebida="SELECT id_kana,nome, tracos,nome_imagem,basico_var_jun FROM "+ hiraOuKata +" WHERE nome LIKE \""+busca+"%\" order by nome";
        cursor=bancoDados.rawQuery(buscaRecebida,null);

        cursor.moveToFirst();

        int indiceColunaTrad=cursor.getColumnIndex("nome");
        int indiceColunaTracos=cursor.getColumnIndex("tracos");
        int indiceColunaID=cursor.getColumnIndex("id_kana");
        int indiceColunaBarVarJun=cursor.getColumnIndex("basico_var_jun");
        int indiceColunaImg=cursor.getColumnIndex("nome_imagem");
        cursor.moveToFirst();

        try{
            arrayKanas=new ArrayList<ItemData>();

            for(int x=0;x<cursor.getCount();x++){
                int imageId = getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
                String[] titulo =cursor.getString(indiceColunaTrad).split(",");
                arrayKanas.add(new KanItemData(titulo[0]
                        ,imageId
                        ,cursor.getInt(indiceColunaTracos)
                        ,cursor.getInt(indiceColunaID)
                        ,cursor.getString(indiceColunaBarVarJun)
                ));
                cursor.moveToNext();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        arrayKanaSectioner.add(new ArrayList<ItemData>(arrayKanas));
        arrayKanas=new ArrayList<ItemData>();
        cursor.close();

        arrayKanaSectioner.add(arrayKanas);
        mAdapter= new AdaptadorRecyclerViewSection(arrayKanaSectioner);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        //imgsTyped.recycle();
        //kanjiTyped.recycle();
        Runtime.getRuntime().gc();
    }



    @OnTextChanged(R.id.kanaSearchText)
    public void onTextChanged(CharSequence s, int start, int before, int count){
        if(!(kanaSearchText.getText().toString().equals(""))){
            searchFromDB(kanaSearchText.getText().toString(),kataAndHiraSpn.getSelectedItemPosition());
        }
        else{
            atualizarLista(kataAndHiraSpn.getSelectedItemPosition(),levelKanaSpn.getSelectedItemPosition());
        }
    }

    @OnItemSelected({R.id.kataAndHiraSpn,R.id.levelKanaSpn})
    public void onItemSelected( Spinner spinner, int thisPosition) {
        switch(spinner.getId()){
            case R.id.kataAndHiraSpn:
                OptionSpn1=thisPosition;
                kanaSearchText.setText("");
                break;
            case R.id.levelKanaSpn:
                OptionSpn2=thisPosition;
                kanaSearchText.setText("");
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
