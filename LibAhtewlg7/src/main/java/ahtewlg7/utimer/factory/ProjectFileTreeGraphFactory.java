package ahtewlg7.utimer.factory;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

import ahtewlg7.utimer.graphs.FileTreeGraph;

public class ProjectFileTreeGraphFactory{
    private static ProjectFileTreeGraphFactory instance;

    private Map<File, FileTreeGraph> fileFileTreeGraphMap;

    private ProjectFileTreeGraphFactory(){
        fileFileTreeGraphMap = Maps.newHashMap();
    }

    public static ProjectFileTreeGraphFactory getInstance(){
        if(instance == null)
            instance = new ProjectFileTreeGraphFactory();
        return instance;
    }

    public void addFileTreeGraph(File rootFile){
        if(rootFile != null && rootFile.exists()){
            FileTreeGraph fileTreeGraph = new FileTreeGraph(rootFile);
            fileTreeGraph.initGraph();
            fileTreeGraph.initNodes();
            fileFileTreeGraphMap.put(rootFile, fileTreeGraph);
        }
    }

    public FileTreeGraph getFileTreeGraph(File rootFile){
        if(rootFile != null && rootFile.exists())
            return fileFileTreeGraphMap.get(rootFile);
        return null;
    }

    public void remoteFileTreeGraph(File rootFile){
        if(rootFile != null && rootFile.exists())
            fileFileTreeGraphMap.remove(rootFile);
    }

    public void toUpdateFileTree(File rootFile){
        FileTreeGraph fileTreeGraph = getFileTreeGraph(rootFile);
        if(fileTreeGraph != null)
            fileTreeGraph.initNodes();
        else
            addFileTreeGraph(rootFile);
    }
}
