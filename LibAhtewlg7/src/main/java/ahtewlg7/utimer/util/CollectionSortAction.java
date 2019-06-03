package ahtewlg7.utimer.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lw on 2019/6/3.
 */
public class CollectionSortAction<E> {

    public void toSort(List<E> list , Comparator<E> c){
        Collections.sort(list, c);
    }
}
