package ahtewlg7.utimer.entity.taskContext;

import ahtewlg7.utimer.enumtype.TaskContextType;
import ahtewlg7.utimer.exception.TaskContextException;
import ahtewlg7.utimer.taskContext.TaskContextAction;

/**
 * Created by lw on 2017/11/22.
 */

public class LocationContext extends ATaskContext {
    public static final String TAG = LocationContext.class.getSimpleName();

    private String name;
    private String detail;
    private double latitude; //纬度
    private double longitude; //经度

    @Override
    public TaskContextType getContextType() {
        return TaskContextType.LOCATION;
    }

    @Override
    public boolean isOk(TaskContextAction taskContextAction) throws TaskContextException {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return super.toString() + ", name = " + name + ", detail = " + detail + ", latitude = " + latitude + ", longitude = " + longitude
                + super.toString();
    }
}
