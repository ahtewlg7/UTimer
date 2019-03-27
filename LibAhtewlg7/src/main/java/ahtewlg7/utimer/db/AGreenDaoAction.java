package ahtewlg7.utimer.db;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.autogen.DaoSession;
import ahtewlg7.utimer.util.MySafeFlowableOnSubscribe;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by lw on 2016/9/6.
 */
public abstract class AGreenDaoAction<T,K>{
    public static final String TAG = AGreenDaoAction.class.getSimpleName();

    public abstract Optional<T> queryByKey(K key);
    protected abstract @NonNull AbstractDao<T,Long> getCustomDao();

    protected AbstractDao<T,Long> greenDao;
    protected DaoSession daoSession;

    protected AGreenDaoAction(){
        daoSession  = GreenDaoAction.getInstance().getDaoSession();
        greenDao    = getCustomDao();
    }

    /*
    * when you want to save some, no need to do it in a new Thread,
    * which will lead to save failed even without a log.
    * For example, to do it whit Rxjava.subscribeOn(Schedulers.io())
    */

    public long insert(@NonNull T entity) {
        return greenDao.insertOrReplace(entity);
    }

    public Flowable<Long> insert(@NonNull Flowable<T> flowable){
        return flowable.map(new Function<T, Long>() {
            @Override
            public Long apply(T t) throws Exception {
                checkNotNull(t);
                return insert(t);
            }
        });
    }

    public boolean insertList(@NonNull List<T> entityList) {
        if (entityList.isEmpty())
            return false;

        greenDao.insertInTx(entityList);
        return true;
    }

    public void delete(@NonNull T entity) {
        greenDao.delete(entity);
    }
    public Flowable<T> delete(@NonNull Flowable<T> flowable){
       return flowable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                delete(t);
            }
        });
    }

    public void update(@NonNull T entity) {
        greenDao.update(entity);
    }
    public Observable<T> update(@NonNull Observable<T> observable){
        return observable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                greenDao.update(t);
            }
        });
    }

    public List<T> loadAll(){
        return greenDao.loadAll();
    }

    public Flowable<T> loadAllRx(){
//        return Flowable.fromIterable(loadAll());
        return Flowable.create(new MySafeFlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                super.subscribe(e);
                try{
                    List<T> list = loadAll();
                    for(int i = 0; i < list.size();){
                        if(ifShouldHoldEmit()) continue;
                        e.onNext(list.get(i));
                        i++;
                    }
                    e.onComplete();
                }catch (Exception exc){
                    e.onError(exc.getCause());
                }
            }
        }, BackpressureStrategy.MISSING);
    }

    protected List<T> query(@NonNull IGreenDaoQueryFiltVisitor<T> filtVisitor){
        List<T> list = null;
        try{
            list = filtVisitor.toFilt(greenDao.queryBuilder()).list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    protected Observable<List<T>> query(@NonNull Observable<IGreenDaoQueryFiltVisitor<T>> filtVisitorObservable){
        return filtVisitorObservable.map(new Function<IGreenDaoQueryFiltVisitor<T>, List<T>>() {
            @Override
            public List<T> apply(IGreenDaoQueryFiltVisitor<T> queryFiltVisitor) throws Exception {
                checkNotNull(queryFiltVisitor);
                return queryFiltVisitor.toFilt(greenDao.queryBuilder()).list();
            }
        });
    }

    public interface IGreenDaoQueryFiltVisitor<K> {
        public QueryBuilder<K> toFilt(QueryBuilder<K> queryBuilder);
    }
}
