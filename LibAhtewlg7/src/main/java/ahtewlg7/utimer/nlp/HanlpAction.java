package ahtewlg7.utimer.nlp;

import android.system.ErrnoException;
import android.system.Os;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2019/2/25.
 */
public class HanlpAction implements INlp {

    public static final String KEY_ENV_HANLP_ROOT_DIR = "HANLP_ROOT";

    @Override
    public void initNLP() {
        try {
            String nlpDir = new FileSystemAction().getNlpDataAbsPath();
            Os.setenv(KEY_ENV_HANLP_ROOT_DIR, nlpDir, true);
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    public void toTest(){
        String test = "今天下午3点开会";
        HanLP.Config.enableDebug(true);
        List<Term> testList = HanLP.segment(test);
        for(Term term : testList){
            Logcat.i("test","toTest : " + term.toString());
        }
    }
}
