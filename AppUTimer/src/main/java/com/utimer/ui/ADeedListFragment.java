package com.utimer.ui;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.binaryfork.spanny.Spanny;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Optional;
import com.utimer.R;
import com.utimer.common.TagInfoFactory;
import com.utimer.entity.span.DeedSpanMoreTag;
import com.utimer.view.DeedTagBottomSheetDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Set;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.span.SimpleMultiSpanTag;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.ADeedListMvpP;
import ahtewlg7.utimer.span.TextClickableSpan;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.ABaseDeedRecyclerView;

public abstract class ADeedListFragment<T,K extends BaseViewHolder> extends AButterKnifeFragment
        implements ABaseDeedRecyclerView.IDeedSpanner {
    public static final int INIT_POSITION = -1;

    protected abstract DeedState[] getLoadDeedState();
    protected abstract @NonNull ADeedListMvpP getDeedMvpP();
    protected abstract @NonNull ABaseDeedRecyclerView<T,K> getRecyclerView();

    protected int editIndex = -1;
    protected boolean showLifeInfo = true;

    protected ADeedListMvpP listMvpP;
    protected TagInfoFactory tagInfoFactory;
    protected MyClickListener mySpanClickListener;
    protected DeedTagBottomSheetDialog bottomSheetDialog;

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        tagInfoFactory      = new TagInfoFactory();
        mySpanClickListener = new MyClickListener();

        getRecyclerView().init(getContext(), null,
                null, null,
                null,null,
                null,null);
        getRecyclerView().setSpanner(this);
        listMvpP = getDeedMvpP();

        EventBusFatory.getInstance().getDefaultEventBus().register(this);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            toLoadDeedOnShow();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK && data != null) {
            GtdDeedEntity entity = (GtdDeedEntity) data.getSerializable(DeedEditFragment.KEY_ACTION);
            if (entity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(entity);
            else if (entity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(entity);
            }
        }*/
    }

    /**********************************************IGtdActionListMvpV**********************************************//*
    @Override
    public void onItemCreate(GtdDeedEntity data) {
        listMvpP.onItemCreated(data);
    }
    */
    /**********************************************IDeedSpanner**********************************************/
    @NonNull
    @Override
    public SpannableStringBuilder toSpan(int position, boolean highLight, @NonNull GtdDeedEntity item) {
        SimpleMultiSpanTag multiSpanTag = getTagInfo(item);
        DeedSpanMoreTag moreTag         = new DeedSpanMoreTag(item);
        multiSpanTag.setShowBracket(false);
        moreTag.setShowBracket(true);

        @ColorRes int contentColor = getSpanColor(item, R.color.colorPrimary);
        @ColorRes int moreColor    = getSpanColor(item, R.color.colorAccent);
        int bgColor                = MyRInfo.getColorByID(R.color.color_600);
        int fgColor                = MyRInfo.getColorByID(R.color.color_stand_c3);
        Spanny spanny = new Spanny();
        if(multiSpanTag.getTagTitle().isPresent())
            spanny.append(multiSpanTag.getTagTitle().get(),
                    new ForegroundColorSpan(MyRInfo.getColorByID(contentColor)),
                    new StyleSpan(Typeface.BOLD));
        if(item.getDeedState() != DeedState.TRASH && highLight)
            spanny.append(item.getTitle().trim(),
                    new TextClickableSpan(multiSpanTag, mySpanClickListener, MyRInfo.getColorByID(contentColor),false, position),
                    new BackgroundColorSpan(bgColor),
                    new ForegroundColorSpan(fgColor));
        else if(item.getDeedState() != DeedState.TRASH)
            spanny.append(item.getTitle().trim(),
                    new TextClickableSpan(multiSpanTag, mySpanClickListener, MyRInfo.getColorByID(contentColor),false, position));
        else if(highLight)
            spanny.append(item.getTitle().trim(),
                    new TextClickableSpan(multiSpanTag, mySpanClickListener, MyRInfo.getColorByID(contentColor),false, position),
                    new BackgroundColorSpan(bgColor),
                    new ForegroundColorSpan(fgColor),
                    new StrikethroughSpan());
        else
            spanny.append(item.getTitle().trim(),
                    new TextClickableSpan(multiSpanTag, mySpanClickListener, MyRInfo.getColorByID(contentColor),false, position),
                    new StrikethroughSpan());
        if(moreTag.getTagTitle().isPresent())
            spanny.append(moreTag.getTagTitle().get(),
                    new TextClickableSpan(moreTag, mySpanClickListener, MyRInfo.getColorByID(moreColor),false, position));
        return spanny;
    }

    protected @ColorRes int getSpanColor(@NonNull GtdDeedEntity item, @ColorRes int defaultColor){
        int color = defaultColor;
        if(item.getDeedState() == DeedState.DONE || item.getDeedState() == DeedState.TRASH || item.getDeedState() == DeedState.USELESS)
            color = R.color.color_stand_c5;
        return color;
    }

    protected SimpleMultiSpanTag getTagInfo(@NonNull GtdDeedEntity item){
        SimpleMultiSpanTag multiSpanTag = new SimpleMultiSpanTag();
        char currTagAscii = tagInfoFactory.getTagIconAscii(item.getDeedState());
        multiSpanTag.appendTag(String.valueOf(currTagAscii));

        if(item.getWorkDateLifeDetail() != null && showLifeInfo)
            multiSpanTag.appendTag(item.getWorkDateLifeDetail());
        return multiSpanTag;
    }

    protected void toLoadDeedOnShow(){
        if(getLoadDeedState() != null)
            listMvpP.toLoadDeedByState(getLoadDeedState());
    }

    protected void onDeedClick(int position){
    }
    /**********************************************EventBus**********************************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeedBusEvent(DeedBusEvent eventBus) {
        listMvpP.toHandleBusEvent(eventBus, getLoadDeedState());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeedDoneBusEvent(DeedDoneBusEvent eventBus) {
        listMvpP.toHandleBusEvent(eventBus, getLoadDeedState());
    }

    /**********************************************IGtdActionListMvpV**********************************************/

    class MyClickListener implements TextClickableSpan.ITextSpanClickListener,
            DeedTagBottomSheetDialog.OnItemClickListener, DeedTagBottomSheetDialog.OnDismissListener {
        //+++++++++++++++++++++++++++++++++++ITextSpanClickListener+++++++++++++++++++++++++++++++
        @Override
        public void onSpanClick(int position, Object o) {
            if(o instanceof SimpleMultiSpanTag)
                onDeedClick(position);
            else if(o instanceof DeedSpanMoreTag)
                createBottomSheet(position);
        }
        //+++++++++++++++++++++++++++++++++++OnItemClickListener+++++++++++++++++++++++++++++++
        @Override
        public void onTagClick(int position, DeedState targetState) {
            listMvpP.toTagDeed((GtdDeedEntity) getRecyclerView().getItem(position), targetState, position);
        }
        //+++++++++++++++++++++++++++++++++++OnDismissListener+++++++++++++++++++++++++++++++
        @Override
        public void onDismiss(DialogInterface dialog) {
            getRecyclerView().toHighLight(Optional.absent());
        }
    }
    protected void createBottomSheet(int position){
        if(position < 0 || position >= getRecyclerView().getAdapter().getItemCount()) {
            Logcat.d("Warning","createBottomSheet failed");
            return;
        }
        GtdDeedEntity currEntity = (GtdDeedEntity)getRecyclerView().getItem(position);
        Set<DeedState> deedStateSet = listMvpP.getNextState(currEntity);
        if(deedStateSet == null)
            return;

        if (bottomSheetDialog == null) {
            bottomSheetDialog = new DeedTagBottomSheetDialog(getContext());
            bottomSheetDialog.setOnItemClickListener(mySpanClickListener);
            bottomSheetDialog.setOnDismissListener(mySpanClickListener);
        }
        getRecyclerView().toHighLight(Optional.of(position));
        bottomSheetDialog.toShow(deedStateSet, position);
    }
    protected void toCreateEditDialog(@NonNull GtdDeedEntity deedEntity){
        new MaterialDialog.Builder(getContext()).title(R.string.edit)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", deedEntity.getTitle().trim(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //do nothing
                    }
                })
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String title = dialog.getInputEditText().getText().toString();
                        if(TextUtils.isEmpty(title))
                            ToastUtils.showShort(R.string.prompt_not_be_empty);
                        else
                            GtdMachine.getInstance().getCurrState(deedEntity).toEdit(deedEntity,title, title);
                    }
                }).show();
    }
}
