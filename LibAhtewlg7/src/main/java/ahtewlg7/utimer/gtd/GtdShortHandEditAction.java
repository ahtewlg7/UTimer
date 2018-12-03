package ahtewlg7.utimer.gtd;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharSink;
import com.google.common.io.Files;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.List;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.util.FileIOAction;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ENTITY_INVALID;

/**
 * Created by lw on 2018/10/18.
 */
public class GtdShortHandEditAction extends AEditAction{
    public static final String TAG = GtdShortHandEditAction.class.getSimpleName();

    private MyBypass myBypass;
    private FileIOAction fileIOAction;

    public GtdShortHandEditAction(ShortHandEntity shortHandEntity) {
        super(shortHandEntity);
        myBypass = new MyBypass();
        fileIOAction = new FileIOAction(shortHandEntity.getAttachFile());
    }

    @Override
    public boolean ifReady(){
        return utimerEntity != null && utimerEntity.ifValid()
                && utimerEntity.getAttachFile() != null && utimerEntity.getAttachFile().ifValid();
    }
    @Override
    public Flowable<Optional<EditElement>> toLoad() {
        return Flowable.just(utimerEntity)
                .flatMap(new Function<AUtimerEntity, Publisher<Optional<EditElement>>>() {
                    @Override
                    public Publisher<Optional<EditElement>> apply(AUtimerEntity utimerEntity) throws Exception {
                        if(!ifReady())
                            throw new UtimerEditException(ERR_EDIT_ENTITY_INVALID);
                        File file = utimerEntity.getAttachFile().getFile();
                        return Flowable.fromIterable(Files.readLines(file, Charsets.UTF_8))
                                .subscribeOn(Schedulers.io())
                                .map(new Function<String, Optional<EditElement>>() {
                                    @Override
                                    public Optional<EditElement> apply(String s) throws Exception {
                                        return Optional.of(new EditElement(s));
                                    }
                                });
                    }
                }).subscribeOn(Schedulers.computation());
    }

    public boolean toSave(String rawTxt){
        return toSave(rawTxt, true);
    }

    public boolean toSave(String rawTxt, boolean append){
        if(!ifValid())
            return false;
        boolean result = false;
        try{
            CharSink writer = fileIOAction.getCharWriter();
            if(append)
                writer = fileIOAction.getAppendCharWriter();
            writer.write(rawTxt);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public Observable<List<EditElement>> toParse(String rawTxt){
        return Observable.just(myBypass.toParseMd(rawTxt));
    }

    public boolean ifValid(){
        return ifReady() && utimerEntity.ifValid() && utimerEntity.getAttachFile().ifValid();
    }
}
