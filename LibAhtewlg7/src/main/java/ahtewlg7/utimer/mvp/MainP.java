package ahtewlg7.utimer.mvp;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.common.base.Optional;

import ahtewlg7.utimer.common.MediaKvAction;
import ahtewlg7.utimer.entity.material.MediaInfo;
import ahtewlg7.utimer.factory.MdBuildFactory;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainP {
    private MainM m;
    private IMainV v;
    private PublishSubject<MediaInfo> mediaInfoRx;

    public MainP(@NonNull IMainV v){
        this.v = v;
        m      = new MainM();
        mediaInfoRx = PublishSubject.create();

        toListenNewMedia();
    }

    public void toCreateDeeds(String title, String detail){
        if(TextUtils.isEmpty(title))
            return;
        if(!TextUtils.isEmpty(detail))
            m.toCreateDeeds(title,detail);
        else
            m.toCreateDeeds(title,title);
    }

    public void toHandleMediaSelected(@NonNull MediaInfo mediaInfo){
        mediaInfoRx.onNext(mediaInfo);
    }

    private void toListenNewMedia(){
        mediaInfoRx.subscribeOn(Schedulers.computation())
                .filter(new Predicate<MediaInfo>() {
                    @Override
                    public boolean test(MediaInfo mediaInfo) throws Exception {
                        return !TextUtils.isEmpty(mediaInfo.getUrl()) && !MediaKvAction.getInstance().ifContain(mediaInfo.getUrl());
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
        Optional<String> toBuildMd(String rawTxt){
            return factory.toBuildImageMd(rawTxt);
        }
    }

    public interface IMainV{
        public @NonNull FragmentActivity getAttachAtivity();
    }
}
