package com.utimer.ui;

import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.mvp.EndDeedListMvpP;

import ahtewlg7.utimer.entity.view.BaseSectionEntity;

public abstract class ABaseDeedSectionListFragment extends ADeedListFragment<BaseSectionEntity, BaseViewHolder>
        implements EndDeedListMvpP.IEndDeedMvpV {
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}
