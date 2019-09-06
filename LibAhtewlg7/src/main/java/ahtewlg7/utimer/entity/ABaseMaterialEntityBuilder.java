package ahtewlg7.utimer.entity;

/**
 * Created by lw on 2018/10/26.
 */
public abstract class ABaseMaterialEntityBuilder<E extends ABaseMaterialEntity, K extends ABaseMaterialEntityBuilder> extends ABaseEntityBuilder<E,K>{
    protected AAttachFile attachFile;
    protected E entity;

    public K setCopyEntity(E entity){
        this.entity = entity;
        return (K)this;
    }

    public K setAttachFile(AAttachFile attachFile){
        this.attachFile = attachFile;
        return (K)this;
    }
}
