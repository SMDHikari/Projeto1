package hikari.com.projeto1;

/**
 * Created by Gustavo on 07/12/2017.
 */

public class ItemDataFactory {
    //use getItemData method to get object of type itemdata
    public ItemData getItemData(String itemdataType){
        if(itemdataType == null){
            return null;
        }
        if(itemdataType.equalsIgnoreCase("KAN")){
            return new KanItemData();
        }
        else if(itemdataType.equalsIgnoreCase("DIALOG")){
            return new dialogItemData();
        }
        return null;
    }
}
