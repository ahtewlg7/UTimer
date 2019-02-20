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

import com.blankj.utilcode.util.ToastUtils;
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
import ahtewlg7.utimer.enumtype.MdEditOperateType;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.md.IMdEditListener;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.mvp.IRxLifeCycleBindView;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;
import ahtewlg7.utimer.view.md.MdEditText;
import ahtewlg7.utimer.view.md.MdEditorWidget;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ALL_READY_GO;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ATTACH_VIEW_NOT_READY;
import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ENTITY_NOT_READY;

/**
 * Created by lw on 2019/2/3.
 */
public class BaseUtimerEidtView extends ABaseLinearRecyclerView<EditElement>{
    public static final String TAG = BaseUtimerEidtView.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    protected int preEditPosition  = INIT_POSITION;
    protected boolean isTxtChanged = false;

    protected boolean toastEnable;
    protected AUtimerEntity utimerEntity;
    protected List<EditElement> editElementList;

    protected MyBypass myBypass;
    protected Subscription loadSubscription;
    protected Disposable clickPositionDisposable;
    protected Disposable insertDisposable;
    protected Disposable modifyDisposable;
    protected PublishSubject<Integer> clickPositionRx;
    protected PublishSubject<MdEditOperateType> mdEditOperateRx;

    protected MdEditorWidget editorWidget;
    protected IUtimerAttachEditView attachEditView;
    protected MyClickListener myClickListener;
    protected MdEditListener mdEditListener;

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

    public MdEditListener getMdEditListener() {
        return mdEditListener;
    }

    public boolean isToastEnable() {
        return toastEnable;
    }

