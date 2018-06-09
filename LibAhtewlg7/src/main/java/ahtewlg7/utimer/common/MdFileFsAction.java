package ahtewlg7.utimer.common;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;

import ahtewlg7.utimer.entity.MdElement;
import ahtewlg7.utimer.enumtype.MdContextErrCode;
import ahtewlg7.utimer.exception.MdContextException;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.mvp.MdContextMvpP;
import ahtewlg7.utimer.util.Logcat;
import in.uncod.android.bypass.Element;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class MdFileFsAction
        implements MdContextMvpP.IMdContextMvpM, MyBypass.MyImageGetter {
    public static final String TAG = MdFileFsAction.class.getSimpleName();

    private File mdSaveFile;
    private MyBypass myBypass;

    public MdFileFsAction(){
        myBypass         = new MyBypass();
    }

    @Override
    public Flowable<Optional<String>> toReadRawContext(@NonNull Flowable<File> contextFileFlowable) {
        return contextFileFlowable.map(new Function<File, Optional<String>>() {
            @Override
            public Optional<String> apply(File file) throws Exception {
                return Optional.fromNullable(FileIOUtils.readFile2String(file));
            }
        });
    }

    @Override
    public Flowable<MdElement> toParseRawContext(@NonNull Flowable<Optional<String>> rawContextFlowable) {
        return rawContextFlowable.flatMap(new Function<Optional<String>, Publisher<MdElement>>() {
            @Override
            public Publisher<MdElement> apply(Optional<String> stringOptional) throws Exception {
                if(!stringOptional.isPresent())
                    throw new MdContextException(MdContextErrCode.ERR_CONTEXT_NULL);
                return myBypass.markdownToSpannableFlowable(stringOptional.get(), MdFileFsAction.this);
            }
        });
    }

    @Override
    public void setMdSaveFilePath(File mdSaveFile) {
        this.mdSaveFile = mdSaveFile;
    }

    @Override
    public Flowable<Boolean> toSaveContext(@NonNull Flowable<Optional<String>> mdFlowable) {
        return mdFlowable.map(new Function<Optional<String>, Boolean>() {
            @Override
            public Boolean apply(Optional<String> mdContextOptional) throws Exception {
                return mdContextOptional.isPresent() && !TextUtils.isEmpty(mdContextOptional.get())
                        && mdSaveFile != null && FileUtils.createOrExistsFile(mdSaveFile)
                        && FileIOUtils.writeFileFromString(mdSaveFile, mdContextOptional.get());
            }
        });
    }

    @Override
    public Flowable<Boolean> toDeleteContext(@NonNull Flowable<File> fileFlowable) {
        return fileFlowable.map(new Function<File, Boolean>() {
            @Override
            public Boolean apply(File file) throws Exception {
                return FileUtils.deleteFile(file);
            }
        });
    }

    @Override
    public Drawable getDrawable(Element element) {
        /*if(spanViewCreator != null) {
            View spanView = spanViewCreator.createSpanView(element);
            if(spanView != null){
                BitmapDrawable bitmpaDrawable = (BitmapDrawable) ViewDrawableUtils.convertViewToDrawable(spanView);
                bitmpaDrawable.setBounds(UPPER_LEFT_X, UPPER_LEFT_Y, bitmpaDrawable.getIntrinsicWidth(), bitmpaDrawable.getIntrinsicHeight());
                return bitmpaDrawable;
            }
        }*/
        Logcat.d(TAG,"getDrawable spanViewCreator is null");
        return null;
    }

    @Override
    public Drawable getDrawable(String source) {
        return null;
    }
}