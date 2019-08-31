package ahtewlg7.utimer.graphs;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.util.FileAttrAction;

/**
 * Created by lw on 2019/7/13.
 */
public class FileTreeGraph implements IGraph<File> {
    private File rootFile;
    private ValueGraph graph;
    private Map<String, File> leafFileMap;

    public FileTreeGraph(File rootFile){
        this.rootFile  = rootFile;
        leafFileMap = Maps.newHashMap();
        initGraph();
    }

    public ValueGraph getGraph(){
        return graph;
    }

    public Collection<File> getAllLeafFile(){
        return leafFileMap.values();
    }
    public File getLeafFile(String fileName){
        return leafFileMap.get(fileName);
    }

    @Override
    public void initGraph(){
        graph =  ValueGraphBuilder.directed()
                .nodeOrder(ElementOrder.<File>insertion())
                .allowsSelfLoops(false)
                .build();
    }

    @Override
    public Optional<Boolean> ifEdgeExist(@NonNull File from, @NonNull File to) {
        return graph != null ? Optional.of(graph.hasEdgeConnecting(from, to)) : Optional.absent();
    }

    @Override
    public Optional<Set<File>> getNextNodeList(@NonNull File node) {
        return graph != null ? Optional.of(graph.successors(node)) : Optional.absent();
    }

    @Override
    public Optional<Set<File>> getPrevNodeList(@NonNull File node) {
        return graph != null ? Optional.of(graph.predecessors(node)) : Optional.absent();
    }

    @Override
    public Optional<Set<File>> getNearNodeList(@NonNull File node) {
        return graph != null ? Optional.of(graph.adjacentNodes(node)) : Optional.absent();
    }

    @Override
    public void initNodes() {
        if(rootFile == null)
            return;
        leafFileMap.clear();
        Iterable<File> fileIterable = new FileSystemAction().listSubFile(rootFile);
        for(File file : fileIterable) {
            boolean result = updateEdge(file);
            if(result && file.isFile())
                leafFileMap.put(file.getName(), file);
        }
    }

    private boolean updateEdge(File file){
        if(graph == null || file == null || !file.exists() || file.equals(rootFile))
            return false;
        DateTime createTime = new FileAttrAction(file).getLassModifyTime();
        if(createTime != null)
            ((MutableValueGraph)graph).putEdgeValue(file.getParentFile(), file, createTime );
        return true;
    }
}
