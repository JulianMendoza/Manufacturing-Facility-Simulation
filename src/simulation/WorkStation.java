package simulation;

public class WorkStation {

    private int id;
    private boolean bufferAvailable;
    public WorkStation(int id) {
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

    public void setBufferAvailable(boolean available){
        bufferAvailable = available;
    }


}