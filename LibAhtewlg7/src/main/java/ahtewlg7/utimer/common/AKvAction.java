package ahtewlg7.utimer.common;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.util.MmkvAction;

public abstract class AKvAction {
    protected abstract void toInitKv();

    private <T> boolean saveObject(@NonNull String key, @NonNull T t){
        boolean result = false;
        try{
            MmkvAction.getInstance().putObject(key, t);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private <T> Optional<T> getObject(@NonNull String key, @NonNull Class<T> targetClass){
        T obj = MmkvAction.getInstance().getObject(key, targetClass);
        if(obj == null )
            return Optional.absent();
        return Optional.of(obj);
    }
}
