import java.util.Random;

public class Inspector {

    public enum componentType {C1, C2, C3}

    private int id;
    private componentType component;
    private boolean isBlocked = false;
    private Random random = new Random();

    public Inspector(int id){
        this.id = id;
        this.isBlocked = isBlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public componentType getComponent() {
        return component;
    }

    public void setComponent(componentType component) {
        this.component = component;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
