package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.graph.ElementOrder;
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
public class DeedStateGraph {
    private MutableGraph<DeedState> graph;

    public DeedStateGraph(){
        initGraph();
    }

    private void initGraph(){
        graph = GraphBuilder.directed()
                .nodeOrder(ElementOrder.<DeedState>insertion())
                .allowsSelfLoops(true)
                .build();
        initMaybeNode();
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

    public boolean ifEdgeExist(@NonNull DeedState fromState, @NonNull DeedState toState){
        return graph.hasEdgeConnecting(fromState, toState);
    }
    public Set<DeedState> getNextNodeList(@NonNull DeedState currState){
        return graph.successors(currState);
    }
    public Set<DeedState> getPrevNodeList(@NonNull DeedState currState){
        return graph.predecessors(currState);
    }
    public Set<DeedState> getNearNodeList(@NonNull DeedState currState){
        return graph.adjacentNodes(currState);
    }

    private void initMaybeNode(){
        graph.putEdge(MAYBE, SCHEDULE);
        graph.putEdge(MAYBE, ONE_QUARTER);
        graph.putEdge(MAYBE, DEFER);
        graph.putEdge(MAYBE, DELEGATE);
//        graph.putEdge(MAYBE, PROJECT);
        graph.putEdge(MAYBE, WISH);
        graph.putEdge(MAYBE, REFERENCE);
        graph.putEdge(MAYBE, DONE);
        graph.putEdge(MAYBE, USELESS);
        graph.putEdge(MAYBE, TRASH);
    }
    private void initInboxNode(){
        graph.putEdge(INBOX, SCHEDULE);
        graph.putEdge(INBOX, ONE_QUARTER);
        graph.putEdge(INBOX, DEFER);
        graph.putEdge(INBOX, DELEGATE);
//        graph.putEdge(INBOX, PROJECT);
        graph.putEdge(INBOX, WISH);
        graph.putEdge(INBOX, REFERENCE);
        graph.putEdge(INBOX, DONE);
        graph.putEdge(INBOX, USELESS);
        graph.putEdge(INBOX, TRASH);
    }
    private void initReferenceNode(){
        graph.putEdge(REFERENCE, SCHEDULE);
        graph.putEdge(REFERENCE, ONE_QUARTER);
        graph.putEdge(REFERENCE, DEFER);
        graph.putEdge(REFERENCE, DELEGATE);
//        graph.putEdge(REFERENCE, PROJECT);
        graph.putEdge(REFERENCE, WISH);
        graph.putEdge(REFERENCE, DONE);
        graph.putEdge(REFERENCE, USELESS);
        graph.putEdge(REFERENCE, TRASH);
    }
    private void initOneQuarterNode(){
        graph.putEdge(ONE_QUARTER, SCHEDULE);
        graph.putEdge(ONE_QUARTER, DEFER);
        graph.putEdge(ONE_QUARTER, DELEGATE);
//        graph.putEdge(ONE_QUARTER, PROJECT);
        graph.putEdge(ONE_QUARTER, WISH);
        graph.putEdge(ONE_QUARTER, DONE);
        graph.putEdge(ONE_QUARTER, USELESS);
        graph.putEdge(ONE_QUARTER, TRASH);
    }
    private void initDeferNode(){
        graph.putEdge(DEFER, SCHEDULE);
        graph.putEdge(DEFER, ONE_QUARTER);
        graph.putEdge(DEFER, DELEGATE);
//        graph.putEdge(DEFER, PROJECT);
        graph.putEdge(DEFER, WISH);
        graph.putEdge(DEFER, DONE);
        graph.putEdge(DEFER, USELESS);
        graph.putEdge(DEFER, TRASH);
    }
    private void initDelegateNode(){
        graph.putEdge(DELEGATE, SCHEDULE);
        graph.putEdge(DELEGATE, ONE_QUARTER);
        graph.putEdge(DELEGATE, DEFER);
//        graph.putEdge(DELEGATE, PROJECT);
        graph.putEdge(DELEGATE, WISH);
        graph.putEdge(DELEGATE, DONE);
        graph.putEdge(DELEGATE, USELESS);
        graph.putEdge(DELEGATE, TRASH);
    }
    private void initProjectNode(){
        graph.putEdge(PROJECT, DONE);
        graph.putEdge(PROJECT, TRASH);
    }
    private void initCalendarNode(){
        graph.putEdge(CALENDAR, ONE_QUARTER);
        graph.putEdge(CALENDAR, DEFER);
        graph.putEdge(CALENDAR, DELEGATE);
//        graph.putEdge(CALENDAR, PROJECT);
        graph.putEdge(CALENDAR, WISH);
        graph.putEdge(CALENDAR, DONE);
        graph.putEdge(CALENDAR, USELESS);
        graph.putEdge(CALENDAR, TRASH);
    }
    private void initWishNode(){
        graph.putEdge(WISH, SCHEDULE);
        graph.putEdge(WISH, ONE_QUARTER);
        graph.putEdge(WISH, DEFER);
        graph.putEdge(WISH, DELEGATE);
//        graph.putEdge(WISH, PROJECT);
        graph.putEdge(WISH, DONE);
        graph.putEdge(WISH, USELESS);
        graph.putEdge(WISH, TRASH);
    }
    private void initUselessNode(){
        graph.putEdge(USELESS, SCHEDULE);
        graph.putEdge(USELESS, ONE_QUARTER);
        graph.putEdge(USELESS, DEFER);
        graph.putEdge(USELESS, DELEGATE);
//        graph.putEdge(USELESS, PROJECT);
        graph.putEdge(USELESS, DONE);
        graph.putEdge(USELESS, WISH);
        graph.putEdge(USELESS, TRASH);
    }
    private void initDoneNode(){
        graph.putEdge(DONE, TRASH);
    }
    private void initTrashNode(){
        graph.putEdge(TRASH, TRASH);
    }
}
