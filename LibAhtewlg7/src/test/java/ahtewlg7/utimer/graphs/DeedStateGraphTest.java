package ahtewlg7.utimer.graphs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DeedStateGraphTest {
    DeedStateGraph graph = null;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void toInit(){
        graph = new DeedStateGraph();
    }

    @Test
    public void toTestInitGraph(){
    }

    @Test
    public void toTestInitNodes(){
        graph.initNodes();
    }
}