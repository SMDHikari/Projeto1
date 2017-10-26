package hikari.com.projeto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.kanaBtn)
    ImageView kanaBtn;
    @BindView(R.id.kanjiBtn)
    ImageView kanjiBtn;
    @BindView(R.id.vocabularioBtn)
    ImageView vocabularioBtn;
    @BindView(R.id.sobreBtn)
    ImageView sobreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
