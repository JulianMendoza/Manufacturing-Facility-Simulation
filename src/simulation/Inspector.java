package simulation;

public class Inspector {

    public enum COMPONENT_TYPE {C1, C2, C3}

    private int id;
    private COMPONENT_TYPE component;
    private boolean isBlocked = false;

    public Inspector(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public COMPONENT_TYPE getComponent() {
        return component;
    }

    public void setComponent(COMPONENT_TYPE component) {
        this.component = component;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
