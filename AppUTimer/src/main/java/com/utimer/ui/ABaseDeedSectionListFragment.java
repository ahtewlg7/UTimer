package com.utimer.ui;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle3.LifecycleProvider;

import ahtewlg7.utimer.entity.view.BaseSectionEntity;
import ahtewlg7.utimer.mvp.ADeedListMvpP;

public abstract class ABaseDeedSectionListFragment<T extends BaseSectionEntity> extends ADeedListFragment<T, BaseViewHolder>
        implements ADeedListMvpP.IADeedMvpV {
    @NonNull
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}
