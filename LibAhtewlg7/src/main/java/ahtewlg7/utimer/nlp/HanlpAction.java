package ahtewlg7.utimer.nlp;

import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IIOAdapter;
import com.hankcs.hanlp.seg.common.Term;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;

/**
 * Created by lw on 2019/2/25.
 */
public class HanlpAction implements INlp {

    public static final int DEFAULT_KEY_NUM             = 5;
    public static final String KEY_ENV_HANLP_ROOT_DIR   = "HANLP_ROOT";

    private boolean inited;
    private FileSystemAction fileSystemAction;

    public HanlpAction(){
        fileSystemAction = new FileSystemAction();
    }

    @Override
    public void initNLP() {
        if(inited)
            return;
        if(ifSdcardSourceExist())
            loadSdcardSource();
        if(!inited)
            loadAssetSource();
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
    public boolean ifSdcardSourceExist(){
        boolean result = false;
        try{
            StringBuilder nlpAbsPath = new StringBuilder(fileSystemAction.getNlpDataAbsPath())
                    .append("data").append(File.separator)
                    .append("dictionary").append(File.separator);
            File file = new File(nlpAbsPath.toString());
            //todo
            FluentIterable<File> fileList = Files.fileTreeTraverser().preOrderTraversal(file).filter(new Predicate<File>() {
                @Override
                public boolean apply(@Nullable File input) {
                    return ifNlpSource(input);
                }
            });
            result = fileList.size() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private void loadAssetSource(){
        try {
            Os.setenv("HANLP_ROOT", "", true);
        } catch (ErrnoException e) {
            inited = false;
            throw new RuntimeException(e);
        }
        final AssetManager assetManager = Utils.getApp().getApplicationContext().getAssets();
        HanLP.Config.IOAdapter = new IIOAdapter(){
            @Override
            public InputStream open(String path) throws IOException{
                return assetManager.open(path);
            }

            @Override
            public OutputStream create(String path) throws IOException{
                throw new IllegalAccessError("不支持写入" + path + "！请在编译前将需要的数据放入app/src/main/assets/data");
            }
        };
    }
    private void loadSdcardSource(){
        try {
            String nlpDir = new FileSystemAction().getNlpDataAbsPath();
            Os.setenv(KEY_ENV_HANLP_ROOT_DIR, nlpDir, true);
            inited = true;
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean ifNlpSource(File file){
        if(!file.isFile())
            return false;
        String fileName = file.getName();
        return fileName.endsWith(".txt") || fileName.endsWith(".txt.bin")
                || fileName.endsWith(".txt.trie.dat") || fileName.endsWith(".table.bin") ;
    }
}
