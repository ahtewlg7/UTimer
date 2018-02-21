package ahtewlg7.utimer.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;

/**
 * Created by lw on 2017/9/21.
 */

public class MyAndroidLogAdapter extends AndroidLogAdapter {
    public static final String TAG = MyAndroidLogAdapter.class.getSimpleName();

    private boolean loggable;

    public MyAndroidLogAdapter() {
        super();
    }

    public MyAndroidLogAdapter(FormatStrategy formatStrategy) {
        super(formatStrategy);
    }

    public void setLoggable(boolean loggable){
        this.loggable = loggable;
    }

    @Override
    public boolean isLoggable(int i, String s) {
        return loggable;
    }
}
