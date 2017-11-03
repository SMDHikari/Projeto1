package hikari.com.projeto1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.kanaBtn)
    Button kanaBtn;
    @BindView(R.id.kanjiBtn)
    Button kanjiBtn;
    @BindView(R.id.vocabularioBtn)
    Button vocabularioBtn;
    @BindView(R.id.sobreBtn)
    Button sobreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.kanaBtn, R.id.kanjiBtn, R.id.vocabularioBtn, R.id.sobreBtn})
    public void onClicked(View v){
    Intent intent;
        switch (v.getId()){
            case R.id.kanaBtn:
                intent = new Intent(this, KanaActivity.class);
                changeActivity(intent);
                break;
            case R.id.kanjiBtn:
                intent = new Intent(this, KanjiActivity.class);
                changeActivity(intent);
                break;
            case R.id.vocabularioBtn:
                intent = new Intent(this, VocabularioActivity.class);
                changeActivity(intent);
                break;
            case R.id.sobreBtn:
                intent = new Intent(this, SobreActivity.class);
                changeActivity(intent);
                break;
        }
    }

    public void changeActivity(Intent intent){
        startActivity(intent);

    }
}
class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

private ClickListener clicklistener;
private GestureDetector gestureDetector;

public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
@Override
public boolean onSingleTapUp(MotionEvent e) {
        return true;
        }

@Override
public void onLongPress(MotionEvent e) {
        View child=recycleView.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null){
        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
        }
        }
        });
        }

@Override
public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
        clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
        }

@Override
public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

@Override
public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
        }
