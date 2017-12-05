package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KanjiActivity extends AppCompatActivity {

    private static int idKanjiClicado;
    @BindView(R.id.recyclerKanjiId)
    RecyclerView recyclerView;
    ArrayList<KanItemData> arrayKanji= new ArrayList<KanItemData>();
    private AdaptadorRecycler mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.kanjiImgs)
    MyGridView kanjiImgsGrid;
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
    private SQLiteDatabase bancoDados;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        Intent intent = getIntent();
        idKanjiClicado = intent.getIntExtra("kanjiClicado",0);
        int capitulo=0;
        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        cursor=bancoDados.rawQuery("SELECT capitulo_kanji FROM kanji where id_kanji ="+idKanjiClicado,null);

        int indiceCapitulo=cursor.getColumnIndex("capitulo_kanji");

        cursor.moveToFirst();

        try{
            while(cursor!=null)
            {
                capitulo=cursor.getInt(indiceCapitulo);
                cursor.moveToNext();
            }
        }catch(Exception e){
            e.printStackTrace();

        }
        atualizarLista(capitulo);



        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        Collections.sort(arrayKanji);
        mAdapter = new AdaptadorRecycler(cast(arrayKanji),false);
        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));

        runLayoutAnimation(recyclerView);
        //recebe a posição do array clicado na outra atividade em relação ao array desta.
        int i=0;
        while(arrayKanji.get(i).getID()!=idKanjiClicado){
            i++;
        }
        recyclerView.scrollToPosition(i);

        AdaptadorRecycler.setSelectedPosition(i);
        //Inicializando a tela com os valores do clicado
        preencherTextos(idKanjiClicado);


        //Adicionado a o onTouch ao recyclerView, pois o mesmo não o contém no padrão.
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int clickPosition) {
                //Recebe os valores de acordo com a posição clicada, e envia para o metodo preencherTextos()
                AdaptadorRecycler.setSelectedPosition(clickPosition);
                recyclerView.getAdapter().notifyDataSetChanged();
                preencherTextos(arrayKanji.get(clickPosition).getID());
                KanjiActivity.idKanjiClicado= arrayKanji.get(clickPosition).getID();
            }

            //caso seja necessária alguma função de longClick.
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }
    public void atualizarLista(int capPosition){

        cursor=bancoDados.rawQuery("SELECT id_kanji,traducao, tracos,kanji_imagem FROM kanji WHERE capitulo_kanji ="+capPosition+" order by tracos ",null);

        int indiceColunaID=cursor.getColumnIndex("id_kanji");
        int indiceColunaImg=cursor.getColumnIndex("kanji_imagem");
        int indiceColunaTracos=cursor.getColumnIndex("tracos");
        cursor.moveToFirst();
        for(int x=0;x<cursor.getCount();x++){
            int idImagem = this.getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
            arrayKanji.add(new KanItemData(""
                    ,idImagem
                    ,cursor.getInt(indiceColunaTracos)
                    ,cursor.getInt(indiceColunaID)
            ));
            cursor.moveToNext();
        }
        //limpeza de memória do imgsTyped
        Collections.sort(arrayKanji);



        mAdapter= new AdaptadorRecycler(cast(arrayKanji),false);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);
        //limpa a variavel imgsTyped no fim da atualização da lista
        //imgsTyped.recycle();
        //kanjiTyped.recycle();
        Runtime.getRuntime().gc();

    }

    public static <KanjiItemData> ArrayList<ItemData> cast(ArrayList list) {
        return list;
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
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void preencherTextos(int position){
        cursor=bancoDados.rawQuery("select traducao,exemplo,traducao_exemplo,tracos,kanji_imagem,sequecia_tracosimg," +
                "leitura_on,leitura_kun from kanji where id_kanji ="+position,null);

        int indiceColunaTrad=cursor.getColumnIndex("traducao");
        int indiceColunaEx=cursor.getColumnIndex("exemplo");
        int indiceColunaTradEx=cursor.getColumnIndex("traducao_exemplo");
        int indiceColunaTracos=cursor.getColumnIndex("tracos");
        int indiceColunaKanjiImg=cursor.getColumnIndex("kanji_imagem");
        int indiceColunaSequenciaImg=cursor.getColumnIndex("sequecia_tracosimg");
        int indiceColunaLeituraOn=cursor.getColumnIndex("leitura_on");
        int indiceColunaLeituraKun=cursor.getColumnIndex("leitura_kun");
        cursor.moveToFirst();
        Toast.makeText(this,cursor.getString(indiceColunaLeituraOn),Toast.LENGTH_SHORT).show();
        int imageId = 0;
        cursor.moveToFirst();
        String[] sequenciaImgsString,arrayString;
        String textoTraduz = null,exemplo= null,exemploTrad= null,leituraOn= null,leituraKun= null;
        Drawable[] idSequenciaImgs=new Drawable[0];
        int tracos = 0;
        

                //inicia o ID da imagem principal do kanji
                imageId = getResources().getIdentifier(cursor.getString(indiceColunaKanjiImg), "drawable", this.getPackageName());
                //recebe string com o nome dos arquivos da sequencia de imagens
                sequenciaImgsString = cursor.getString(indiceColunaSequenciaImg).split(",");
                //inicia String Tradução
                 arrayString = cursor.getString(indiceColunaTrad).split(",");
                 textoTraduz="";
                for(int i = 0;i<arrayString.length;i++){
                    textoTraduz += (i+1)+". "+arrayString[i]+"\n";
                }
                //Inicia Array com ids das Imagems da sequencia
                 idSequenciaImgs= new Drawable[sequenciaImgsString.length];
                for(int x=0;x<sequenciaImgsString.length;x++){
                    idSequenciaImgs[x]= getResources().getDrawable(getResources().getIdentifier(sequenciaImgsString[x], "drawable", this.getPackageName()));
                }
                //recebe numero de traços
                tracos= cursor.getInt(indiceColunaTracos);
                //recebe coluna exemplo e coluna exemplo traducao
                exemplo= cursor.getString(indiceColunaEx);
                exemploTrad= cursor.getString(indiceColunaTradEx);

                //Recebe Colunas Leitura
                leituraOn= cursor.getString(indiceColunaLeituraOn);
                leituraKun= cursor.getString(indiceColunaLeituraKun);


        kanjiImgsGrid.setNumColumns(4);
        kanjiImgsGrid.setAdapter(new ImageAdapter(this,idSequenciaImgs));
        kanjiImgsGrid.setHorizontalSpacing(-250);
        kanjiImgsGrid.setMinimumHeight(kanjiImgsGrid.getMeasuredHeight());
        kanjiImgsGrid.deferNotifyDataSetChanged();
        kanjiImg.setBackground(getResources().getDrawable(R.drawable.quadrado));
        kanjiImg.setImageResource(imageId);
        kanjiTraducao.setText(textoTraduz);
        onYomiTxt.setText(leituraOn);
        kunYomiTxt.setText(leituraKun);
        tracosTxt.setText(String.valueOf(tracos));
        exemploTxt.setText(exemplo);
        traducaoExemploTxt.setText(exemploTrad);

        textoTraduz=null;
        arrayString=null;
        Runtime.getRuntime().gc();
    }

    @Override
    public void onDestroy(){
        clearMemory();
        AdaptadorRecycler.setSelectedPosition(-1);
        super.onDestroy();
    }

    public void clearMemory(){
        mAdapter=null;
        recyclerView.addOnItemTouchListener(null);
        recyclerView.setLayoutManager(null);
        recyclerView=null;
        kanjiTyped=null;
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
