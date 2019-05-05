package ahtewlg7.utimer.entity.w5h2;

import ahtewlg7.utimer.entity.IValidEntity;

/**
 * Created by lw on 2019/1/16.
 */
public class BaseW5h2Entity implements IValidEntity {
    private W5h2What what;
    private W5h2When when;
    private AW5h2Why why;
    private W5h2Who who;
    private W5h2Where where;
    private AW5h2How how;
    private W5h2HowMuch howMuch;

    @Override
    public boolean ifValid() {
        return when != null && when.ifValid();
    }

    public W5h2What getWhat() {
        return what;
    }

    public void setWhat(W5h2What what) {
        this.what = what;
    }

    public AW5h2Why getWhy() {
        return why;
    }

    public void setWhy(AW5h2Why why) {
        this.why = why;
    }

    public W5h2Who getWho() {
        return who;
    }

    public void setWho(W5h2Who who) {
        this.who = who;
    }

    public W5h2When getWhen() {
        return when;
    }

    public void setWhen(W5h2When when) {
        this.when = when;
    }

    public W5h2Where getWhere() {
        return where;
    }

    public void setWhere(W5h2Where where) {
        this.where = where;
    }

    public AW5h2How getHow() {
        return how;
    }

    public void setHow(AW5h2How how) {
        this.how = how;
    }

    public W5h2HowMuch getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(W5h2HowMuch howMuch) {
        this.howMuch = howMuch;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if(what != null)
            builder.append(what.toString());
        if(why != null)
            builder.append(",").append(why.toString());
        if(who != null)
            builder.append(",").append(who.toString());
        if(when != null)
            builder.append(",").append(when.toString());
        if(where != null)
            builder.append(",").append(where.toString());
        if(how != null)
            builder.append(",").append(how.toString());
        if(howMuch != null)
            builder.append(",").append(howMuch.toString());
        return builder.append("}").toString();
    }
}
