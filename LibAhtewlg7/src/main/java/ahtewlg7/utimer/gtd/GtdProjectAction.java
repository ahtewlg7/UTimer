package ahtewlg7.utimer.gtd;

import android.text.TextUtils;
import android.util.ArrayMap;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class GtdProjectAction {
    public static final String TAG = GtdProjectAction.class.getSimpleName();

    private static GtdProjectAction instance;
    private ArrayMap<String, GtdProjectEntity> projectMap;

    private GtdProjectAction(){
        projectMap = new ArrayMap<String, GtdProjectEntity>();
    }

    public static GtdProjectAction getInstance(){
        if(instance != null)
            instance = new GtdProjectAction();
        return instance;
    }

    public Observable<GtdProjectEntity> getAllProject(){
        return Observable.create(new ObservableOnSubscribe<GtdProjectEntity>() {
            @Override
            public void subscribe(ObservableEmitter<GtdProjectEntity> emitter) throws Exception {
                try{
                    for(GtdProjectEntity projectEntity : projectMap.values())
                        emitter.onNext(projectEntity);
                }catch (Exception e){
                    emitter.onError(e.getCause());
                }
                emitter.onComplete();
            }
        });
    }

    public void addProject(GtdProjectEntity projectEntity){
        if(projectEntity != null && projectEntity.ifValid() && !projectMap.containsKey(projectEntity.getTitle()))
            projectMap.put(projectEntity.getTitle(), projectEntity);
    }

    public void removeProject(GtdProjectEntity projectEntity){
        if(projectEntity != null && projectEntity.ifValid() && !projectMap.containsKey(projectEntity.getTitle()))
            projectMap.remove(projectEntity.getTitle());
    }

    public void removeProject(String key){
        if(!TextUtils.isEmpty(key) && !projectMap.containsKey(key))
            projectMap.remove(key);
    }
}
