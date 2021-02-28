package simulation;

import java.util.*;

public class SimModel {
    private static final String[] FILEPATHS=new String[]{"data/servinps1.dat","data/servinps22.dat","data/servinps23.dat","data/ws1.dat","data/ws2.dat","data/ws3.dat"};
    private double clock,lifeCycle;
    private Random random = new Random();
    private Queue<SimEvent> FEL;
    private Inspector I1,I2;
    private WorkStation W1,W2,W3;

    // Statistics and important information
    private int P1,P2,P3;
    private double I1Busy,I2Busy;


    public SimModel(double lifeCycle){
        init(lifeCycle);
    }
    private void init(double lifeCycle){
        this.lifeCycle=lifeCycle;
        clock=0;
        I1 = new Inspector(1);
        I2 = new Inspector(2);
        W1 = new WorkStation(1);
        W2 = new WorkStation(2);
        W3 = new WorkStation(3);
        I1Busy=0;
        I2Busy=0;
    }

    /**
     *  Method to start the simulation event
     */
    private void start(){
       while(clock<=lifeCycle &&!(FEL.isEmpty())){

       }
    }
    /**
     * This method handles inspector event
     * @param event
     */
    private void processInspection(SimEvent event){

    }

    /**
     * This method handles blocked events
     * @param event
     */
    private void processBlocked(SimEvent event){

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
     * Creates new event of event type
     * @param type
     * @param entity
     */
    private void createEvent(SimEvent.EVENT type, Object entity){

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


