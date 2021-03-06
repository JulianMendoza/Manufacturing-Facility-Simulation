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
    private double[] productionTime;
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
        productionTime=new double[3];
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
       }
    }
    /**
     * This method handles inspector event
     * @param event
     */
    private void processInspection1(SimEvent event){
        double temp=clock;
        //get the workstation with the smallest amount of available buffers
        WorkStation w=getPriority(0);
        if(w!=null){
            w.setBufferAvailable(0,false);
            System.out.println("Placing C1 buffer into Workstation "+w.getId());
        }else{
            System.out.println("BLOCKED");
            //blocked
            //compute which workstation will be done first -> add
            //double closestFinish=Math.min(productionTime[0],Math.min(productionTime[1],productionTime[2]));
            //temp=closestFinish;
        }
        handleChanges();
        FEL.add(new SimEvent(I1,temp,random.nextDouble()));
    }
    private WorkStation getPriority(int index){
        int temp=W1.getNumAvail(index);
        int temp2=W2.getNumAvail(index);
        int temp3=W3.getNumAvail(index);
        if(temp==temp2&&temp==temp3&&temp==0){
            return null;
        }else if (temp<temp2&&temp<temp3||temp==temp2&&temp==temp3) {
            return W1;
        }else if(temp2<temp&&temp2<temp3||temp2==temp3){
            return W2;
        }else if(temp3<temp&&temp3<temp2){
            return W3;
        }else{
            System.out.println("Should not even get here");
            return null;
        }

    }
    private void handleChanges(){
        if(!W1.bufferAvailable(0)){
            System.out.println("Removing buffer from Workstation 1");
            W1.setBufferAvailable(0,true);
            SimEvent e=new SimEvent(W1,clock,random.nextDouble());
            FEL.add(e);
            productionTime[0]=e.getTime();
        }else if(!W2.bufferAvailable(0)&&!W2.bufferAvailable(1)){
            System.out.println("Removing buffers C1 and C2 from Workstation 2");
            W2.setBufferAvailable(0,true);
            W2.setBufferAvailable(1,true);
            SimEvent e=new SimEvent(W2,clock,random.nextDouble());
            FEL.add(e);
            productionTime[1]=e.getTime();
        }else if(!W3.bufferAvailable(0)&&!W3.bufferAvailable(1)){
            System.out.println("Removing buffers C1 and C3 from Workstation 3");
            W3.setBufferAvailable(0,true);
            W3.setBufferAvailable(1,true);
            SimEvent e=new SimEvent(W3,clock,random.nextDouble());
            FEL.add(e);
            productionTime[2]=e.getTime();
        }
    }
    private void processInspection2(SimEvent event){
        double temp=clock;
        switch (event.getComponent()){
            case C2:
                if(W2.bufferAvailable(1)){
                    System.out.println("Placing C2 buffer into Workstation 2");
                    W2.setBufferAvailable(1,false);
                }else{
                    System.out.println("BLOCKED");
                    //block
                    //double closestFinish=Math.min(productionTime[1],productionTime[2]);
                   //temp=closestFinish;
                }
                break;
            case C3:
            if(W3.bufferAvailable(1)){
                System.out.println("Placing C3 buffer into Workstation 3");
                W3.setBufferAvailable(1,false);
            }else{
                System.out.println("BLOCKED");
                //block
                // double closestFinish=Math.min(productionTime[1],productionTime[2]);
                // temp=closestFinish;
            }
                break;
        }
        handleChanges();
        FEL.add(new SimEvent(I2,temp,random.nextDouble()));
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
        handleChanges();
    }

    /**
     * Computes statistics for throughput...etc
     */
    private void generateReport(){

    }
    public static void main(String[] args){
        SimModel model=new SimModel(80);
        model.start();
        model.generateReport();

    }
}


