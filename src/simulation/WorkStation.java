package simulation;

public class WorkStation {

    private int id;
    private boolean[][] buffers;
    private double sampleMean,completionTime;
    public WorkStation(int id,double mean) {
        this.id = id;
        this.sampleMean=mean;
        buffers= new boolean[][]{{true,true},{true,true}};
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean bufferAvailable(int buffer){
        return buffers[buffer][0]||buffers[buffer][1];
    }

    /**
     * Sets a buffer to either true or false
     * @param buffer
     * @param available
     */
    public void setBufferAvailable(int buffer,boolean available){
        if(buffers[buffer][0]!=available){
            buffers[buffer][0]=available;
        }else{
            buffers[buffer][1]=available;
        }
    }
    public int getNumAvail(int index){
        int count=0;
        for(boolean b:buffers[index]){
            if(b) count++;
        }
        return count;
    }

    public double getCompletionTime() {
        return completionTime;
    }
    public double getSampleMean() {
        return sampleMean;
    }
}