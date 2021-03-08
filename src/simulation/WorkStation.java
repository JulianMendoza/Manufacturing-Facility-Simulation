package simulation;

public class WorkStation {

    private int id;
    private boolean[][] buffers;
    private double sampleMean;
    private boolean isBusy;
    public WorkStation(int id,double mean) {
        this.id = id;
        this.sampleMean=mean;
        buffers= new boolean[][]{{true,true},{true,true}};
        this.isBusy=false;
    }

    public int getId() {
        return id;
    }

    public boolean bufferAvailable(int buffer,int index){
        return buffers[buffer][index];
    }
    public boolean bufferAvailable(int buffer){
        return buffers[buffer][0]&&buffers[buffer][1];
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
    public boolean isBusy(){
        return isBusy;
    }
    public void setBusy(boolean b){
        this.isBusy=b;
    }
    public double getSampleMean() {
        return sampleMean;
    }
    @Override
    public String toString(){
        if(id==1){
            return "Is busy: "+isBusy +"\nBuffer 0 ["+buffers[0][0]+","+buffers[0][1]+"]";
        }else{
            return "Is busy: "+isBusy +"\nBuffer 0 ["+buffers[0][0]+","+buffers[0][1]+"]\n"+"Buffer 1 ["+buffers[1][0]+","+buffers[1][1]+"]";
        }
    }
}