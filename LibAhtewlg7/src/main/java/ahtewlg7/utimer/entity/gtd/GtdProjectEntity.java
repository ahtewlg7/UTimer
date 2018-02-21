package ahtewlg7.utimer.entity.gtd;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdProjectEntity extends AGtdTaskEntity {
    public static final String TAG = GtdProjectEntity.class.getSimpleName();

    protected List<TaskPurposeBean> purposeList;
    protected List<TaskPricipleBean> pricipleList;
    protected List<TaskEnvisionBean> envisionList;

    public GtdProjectEntity(){
        super();
        purposeList  = new ArrayList<TaskPurposeBean>();
        pricipleList = new ArrayList<TaskPricipleBean>();
        envisionList = new ArrayList<TaskEnvisionBean>();
    }

    public void initByInbox(GtdInboxEntity inboxEntity){

    }

    @Override
    public GtdType getTaskType() {
        return GtdType.PROJECT;
    }

    public void addPurpose(TaskPurposeBean purposeBean){
        purposeList.add(purposeBean);
    }
    public void removePurpose(TaskPurposeBean purposeBean){
        purposeList.remove(purposeBean);
    }
    public List<TaskPurposeBean> getPurposeList(){
        return purposeList;
    }

    public void addPriciple(TaskPricipleBean pricipleBean){
        pricipleList.add(pricipleBean);
    }
    public void removePriciple(TaskPricipleBean pricipleBean){
        pricipleList.remove(pricipleBean);
    }
    public List<TaskPricipleBean> getPricipleList(){
        return pricipleList;
    }

    public void addEnvision(TaskEnvisionBean envisionBean){
        envisionList.add(envisionBean);
    }
    public void removeEnvision(TaskEnvisionBean envisionBean){
        envisionList.remove(envisionBean);
    }
    public List<TaskEnvisionBean> getEnvisionList(){
        return envisionList;
    }

    @Override
    public String toString() {
        StringBuilder tmp =  new StringBuilder();
        tmp.append(super.toString());
        for(TaskPurposeBean purposeBean : purposeList){
            String bean =  "{" + purposeBean.toString() + "}";
            tmp.append(bean);
        }
        for(TaskPricipleBean pricipleBean : pricipleList){
            String bean =  "{" + pricipleBean.toString() + "}";
            tmp.append(bean);
        }
        for(TaskEnvisionBean envisionBean : envisionList){
            String bean =  "{" + envisionBean.toString() + "}";
            tmp.append(bean);
        }
        return tmp.toString();
    }
}
