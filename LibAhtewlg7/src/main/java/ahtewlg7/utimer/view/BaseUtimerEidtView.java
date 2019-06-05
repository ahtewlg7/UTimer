package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.common.UTimerRawReadAction;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.EditMode;
import ahtewlg7.utimer.enumtype.ElementEditType;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.mvp.IRxLifeCycleBindView;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import ahtewlg7.utimer.view.md.MdEditText;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ALL_READY_GO;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ATTACH_VIEW_NOT_READY;

/**
 * Created by lw on 2019/2/3.
 */
public class BaseUtimerEidtView extends ABaseLinearRecyclerView<EditElement>{
    public static final int INIT_POSITION = -1;

    protected int lastAccessPosition    = INIT_POSITION;
    protected int preEditPosition       = INIT_POSITION;
    protected boolean isTxtChanged      = false;

    protected AUtimerEntity utimerEntity;
    protected List<EditElement> editElementList;
    protected Table<Integer, Integer, EditElement> editElementTable;

    protected MyBypass myBypass;
    protected Subscription loadSubscription;
    protected Disposable clickPositionDisposable;
    protected Disposable insertDisposable;
    protected Disposable modifyDisposable;
    protected PublishSubject<Integer> clickPositionRx;

    protected IOnEditModeListener editModeListener;
    protected IUtimerAttachEditView attachEditView;
    protected MyClickListener myClickListener;

    public BaseUtimerEidtView(Context context) {
        super(context);
        init();
    }

    public BaseUtimerEidtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseUtimerEidtView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public @LayoutRes int getViewItemLayout() {
        return R.layout.view_md_edit;
    }

    @Override
    public void init(Context context, List<EditElement> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     IOnItemTouchListener itemTouchListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemTouchListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    @NonNull
    @Override
    public BaseItemAdapter<EditElement> createAdapter(List<EditElement> entityList) {
        return new BaseUtimerEditItemAdapter(entityList);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP)
            clickPositionRx.onNext(getChildCount() - 1);
        return super.onTouchEvent(e);
    }

    public AUtimerEntity getUTimerEntity() {
        return utimerEntity;
    }

    public void setUTimerEntity(AUtimerEntity utimerEntity) {
        this.utimerEntity = utimerEntity;
    }

    public void setAttachEditView(IUtimerAttachEditView attachEditView) {
        this.attachEditView = attachEditView;
    }

    public void setEditModeListener(IOnEditModeListener editModeListener) {
        this.editModeListener = editModeListener;
    }

    public EditElement getLastAccessEditElement(){
        return lastAccessPosition != INIT_POSITION ? editElementList.get(lastAccessPosition) : editElementList.get(0);
    }

    public List<EditElement> getEditElementList(){
        return editElementList;
    }

    //Table: line_index + column_index + EditElement
    public Table<Integer, Integer, EditElement> getEditElementTable(){
        return editElementTable;
    }

    public boolean ifTxtChanged(){
        return isTxtChanged;
    }

    public boolean ifEmpty(){
        return editElementList == null || editElementList.isEmpty()
                ||(editElementList.size() == 1 && TextUtils.isEmpty(editElementList.get(0).getMdCharSequence().toString()));
    }

    public boolean ifAttachViewReady(){
        return attachEditView != null;
    }

    public boolean ifEditing(){
        return preEditPosition != INIT_POSITION;
    }

