package com.utimer.ui;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.mvp.EndDeedListMvpP;

import ahtewlg7.utimer.entity.view.BaseSectionEntity;

public abstract class ABaseDeedSectionListFragment<T extends BaseSectionEntity> extends ADeedListFragment<T, BaseViewHolder>
        implements EndDeedListMvpP.IEndDeedMvpV {
    @NonNull
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}
