package ahtewlg7.utimer.nlp;

import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2019/2/25.
 */
public class HanlpAction implements INlp {

    public static final int DEFAULT_KEY_NUM          = 5;
    public static final String KEY_ENV_HANLP_ROOT_DIR   = "HANLP_ROOT";

    @Override
    public void initNLP() {
        try {
            String nlpDir = new FileSystemAction().getNlpDataAbsPath();
            Os.setenv(KEY_ENV_HANLP_ROOT_DIR, nlpDir, true);
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getKeyWords(String raw){
        if(TextUtils.isEmpty(raw))
            return null;
        return HanLP.extractKeyword(raw, DEFAULT_KEY_NUM);
    }
    public List<String> getKeyWords(String raw, int keyWordNum){
        if(TextUtils.isEmpty(raw))
            return null;
        return HanLP.extractKeyword(raw, keyWordNum > 0 ? keyWordNum : DEFAULT_KEY_NUM);
    }

    public void toTest(){
        String test = "今天下午3点开会";
        HanLP.Config.enableDebug(true);
        List<Term> testList = HanLP.segment(test);
        for(Term term : testList){
            Logcat.i("test","toTest Term: " + term.toString());
        }
        List<String>  keywords = HanLP.extractKeyword(test,3);
        for(String keyword : keywords){
            Logcat.i("test","toTest keyword: " + keyword);
        }
    }
}
