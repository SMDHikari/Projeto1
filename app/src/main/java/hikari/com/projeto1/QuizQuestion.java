package hikari.com.projeto1;

import android.graphics.Bitmap;

/**
 * Created by Gustavo on 03/11/2017.
 */

public class QuizQuestion {
    String question;
    Bitmap questionImg;
    String[] answers;
    Bitmap[] answersImg;
    QuizQuestion(String question, Bitmap[] answersImg){
        this.question=question;
        this.answersImg=answersImg;

    }
    QuizQuestion(Bitmap questionImg, String[] answers){
        this.questionImg=questionImg;
        this.answers=answers;
    }

}
