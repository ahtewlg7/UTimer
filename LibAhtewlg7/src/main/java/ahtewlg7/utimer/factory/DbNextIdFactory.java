package ahtewlg7.utimer.factory;

import ahtewlg7.utimer.enumtype.GtdType;

public class DbNextIdFactory extends ABaseMapFactory<GtdType, Long> {

    private static DbNextIdFactory instance;

    public static DbNextIdFactory getInstance(){
        if(instance == null)
            instance = new DbNextIdFactory();
        return instance;
    }

    private DbNextIdFactory(){
        super();
        initMap();
    }

    @Override
    public boolean ifKeyValid(GtdType gtdType) {
        return gtdType != null;
    }

    @Override
    public boolean ifValueValid(Long aLong) {
        return aLong >= 0;
    }

    private void initMap(){
        add(GtdType.ACTION,0L);
    }
}
