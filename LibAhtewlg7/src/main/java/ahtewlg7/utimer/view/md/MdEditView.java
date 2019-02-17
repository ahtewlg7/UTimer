package ahtewlg7.utimer.view.md;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.util.AndrManagerFactory;
import ahtewlg7.utimer.view.BaseUtimerEidtView;

/**
 * Created by lw on 2016/5/17.
 */
public class MdEditView extends FrameLayout {
    public static final String TAG = MdEditView.class.getSimpleName();

    protected MdEditorWidget editorWidget;
    protected BaseUtimerEidtView editRecyclerView;

    protected AUtimerEntity utimerEntity;

    public MdEditView(Context context) {
        super(context);
        findView();
    }
    public MdEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        findView();
    }


    private void findView(){
        View layout = null;
        try{
            layout = new AndrManagerFactory().getLayoutInflater().inflate(R.layout.layout_md_edit, null);
            editorWidget         = (MdEditorWidget)layout.findViewById(R.id.layout_edit_widget);
            editRecyclerView     = (BaseUtimerEidtView)layout.findViewById(R.id.layout_edit_recycler_view);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.addView(layout);
    }

    public void setAttachEditView(BaseUtimerEidtView.IUtimerAttachEditView attachEditView) {
        editRecyclerView.setAttachEditView(attachEditView);
    }

    public AUtimerEntity getUTimerEntity() {
        return utimerEntity;
    }

    public void setUTimerEntity(AUtimerEntity utimerEntity) {
        this.utimerEntity = utimerEntity;
    }

    public void toStartEdit(){
        editRecyclerView.setUTimerEntity(utimerEntity);
        editRecyclerView.toInitMdToastable();
        editorWidget.initEditListener(editRecyclerView.getMdEditListener());
        editRecyclerView.toStartEdit();
    }

    public void toEndEdit(){
        editRecyclerView.toEndEdit();
    }

    public List<EditElement> getEditElementList(){
        return editRecyclerView.getEditElementList();
    }
}
