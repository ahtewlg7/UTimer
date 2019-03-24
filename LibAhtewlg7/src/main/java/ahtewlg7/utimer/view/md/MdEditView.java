package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Table;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.MdEditOperateType;
import ahtewlg7.utimer.md.IMdEditListener;
import ahtewlg7.utimer.util.AndrManagerFactory;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySimpleObserver;
import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;
import ahtewlg7.utimer.view.BaseUtimerEidtView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2016/5/17.
 */
public class MdEditView extends FrameLayout {
    public static final String TAG = MdEditView.class.getSimpleName();

    protected MdEditorWidget editorWidget;
    protected MdEditListener mdEditListener;
    protected BaseUtimerEidtView editRecyclerView;
    protected MyEditModeListener myEditModeListener;
    protected BaseUtimerEidtView.IUtimerAttachEditView attachEditView;

    protected boolean toastEnable;
    protected AUtimerEntity utimerEntity;
    protected PublishSubject<MdEditOperateType> mdEditOperateRx;

    public MdEditView(Context context) {
        super(context);
        initView();
    }
    public MdEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void toSetAttachEditView(BaseUtimerEidtView.IUtimerAttachEditView attachEditView) {
        this.attachEditView = attachEditView;
        editRecyclerView.setAttachEditView(attachEditView);
    }

    public AUtimerEntity getUTimerEntity() {
        return utimerEntity;
    }

    public void setUTimerEntity(AUtimerEntity utimerEntity) {
        this.utimerEntity = utimerEntity;
    }

    public boolean ifEditing(){
        return editRecyclerView.ifEditing();
    }

    public boolean isToastEnable() {
        return toastEnable;
    }

    public void setToastEnable(boolean toastEnable) {
        this.toastEnable = toastEnable;
    }

    public void toStartEdit(){
        toInitMdToastable();
        toListenMdEditOperate();

        editRecyclerView.setUTimerEntity(utimerEntity);
        editRecyclerView.toStartEdit();
    }

    public void toPauseEdit(){
        editRecyclerView.toPauseEdit();
    }

    public void toEndEdit(){
        editRecyclerView.toEndEdit();
    }

    public List<EditElement> getEditElementList(){
        return editRecyclerView.getEditElementList();
    }

    public Table<Integer, Integer, EditElement> getEditElementTable(){
        return editRecyclerView.getEditElementTable();
    }

