package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.R;
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
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ALL_READY_GO;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ATTACH_VIEW_NOT_READY;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ENTITY_NOT_READY;

/**
 * Created by lw on 2019/2/3.
 */
public class BaseUtimerEidtView extends ABaseLinearRecyclerView<EditElement> {
    public static final String TAG = EditLinerRecyclerView.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    protected int preEditPosition  = INIT_POSITION;
    protected boolean isTxtChanged = false;

    protected AUtimerEntity utimerEntity;
    protected List<EditElement> editElementList;

    protected MyBypass myBypass;
    protected Subscription loadSubscription;
    protected Disposable clickPositionDisposable;
    protected Disposable insertDisposable;
    protected Disposable modifyDisposable;
    protected PublishSubject<Integer> clickPositionRx;

    protected IUtimerAttachEditView attachEditView;
    protected MyClickListener myClickListener;

    public BaseUtimerEidtView(Context context) {
        super(context);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        myBypass        = new MyBypass();
        editElementList = Lists.newArrayList();
    }

    public BaseUtimerEidtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        myBypass        = new MyBypass();
        editElementList = Lists.newArrayList();
    }

    public BaseUtimerEidtView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        myBypass        = new MyBypass();
        editElementList = Lists.newArrayList();
    }

    @Override
    public @LayoutRes int getViewItemLayout() {
        return R.layout.view_md_edit;
    }

    @Override
    public void init(Context context, List<EditElement> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    @NonNull
    @Override
    public BaseItemAdapter<EditElement> createAdapter(List<EditElement> entityList) {
        return new BaseUtimerEditItemAdapter(entityList);
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

    public boolean ifEntityReady(){
        return utimerEntity != null && utimerEntity.ifValid()
                && utimerEntity.getAttachFile() != null && utimerEntity.ensureAttachFileExist();
    }

    public boolean ifAttachViewReady(){
        return attachEditView != null;
    }

    public void toStartEdit() {
        if(!ifAttachViewReady()){
            Logcat.i(TAG,"toLoad : attach view is not valid , so cancel");
            attachEditView.onLoadErr(new UtimerEditException(ERR_EDIT_ATTACH_VIEW_NOT_READY));
            return;
        }
        if(!ifEntityReady()){
            Logcat.i(TAG,"toLoad : uTimerEnity is not valid , so cancel");
            attachEditView.onLoadErr(new UtimerEditException(ERR_EDIT_ENTITY_NOT_READY));
            return;
        }
        if(loadSubscription != null){
            Logcat.i(TAG,"toLoad : is allready editing, so cancel");
            attachEditView.onLoadErr(new UtimerEditException(ERR_EDIT_ALL_READY_GO));
            return;
        }

        Flowable.just(utimerEntity)
            .subscribeOn(Schedulers.io())
            .flatMap(new Function<AUtimerEntity, Publisher<EditElement>>() {
                @Override
                public Publisher<EditElement> apply(AUtimerEntity entity) throws Exception {
                    return toParseRawTxt(Flowable.fromIterable(Files.readLines(entity.getAttachFile().getFile(), Charsets.UTF_8)));
                }
            })
            .compose(((RxFragment) attachEditView.getRxLifeCycleBindView()).<EditElement>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<EditElement>(){
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    Logcat.i(TAG,"toLoadTxt start");
                    loadSubscription = s;
                    editElementList.clear();
                    if(attachEditView != null)
                        attachEditView.onLoadStart();
                }

                @Override
                public void onNext(EditElement editElement) {
                    super.onNext(editElement);
                    editElementList.add(editElement);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Logcat.i(TAG,"toLoadTxt err");
                    if(attachEditView != null)
                        attachEditView.onLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    Logcat.i(TAG,"toLoadTxt succ");
                    initEditView();
                    if(attachEditView != null)
                        attachEditView.onLoadSucc();
                }
            });
    }

    public void toEndEdit(){
        cancelLoad();
    }

    protected void initEditView(){
        if(editElementList.size() == 0)
            editElementList.add(new EditElement(""));
        if(myClickListener == null)
            myClickListener = new MyClickListener();
        init(getContext(), editElementList,
                null, myClickListener,
                null, null,
                null,null);
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
                    }
                });
    }

    protected void onEditMode(int position, @NonNull EditMode editMode, @NonNull Optional<EditElement> editElementOptional) {
        Optional<EditText> optional = getEditTextItem(position);
        Logcat.i(TAG,"onEditMode isPresent = " + optional.isPresent() + ", position = " + position + ", editMode = " + editMode.name());
        if(!optional.isPresent()){
            Logcat.i(TAG,"onEditMode cancel");
            return;
        }
        if(editMode == EditMode.OFF){
            optional.get().setFocusable(false);
            optional.get().setFocusableInTouchMode(false);

            String eidtTxt = optional.get().getText().toString();
            Optional<EditElement> currEditElement = getEditElement(position);
            if(currEditElement.isPresent() &&
                (eidtTxt.equals(currEditElement.get().getMdCharSequence().toString()) || eidtTxt.equals(currEditElement.get().getRawText()))){
                resetData(position, currEditElement.get());
            } else {
                isTxtChanged = true;
                toModify(position, Observable.just(eidtTxt.trim()));
            }
        }else if(editMode == EditMode.ON){
            optional.get().setFocusable(true);
            optional.get().setFocusableInTouchMode(true);

            if(editElementOptional.isPresent()) {
                String rawText = editElementOptional.get().getRawText();
                optional.get().setText(rawText.trim());
            }
        }
    }

    protected Optional<EditText> getEditTextItem(int index) {
        MdEditText currEditText = null;
        try{
            currEditText = (MdEditText) getAdapter().getViewByPosition(index, R.id.view_md_edit_tv);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.fromNullable((EditText)currEditText);
    }

    protected Optional<EditElement> getEditElement(int index){
        EditElement editElement = null;
        try{
            editElement = editElementList.get(index);
        }catch (Exception e){
            Logcat.i(TAG,"getEditElement err: " + e.getMessage());
        }
        return Optional.fromNullable(editElement);
    }
    protected Optional<EditElement> toParseRawTxt(String rawTxt){
        EditElement editElement = null;
        if(!TextUtils.isEmpty(rawTxt)){
            editElement = new EditElement(rawTxt);
            editElement.setMdCharSequence(myBypass.markdownToSpannable(rawTxt));
        }
        return Optional.fromNullable(editElement);
    }
    protected Flowable<EditElement> toParseRawTxt(@NonNull Flowable<String> rawTxtRx) {
        return rawTxtRx.map(new Function<String, Optional<EditElement>>() {
                    @Override
                    public Optional<EditElement> apply(String s) throws Exception {
                        return toParseRawTxt(s);
                    }
                })
                .filter(new Predicate<Optional<EditElement>>() {
                    @Override
                    public boolean test(Optional<EditElement> editElementOptional) throws Exception {
                        return editElementOptional.isPresent();
                    }
                })
                .map(new Function<Optional<EditElement>, EditElement>() {
                    @Override
                    public EditElement apply(Optional<EditElement> editElementOptional) throws Exception {
                        return editElementOptional.get();
                    }
                });
    }
    protected void toListenEditClick(){
        if(clickPositionRx == null)
            clickPositionRx = PublishSubject.create();
        clickPositionDisposable = clickPositionRx.debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        Logcat.i(TAG, "toListenEditClick position = " + position + ", preEditPosition = " + preEditPosition);
                        if(preEditPosition != position) {
                            onEditMode(preEditPosition, EditMode.OFF, getEditElement(preEditPosition));
                            onEditMode(position, EditMode.ON, getEditElement(position));
                        }
                        preEditPosition = position;
                    }
                });
    }

    class BaseUtimerEditItemAdapter extends BaseItemAdapter<EditElement>{
        public BaseUtimerEditItemAdapter(List<EditElement> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, EditElement item) {
            helper.setText(R.id.view_md_edit_tv, item.getMdCharSequence())
                    .addOnClickListener(R.id.view_md_edit_tv);
        }
    }
    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Logcat.i(TAG, "onItemClick position = " + position + ", preEditPosition = " + preEditPosition);
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            clickPositionRx.onNext(position);
        }
    }
    public interface IUtimerAttachEditView extends IRxLifeCycleBindView {
        public void onLoadStart();
        public void onLoadSucc();
        public void onLoadErr(Throwable e);
    }
}

