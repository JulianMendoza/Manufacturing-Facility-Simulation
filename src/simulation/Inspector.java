package simulation;

import java.util.Random;

public class Inspector {

    public enum COMPONENT_TYPE {C1, C2, C3}

    private int id;
    private boolean isBlocked = false;
    private double sampleMean1,sampleMean2;
    private Random random;
    private COMPONENT_TYPE component;
    public Inspector(int id,double mean){
        this(id,mean,0);
    }
    public Inspector(int id,double mean,double mean2){
        this.id=id;
        this.sampleMean1=mean;
        this.sampleMean2=mean2;
        this.random=new Random();
        random.setSeed(SimModel.SEED);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public COMPONENT_TYPE getComponent() {
        if(this.id==1){
            this.component=COMPONENT_TYPE.C1;
        }else{
            if(random.nextDouble()<=0.5){
                this.component= COMPONENT_TYPE.C2;
            }else{
                this.component=COMPONENT_TYPE.C3;
            }
        }
        return component;
    }


    public boolean isBlocked() {
        return isBlocked;
    }
    public void setBlocked(boolean blocked){
        this.isBlocked=blocked;
    }
    public double getSampleMean(){
        return this.sampleMean1;
    }
    public double getSampleMean2(){
        return this.sampleMean2;
    }
}
