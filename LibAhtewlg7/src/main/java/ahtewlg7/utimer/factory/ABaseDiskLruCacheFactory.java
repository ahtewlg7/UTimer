package ahtewlg7.utimer.factory;

import com.jakewharton.disklrucache.DiskLruCache;

/**
 * Created by lw on 2019/3/13.
 */
public abstract class ABaseDiskLruCacheFactory<K,V> {
    protected int maxSize;
    protected DiskLruCache cache;

    protected abstract boolean ifKeyValid(String k);
    protected abstract boolean ifValueValid(V v);

    protected ABaseDiskLruCacheFactory(){
    }

    public boolean add(K k, V v){
        return true;
    }

    public V get(K k){
        return null;
    }

    public boolean remove(K k){
        return true;
    }

    public void clearAll(){
    }
}