    public void setToastEnable(boolean toastEnable) {
        this.toastEnable = toastEnable;
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

    public List<EditElement> getEditElementList(){
        return editElementList;
    }

    public boolean ifTxtChanged(){
        return isTxtChanged;
    }
    public boolean ifEntityReady(){
        return utimerEntity != null && utimerEntity.ifValid() && utimerEntity.ensureAttachFileExist();
    }

    public boolean ifAttachViewReady(){
        return attachEditView != null;
    }

    public void toInitMdToastable(){
        Optional configOptional = VcFactoryBuilder.getInstance().getVcConfig();
        boolean toastEnable = true;
        if(configOptional.isPresent()){
            BaseConfig config = (BaseConfig)configOptional.get();
            toastEnable       = config.ifMdEditToastable();
        }
        setToastEnable(toastEnable);
    }


    public boolean ifEditing(){
        return preEditPosition != INIT_POSITION;
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
                    toListenMdEditOperate();
                    if(attachEditView != null)
                        attachEditView.onLoadSucc();
                }
            });
    }

    public void toPauseEdit(){
        clickPositionRx.onNext(INIT_POSITION);
    }

    public void toEndEdit(){
        /*if(preEditPosition == INIT_POSITION)
            preEditPosition = 0;*/
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

    public void toListenMdEditOperate(){
        mdEditOperateRx.debounce(1, TimeUnit.SECONDS)
            .compose(((RxFragment) attachEditView.getRxLifeCycleBindView()).<MdEditOperateType>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySimpleObserver<MdEditOperateType>(){
                @Override
                public void onNext(MdEditOperateType mdEditOperateType) {
                    Optional<MdEditText> optional = getEditTextItem(preEditPosition);
                    if(!optional.isPresent()) {
                        Logcat.i(TAG,"to cancel edit operate");
                        return;
                    }
                    Optional<Integer> toastOptional = Optional.absent();
                    String cmd = null;
                    switch (mdEditOperateType){
                        case UNDO:
                            toastOptional = Optional.of(R.string.editor_undo);
                            break;
                        case REDO:
                            toastOptional = Optional.of(R.string.editor_redo);
                            break;
                        case BOLD:
                            if(optional.get().ifHasSelected()){
                                cmd = MyRInfo.getStringByID(R.string.block);
                                optional.get().insert(cmd, cmd);
                                toastOptional = Optional.of(R.string.editor_bold);
                            }else{
                                toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                            }
                            break;
                        case ITALIC:
                            if(optional.get().ifHasSelected()){
                                cmd = MyRInfo.getStringByID(R.string.italic);
                                optional.get().insert(cmd, cmd);
                                toastOptional = Optional.of(R.string.editor_italic);
                            }else{
                                toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                            }
                            break;
                        case SUB_SCRIPT:
                            /*cmd = MyRInfo.getStringByID(R.string.);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_subscript);
                            break;
                        case SUPER_SCRIPT:
                            /*cmd = MyRInfo.getStringByID(R.string.block);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_superscript);
                            break;
                        case STRIKE_THROUGH:
                            if(optional.get().ifHasSelected()){
                                cmd = MyRInfo.getStringByID(R.string.strikethrough);
                                optional.get().insert(cmd, cmd);
                                toastOptional = Optional.of(R.string.editor_strike_through);
                            }else{
                                toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                            }
                            break;
                        case UNDER_LINE:
                            /*cmd = MyRInfo.getStringByID(R.string.line);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_under_line);
                            break;
                        case HORIZONTAL_RULE:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.horizontalrule);
                            optional.get().insert(cmd, cmd);
                            toastOptional = Optional.of(R.string.action_horizontal_rule);
                            break;
                        case HEAD1:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H1);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading1);
                            break;
                        case HEAD2:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H2);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading2);
                            break;
                        case HEAD3:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H3);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading3);
                            break;
                        case HEAD4:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H4);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading4);
                            break;
                        case HEAD5:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H5);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading5);
                            break;
                        case HEAD6:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.H6);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_heading6);
                            break;
                        case TXT_COLOR:
                            /*cmd = MyRInfo.getStringByID(R.string.colo);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_text_color);
                            break;
                        case BACKGROUND_COLOR:
                            /*cmd = MyRInfo.getStringByID(R.string.block);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_text_background_color);
                            break;
                        case INDENT:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.indent);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_indent);
                            break;
                        case OUTDENT:
                            /*cmd = MyRInfo.getStringByID(R.string.block);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_outdent);
                            break;
                        case ALIGN_LEFT:
                            optional.get().setAlignLeft();
                            toastOptional = Optional.of(R.string.editor_align_left);
                            break;
                        case ALIGN_CENTER:
                            optional.get().setAlignCenter();
                            toastOptional = Optional.of(R.string.editor_align_center);
                            break;
                        case ALIGN_RIGHT:
                            optional.get().setAlignRight();
                            toastOptional = Optional.of(R.string.editor_align_right);
                            break;
                        case BLOCK_QUOTE:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.blockquote);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_block_quote);
                            break;
                        case FILE:
                            /*cmd = MyRInfo.getStringByID(R.string.block);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_insert_file);
                            break;
                        case LINK:
                            /*cmd = MyRInfo.getStringByID(R.string.block);
                            optional.get().insert(cmd);*/
                            toastOptional = Optional.of(R.string.editor_insert_link);
                            break;
                        case UNORDERED_LIST:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.unorderedlists);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_insert_unordered_list);
                            break;
                        case ORDERED_LIST:
                            gotoLineStart(optional);
                            cmd = MyRInfo.getStringByID(R.string.orderedlists);
                            optional.get().insert(cmd);
                            toastOptional = Optional.of(R.string.editor_insert_ordered_list);
                            break;
                    }
                    if(toastEnable && toastOptional.isPresent())
                        ToastUtils.showShort(toastOptional.get());
                }
            });
    }
    protected void gotoLineStart(Optional<MdEditText> curEditText){
        if(curEditText.isPresent()){
            MdEditText mdEditText = curEditText.get();
            if(mdEditText.getCursorColumnIndex(mdEditText.getCurrLineIndex()) > 0 || !TextUtils.isEmpty(String.valueOf(mdEditText.getText().charAt(mdEditText.getSelectionStart()-1))))
                mdEditText.goToNewLine();
        }
    }
    protected void init(){
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        myBypass        = new MyBypass();
        editElementList = Lists.newArrayList();
        mdEditListener  = new MdEditListener();
        mdEditOperateRx = PublishSubject.create();
    }
    protected void initEditView(){
        if(editElementList.size() == 0)
            editElementList.add(new EditElement(" "));
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
                    }
                });
    }

    protected void onEditMode(int position, @NonNull EditMode editMode, @NonNull Optional<EditElement> editElementOptional) {
        Optional<MdEditText> optional = getEditTextItem(position);
        Logcat.i(TAG,"onEditMode isPresent = " + optional.isPresent() + ", position = " + position + ", editMode = " + editMode.name());
        if(!optional.isPresent()){
            Logcat.i(TAG,"onEditMode cancel");
            return;
        }
        if(editMode == EditMode.OFF){
            optional.get().enableEdit(false);
            editorWidget.setVisibility(View.GONE);

            String eidtTxt = optional.get().getText().toString();
            Optional<EditElement> currEditElement = getEditElement(position);
            if(currEditElement.isPresent() &&
                (eidtTxt.equals(currEditElement.get().getMdCharSequence().toString()) || eidtTxt.equals(currEditElement.get().getRawText()))){
                resetData(position, currEditElement.get());
            } else {
                isTxtChanged = true;
                toModify(position, Observable.just(eidtTxt));
            }
        }else if(editMode == EditMode.ON){
            optional.get().enableEdit(true);
            if(optional.get().ifEditable())
                editorWidget.setVisibility(View.VISIBLE);

            if(editElementOptional.isPresent())
                optional.get().setText(editElementOptional.get().getRawText());
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
            Logcat.i(TAG,"getEditElement err: " + e.getMessage());
        }
        return Optional.fromNullable(editElement);
    }
    protected EditElement toParseRawTxt(String rawTxt){
        EditElement editElement = new EditElement(rawTxt);
        editElement.setMdCharSequence(myBypass.markdownToSpannable(rawTxt));
        return editElement;
    }
    protected Flowable<EditElement> toParseRawTxt(@NonNull Flowable<String> rawTxtRx) {
        return rawTxtRx.map(new Function<String, EditElement>() {
                    @Override
                    public EditElement apply(String s) throws Exception {
                        return toParseRawTxt(s);
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
                        Logcat.i(TAG, "toListenEditClick position = " + position + ", preEditPosition = " + preEditPosition);
                        if(preEditPosition != position) {
                            onEditMode(preEditPosition, EditMode.OFF, getEditElement(preEditPosition));
                            onEditMode(position, EditMode.ON, getEditElement(position));
                        }
                        preEditPosition = position;
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
    class MdEditListener implements IMdEditListener {
        @Override
        public void setMdEditorWidget(MdEditorWidget mdEditorWidget) {
            editorWidget = mdEditorWidget;
        }

        @Override
        public void toAlignLeft() {
            mdEditOperateRx.onNext(MdEditOperateType.ALIGN_LEFT);
        }

        @Override
        public void toAlignRight() {
            mdEditOperateRx.onNext(MdEditOperateType.ALIGN_RIGHT);
        }

        @Override
        public void toAlignTop() {

        }

        @Override
        public void toAlignBottom() {

        }

        @Override
        public void toAlignMiddle() {
        }

        @Override
        public void toAligenCenterHori() {

        }

        @Override
        public void toAligenCenter() {
            mdEditOperateRx.onNext(MdEditOperateType.ALIGN_CENTER);
        }

        @Override
        public void toFontColor() {

        }

        @Override
        public void toFontSizeByPx() {

        }

        @Override
        public void toFontSizeByLevel() {

        }

        @Override
        public void toBackgroundColor() {
            mdEditOperateRx.onNext(MdEditOperateType.BACKGROUND_COLOR);
        }

        @Override
        public void toBackground() {

        }

        @Override
        public void toUndo() {
            mdEditOperateRx.onNext(MdEditOperateType.UNDO);
        }

        @Override
        public void toRedo() {
            mdEditOperateRx.onNext(MdEditOperateType.REDO);
        }

        @Override
        public void toBold() {
            mdEditOperateRx.onNext(MdEditOperateType.BOLD);
        }

        @Override
        public void toItalic() {
            mdEditOperateRx.onNext(MdEditOperateType.ITALIC);
        }

        @Override
        public void toSubscript() {
            mdEditOperateRx.onNext(MdEditOperateType.SUB_SCRIPT);
        }

        @Override
        public void toSuperscript() {
            mdEditOperateRx.onNext(MdEditOperateType.SUPER_SCRIPT);
        }

        @Override
        public void toStrikeThrough() {
            mdEditOperateRx.onNext(MdEditOperateType.STRIKE_THROUGH);
        }

        @Override
        public void toUnderLine() {
            mdEditOperateRx.onNext(MdEditOperateType.UNDER_LINE);
        }

        @Override
        public void toHorizontalRule() {
            mdEditOperateRx.onNext(MdEditOperateType.HORIZONTAL_RULE);
        }

        @Override
        public void toTextColor() {
            mdEditOperateRx.onNext(MdEditOperateType.TXT_COLOR);
        }

        @Override
        public void toTextBackgroundColor() {
            mdEditOperateRx.onNext(MdEditOperateType.TXT_BACKGROUND_COLOR);
        }

        @Override
        public void toRemoveFormat() {

        }

        @Override
        public void toHead1() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD1);
        }

        @Override
        public void toHead2() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD2);
        }

        @Override
        public void toHead3() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD3);
        }

        @Override
        public void toHead4() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD4);
        }

        @Override
        public void toHead5() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD5);
        }

        @Override
        public void toHead6() {
            mdEditOperateRx.onNext(MdEditOperateType.HEAD6);
        }

        @Override
        public void toIndent() {
            mdEditOperateRx.onNext(MdEditOperateType.INDENT);
        }

        @Override
        public void toOutdent() {
            mdEditOperateRx.onNext(MdEditOperateType.OUTDENT);
        }

        @Override
        public void toBlockQuote() {
            mdEditOperateRx.onNext(MdEditOperateType.BLOCK_QUOTE);
        }

        @Override
        public void toBullets() {
        }

        @Override
        public void toInsertFile() {
            mdEditOperateRx.onNext(MdEditOperateType.FILE);
        }

        @Override
        public void toInsertLink() {
            mdEditOperateRx.onNext(MdEditOperateType.LINK);
        }

        @Override
        public void toInsertOrderedList() {
            mdEditOperateRx.onNext(MdEditOperateType.ORDERED_LIST);
        }

        @Override
        public void toInsertUnorderedList() {
            mdEditOperateRx.onNext(MdEditOperateType.UNORDERED_LIST);
        }

        /*@Override
        public void toInsertTodo() {

        }*/
    }
    public interface IUtimerAttachEditView extends IRxLifeCycleBindView {
        public void onLoadStart();
        public void onLoadSucc();
        public void onLoadErr(Throwable e);
    }
}

