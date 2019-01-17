package ahtewlg7.utimer.entity.w5h2;

/**
 * Created by lw on 2019/1/16.
 */
public abstract class AW5h2Entity {
    private AW5h2What what;
    private AW5h2Why why;
    private AW5h2Who who;
    private AW5h2When when;
    private AW5h2Where where;
    private AW5h2How how;
    private AW5h2HowMuch howMuch;

    public AW5h2What getWhat() {
        return what;
    }

    public void setWhat(AW5h2What what) {
        this.what = what;
    }

    public AW5h2Why getWhy() {
        return why;
    }

    public void setWhy(AW5h2Why why) {
        this.why = why;
    }

    public AW5h2Who getWho() {
        return who;
    }

    public void setWho(AW5h2Who who) {
        this.who = who;
    }

    public AW5h2When getWhen() {
        return when;
    }

    public void setWhen(AW5h2When when) {
        this.when = when;
    }

    public AW5h2Where getWhere() {
        return where;
    }

    public void setWhere(AW5h2Where where) {
        this.where = where;
    }

    public AW5h2How getHow() {
        return how;
    }

    public void setHow(AW5h2How how) {
        this.how = how;
    }

    public AW5h2HowMuch getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(AW5h2HowMuch howMuch) {
        this.howMuch = howMuch;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(what != null)
            builder.append(what.toString());
        if(why != null)
            builder.append("," + why.toString());
        if(who != null)
            builder.append("," + who.toString());
        if(when != null)
            builder.append("," + when.toString());
        if(where != null)
            builder.append("," + where.toString());
        if(how != null)
            builder.append("," + how.toString());
        if(howMuch != null)
            builder.append("," + howMuch.toString());
        return builder.toString();
    }
}
