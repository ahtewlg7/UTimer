package ahtewlg7.utimer.gtd;

import android.text.TextUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharSink;
import com.google.common.io.Files;

import org.reactivestreams.Publisher;

import java.io.File;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.util.FileIOAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ENTITY_INVALID;

/**
 * Created by lw on 2018/12/9.
 */
public class BaseTxtEditAction extends AEditAction {
    public static final String TAG = BaseTxtEditAction.class.getSimpleName();

    private MyBypass myBypass;
    private FileIOAction fileIOAction;

    public BaseTxtEditAction(AUtimerEntity utimerEntity) {
        super(utimerEntity);
        myBypass = new MyBypass();
    }

    @Override
    public boolean ifReady(){
        return utimerEntity != null && utimerEntity.ifValid()
                && utimerEntity.getAttachFile() != null && utimerEntity.ensureAttachFileExist();
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
                                        if(TextUtils.isEmpty(s.trim()))
                                            return Optional.absent();
                                        EditElement element = new EditElement(s.trim() + System.lineSeparator());
                                        element.setMdCharSequence(toParse(s));
                                        return Optional.of(element);
                                    }
                                });
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public boolean toSave(String rawTxt){
        return toSave(rawTxt, true);
    }

    public boolean toSave(String rawTxt, boolean append){
        if(rawTxt == null || !ifReady()) {
            Logcat.i(TAG,"toSave cancel");
            return false;
        }
        boolean result = false;
        try{
            if(fileIOAction == null)
                fileIOAction = new FileIOAction(utimerEntity.getAttachFile());
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

    public CharSequence toParse(String rawTxt){
        return myBypass.markdownToSpannable(rawTxt);
    }
}
