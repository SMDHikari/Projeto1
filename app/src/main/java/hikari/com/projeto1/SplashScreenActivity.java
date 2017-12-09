package hikari.com.projeto1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class SplashScreenActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;


    Cursor cursor;
    String[][] kanjiList, vocabularioList;
    String[] hiraList, kataList;
    SimpleDatabaseHelper dbh;
    TextView carregando;
    final int controleRequest=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        dbh = new SimpleDatabaseHelper(this);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbar_title =  findViewById(R.id.toolbar_title);
        toolbar_title.setText("Seito");
        kanjiList = new String[][]{getResources().getStringArray(R.array.KanjiCapitulo1), getResources().getStringArray(R.array.KanjiCapitulo2), getResources().getStringArray(R.array.KanjiCapitulo3), getResources().getStringArray(R.array.KanjiCapitulo4), getResources().getStringArray(R.array.KanjiCapitulo5)};
        hiraList = getResources().getStringArray(R.array.hiraTodos);
        kataList = getResources().getStringArray(R.array.kataTodos);
        String[] vocabuList = getResources().getStringArray(R.array.vocabulario);
        String storage_folder = "/myapp/db";
        /*
        Requerimento de permissão para escrever na memória externa( não utilizado por enquanto)
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        controleRequest);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/


        //inicializa o Banco de dados
        try {
            bancoDados = openOrCreateDatabase("app", Context.MODE_PRIVATE, null);

            //Cria a tabela pessoas, caso ela não exista no dispositivo
            //bancoDados.execSQL("DROP TABLE pessoas;");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS kanji (" +
                    "    id_kanji INTEGER  PRIMARY KEY AUTOINCREMENT," +
                    "    exemplo VARCHAR," +
                    "    traducao VARCHAR," +
                    "    traducao_exemplo VARCHAR," +
                    "    tracos INTEGER(2)," +
                    "    capitulo_kanji INTEGER(2)," +
                    "    kanji_imagem VARCHAR," +
                    "    sequecia_tracosimg VARCHAR," +
                    "    sequencia_formekanji VARCHAR," +
                    "    sequencia_correta_forme VARCHAR,"+
                    "    leitura_on VARCHAR," +
                    "    leitura_kun VARCHAR" +
                    ")");
            bancoDados.execSQL("CREATE TABLE if not exists hiragana (\n" +
                    "    id_kana INTEGER PRIMARY KEY,\n" +
                    "    nome VARCHAR,\n" +
                    "    tracos INTEGER(2),\n" +
                    "    nome_imagem VARCHAR,\n" +
                    "    basico_var_jun VARCHAR,\n" +
                    "    sequencia_tracos VARCHAR,\n" +
                    "    aviso varchar" +
                    ");");
            bancoDados.execSQL("CREATE TABLE if not exists katakana (\n" +
                    "    id_kana INTEGER PRIMARY KEY,\n" +
                    "    nome VARCHAR,\n" +
                    "    tracos INTEGER(2),\n" +
                    "    nome_imagem VARCHAR,\n" +
                    "    basico_var_jun VARCHAR,\n" +
                    "    sequencia_tracos VARCHAR,\n" +
                    "    aviso varchar" +
                    ");");
            /*bancoDados.execSQL("CREATE TABLE IF not exists vocabulario(\n"+
                    " id_vocab INTEGER PRIMARY KEY, \n"+
                    " palavra VARCHAR,"+
                    " traducao VARCHAR,"+
                    " parte VARCHAR)");*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        //inicializa o cursor para verificar a quantidade de rows "nome" na tabela kanji

       /* cursor = bancoDados.rawQuery("SELECT count(id_vocab) FROM vocabulario", null);
        cursor.moveToFirst();

        if (icount <= 0) {
            //Inserir dados kanji
            for (int i = 0; i <= vocabuList.length; i++) {

                int id = this.getResources().getIdentifier(vocabuList[i], "array", this.getPackageName());
                getString = getResources().getStringArray(id);
                bancoDados.execSQL("INSERT INTO vocabulario(palavra,traducao," +
                        "parte) " +
                        "VALUES (\"" + getString[0] +
                        "\",\"" + getString[1] +
                        "\",\"" + getString[2] +
                        "\")");
            }
        }*/









        //inicializa o cursor para verificar a quantidade de rows "nome" na tabela kanji

        cursor = bancoDados.rawQuery("SELECT count(id_kana) FROM katakana", null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        String[] getString;





        //caso não exista nenhum valor na tabela, se adiciona os valores corretos dos kanjis a partir do xml
        if (icount <= 0) {
            //Inserir dados kanji
            for (int i = 1; i <= kataList.length; i++) {

                int id = this.getResources().getIdentifier(kataList[i - 1], "array", this.getPackageName());
                getString = getResources().getStringArray(id);
                bancoDados.execSQL("INSERT INTO katakana(nome,tracos," +
                        "nome_imagem,sequencia_tracos,aviso,basico_var_jun) " +
                        "VALUES (\"" + getString[0] +
                        "\",\"" + getString[1] +
                        "\",\"" + getString[2] +
                        "\",\"" + getString[3] +
                        "\",\"" + getString[4] +
                        "\",\"" + getString[5] +
                        "\")");
            }
        }
        cursor = bancoDados.rawQuery("SELECT count(id_kana) FROM hiragana", null);
        cursor.moveToFirst();
        icount = cursor.getInt(0);
        if (icount <= 0) {
            for (int i = 1; i <= hiraList.length; i++) {

                int id = this.getResources().getIdentifier(hiraList[i - 1], "array", this.getPackageName());
                getString = getResources().getStringArray(id);
                bancoDados.execSQL("INSERT INTO hiragana(nome,tracos," +
                        "nome_imagem,sequencia_tracos,aviso,basico_var_jun) " +
                        "VALUES (\"" + getString[0] +
                        "\",\"" + getString[1] +
                        "\",\"" + getString[2] +
                        "\",\"" + getString[3] +
                        "\",\"" + getString[4] +
                        "\",\"" + getString[5] +
                        "\")");
            }
        }

                /*cursor = bancoDados.rawQuery("SELECT traducao FROM kanji where traducao like \"%Dia%\"", null);
                cursor.moveToFirst();
                int x=cursor.getColumnIndex("traducao");
                String activity_splash_screen = cursor.getString(x);
                cursor.moveToFirst();
                Toast.makeText(this,activity_splash_screen,Toast.LENGTH_SHORT).show();*/


            cursor = bancoDados.rawQuery("SELECT count(id_kanji) FROM kanji", null);
            cursor.moveToFirst();
            icount = cursor.getInt(0);

            //caso não exista nenhum valor na tabela, se adiciona os valores corretos dos kanji a partir do xml
            if (icount <= 0) {
                //Inserir dados kanji
                for (int i = 1; i <= kanjiList.length; i++) {
                    //devido ao fato de os dados não poderem ser modificados, não foi necessário fazer verificações de conversão

                    for (int x = 0; x < kanjiList[i - 1].length; x++) {

                        int id = this.getResources().getIdentifier(kanjiList[i - 1][x], "array", this.getPackageName());
                        getString = getResources().getStringArray(id);
                        bancoDados.execSQL("INSERT INTO kanji(traducao,leitura_on,leitura_kun,tracos,capitulo_kanji," +
                                "exemplo,traducao_exemplo,kanji_imagem,sequecia_tracosimg,sequencia_formekanji,sequencia_correta_forme) " +
                                "VALUES (\"" + getString[0] +
                                "\",\"" + getString[1] +
                                "\",\"" + getString[2] +
                                "\"," + getString[3] +
                                "," + (i) +
                                ",\"" + getString[4] +
                                "\",\"" + getString[5] +
                                "\",\"" + getString[6] +
                                "\",\"" + getString[7] +
                                "\",\"" + getString[8] +
                                "\",\"" + getString[9] +
                                "\")");
                    }
                }
                /*cursor = bancoDados.rawQuery("SELECT traducao FROM kanji where traducao like \"%Dia%\"", null);
                cursor.moveToFirst();
                int x=cursor.getColumnIndex("traducao");
                String activity_splash_screen = cursor.getString(x);
                cursor.moveToFirst();
                Toast.makeText(this,activity_splash_screen,Toast.LENGTH_SHORT).show();*/


                String[] getStringKana;
            }
        Thread splashTimer = new Thread() {
            public void run() {

                try {



                    sleep(2000);


                    // Advance to the next screen.
                    startActivity(new Intent(SplashScreenActivity.this,
                            MainActivity.class));//}


                } catch (Exception e) {
                    Log.e("ex", e.toString());
                } finally {
                    finish();
                }
            }
        };
        splashTimer.start();
            //Se fecha
            cursor.close();
            Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);

        }
        /*
        Requerimento de permissão para escrever na memória externa( não utilizado por enquanto)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case controleRequest: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
    }
