public class Workstation {

    private int id;
    private boolean bufferAvailable;
    private Inspector.componentType component;
    public Workstation(int id) {
        this.id = id;
        this.bufferAvailable = true;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean bufferAvailable(){
        return bufferAvailable;
    }

    public void setBufferAvailable(){
        bufferAvailable = true;
    }


}