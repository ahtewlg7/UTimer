package ahtewlg7.utimer.entity;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.enumtype.GtdType;

public class BaseGtdEntity<T extends ABaseEntityBuilder> extends ABaseEntity<T> {
    protected BaseGtdEntity(@Nonnull T t){
        super(t);
    }

    @Override
    public GtdType getGtdType() {
        return GtdType.DEED;
    }
}