    private void initView(){
        View layout = null;
        try{
            layout = new AndrManagerFactory().getLayoutInflater().inflate(R.layout.layout_md_edit, null);
            editorWidget         = (MdEditorWidget)layout.findViewById(R.id.layout_edit_widget);
            editRecyclerView     = (BaseUtimerEidtView)layout.findViewById(R.id.layout_edit_recycler_view);

            mdEditListener       = new MdEditListener();
            myEditModeListener   = new MyEditModeListener();

            editorWidget.setEditListener(mdEditListener);
            editRecyclerView.setEditModeListener(myEditModeListener);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.addView(layout);
    }

    private void toInitMdToastable(){
        Optional configOptional = VcFactoryBuilder.getInstance().getVcConfig();
        boolean toastEnable = true;
        if(configOptional.isPresent()){
            BaseConfig config = (BaseConfig)configOptional.get();
            toastEnable       = config.ifMdEditToastable();
        }
        setToastEnable(toastEnable);
    }

    private void toListenMdEditOperate(){
        if(mdEditOperateRx == null)
            mdEditOperateRx      = PublishSubject.create();
        mdEditOperateRx.compose(((RxFragment) attachEditView.getRxLifeCycleBindView()).<MdEditOperateType>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleObserver<MdEditOperateType>(){
                    @Override
                    public void onNext(MdEditOperateType mdEditOperateType) {
                        if(!editRecyclerView.ifEditing()) {
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
                                if(editRecyclerView.ifHasSelected()){
                                    cmd = MyRInfo.getStringByID(R.string.block);
                                    editRecyclerView.insert(cmd, cmd);
                                    toastOptional = Optional.of(R.string.editor_bold);
                                }else{
                                    toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                                }
                                break;
                            case ITALIC:
                                if(editRecyclerView.ifHasSelected()){
                                    cmd = MyRInfo.getStringByID(R.string.italic);
                                    editRecyclerView.insert(cmd, cmd);
                                    toastOptional = Optional.of(R.string.editor_italic);
                                }else{
                                    toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                                }
                                break;
                            case SUB_SCRIPT:
                                /*cmd = MyRInfo.getStringByID(R.string.);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_subscript);
                                break;
                            case SUPER_SCRIPT:
                                /*cmd = MyRInfo.getStringByID(R.string.block);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_superscript);
                                break;
                            case STRIKE_THROUGH:
                                if(editRecyclerView.ifHasSelected()){
                                    cmd = MyRInfo.getStringByID(R.string.strikethrough);
                                    editRecyclerView.insert(cmd, cmd);
                                    toastOptional = Optional.of(R.string.editor_strike_through);
                                }else{
                                    toastOptional = Optional.of(R.string.prompt_md_edit_select_first);
                                }
                                break;
                            case UNDER_LINE:
                                /*cmd = MyRInfo.getStringByID(R.string.line);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_under_line);
                                break;
                            case HORIZONTAL_RULE:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.horizontalrule);
                                editRecyclerView.insert(cmd, cmd);
                                toastOptional = Optional.of(R.string.action_horizontal_rule);
                                break;
                            case HEAD1:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H1);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading1);
                                break;
                            case HEAD2:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H2);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading2);
                                break;
                            case HEAD3:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H3);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading3);
                                break;
                            case HEAD4:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H4);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading4);
                                break;
                            case HEAD5:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H5);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading5);
                                break;
                            case HEAD6:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.H6);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_heading6);
                                break;
                            case TXT_COLOR:
                                /*cmd = MyRInfo.getStringByID(R.string.colo);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_text_color);
                                break;
                            case BACKGROUND_COLOR:
                                /*cmd = MyRInfo.getStringByID(R.string.block);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_text_background_color);
                                break;
                            case INDENT:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.indent);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_indent);
                                break;
                            case OUTDENT:
                                /*cmd = MyRInfo.getStringByID(R.string.block);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_outdent);
                                break;
                            case ALIGN_LEFT:
                                editRecyclerView.setAlign(TEXT_ALIGNMENT_TEXT_START);
                                toastOptional = Optional.of(R.string.editor_align_left);
                                break;
                            case ALIGN_CENTER:
                                editRecyclerView.setAlign(TEXT_ALIGNMENT_CENTER);
                                toastOptional = Optional.of(R.string.editor_align_center);
                                break;
                            case ALIGN_RIGHT:
                                editRecyclerView.setAlign(TEXT_ALIGNMENT_TEXT_END);
                                toastOptional = Optional.of(R.string.editor_align_right);
                                break;
                            case BLOCK_QUOTE:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.blockquote);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_block_quote);
                                break;
                            case FILE:
                                /*cmd = MyRInfo.getStringByID(R.string.block);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_insert_file);
                                break;
                            case LINK:
                                /*cmd = MyRInfo.getStringByID(R.string.block);
                                editRecyclerView.insert(cmd);*/
                                toastOptional = Optional.of(R.string.editor_insert_link);
                                break;
                            case UNORDERED_LIST:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.unorderedlists);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_insert_unordered_list);
                                break;
                            case ORDERED_LIST:
                                editRecyclerView.gotoLineStart();
                                cmd = MyRInfo.getStringByID(R.string.orderedlists);
                                editRecyclerView.insert(cmd);
                                toastOptional = Optional.of(R.string.editor_insert_ordered_list);
                                break;
                        }
                        if(toastEnable && toastOptional.isPresent())
                            ToastUtils.showShort(toastOptional.get());
                    }
                });
    }

    class MdEditListener implements IMdEditListener {
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
    class MyEditModeListener implements BaseUtimerEidtView.IOnEditModeListener {
        @Override
        public void onEditModeOn() {
            editorWidget.setVisibility(View.VISIBLE);
        }

        @Override
        public void onEditModeOff() {
            editorWidget.setVisibility(View.GONE);
        }
    }
}
