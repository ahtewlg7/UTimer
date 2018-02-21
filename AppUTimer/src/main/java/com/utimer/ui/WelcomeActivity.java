package com.utimer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.utimer.R;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.ui.BaseBinderActivity;
import ahtewlg7.utimer.util.Logcat;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2017/12/27.
 */

public class WelcomeActivity extends BaseBinderActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();

    @BindView(R.id.fullscreen_content)
    TextView fullscreenContent;

    //todo: just for test
//    String noteEntityId;
//    String gtdEntityId;
//    Observable<MotionEvent> motionEventObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        Logcat.i(TAG,"onCreate");
        ButterKnife.bind(this);

        toDelayFinish();
    }

    /*@Override
    protected void onServiceBinderConnected(ComponentName name) {
        serviceBinderProxy.toTest();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logcat.i(TAG,"onActivityResult requestCode = " + requestCode +", resultCode = " + resultCode);
        try{
            switch (requestCode){
                case MdEditorActivity.ACTIVITY_START_RESULT:
                    handleStartResult(data);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }    }*/


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
            InitJar.setLogFlag(true);
        else if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
            InitJar.setLogFlag(false);*/
        return super.onKeyUp(keyCode, event);
    }

    /*@Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(motionEventObservable == null)
            motionEventObservable = Observable.defer(new Callable<ObservableSource<? extends MotionEvent>>() {
                @Override
                public ObservableSource<? extends MotionEvent> call() throws Exception {
                    return Observable.just(event);
                }
            });
        if(event.getAction() == MotionEvent.ACTION_UP)
            motionEventObservable.subscribe(new Consumer<MotionEvent>() {
                        @Override
                        public void accept(MotionEvent motionEvent) throws Exception {
                            startNoteActivity(gtdEntityId,noteEntityId);
                        }
                    });

        return super.onTouchEvent(event);
    }*/

    private void toDelayFinish(){
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        startNoteActivity(gtdEntityId,noteEntityId);//todo: just for test
                        ActivityUtils.startActivity(FunctionMainActivity.class);
                        WelcomeActivity.this.finish();
                    }
                });
    }

    //todo: just for test
    /*private void handleStartResult(Intent intent){
        noteEntityId = intent.getStringExtra(MdEditorActivity.EXTRA_KEY_NOTE_ID);
        gtdEntityId  = intent.getStringExtra(MdEditorActivity.EXTRA_KEY_GTD_ID);
        Logcat.i(TAG,"handleStartResult noteEntityId = " + noteEntityId + ", gtdEntityId = " + gtdEntityId);
        AGtdEntity gtdEntity  = new GtdEntityFactory().getEntity(GtdType.INBOX,gtdEntityId);
        NoteEntity noteEntity = new NoteEntityFactory().getNoteEntity(noteEntityId);
        Logcat.i(TAG,"handleStartResult gtdEntity : " + gtdEntity.toString());
        Logcat.i(TAG,"handleStartResult noteEntity : " + noteEntity.toString());
    }
    */
}
