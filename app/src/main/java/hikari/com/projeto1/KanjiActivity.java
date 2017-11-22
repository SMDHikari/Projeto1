package hikari.com.projeto1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        kanjiTyped= getResources().obtainTypedArray(R.array.KanjiCapitulo1);
        TypedArray imgsTyped = getResources().obtainTypedArray(R.array.kanjiTopList1);

        for (int i = 0; i < getResources().getIntArray(R.array.kanjiTopList1).length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgsTyped.getResourceId(i, -1));
            //arrayKanji.add(new KanjiItemData(bitmap, getResources().getStringArray(kanjiTyped.getResourceId(i,-1))[1]));
        }
        mAdapter = new AdaptadorRecycler(arrayKanji);
        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));

        //Inicializando a tela com os valores do primeiro
        preencherTextos(0);


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

    public void preencherTextos(int position){
        int identificador = getResources().getIdentifier(getResources().getStringArray(kanjiTyped.getResourceId(position,-1))[1], "array", getPackageName());
        String[] arrayString = getResources().getStringArray(identificador);
        kanjiImg.setImageDrawable(arrayKanji.get(position).getImage());
        kanjiTraducao.setText(arrayString[0]);
        onYomiTxt.setText(arrayString[1]);
        kunYomiTxt.setText(arrayString[2]);
        tracosTxt.setText(arrayString[3]);
        exemploTxt.setText(arrayString[4]);
        traducaoExemploTxt.setText(arrayString[5]);

    }




}
