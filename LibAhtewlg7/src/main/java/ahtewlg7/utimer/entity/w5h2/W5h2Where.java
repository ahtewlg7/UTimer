package ahtewlg7.utimer.entity.w5h2;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.context.PlaceContext;

/**
 * Created by lw on 2019/1/16.
 */
public class W5h2Where implements ITipsEntity {
    private List<PlaceContext> placeList;

    public W5h2Where() {
        placeList = Lists.newArrayList();
    }

    public void addPlace(PlaceContext place){
        placeList.add(place);
    }
    public void removePlace(PlaceContext place){
        placeList.remove(place);
    }
    public void clearPlace(){
        placeList.clear();
    }

    @Override
    public Optional<String> toTips() {
        StringBuilder builder   = new StringBuilder("Where：");
        for(PlaceContext place : placeList)
            builder.append(place.getName()).append(",");
        return Optional.of(builder.toString());
    }
}
