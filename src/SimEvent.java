import org.w3c.dom.events.Event;

import java.util.*;

public abstract class SimEvent implements Comparable<Event> {

    static enum event {I1, I2, W1, W2, W3};
    private Object entity;
    private event type;
    private int serviceTime;

    public SimEvent(){

        this.type = type;
        this.entity = entity;
        this.serviceTime = serviceTime;

    }

    public void setType(event type) {
        this.type = type;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public void setTime(int time) {
        this.serviceTime = time;
    }

    public event getType() {
        return type;
    }

    public Object getEntity() {
        return entity;
    }

    public int getTime() {
        return serviceTime;
    }


    public abstract void updateTime(int time);


}
