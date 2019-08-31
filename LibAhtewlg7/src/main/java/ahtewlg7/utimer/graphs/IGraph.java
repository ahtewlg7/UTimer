package ahtewlg7.utimer.graphs;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import java.util.Set;

/**
 * Created by lw on 2019/7/13.
 */
public interface IGraph<N> {
    public void initGraph();
    public void initNodes();
    public Optional<Boolean> ifEdgeExist(@NonNull N from, @NonNull N to);
    public Optional<Set<N>> getNextNodeList(@NonNull N node);
    public Optional<Set<N>> getPrevNodeList(@NonNull N node);
    public Optional<Set<N>> getNearNodeList(@NonNull N node);
}
