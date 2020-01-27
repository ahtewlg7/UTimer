package ahtewlg7.utimer.mvp;

import android.Manifest;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.google.common.base.Optional;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import ahtewlg7.utimer.common.LibContextInit;
import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.entity.material.MediaInfo;
import ahtewlg7.utimer.factory.MdBuildFactory;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainP {
    private MainM m;
    private IMainV v;
    private RxPermissions rxPermissions;

    public MainP(@NonNull IMainV v){
        this.v = v;
        m = new MainM();
        rxPermissions  = new RxPermissions(v.getAttachAtivity());
    }

    public void toCreateDeeds(String title, String detail){
        if(TextUtils.isEmpty(title))
            return;
        if(!TextUtils.isEmpty(detail))
            m.toCreateDeeds(title,detail);
        else
            m.toCreateDeeds(title,title);
    }

    public void initWorkContext(){
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new MySimpleObserver<Boolean>(){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if(aBoolean)
                            m.toInit();
                    }
                });
    }

    public void toHandleMediaSelected(@NonNull Observable<MediaInfo> mediaInfoRx){
        mediaInfoRx.subscribeOn(Schedulers.computation())
                .doOnNext(new Consumer<MediaInfo>() {
                    @Override
                    public void accept(MediaInfo mediaInfo) throws Exception {
                        try{
                            Glide.with(Utils.getApp()).asDrawable().load(new File(mediaInfo.getUrl())).submit().get();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
                .map(new Function<MediaInfo, Optional<String>>() {
                    @Override
                    public Optional<String> apply(MediaInfo mediaInfo) throws Exception {
                        return m.toBuildMd(mediaInfo.getUrl());
                    }
                })
                .subscribe(new MySimpleObserver<Optional<String>>() {
                    @Override
                    public void onNext(Optional<String> s) {
                        super.onNext(s);
                        if(s.isPresent())
                            GtdMachine.getInstance().getCurrState(null).toInbox(s.get(), s.get(), true);
                    }
                });
    }

    class MainM{
        MdBuildFactory factory;

        MainM(){
            factory = new MdBuildFactory() ;
        }

        void toCreateDeeds(String title, String detail){
            GtdMachine.getInstance().getCurrState(null).toInbox(title, detail);
        }
        void toInit(){
            LibContextInit.initWorkingFileSystem();
            GreenDaoAction.getInstance().init();
        }
        Optional<String> toBuildMd(String rawTxt){
            return factory.toBuildImageMd(rawTxt);
        }
    }

    public interface IMainV{
        public @NonNull FragmentActivity getAttachAtivity();
    }
}
