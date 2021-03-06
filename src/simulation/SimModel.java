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
           switch(e.getType()){
               case I1:  processInspection1(e);
               case I2:
                   processInspection2(e);
                   break;
               case W1:
               case W2:
               case W3:
                   processProduction(e);
           }
           clock=e.getTime();
       }
    }
    /**
     * This method handles inspector event
     * @param event
     */
    private void processInspection1(SimEvent event){
        if(!W1.isBusy()){
            W1.setBusy(true);
            
        }
    }
    private void processInspection2(SimEvent event){
    }

    /**
     * this method increments the product number
     *  checks for component and
     *  buffer availability
     * @param event
     */
    private void processProduction(SimEvent event){

    }

    /**
     * Computes statistics for throughput...etc
     */
    private void generateReport(){

    }
    public static void main(String[] args){
        SimModel model=new SimModel(3000);
        model.start();
        model.generateReport();

    }
}