    public void toStartEdit() {
        if(!ifAttachViewReady()){
            attachEditView.onLoadErr(new UtimerEditException(ERR_EDIT_ATTACH_VIEW_NOT_READY));
            return;
        }
        if(loadSubscription != null){
            attachEditView.onLoadErr(new UtimerEditException(ERR_EDIT_ALL_READY_GO));
            return;
        }

        toParseRawTxt(new UTimerRawReadAction().toReadRaw(getUTimerEntity()))
            .compose(((RxFragment) attachEditView.getRxLifeCycleBindView()).<EditElement>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<EditElement>(){
                int index = 0;
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    Logcat.i(TAG,"toLoadTxt start");
                    loadSubscription = s;
                    editElementList.clear();
                    editElementTable.clear();
                    if(attachEditView != null)
                        attachEditView.onLoadStart();
                }

                @Override
                public void onNext(EditElement editElement) {
                    super.onNext(editElement);
                    editElementList.add(editElement);
                    editElementTable.put(index++, 0, editElement);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(attachEditView != null)
                        attachEditView.onLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    initEditView();
                    if(attachEditView != null)
                        attachEditView.onLoadSucc();
                }
            });
    }

    public void toPauseEdit(){
        clickPositionRx.onNext(INIT_POSITION);
    }

    public void toEndEdit(){
        //todo: if empty doc ,to del it
        /*if(!ifTxtChanged() && ifEmpty())
            utimerEntity.destory();*/
        Optional<MdEditText> lastAccessEditText       = getEditTextItem(preEditPosition);
        if(lastAccessEditText.isPresent()){
            String lassAccessTxt    = lastAccessEditText.get().getText().toString();
            EditElement element     = new EditElement(lassAccessTxt);

            editElementList.set(preEditPosition, element);
            resetData(preEditPosition, element);
            preEditPosition = INIT_POSITION;
        }
        cancelLoad();
        cancelListenEditClick();
    }

    public void gotoLineStart(){
        Optional<MdEditText> curEditText = getEditTextItem(preEditPosition);
        if(curEditText.isPresent()){
            MdEditText mdEditText = curEditText.get();
            if(mdEditText.getCursorColumnIndex(mdEditText.getCurrLineIndex()) > 0 || +mdEditText.getSelectionStart() > 0 )
                mdEditText.goToNewLine();
        }
    }

    public boolean ifHasSelected(){
        Optional<MdEditText> optional = getEditTextItem(preEditPosition);
        if(!optional.isPresent())
            return false;
        return optional.get().ifHasSelected();
    }
    public void insert(String cmd){
        Optional<MdEditText> optional = getEditTextItem(preEditPosition);
        if(!optional.isPresent())
            return;
        if(!TextUtils.isEmpty(cmd))
            optional.get().insert(cmd);
    }
    public void insert(String leftCmd, String rightCmd){
        Optional<MdEditText> optional = getEditTextItem(preEditPosition);
        if(!optional.isPresent())
            return;
        if(leftCmd != null && rightCmd != null)
            optional.get().insert(leftCmd, rightCmd);
    }

    public void setAlign(int alignType){
        Optional<MdEditText> optional = getEditTextItem(preEditPosition);
        if(!optional.isPresent())
            return;
        switch(alignType){
            default:
            case TEXT_ALIGNMENT_TEXT_START:
                optional.get().setAlignLeft();
                break;
            case TEXT_ALIGNMENT_TEXT_END:
                optional.get().setAlignRight();
                break;
            case TEXT_ALIGNMENT_CENTER:
                optional.get().setAlignCenter();
                break;
        }
    }

    protected void init(){
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        myBypass         = new MyBypass();
        editElementList  = Lists.newArrayList();
        editElementTable = HashBasedTable.create();
    }
    protected void initEditView(){
        if(editElementList.size() == 0)
            editElementList.add(new EditElement(""));
        if(myClickListener == null)
            myClickListener = new MyClickListener();
        init(getContext(), editElementList,
                myClickListener, myClickListener,
                null, null,
                myClickListener,null,null);
        toListenEditClick();
    }

    protected void cancelLoad(){
        if(loadSubscription != null)
            loadSubscription.cancel();
        loadSubscription = null;
    }

    protected void toModify(final int index, Observable<String> rawTxtRx){
        toEdit(index, ElementEditType.MODIFY, rawTxtRx);
    }
    protected void toEdit(final int index, final ElementEditType editType,
                          @NonNull Observable<String> rawTxtRx){
        rawTxtRx.flatMap(new Function<String, ObservableSource<EditElement>>() {
                    @Override
                    public ObservableSource<EditElement> apply(String s) throws Exception {
                        return toParseRawTxt(Flowable.just(s)).toObservable();
                    }
                })
                .compose(((RxFragment)attachEditView.getRxLifeCycleBindView()).<EditElement>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new MySimpleObserver<EditElement>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        if(editType == ElementEditType.INSERT)
                            insertDisposable = d;
                        else if(editType == ElementEditType.MODIFY)
                            modifyDisposable = d;
                    }

                    @Override
                    public void onNext(EditElement element) {
                        super.onNext(element);
                        editElementList.set(index, element);
                        resetData(index, element);
                        preEditPosition = INIT_POSITION;
                        editElementTable.put(index, editElementTable.row(index).size(),element);
                    }
                });
    }

    protected void onEditMode(int position, @NonNull EditMode editMode, @NonNull Optional<EditElement> editElementOptional) {
        Optional<MdEditText> optional = getEditTextItem(position);
        if(!optional.isPresent())
            return;
        if(editMode == EditMode.OFF){
            optional.get().enableEdit(false, false);
            if(editModeListener != null)
                editModeListener.onEditModeOff();

            String editTxt = optional.get().getText().toString();
            if(editElementOptional.isPresent() &&
                (editTxt.equals(editElementOptional.get().getMdCharSequence().toString()) || editTxt.equals(editElementOptional.get().getRawText()))){
                resetData(position, editElementOptional.get());
            } else {
                isTxtChanged = true;
                toModify(position, Observable.just(editTxt));
            }
        }else if(editMode == EditMode.ON){
            optional.get().enableEdit(true, getAdapter().getData().size() == 1 );
            if(optional.get().ifEditable() && editModeListener != null)
                    editModeListener.onEditModeOn();

            if(editElementOptional.isPresent())
                optional.get().setText(editElementOptional.get().getRawText());
            optional.get().setCursorToEnd();
        }
    }

    protected Optional<MdEditText> getEditTextItem(int index) {
        MdEditText currEditText = null;
        try{
            currEditText = (MdEditText) getAdapter().getViewByPosition(index, R.id.view_md_edit_tv);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.fromNullable((MdEditText)currEditText);
    }

    protected Optional<EditElement> getEditElement(int index){
        EditElement editElement = null;
        try{
            editElement = editElementList.get(index);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.fromNullable(editElement);
    }
    protected Flowable<EditElement> toParseRawTxt(@NonNull Flowable<String> rawTxtRx) {
        return rawTxtRx.map(new Function<String, EditElement>() {
                    @Override
                    public EditElement apply(String s) throws Exception {
                        return myBypass.toParseMd(s);
                    }
                });
    }
    protected void toListenEditClick(){
        if(clickPositionRx == null)
            clickPositionRx = PublishSubject.create();
        clickPositionDisposable = clickPositionRx.throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        if(preEditPosition != position) {
                            onEditMode(preEditPosition, EditMode.OFF, getEditElement(preEditPosition));
                            onEditMode(position, EditMode.ON, getEditElement(position));
                        }
                        if(position != INIT_POSITION)
                            lastAccessPosition = position;
                        preEditPosition     = position;
                    }
                });
    }
    protected void cancelListenEditClick(){
        if(clickPositionDisposable != null && !clickPositionDisposable.isDisposed())
            clickPositionDisposable.dispose();
        clickPositionDisposable = null;
    }

    class BaseUtimerEditItemAdapter extends BaseItemAdapter<EditElement>{
        public BaseUtimerEditItemAdapter(List<EditElement> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, EditElement item) {
            helper.setText(R.id.view_md_edit_tv, item.getMdCharSequence());
        }
        @Override
        public void onBindViewHolder(BaseViewHolder holder, final int position) {
            super.onBindViewHolder(holder,position);
            holder.getView(R.id.view_md_edit_tv).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(mItemTouchListener != null && event.getAction() == MotionEvent.ACTION_UP)
                        mItemTouchListener.onItemTouch(v, position);
                    return false;
                }
            });
        }
    }

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, IOnItemTouchListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            clickPositionRx.onNext(position);
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            clickPositionRx.onNext(position);
        }

        @Override
        public void onItemTouch(View view, int position) {
            clickPositionRx.onNext(position);
        }
    }

    public interface IUtimerAttachEditView extends IRxLifeCycleBindView {
        public void onLoadStart();
        public void onLoadSucc();
        public void onLoadErr(Throwable e);
    }
    public interface IOnEditModeListener {
        public void onEditModeOn();
        public void onEditModeOff();
    }
}

