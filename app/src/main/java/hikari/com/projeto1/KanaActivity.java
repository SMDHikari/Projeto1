package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KanaActivity extends AppCompatActivity {

    @BindView(R.id.recyclerKanaId)
    RecyclerView recyclerView;
    ArrayList<ItemData> arrayKana= new ArrayList<ItemData>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.kanaImg)
    ImageView kanaImg;
    @BindView(R.id.kanaTraducao)
    TextView kanaTraducao;
    @BindView(R.id.tracosTxt)
    TextView tracosTxt;
    @BindView(R.id.alertKanaText)
    TextView alertKanaText;

    //TypedArray kanaTyped;
    String[] kanaTyped;
    int[] []kanaList;
    int[] /*[]*/kanaImgs;

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
        int position = intent.getIntExtra("kanaClicado",0);
        int kanaSpn1 = intent.getIntExtra("kanaSpn1",0);
        int kanaSpn2 = intent.getIntExtra("kanaSpn2",0);

        //kanaList=new int[][]{{R.array.hiraBasico,R.array.hiraVar,R.array.hiraJun},{R.array.kataBasico,R.array.kataVar,R.array.kataJun,R.array.placeholder}};
        //kanaImgs = new int[]/*[]*/{R.array.hiraBasicoImg,R.array.hiraVarImg,R.array.hiraJunImg};/*,{R.array.kataBasicoImg,R.array.kataVarImg,R.array.kataJunImg}};*/

        int tamanhoBasicos= getResources().getStringArray(kanaList[1][0]).length;
        int tamanhovar= getResources().getStringArray(kanaList[1][1]).length;


        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        switch (kanaSpn2){
            case 0:
            case 1:
            case 2:
                kanaTyped=getResources().getStringArray(kanaList[kanaSpn1][kanaSpn2]);
                break;

            case 3:
                if(position <=tamanhoBasicos+1){
                    kanaTyped=getResources().getStringArray(kanaList[kanaSpn1][0]);
                    kanaSpn2=0;
                    position=position-1;
                } else if (position>tamanhoBasicos+1&&
                        position<=tamanhoBasicos+tamanhovar+1) {
                    kanaTyped=getResources().getStringArray(kanaList[kanaSpn1][1]);
                    kanaSpn2=1;
                    position = position - (tamanhoBasicos+2);
                }
                else{
                    kanaTyped=getResources().getStringArray(kanaList[kanaSpn1][2]);
                    kanaSpn2=2;
                    position = position - (tamanhoBasicos+tamanhovar+3);
                }
                break;
        }
        TypedArray imgsTyped = getResources().obtainTypedArray(kanaImgs[kanaSpn2]);
        for (int i = 0; i < kanaTyped.length; i++) {
            String titulo = kanaTyped[i];
            //arrayKana.add(new KanItemData(titulo,imgsTyped.getResources().getDrawable(imgsTyped.getResourceId(i,0))));
        }
        mAdapter = new AdaptadorRecycler(arrayKana,false);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        runLayoutAnimation(recyclerView);
        AdaptadorRecycler.setSelectedPosition(position);
        recyclerView.getAdapter().notifyDataSetChanged();
        preencherTextos(position);



        ;
        preencherTextos(position);
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
        //String[] arrayString = getResources().getStringArray(kanaTyped.getResourceId(position,0));
        kanaImg.setImageDrawable(arrayKana.get(position).getImage());
        // String[] recebeTexto = arrayString[0].split(",");
        //String textoTraduz="";
        //tracosTxt.setText();
        kanaTraducao.setText(arrayKana.get(position).getTitle());
        //alertKanaText.setText();

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
        kanaTyped=null;
        kanaList=null;
        kanaImgs=null;
        kanaImg=null;
        for(int i =0;i<arrayKana.size();i++){
            arrayKana.get(i).clearMemory();
        }
        //limpeza textos
        kanaTraducao=null;
        tracosTxt=null;
        alertKanaText=null;

        Runtime.getRuntime().gc();

    }



}
