package simulation;

import util.COMPONENT_TYPE;

import java.util.Random;

public class Inspector {

    private int id;
    private boolean isBlocked = false;
    private double sampleMean1,sampleMean2;
    private Random random;
    private COMPONENT_TYPE component;
    private double blockedTime;
    public Inspector(int id,double mean,long seed){
        this(id,mean,0,seed);
    }
    public Inspector(int id,double mean,double mean2,long seed){
        this.id=id;
        this.sampleMean1=mean;
        this.sampleMean2=mean2;
        this.random=new Random();
        this.random.setSeed(seed);
        this.blockedTime=-1;

    }

    public int getId() {
        return id;
    }

    public COMPONENT_TYPE generateComponent() {
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
    public COMPONENT_TYPE getComponent(){
        return this.component;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
    public double getBlockedTime(){
        return this.blockedTime;
    }
    public void setBlocked(boolean blocked,double time){
        this.isBlocked=blocked;
        this.blockedTime=time;
    }
    public double getSampleMean(){
        return this.sampleMean1;
    }
    public double getSampleMean2(){
        return this.sampleMean2;
    }
}
