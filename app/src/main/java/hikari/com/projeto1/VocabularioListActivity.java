package hikari.com.projeto1;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocabularioListActivity extends AppCompatActivity {
    @BindView(R.id.capituloSpn)
    Spinner capituloSpn;
    @BindView(R.id.conteudoSpn)
    Spinner conteudoSpn;
    @BindView(R.id.recyclerVocabulario)
    RecyclerView recyclerVocabulario;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemData> arrayKanas;
    int OptionSpn1=0,OptionSpn2=0;

    //MODIFICAR PARA OS VALORES DAS STRINGS DO VOCABULARIO
    //private int[][] vocabList={{R.array.placeholder,R.array.hiraBasico,R.array.hiraVar,R.array.hiraJun},{R.array.placeholder,R.array.kataBasico,R.array.kataVar,R.array.kataJun}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        mLayoutManager= new LinearLayoutManager(this);
        recyclerVocabulario.setLayoutManager(mLayoutManager);
        atualizarLista(OptionSpn1,OptionSpn2);
        recyclerVocabulario.addItemDecoration(new SpacesItemDecoration(10));
        recyclerVocabulario.setHasFixedSize(true);

        //Definindo Spinners e inicializando valores dos spinners
        ArrayAdapter<CharSequence> adapterSpn1 = ArrayAdapter.createFromResource(this,
                R.array.kanaSpinner1, R.layout.spinner_item_top);
        ArrayAdapter<CharSequence> adapterSpn2 = ArrayAdapter.createFromResource(this,
                R.array.kanaSpinner2, R.layout.spinner_item_top);
        // Specificando layout do dropdown
        adapterSpn1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapterSpn2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        // Aplicando o adaptador no spinner
        capituloSpn.setAdapter(adapterSpn1);
        conteudoSpn.setAdapter(adapterSpn2);
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

    public void atualizarLista(int escolhaSpinner1, int escolhaSpinner2){
        //Modificar para atualizar as listas de acordo com os vocabulrios da lista
        //Utilizar a variavel "vocabList" para um controle melhor
        /*
        arrayKanas=new ArrayList<ItemData>();
        ArrayList<int[]> todos= new ArrayList<int[]>();
        TypedArray imgsTyped;
        if(escolhaSpinner2==0){
            for(int i=0;i< imgs[escolhaSpinner1].length;i++){
                imgsTyped=getResources().obtainTypedArray(imgs[escolhaSpinner1][i]);
                for(int x=0;x<getResources().getIntArray(imgs[escolhaSpinner1][i]).length;x++){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imgsTyped.getResourceId(x,-1));
                    arrayKanas.add(new KanItemData(getResources().getStringArray(kanaList[escolhaSpinner1][i])[x],bitmap));
                }
            }
        }
        else{
            imgsTyped = getResources().obtainTypedArray(imgs[escolhaSpinner1][escolhaSpinner2]);
            for(int i=0;i<getResources().getStringArray(kanaList[escolhaSpinner1][escolhaSpinner2]).length;i++){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imgsTyped.getResourceId(i,-1));
                arrayKanas.add(new KanItemData(getResources().getStringArray(kanaList[escolhaSpinner1][escolhaSpinner2])[i],bitmap));
            }
        }

        mAdapter= new AdaptadorRecycler(arrayKanas);
        mAdapter.notifyDataSetChanged();
        recyclerVocabulario.setAdapter(mAdapter);
*/
    }
}
