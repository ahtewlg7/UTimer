package ahtewlg7.utimer.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public abstract class ABaseMapFactory<K, V> {
    public static final String TAG = ABaseMapFactory.class.getSimpleName();

    protected BiMap<K, V> baseMap;

    public abstract boolean ifKeyValid(K k);
    public abstract boolean ifValueValid(V v);
    public abstract V newValue();

    protected ABaseMapFactory(){
        baseMap = HashBiMap.create();
    }

    public int getNum(){
        return baseMap.size();
    }

    public void addValue(K k, V v){
        if(ifKeyValid(k) && ifValueValid(v) && !baseMap.containsKey(k))
            baseMap.put(k, v);
    }


    public V getValue(K k){
        return baseMap.get(k);
    }
    public K getKey(V v){
        return baseMap.inverse().get(v);
    }


    public void removeByKey(K k){
        baseMap.remove(k);
    }
    public void removeByValue(V v){
        baseMap.inverse().remove(v);
    }

    public void clearAll(){
        baseMap.clear();
    }
}
