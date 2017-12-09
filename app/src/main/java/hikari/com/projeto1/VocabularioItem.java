package hikari.com.projeto1;

/**
 * Created by Gustavo on 08/12/2017.
 */

public class VocabularioItem {
    String titulo;
    String traducao;

    VocabularioItem(String titulo, String traducao){
        this.titulo=titulo;
        this.traducao=traducao;

    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }


}
