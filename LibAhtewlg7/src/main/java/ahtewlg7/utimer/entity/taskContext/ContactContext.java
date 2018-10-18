package ahtewlg7.utimer.entity.taskContext;

import android.net.Uri;
import android.text.TextUtils;

import ahtewlg7.utimer.enumtype.GenderType;

public class ContactContext implements ITaskContext {
    public static final String TAG = ContactContext.class.getSimpleName();

    private GenderType gender;
    private AddrContext addr;
    private String aliasName;
    private String realName;
    private String phoneNum1;
    private String phoneNum2;


    public ContactContext(String realName) {
        this.realName = realName;
    }
    public ContactContext(String realName, String aliasName) {
        this.aliasName = aliasName;
        this.realName = realName;
    }

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(aliasName) && !TextUtils.isEmpty(realName) ;
    }

    public String getRealName() {
        return realName;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public AddrContext getAddr() {
        return addr;
    }

    public void setAddr(AddrContext addr) {
        this.addr = addr;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getPhoneNum1() {
        return phoneNum1;
    }

    public void setPhoneNum1(String phoneNum1) {
        this.phoneNum1 = phoneNum1;
    }

    public String getPhoneNum2() {
        return phoneNum2;
    }

    public void setPhoneNum2(String phoneNum2) {
        this.phoneNum2 = phoneNum2;
    }

    public void merge(Uri uri){

    }
}
