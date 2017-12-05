package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
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

public class KanaActivity extends AppCompatActivity {

    @BindView(R.id.recyclerKanaId)
    RecyclerView recyclerView;
    ArrayList<ItemData> arrayKana= new ArrayList<ItemData>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.gridViewKana)
    MyGridView kanaImgsGrid;

    @BindView(R.id.kanaImg)
    ImageView kanaImg;
    @BindView(R.id.kanaNome)
    TextView kanaNome;
    @BindView(R.id.tracosTxt)
    TextView tracosTxt;
    @BindView(R.id.alertKanaText)
    TextView alertKanaText;
    private SQLiteDatabase bancoDados;
    Cursor cursor;
    int idKanaClicado;
    String BasVarJun ="",controleKataOuHira="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kana);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        Intent intent = getIntent();
        idKanaClicado = intent.getIntExtra("kanaClicado",0);
        int kanaSpn1 = intent.getIntExtra("kanaSpn1",0);

        switch(kanaSpn1){
            case 0:
                controleKataOuHira= "hiragana";
                break;
            case 1:
                controleKataOuHira = "katakana";
                break;
        }

        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        cursor=bancoDados.rawQuery("SELECT basico_var_jun FROM "+controleKataOuHira+" where id_kana ="+idKanaClicado,null);

        int indiceColunaBasVarJun=cursor.getColumnIndex("basico_var_jun");
        cursor.moveToFirst();

        BasVarJun= cursor.getString(indiceColunaBasVarJun);

        atualizarLista();
        //kanaList=new int[][]{{R.array.hiraBasico,R.array.hiraVar,R.array.hiraJun},{R.array.kataBasico,R.array.kataVar,R.array.kataJun,R.array.placeholder}};
        //kanaImgs = new int[]/*[]*/{R.array.hiraBasicoImg,R.array.hiraVarImg,R.array.hiraJunImg};/*,{R.array.kataBasicoImg,R.array.kataVarImg,R.array.kataJunImg}};*/

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);



        int i=0;
        while(arrayKana.get(i).getID()!=idKanaClicado){
            i++;
        }
        recyclerView.scrollToPosition(i);

        AdaptadorRecycler.setSelectedPosition(i);


        preencherTextos(i);
        //Adicionado a o onTouch ao recyclerView, pois o mesmo não o contém no padrão.
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Recebe os valores de acordo com a posição clicada, e envia para o metodo preencherTextos()
                AdaptadorRecycler.setSelectedPosition(position);
                recyclerView.getAdapter().notifyDataSetChanged();
                preencherTextos(position);
            }

            //caso seja necessária alguma função de longClick.
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        /*kanaTyped= getResources().obtainTypedArray(kanaList[capPosition]);


        */



    }

    public void atualizarLista(){

        cursor=bancoDados.rawQuery("SELECT id_kana,nome, tracos,nome_imagem FROM "+ controleKataOuHira +" WHERE basico_var_jun =\""+BasVarJun+"\" order by id_kana ",null);


        int indiceColunaID=cursor.getColumnIndex("id_kana");
        int indiceColunaImg=cursor.getColumnIndex("nome_imagem");
        int indiceColunaTracos=cursor.getColumnIndex("tracos");
        cursor.moveToFirst();
        for(int x=0;x<cursor.getCount();x++){
            int idImagem = this.getResources().getIdentifier(cursor.getString(indiceColunaImg), "drawable", this.getPackageName());
            arrayKana.add(new KanItemData(""
                    ,idImagem
                    ,cursor.getInt(indiceColunaTracos)
                    ,cursor.getInt(indiceColunaID)
            ));
            cursor.moveToNext();
        }
        //limpeza de memória do imgsTyped
        Collections.sort(arrayKana);



        mAdapter= new AdaptadorRecycler(cast(arrayKana),false);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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

    public void preencherTextos(int position){
        idKanaClicado= arrayKana.get(position).getID();
        cursor=bancoDados.rawQuery("select nome,aviso,sequencia_tracos from "+controleKataOuHira+" where id_kana ="+idKanaClicado,null);
        //String[] arrayString = getResources().getStringArray(kanaTyped.getResourceId(position,0));
        int indiceColunaNome=cursor.getColumnIndex("nome");
        int indiceColunaAviso=cursor.getColumnIndex("aviso");
        int indiceColunaSequenciaImg=cursor.getColumnIndex("sequencia_tracos");
        cursor.moveToFirst();

        kanaImg.setImageResource(arrayKana.get(position).getImage());
        tracosTxt.setText(String.valueOf(arrayKana.get(position).getTracos()));
        kanaNome.setText(cursor.getString(indiceColunaNome));
        alertKanaText.setText(cursor.getString(indiceColunaAviso));
        cursor.moveToFirst();
        String[] sequenciaImgsString;
        sequenciaImgsString = cursor.getString(indiceColunaSequenciaImg).split(",");
        Drawable[] idSequenciaImgs= new Drawable[sequenciaImgsString.length];
        cursor.moveToFirst();
        Toast.makeText(this,sequenciaImgsString[0],Toast.LENGTH_SHORT).show();
        for(int x=0;x<sequenciaImgsString.length;x++){
            idSequenciaImgs[x]= getResources().getDrawable(getResources().getIdentifier(sequenciaImgsString[x], "drawable", this.getPackageName()));
        }
        //alertKanaText.setText();
        kanaImgsGrid.setNumColumns(4);
        kanaImgsGrid.setAdapter(new ImageAdapter(this,idSequenciaImgs));
        kanaImgsGrid.setHorizontalSpacing(10);
        kanaImgsGrid.setMinimumHeight(0-kanaImgsGrid.getMeasuredHeight());
        kanaImgsGrid.deferNotifyDataSetChanged();

        //textoTraduz=null;
        //recebeTexto=null;
        //arrayString=null;
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

        kanaImg=null;
        for(int i =0;i<arrayKana.size();i++){
            arrayKana.get(i).clearMemory();
        }
        //limpeza textos
        kanaNome=null;
        tracosTxt=null;
        alertKanaText=null;

        Runtime.getRuntime().gc();

    }



}
