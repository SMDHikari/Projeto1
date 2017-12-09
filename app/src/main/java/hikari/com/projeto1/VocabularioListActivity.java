package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class VocabularioListActivity extends AppCompatActivity {
    @BindView(R.id.capituloSpn)
    Spinner capituloSpn;
    @BindView(R.id.parteSpn)
    Spinner parteSpn;
    @BindView(R.id.recyclerVocabulario)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VocabularioItem> arrayVocab;
    int OptionSpn1=0,OptionSpn2=0;
    private SQLiteDatabase bancoDados;
    Cursor cursor;

    //MODIFICAR PARA OS VALORES DAS STRINGS DO VOCABULARIO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        iniciarRecycler();


        /*Implementação do ClickListener (não existe naturalmente no RecyclerView)
        * Resto do código da implementação, estão na interface ClickListener e na innerClass
        * RecyclerTouchListener no MainActivity.*/


    }
    public void iniciarRecycler(){
        mLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        atualizarLista();
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.getRecycledViewPool().clear();



        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(   this,
                R.array.vocabSpn1, R.layout.spinner_item_top);
        ArrayAdapter<CharSequence> adapterSpn2 = ArrayAdapter.createFromResource(this,
                R.array.vocabSpn2, R.layout.spinner_item_top);
        // Specificando layout do dropdown
        adapterSpn1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterSpn2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        // Aplicando o adaptador no spinner
        capituloSpn.setAdapter(adapterSpn1);
        parteSpn.setAdapter(adapterSpn2);


        runLayoutAnimation(recyclerView);


    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
    @OnItemSelected({R.id.capituloSpn,R.id.parteSpn})
    public void OnItemSelected(Spinner spn, int position){
        switch(spn.getId()){
            case R.id.capituloSpn:
                break;
            case R.id.parteSpn:
                    atualizarLista();
                break;
        }
    }

    public void atualizarLista(){
        //Modificar para atualizar as listas de acordo com os vocabulrios da lista
        //Utilizar a variavel "vocabList" para um controle melhor
        cursor= bancoDados.rawQuery("select palavra, traducao from vocabulario where parte like \"%"+(parteSpn.getSelectedItemPosition()+1)+"%\"",null);
        int indiceColunaNome=cursor.getColumnIndex("palavra");
        int indiceColunaTrad=cursor.getColumnIndex("traducao");
        cursor.moveToFirst();
        try{
            arrayVocab=new ArrayList<VocabularioItem>();

            for(int x=0;x<cursor.getCount();x++){
                String titulo =cursor.getString(indiceColunaNome);
                String traducao =cursor.getString(indiceColunaTrad);
                arrayVocab.add(new VocabularioItem(titulo,traducao));
                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        mAdapter= new AdaptadorRecycler(arrayVocab,true,true);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        runLayoutAnimation(recyclerView);

    }
}
