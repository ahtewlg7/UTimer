package ahtewlg7.utimer.graphs;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Set;

import ahtewlg7.utimer.enumtype.DeedState;

import static ahtewlg7.utimer.enumtype.DeedState.CALENDAR;
import static ahtewlg7.utimer.enumtype.DeedState.DEFER;
import static ahtewlg7.utimer.enumtype.DeedState.DELEGATE;
import static ahtewlg7.utimer.enumtype.DeedState.DONE;
import static ahtewlg7.utimer.enumtype.DeedState.INBOX;
import static ahtewlg7.utimer.enumtype.DeedState.MAYBE;
import static ahtewlg7.utimer.enumtype.DeedState.ONE_QUARTER;
import static ahtewlg7.utimer.enumtype.DeedState.PROJECT;
import static ahtewlg7.utimer.enumtype.DeedState.REFERENCE;
import static ahtewlg7.utimer.enumtype.DeedState.SCHEDULE;
import static ahtewlg7.utimer.enumtype.DeedState.TRASH;
import static ahtewlg7.utimer.enumtype.DeedState.USELESS;
import static ahtewlg7.utimer.enumtype.DeedState.WISH;

/**
 * Created by lw on 2019/7/13.
 */
public class DeedStateGraph implements IGraph<DeedState> {
    private Graph<DeedState> graph;
    
    public DeedStateGraph(){
        initGraph();
    }

    @Override
    public void initGraph(){
        graph =  GraphBuilder.directed()
                .nodeOrder(ElementOrder.<DeedState>insertion())
                .allowsSelfLoops(true)
                .build();
    }

    @Override
    public void initNodes() {
        initMaybeNode();
        initScheduleNode();
        initInboxNode();
        initCalendarNode();
        initDeferNode();
        initDelegateNode();
        initDoneNode();
        initOneQuarterNode();
        initProjectNode();
        initReferenceNode();
        initTrashNode();
        initUselessNode();
        initWishNode();
    }

    @Override
    public Optional<Boolean> ifEdgeExist(@NonNull DeedState from, @NonNull DeedState to) {
        return graph != null ? Optional.of(graph.hasEdgeConnecting(from, to)) : Optional.absent();
    }

    @Override
    public Optional<Set<DeedState>> getNextNodeList(@NonNull DeedState node) {
        return graph != null ? Optional.of(graph.successors(node)) : Optional.absent();
    }

    @Override
    public Optional<Set<DeedState>> getPrevNodeList(@NonNull DeedState node) {
        return graph != null ? Optional.of(graph.predecessors(node)) : Optional.absent();
    }

    @Override
    public Optional<Set<DeedState>> getNearNodeList(@NonNull DeedState node) {
        return graph != null ? Optional.of(graph.adjacentNodes(node)) : Optional.absent();
    }

