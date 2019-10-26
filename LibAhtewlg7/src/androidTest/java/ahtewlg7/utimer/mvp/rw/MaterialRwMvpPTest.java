package ahtewlg7.utimer.mvp.rw;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class MaterialRwMvpPTest {
    private MvpV mvpV;
    private File docFile;
    private MaterialRwMvpP materialRwMvpP;
    @Before
    public void setUp() throws Exception {
        mvpV = new MvpV();
        docFile = new File("/sdcard/UTimer/doc/notebook/Project/startup");
        materialRwMvpP = new MaterialRwMvpP(mvpV);
    }

    @Test
    public void toLoadAll() {
        materialRwMvpP.toLoad(docFile);
    }
    class MvpV implements AUtimerRwMvpP.IDbMvpV{
        @Override
        public void onAllLoadStarted() {

        }

        @Override
        public void onAllLoadEnd() {

        }
    }
}