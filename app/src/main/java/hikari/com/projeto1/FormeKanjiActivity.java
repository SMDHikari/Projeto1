package hikari.com.projeto1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;

public class FormeKanjiActivity extends AppCompatActivity {
    @BindView(R.id.formeGrid)
    RecyclerView formeGrid;
    @BindView(R.id.formeImage)
    ImageView formeImage;
    AdaptadorRecycler adapter;
    AdaptadorRecycler mAdapter;
    ArrayList<KanItemData> arrayTracos;
    private SQLiteDatabase bancoDados;
    private RecyclerView.LayoutManager mLayoutManager;
    Cursor cursor;
    int[] imagesIds;
    String[] listaTracos= {"traco01","traco02","traco03","traco04","traco05","traco06","traco07","traco08","traco09","traco11","traco12","traco13","traco14","traco15","traco16","traco17","traco18","traco19","traco20","traco21","traco22","traco23","traco24","traco25","traco26","traco27","traco28","traco29","traco30","traco31","traco32","traco33","traco34","traco35"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forme_kanji);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        String Traducao;
        try{
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE,null);

        }catch(Exception e){

            e.printStackTrace();
        }
        cursor= bancoDados.rawQuery("select traducao,sequencia_formekanji,sequencia_correta_forme from kanji ORDER BY RANDOM() LIMIT 1",null);

        int idSequenciaCorreta=cursor.getColumnIndex("sequencia_correta_forme");
        int idSequenciaForm=cursor.getColumnIndex("sequencia_formekanji");
        int idTrad= cursor.getColumnIndex("traducao");

        cursor.moveToFirst();
        Traducao= cursor.getString(idTrad);
        String[] sequenciaCorreta=cursor.getString(idSequenciaCorreta).split(",");
        String[] sequenciaImgsForme=cursor.getString(idSequenciaForm).split(",");
        int[] sequenciaCorretaIds = new int[sequenciaCorreta.length];
        int[] sequenciaFormeIds = new int[sequenciaImgsForme.length];
        for(int i =0;i<sequenciaCorreta.length;i++){
            sequenciaCorretaIds[i]=getResources().getIdentifier(sequenciaCorreta[i],"drawable",this.getPackageName());
        }



        arrayTracos=new ArrayList<KanItemData>();
        for(int i=0;i<listaTracos.length;i++) {
            arrayTracos.add(new KanItemData(getResources().getIdentifier(listaTracos[i],"drawable",this.getPackageName())));
        }

        mLayoutManager = new GridLayoutManager(this,  4);
        formeGrid= new RecyclerView(this);
        adapter = new AdaptadorRecycler(cast(arrayTracos),true);
        formeGrid.setLayoutManager(mLayoutManager);
        formeGrid.setAdapter(adapter);
        formeGrid.getAdapter().notifyDataSetChanged();
        formeGrid.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundResource(R.drawable.icone_acerto);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundResource(R.drawable.icone_aprendendo1);
                        break;
                    case DragEvent.ACTION_DROP:
                        v.setBackgroundResource(R.drawable.icone_acerto);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                }
                return  true;
            }
        });

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
}