    private void initMaybeNode(){
        ((MutableGraph)graph).putEdge(MAYBE, SCHEDULE);
        ((MutableGraph)graph).putEdge(MAYBE, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(MAYBE, DEFER);
        ((MutableGraph)graph).putEdge(MAYBE, DELEGATE);
//        ((MutableGraph)graph).putEdge(MAYBE, PROJECT);
        ((MutableGraph)graph).putEdge(MAYBE, WISH);
        ((MutableGraph)graph).putEdge(MAYBE, REFERENCE);
        ((MutableGraph)graph).putEdge(MAYBE, DONE);
        ((MutableGraph)graph).putEdge(MAYBE, USELESS);
        ((MutableGraph)graph).putEdge(MAYBE, TRASH);
    }
    private void initScheduleNode(){
        ((MutableGraph)graph).putEdge(SCHEDULE, DEFER);
        ((MutableGraph)graph).putEdge(SCHEDULE, WISH);
        ((MutableGraph)graph).putEdge(SCHEDULE, DONE);
        ((MutableGraph)graph).putEdge(SCHEDULE, USELESS);
        ((MutableGraph)graph).putEdge(SCHEDULE, TRASH);
    }
    private void initInboxNode(){
        ((MutableGraph)graph).putEdge(INBOX, SCHEDULE);
        ((MutableGraph)graph).putEdge(INBOX, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(INBOX, DEFER);
        ((MutableGraph)graph).putEdge(INBOX, DELEGATE);
//        ((MutableGraph)graph).putEdge(INBOX, PROJECT);
        ((MutableGraph)graph).putEdge(INBOX, WISH);
        ((MutableGraph)graph).putEdge(INBOX, REFERENCE);
        ((MutableGraph)graph).putEdge(INBOX, DONE);
        ((MutableGraph)graph).putEdge(INBOX, USELESS);
        ((MutableGraph)graph).putEdge(INBOX, TRASH);
    }
    private void initReferenceNode(){
        ((MutableGraph)graph).putEdge(REFERENCE, SCHEDULE);
        ((MutableGraph)graph).putEdge(REFERENCE, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(REFERENCE, DEFER);
        ((MutableGraph)graph).putEdge(REFERENCE, DELEGATE);
//        ((MutableGraph)graph).putEdge(REFERENCE, PROJECT);
        ((MutableGraph)graph).putEdge(REFERENCE, WISH);
        ((MutableGraph)graph).putEdge(REFERENCE, DONE);
        ((MutableGraph)graph).putEdge(REFERENCE, USELESS);
        ((MutableGraph)graph).putEdge(REFERENCE, TRASH);
    }
    private void initOneQuarterNode(){
        ((MutableGraph)graph).putEdge(ONE_QUARTER, SCHEDULE);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, DEFER);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, DELEGATE);
//        ((MutableGraph)graph).putEdge(ONE_QUARTER, PROJECT);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, WISH);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, DONE);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, USELESS);
        ((MutableGraph)graph).putEdge(ONE_QUARTER, TRASH);
    }
    private void initDeferNode(){
        ((MutableGraph)graph).putEdge(DEFER, SCHEDULE);
        ((MutableGraph)graph).putEdge(DEFER, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(DEFER, DELEGATE);
//        ((MutableGraph)graph).putEdge(DEFER, PROJECT);
        ((MutableGraph)graph).putEdge(DEFER, WISH);
        ((MutableGraph)graph).putEdge(DEFER, DONE);
        ((MutableGraph)graph).putEdge(DEFER, USELESS);
        ((MutableGraph)graph).putEdge(DEFER, TRASH);
    }
    private void initDelegateNode(){
        ((MutableGraph)graph).putEdge(DELEGATE, SCHEDULE);
        ((MutableGraph)graph).putEdge(DELEGATE, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(DELEGATE, DEFER);
//        ((MutableGraph)graph).putEdge(DELEGATE, PROJECT);
        ((MutableGraph)graph).putEdge(DELEGATE, WISH);
        ((MutableGraph)graph).putEdge(DELEGATE, DONE);
        ((MutableGraph)graph).putEdge(DELEGATE, USELESS);
        ((MutableGraph)graph).putEdge(DELEGATE, TRASH);
    }
    private void initProjectNode(){
        ((MutableGraph)graph).putEdge(PROJECT, DONE);
        ((MutableGraph)graph).putEdge(PROJECT, TRASH);
    }
    private void initCalendarNode(){
        ((MutableGraph)graph).putEdge(CALENDAR, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(CALENDAR, DEFER);
        ((MutableGraph)graph).putEdge(CALENDAR, DELEGATE);
//        ((MutableGraph)graph).putEdge(CALENDAR, PROJECT);
        ((MutableGraph)graph).putEdge(CALENDAR, WISH);
        ((MutableGraph)graph).putEdge(CALENDAR, DONE);
        ((MutableGraph)graph).putEdge(CALENDAR, USELESS);
        ((MutableGraph)graph).putEdge(CALENDAR, TRASH);
    }
    private void initWishNode(){
        ((MutableGraph)graph).putEdge(WISH, REFERENCE);
        ((MutableGraph)graph).putEdge(WISH, SCHEDULE);
        ((MutableGraph)graph).putEdge(WISH, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(WISH, DEFER);
        ((MutableGraph)graph).putEdge(WISH, DELEGATE);
//        ((MutableGraph)graph).putEdge(WISH, PROJECT);
        ((MutableGraph)graph).putEdge(WISH, DONE);
        ((MutableGraph)graph).putEdge(WISH, USELESS);
        ((MutableGraph)graph).putEdge(WISH, TRASH);
    }
    private void initUselessNode(){
        ((MutableGraph)graph).putEdge(USELESS, REFERENCE);
        ((MutableGraph)graph).putEdge(USELESS, SCHEDULE);
        ((MutableGraph)graph).putEdge(USELESS, ONE_QUARTER);
        ((MutableGraph)graph).putEdge(USELESS, DEFER);
        ((MutableGraph)graph).putEdge(USELESS, DELEGATE);
//        ((MutableGraph)graph).putEdge(USELESS, PROJECT);
        ((MutableGraph)graph).putEdge(USELESS, DONE);
        ((MutableGraph)graph).putEdge(USELESS, WISH);
        ((MutableGraph)graph).putEdge(USELESS, TRASH);
    }
    private void initDoneNode(){
        ((MutableGraph)graph).putEdge(DONE, TRASH);
    }
    private void initTrashNode(){
        ((MutableGraph)graph).putEdge(TRASH, WISH);
        ((MutableGraph)graph).putEdge(TRASH, TRASH);
    }
}
