package ahtewlg7.utimer.entity.w5h2;


import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.context.ContactContext;

/**
 * Created by lw on 2019/1/16.
 */
public class W5h2Who implements ITipsEntity {
    private List<ContactContext> contactList;

    public W5h2Who(){
        contactList = Lists.newArrayList();
    }

    public void addContact(ContactContext contact){
        contactList.add(contact);
    }
    public void removeContact(ContactContext contact){
        contactList.remove(contact);
    }
    public void clearContact(){
        contactList.clear();
    }

    @Override
    public Optional<String> toTips() {
        StringBuilder builder   = new StringBuilder("Who：");
        for(ContactContext contact : contactList)
            builder.append(contact.getName()).append(",");
        return Optional.of(builder.toString());
    }
}
