package ahtewlg7.utimer.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by lw on 2017/9/21.
 */

public class MyLoggerAdapterFactory {
    public static final String TAG = MyLoggerAdapterFactory.class.getSimpleName();

    public AndroidLogAdapter getBaseAndroidLogAdapter(boolean loggable){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
                .build();
        MyAndroidLogAdapter myAndroidLogAdapter = new MyAndroidLogAdapter(formatStrategy);
        myAndroidLogAdapter.setLoggable(loggable);
        return myAndroidLogAdapter;
    }

    public DiskLogAdapter getBaseDiskLogAdapter(){
        return new DiskLogAdapter();
    }
}
