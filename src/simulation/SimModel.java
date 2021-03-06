package simulation;

import java.util.*;

public class SimModel {
    private double clock,lifeCycle;
    private Random random;
    private Queue<SimEvent> FEL;
    private Inspector I1,I2;
    private WorkStation W1,W2,W3;
    public static final long SEED=111;
    // Statistics and important information
    private int P1,P2,P3;
    private double I1Busy,I2Busy;
    private final double I1Mean=10.3579;
    private final double I22Mean=15.5369;
    private final double I23Mean=20.6327;
    private final double W1Mean=4.5044;
    private final double W2Mean=11.0926;
    private final double W3Mean=8.79558;

    public SimModel(double lifeCycle){
        init(lifeCycle);
    }
    private void init(double lifeCycle){
        System.out.println("CLOCK:"+clock);
        this.lifeCycle=lifeCycle;
        random=new Random();
        random.setSeed(SEED);
        clock=0;
        I1 = new Inspector(1,I1Mean);
        I2 = new Inspector(2,I22Mean,I23Mean);
        W1 = new WorkStation(1,W1Mean);
        W2 = new WorkStation(2,W2Mean);
        W3 = new WorkStation(3,W3Mean);
        I1Busy=0;
        I2Busy=0;
        FEL=new PriorityQueue<>();
        FEL.add(new SimEvent(I1,clock,random.nextDouble()));
        FEL.add(new SimEvent(I2,clock,random.nextDouble()));
    }

    /**
     *  Method to start the simulation event
     */
    private void start(){
       while(clock<=lifeCycle &&!(FEL.isEmpty())){
           SimEvent e=FEL.remove();
           clock=e.getTime();
           System.out.println("CLOCK:"+clock);
           switch(e.getType()){
               case I1: processInspection1(e); break;
               case I2: processInspection2(e); break;
               case W1:
               case W2:
               case W3:
                   processProduction(e);
                   break;
           }
           printState();
       }
    }
    private void printState(){
        System.out.println("Inspector 1 blocked status: "+ I1.isBlocked());
        System.out.println("Inspector 2 blocked status: "+ I2.isBlocked());
        System.out.println("WorkStation 1:\n"+W1);
        System.out.println("WorkStation 2:\n"+W2);
        System.out.println("WorkStation 3:\n"+W3);

    }
    /**
     * This method handles inspector event
     * @param event
     */
    private void processInspection1(SimEvent event){
        //get the workstation with the greatest amount of available buffers
        WorkStation w=getPriority(0);
        if(w!=null){
            w.setBufferAvailable(0,false);
            System.out.println("Placing C1 buffer into Workstation "+w.getId());
        }else{
            System.out.println("I1 BLOCKED");
            I1.setBlocked(true,clock);
        }
        handleChanges();
        if(!I1.isBlocked()) {
            FEL.add(new SimEvent(I1, clock, random.nextDouble()));
        }
    }
    private WorkStation getPriority(int index){
        int temp=W1.getNumAvail(index);
        int temp2=W2.getNumAvail(index);
        int temp3=W3.getNumAvail(index);
        //priority is based off of whether or not a workstation is busy THEN the buffers THEN the PRIORITY.
        if(!W1.isBusy()){
            return W1;
        }else if(!W2.isBusy()){
            return W2;
        }else if(!W3.isBusy()){
            return W3;
        }
        //all full
        if(temp==temp2&&temp==temp3&&temp==0){
            return null;
        }else if (temp>temp2&&temp>temp3||temp==temp2&&temp==temp3) {
                return W1;
        }else if(temp2>temp&&temp2>temp3||temp2==temp3){
            return W2;
        }else if(temp3>temp&&temp3>temp2){
            return W3;
        }
        return null;

    }
    private void handleChanges(){
        if(!W1.bufferAvailable(0)&&!W1.isBusy()){
            System.out.println("Removing buffer from Workstation 1");
            W1.setBufferAvailable(0,true);
            W1.setBusy(true);
            SimEvent e=new SimEvent(W1,clock,random.nextDouble());
            FEL.add(e);
        }
        if(!W2.bufferAvailable(0)&&!W2.bufferAvailable(1)&&!W2.isBusy()){
            System.out.println("Removing buffers C1 and C2 from Workstation 2");
            if(I2.isBlocked()&&I2.getComponent().equals(Inspector.COMPONENT_TYPE.C2)){
                System.out.println("Inspector 2 UNBLOCKED with C2");
                I2.setBlocked(false,-1);
                FEL.add(new SimEvent(I2,clock, random.nextDouble()));
            }else if(I1.isBlocked()){
                System.out.println("I1 UNBLOCKED");
                I1.setBlocked(false,-1);
                FEL.add(new SimEvent(I1,clock,random.nextDouble()));
            }
            W2.setBufferAvailable(0,true);
            W2.setBufferAvailable(1,true);
            W2.setBusy(true);
            SimEvent e=new SimEvent(W2,clock,random.nextDouble());
            FEL.add(e);
        }
        if(!W3.bufferAvailable(0)&&!W3.bufferAvailable(1)&&!W3.isBusy()){
            if(I2.isBlocked()&&I2.getComponent().equals(Inspector.COMPONENT_TYPE.C3)){
                System.out.println("Inspector 2 UNBLOCKED with C3");
                I2.setBlocked(false,-1);
                FEL.add(new SimEvent(I2,clock, random.nextDouble()));
            }else if(I1.isBlocked()){
                System.out.println("I1 UNBLOCKED");
                I1.setBlocked(false,-1);
                FEL.add(new SimEvent(I1,clock,random.nextDouble()));
            }
            System.out.println("Removing buffers C1 and C3 from Workstation 3");
            W3.setBufferAvailable(0,true);
            W3.setBufferAvailable(1,true);
            W3.setBusy(true);
            SimEvent e=new SimEvent(W3,clock,random.nextDouble());
            FEL.add(e);
        }
    }
    private void processInspection2(SimEvent event){
        switch (event.getComponent()){
            case C2:
                if(W2.bufferAvailable(1,0)|| W2.bufferAvailable(1,1)){
                    System.out.println("Placing C2 buffer into Workstation 2");
                    W2.setBufferAvailable(1,false);
                }else{
                    System.out.println("Inspector 2 BLOCKED with C2");
                    I2.setBlocked(true,clock);
                }
                break;
            case C3:
            if(W3.bufferAvailable(1,0)||W3.bufferAvailable(1,1)){
                System.out.println("Placing C3 buffer into Workstation 3");
                W3.setBufferAvailable(1,false);
            }else{
                System.out.println("Inspector 2 BLOCKED with C3");
                I2.setBlocked(true,clock);
            }
                break;
        }
        handleChanges();
        if(!I2.isBlocked()) {
            FEL.add(new SimEvent(I2, clock, random.nextDouble()));
        }
    }

    /**
     * this method increments the product number
     *  checks for component and
     *  buffer availability
     * @param event
     */
    private void processProduction(SimEvent event){
        WorkStation w=(WorkStation)event.getEntity();
        System.out.println("Workstation "+w.getId()+" finished production of product: "+w.getId()+" at time: "+event.getTime());
        w.setBusy(false);
        handleChanges();
    }

    /**
     * Computes statistics for throughput...etc
     */
    private void generateReport(){

    }
    public static void main(String[] args){
        SimModel model=new SimModel(1000);
        model.start();
        model.generateReport();

    }
}


