package simulation;

public class WorkStation {

    private int id;
    private boolean[] buffers;
    private boolean busy;
    private double sampleMean;
    public WorkStation(int id,double mean) {
        this.id = id;
        this.sampleMean=mean;
        buffers= new boolean[] {true,true,true};
        this.busy=false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean bufferAvailable(int buffer){
        return buffers[buffer];
    }
    public void setBufferAvailable(int buffer, boolean available){
        buffers[buffer]=available;
    }
    public void setBusy(boolean busy){
        this.busy=busy;
    }
    public boolean isBusy(){
        return this.busy;
    }


}