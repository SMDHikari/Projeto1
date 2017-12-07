package hikari.com.projeto1;

import android.graphics.drawable.Drawable;

/**
 * Created by Gustavo on 07/12/2017.
 */

public class quizQuestions {
    private String resposta;
    private boolean acertouOuErrou;
    private int questaoImg;

    quizQuestions(String resposta, boolean acertouOuErrou, int questaoImg){
        this.acertouOuErrou=acertouOuErrou;
        this.questaoImg=questaoImg;
        this.resposta=resposta;
    }

    public String getresposta(){
        return resposta;
    }
    public boolean getAcertouOuErrou(){return acertouOuErrou;}
    public int getQuestaoImg(){return questaoImg;}
}
