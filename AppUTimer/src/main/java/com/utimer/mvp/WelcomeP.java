package com.utimer.mvp;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import ahtewlg7.utimer.common.LibContextInit;
import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Observable;

public class WelcomeP {
    private WelcomeM m;
    private IWelcomeV v;

    public WelcomeP(@NonNull IWelcomeV v){
        this.v = v;
        m = new WelcomeM();
    }

    public void initWorkContext(){
        m.toRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe(new MySimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean result) {
                    super.onNext(result);
                    if(result)
                        m.toInit();
                    if(v != null)
                        v.onPermissionRequest(result);
                }
            });
    }

    class WelcomeM {
        RxPermissions rxPermissions;

        WelcomeM(){
            rxPermissions  = new RxPermissions(v.getAttachAtivity());
        }

        Observable<Boolean> toRequestPermission(@NonNull String... permissiones){
           return rxPermissions.request(permissiones);
        }

        void toInit(){
            LibContextInit.initWorkingFileSystem();
            GreenDaoAction.getInstance().init();
        }
    }

    public interface IWelcomeV {
        public @NonNull FragmentActivity getAttachAtivity();
        public void onPermissionRequest(boolean result);
    }
}
