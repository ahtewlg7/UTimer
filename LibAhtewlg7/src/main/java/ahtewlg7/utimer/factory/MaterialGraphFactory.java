package ahtewlg7.utimer.factory;

import java.io.File;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.graphs.FileTreeGraph;

public class MaterialGraphFactory {
    private static MaterialGraphFactory instance;

    private File rooteFile;
    private FileTreeGraph fileTreeGraph;

    private MaterialGraphFactory(){
        String rootFile = new FileSystemAction().getGtdDocAbsPath();
        rooteFile = new File(rootFile);
        fileTreeGraph = new FileTreeGraph(rooteFile);
    }

    public static MaterialGraphFactory getInstance(){
        if(instance != null)
            instance = new MaterialGraphFactory();
        return instance;
    }

    public FileTreeGraph getFileTreeGraph(){
        return fileTreeGraph;
    }

    public void toInitGraph(){
        fileTreeGraph.initGraph();
        fileTreeGraph.initNodes();
    }

    public void toUpdateFileTree(){
        fileTreeGraph.initNodes();
    }

}
