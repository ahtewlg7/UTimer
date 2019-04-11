package ahtewlg7.utimer.nlp;

import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;

/**
 * Created by lw on 2019/2/25.
 */
public class HanlpAction implements INlp {

    public static final int DEFAULT_KEY_NUM             = 5;
    public static final String KEY_ENV_HANLP_ROOT_DIR   = "HANLP_ROOT";

    private boolean inited;

    @Override
    public void initNLP() {
        try {
            String nlpDir = new FileSystemAction().getNlpDataAbsPath();
            Os.setenv(KEY_ENV_HANLP_ROOT_DIR, nlpDir, true);
            inited = true;
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getKeyWords(String raw){
        if(!inited || TextUtils.isEmpty(raw))
            return null;
        return HanLP.extractKeyword(raw, DEFAULT_KEY_NUM);
    }
    public List<String> getKeyWords(String raw, int keyWordNum){
        if(!inited || TextUtils.isEmpty(raw))
            return null;
        return HanLP.extractKeyword(raw, keyWordNum > 0 ? keyWordNum : DEFAULT_KEY_NUM);
    }

    public List<Term>  toSegment(String raw){
        if(!inited || TextUtils.isEmpty(raw))
            return null;
        return HanLP.newSegment().seg(raw);
    }
    public List<Term>  toSegment(String raw, boolean nameEnable,boolean placeEnable, boolean orgEnable){
        if(!inited || TextUtils.isEmpty(raw))
            return null;
        return HanLP.newSegment().enableNameRecognize(nameEnable).enablePlaceRecognize(placeEnable)
                .enableOrganizationRecognize(orgEnable).seg(raw);
    }
}
