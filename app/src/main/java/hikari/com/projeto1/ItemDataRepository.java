package hikari.com.projeto1;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gustavo on 07/12/2017.
 */

public class ItemDataRepository implements Container {
    public ArrayList<ItemData> itemdata;

    ItemDataRepository()
    {
        this.itemdata = new ArrayList<>();
    }

    ItemDataRepository(ArrayList<ItemData> list)
    {
        this.itemdata = list;
    }

    public void addItem(ItemData item)
    {
        this.itemdata.add(item);
    }

    @Override
    public hikari.com.projeto1.Iterator getIterator()
    {
        return new ItemDataIterator();
    }

    private class ItemDataIterator implements hikari.com.projeto1.Iterator
    {
        int index;

        @Override
        public boolean hasNext()
        {
            if(index < itemdata.size())
            {
                return true;
            }
            return false;
        }

        @Override
        public Object next()
        {
            if(this.hasNext())
            {
                return itemdata.get(index++);
            }
            return null;
        }
    }
}
