package ahtewlg7.utimer.graphs;

import androidx.test.filters.RequiresDevice;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class FileTreeGraphTest{
    private File rootFile = null;
    private FileTreeGraph graph = null;
    @Before
    public void setUp() throws Exception {
        rootFile = new File("/sdcard/UTimer/doc/notebook/Project");
        graph    = new FileTreeGraph(rootFile);
    }

    @Test
    @RequiresDevice
    public void toTestGraph(){
        graph.initGraph();
    }
    @Test
    @RequiresDevice
    public void tInitNodes(){
        graph.initNodes();
        System.out.println("graph = " + graph.getGraph().toString());
    }
}