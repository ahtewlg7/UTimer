package ahtewlg7.utimer.entity.taskContext;

import android.location.Address;

public class AddrContext implements ITaskContext{
    public static final String TAG = AddrContext.class.getSimpleName();

    private Address address;

    public AddrContext(Address address) {
        this.address = address;
    }

    @Override
    public boolean ifValid() {
        return false;
    }

    public Address getAddress() {
        return address;
    }
}
