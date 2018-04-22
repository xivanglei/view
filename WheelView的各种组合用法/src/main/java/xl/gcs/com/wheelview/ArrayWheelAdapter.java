package xl.gcs.com.wheelview;

import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

public class ArrayWheelAdapter<T> implements WheelAdapter {

    private List<T> mItems;

    public ArrayWheelAdapter(List<T> items) {
        this.mItems = items;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < mItems.size()) {
            return mItems.get(index);
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return mItems.size();
    }

    @Override
    public int indexOf(Object o){
        return mItems.indexOf(o);
    }

}