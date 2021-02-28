package simulation;

import org.w3c.dom.events.Event;

public abstract class SimEvent implements Comparable<Event> {

    public enum EVENT {I1, I2, W1, W2, W3};
    private Object entity;
    private EVENT type;
    private double serviceTime;

    public void setType(EVENT type) {
        this.type = type;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public void setTime(double time) {
        this.serviceTime = time;
    }

    public EVENT getType() {
        return type;
    }

    public Object getEntity() {
        return entity;
    }

    public double getTime() {
        return serviceTime;
    }


    public abstract void updateTime(double time);


}
