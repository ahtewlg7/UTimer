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
import ahtewlg7.utimer.common.MediaKvAction;
import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.entity.material.MediaInfo;
import ahtewlg7.utimer.factory.MdBuildFactory;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
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
                .filter(new Predicate<MediaInfo>() {
                    @Override
                    public boolean test(MediaInfo mediaInfo) throws Exception {
                        boolean filter = false;
                        try{
                            if(!TextUtils.isEmpty(mediaInfo.getUrl()))
                                filter =  MediaKvAction.getInstance().ifContain(mediaInfo.getUrl())
                                        || mediaInfo.getUrl().startsWith(Utils.getApp().getExternalFilesDir(null).getAbsolutePath());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return !filter;
                    }
                })
                .subscribe(new MySimpleObserver<MediaInfo>() {
                    @Override
                    public void onNext(MediaInfo mediaInfo) {
                        super.onNext(mediaInfo);
                        String url = mediaInfo.getUrl();
                        Optional<String>  s = m.toBuildMd(url);
                        if(s.isPresent()) {
                            MediaKvAction.getInstance().encodeObj(mediaInfo.getUrl(),mediaInfo);
                            GtdMachine.getInstance().getCurrState(null).toInbox(s.get(), url, true);
                        }
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
