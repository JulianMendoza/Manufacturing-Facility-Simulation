package simulation;


import util.COMPONENT_TYPE;
import util.EVENT;

public class SimEvent implements Comparable<SimEvent>{
    private Object entity;
    private EVENT type;
    private double serviceTime;
    private double duration;
    private COMPONENT_TYPE component;
    public SimEvent(Object entity,double clock,double x){
        this.entity=entity;
        create(clock,x);
    }
    private void create(double clock,double x){
        if(this.entity instanceof Inspector){
            Inspector i= (Inspector)entity;
            if(i.getId()==1) {
                this.type = EVENT.I1;
            }else{
                this.type = EVENT.I2;
            }
            component=i.generateComponent();
            if(component.equals(COMPONENT_TYPE.C1)||component.equals(COMPONENT_TYPE.C2)){
                this.duration=(i.getSampleMean()*-Math.log(-x+1));
            }else{
                this.duration=(i.getSampleMean2()*-Math.log(-x+1));
            }
            this.serviceTime=clock+this.duration;
            System.out.println("Creating event for Inspector "+i.getId()+" time of completion: "+serviceTime);
        }else if(this.entity instanceof WorkStation){
            WorkStation w=(WorkStation)entity;
            if(w.getId()==1){
                this.type=EVENT.W1;
            }else if(w.getId()==2){
                this.type=EVENT.W2;
            }else{
                this.type=EVENT.W3;
            }
            this.duration=(w.getSampleMean()*-Math.log(-x+1));
            this.serviceTime=clock+this.duration;
            System.out.println("Creating event for Workstation "+w.getId()+" time of completion: "+serviceTime);
        }
    }
    public EVENT getType() {
        return type;
    }
    public Object getEntity() {
        return entity;
    }
    public COMPONENT_TYPE getComponent() {
        return component;
    }
    public double getTime() {
        return serviceTime;
    }
    public double getDuration() {
        return duration;
    }
    @Override
    //probably need margin of error
    public int compareTo(SimEvent o) {
        if(serviceTime>o.getTime()){
            return 1;
        }else if(serviceTime<o.getTime()){
            return -1;
        }else{
            return 0;
        }
    }
}
