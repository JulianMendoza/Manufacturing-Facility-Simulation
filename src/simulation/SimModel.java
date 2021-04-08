package simulation;

import util.COMPONENT_TYPE;
import util.CSVFile;

import java.util.*;

/**
 * Driving unit for the simulation
 */
public class SimModel {
    private CSVFile file;
    private double clock, lifeCycle;
    private Random[] random; //index 0-5 for I1,I22,I23,W1,W2,W3 respectively
    private Queue<SimEvent> FEL;
    private Inspector I1, I2;
    private WorkStation W1, W2, W3;
    private long SEED;
    public long[] SEEDS;
    private int trial;
    // Statistics and important information
    private int P1, P2, P3;
    private double I1Busy, I2Busy, I1Blocked, I2Blocked, W1Busy, W2Busy, W3Busy;
    private final double I1Mean = 10.3579;
    private final double I22Mean = 15.5369;
    private final double I23Mean = 20.6327;
    private final double W1Mean = 4.5044;
    private final double W2Mean = 11.0926;
    private final double W3Mean = 8.79558;

    public SimModel(double lifeCycle,long seed,int trial,CSVFile file) {
        this.trial=trial;
        random = new Random[6];
        this.SEED=seed;
        SEEDS = new long[]{SEED, SEED + 1, SEED + 2, SEED + 3, SEED + 4, SEED + 5, SEED + 6}; //index 0-4 for I1,I22,I23,W1,W2,W3 respectively index 5 for I2 component RNG
        init(lifeCycle);
        this.file=file;
    }

    /**
     * Initializes the simulation
     *
     * @param lifeCycle
     */
    private void init(double lifeCycle) {
        //System.out.println("CLOCK:" + clock);
        this.lifeCycle = lifeCycle;
        for (int i = 0; i < random.length; i++) {
            random[i] = new Random();
            random[i].setSeed(SEEDS[i]);
        }
        clock = I1Busy = I2Busy = I1Blocked = I2Blocked = W1Busy = W2Busy = W3Busy = 0;
        P1 = P2 = P3 = 0;
        I1 = new Inspector(1, I1Mean,SEEDS[0]);
        I2 = new Inspector(2, I22Mean, I23Mean,SEEDS[1]);
        W1 = new WorkStation(1, W1Mean);
        W2 = new WorkStation(2, W2Mean);
        W3 = new WorkStation(3, W3Mean);
        FEL = new PriorityQueue<>();
        FEL.add(new SimEvent(I1, clock, random[0].nextDouble()));
        FEL.add(new SimEvent(I2, clock, random[1].nextDouble())); // could actually be either random[1] or [2] but doesn't really matter
    }

    /**
     * Starts the simulation
     */
    private void start() {
        while (clock <= lifeCycle && !(FEL.isEmpty())) {
            SimEvent e = FEL.remove();
            clock = e.getTime();
            int w = 0;
            //System.out.println("CLOCK:" + clock);
            switch (e.getType()) {
                case I1:
                    processInspection1(e);
                    break;
                case I2:
                    processInspection2(e);
                    break;
                case W1:
                    w = 1;
                    processProduction(e);
                    break;
                case W2:
                    w = 2;
                    processProduction(e);
                    break;
                case W3:
                    w = 3;
                    processProduction(e);
                    break;
            }
            //printState(w);
        }
    }

    /**
     * Helper method to print the state of the simulation
     */
    private void printState(int w) {
        System.out.println("Inspector 1 blocked status: " + I1.isBlocked());
        System.out.println("Inspector 2 blocked status: " + I2.isBlocked());
        System.out.println("WorkStation 1:\n" + W1);
        System.out.println("WorkStation 2:\n" + W2);
        System.out.println("WorkStation 3:\n" + W3);
        StringBuilder sb = new StringBuilder();
        sb.append(clock + ",");
        sb.append((I1.isBlocked() ? 0 : 1) + ",");
        sb.append((I2.isBlocked() ? 0 : 1) + ",");
        sb.append((W1.isBusy() ? 1 : 0) + ",");
        sb.append(W1.getNumAvail(0) + ",");
        sb.append((W2.isBusy() ? 1 : 0) + ",");
        sb.append(W2.getNumAvail(0) + ",");
        sb.append(W2.getNumAvail(1) + ",");
        sb.append((W3.isBusy() ? 1 : 0) + ",");
        sb.append(W3.getNumAvail(0) + ",");
        sb.append(W3.getNumAvail(1) + ",");
        if (w == 0) {
            sb.append(0 + ",");
            sb.append(0 + ",");
            sb.append(0 + ",\n");
        } else if (w == 1) {
            sb.append(1 + ",");
            sb.append(0 + ",");
            sb.append(0 + ",\n");
        } else if (w == 2) {
            sb.append(0 + ",");
            sb.append(1 + ",");
            sb.append(0 + ",\n");
        } else {
            sb.append(0 + ",");
            sb.append(0 + ",");
            sb.append(1 + ",\n");
        }
        //System.out.println(sb.toString());
        //file.write(sb.toString());
    }

    /**
     * Handles events that involve inspector 1
     *
     * @param event
     */
    private void processInspection1(SimEvent event) {
        //get the workstation with the greatest amount of available buffers
        WorkStation w = getPriority(0);
        if (w != null) {
            w.setBufferAvailable(0, false);
            //System.out.println("Placing C1 buffer into Workstation " + w.getId());
            I1Busy += event.getDuration();
        } else {
            //System.out.println("I1 BLOCKED");
            I1.setBlocked(true, clock);
        }
        handleChanges();
        if (!I1.isBlocked()) {
            FEL.add(new SimEvent(I1, clock, random[0].nextDouble()));
        }
    }

    /**
     * Helper method to get the workstation with the highest priority
     *
     * @param index the component that's going to be transfered
     * @return null if there is no station, the corresponding workstation otherwise
     */
    private WorkStation getPriority(int index) {
        int temp = W1.getNumAvail(index);
        int temp2 = W2.getNumAvail(index);
        int temp3 = W3.getNumAvail(index);
        //priority is based off of shortest queue then priority
        //all full
        if (temp == temp2 && temp == temp3 && temp == 0) {
            return null;
        } else if (temp > temp2 && temp >= temp3 || temp == temp2 && temp == temp3) {
            return W1;
        } else if (temp2 > temp && temp2 >= temp3 || temp2 == temp3) {
            return W2;
        } else if (temp3 > temp && temp3 > temp2) {
            return W3;
        }
        if (!W1.isBusy()) {
            return W1;
        } else if (!W2.isBusy()) {
            return W2;
        } else if (!W3.isBusy()) {
            return W3;
        }
        return null;

    }

    /**
     * Helper method to handle changes to the state of the workstations.
     * Checks the buffers
     */
    private void handleChanges() {
        if (!W1.bufferAvailable(0) && !W1.isBusy()) {
            //System.out.println("Removing buffer from Workstation 1");
            W1.setBufferAvailable(0, true);
            W1.setBusy(true);
            SimEvent e = new SimEvent(W1, clock, random[3].nextDouble());
            FEL.add(e);
            if (I1.isBlocked()) {
                //System.out.println("I1 UNBLOCKED");
                I1Blocked += clock - I1.getBlockedTime();
                I1.setBlocked(false, -1);
                FEL.add(new SimEvent(I1, clock, random[0].nextDouble()));
            }
        }
        if (!W2.bufferAvailable(0) && !W2.bufferAvailable(1) && !W2.isBusy()) {
            //System.out.println("Removing buffers C1 and C2 from Workstation 2");
            if (I2.isBlocked() && I2.getComponent().equals(COMPONENT_TYPE.C2)) {
                //System.out.println("Inspector 2 UNBLOCKED with C2");
                I2Blocked += clock - I2.getBlockedTime();
                I2.setBlocked(false, -1);
                FEL.add(new SimEvent(I2, clock, random[1].nextDouble())); //again could be [1] or [2]
            } else if (I1.isBlocked()) {
                //System.out.println("I1 UNBLOCKED");
                I1Blocked += clock - I1.getBlockedTime();
                I1.setBlocked(false, -1);
                FEL.add(new SimEvent(I1, clock, random[0].nextDouble()));
            }
            W2.setBufferAvailable(0, true);
            W2.setBufferAvailable(1, true);
            W2.setBusy(true);
            SimEvent e = new SimEvent(W2, clock, random[4].nextDouble());
            FEL.add(e);
        }
        if (!W3.bufferAvailable(0) && !W3.bufferAvailable(1) && !W3.isBusy()) {
            if (I2.isBlocked() && I2.getComponent().equals(COMPONENT_TYPE.C3)) {
                //System.out.println("Inspector 2 UNBLOCKED with C3");
                I2Blocked += clock - I2.getBlockedTime();
                I2.setBlocked(false, -1);
                FEL.add(new SimEvent(I2, clock, random[2].nextDouble())); //again could be [1] or [2]
            } else if (I1.isBlocked()) {
                //System.out.println("I1 UNBLOCKED");
                I1Blocked += clock - I1.getBlockedTime();
                I1.setBlocked(false, -1);
                FEL.add(new SimEvent(I1, clock, random[0].nextDouble()));
            }
            //System.out.println("Removing buffers C1 and C3 from Workstation 3");
            W3.setBufferAvailable(0, true);
            W3.setBufferAvailable(1, true);
            W3.setBusy(true);
            SimEvent e = new SimEvent(W3, clock, random[5].nextDouble());
            FEL.add(e);
        }
    }

    /**
     * Handles events that involve inspector 2
     *
     * @param event
     */
    private void processInspection2(SimEvent event) {
        switch (event.getComponent()) {
            case C2:
                if (W2.bufferAvailable(1, 0) || W2.bufferAvailable(1, 1)) {
                    //System.out.println("Placing C2 buffer into Workstation 2");
                    I2Busy += event.getDuration();

                    W2.setBufferAvailable(1, false);
                } else {
                    //System.out.println("Inspector 2 BLOCKED with C2");
                    I2.setBlocked(true, clock);
                }
                break;
            case C3:
                if (W3.bufferAvailable(1, 0) || W3.bufferAvailable(1, 1)) {
                    //System.out.println("Placing C3 buffer into Workstation 3");
                    I2Busy += event.getDuration();
                    W3.setBufferAvailable(1, false);
                } else {
                    //System.out.println("Inspector 2 BLOCKED with C3");
                    I2.setBlocked(true, clock);
                }
                break;
        }
        handleChanges();
        if (!I2.isBlocked()) {
            FEL.add(new SimEvent(I2, clock, random[2].nextDouble())); //again could be [1] or [2]
        }
    }

    /**
     * Increments the product that finished production
     * call to handleChanges will simulate that the buffers have changed
     *
     * @param event
     */
    private void processProduction(SimEvent event) {
        WorkStation w = (WorkStation) event.getEntity();
        //System.out.println("Workstation " + w.getId() + " finished production of product: " + w.getId() + " at time: " + event.getTime());
        switch (w.getId()) {
            case 1:
                P1++;
                break;
            case 2:
                P2++;
                break;
            case 3:
                P3++;
                break;
        }
        w.setBusy(false);
        handleChanges();
    }

    /**
     * Computes statistics for throughput...etc
     */
    private double[] generateReport() {
        if (I1.isBlocked()) {
            I1Blocked += clock - I1.getBlockedTime();
        }
        if (I2.isBlocked()) {
            I2Blocked += clock - I2.getBlockedTime();
        }
        System.out.println("Product 1 tally: " + P1 + " throughput: " + P1 / clock + " units/unit time");
        System.out.println("Product 2 tally: " + P2 + " throughput: " + P2 / clock + " units/unit time");
        System.out.println("Product 3 tally: " + P3 + " throughput: " + P3 / clock + " units/unit time");
        System.out.println("Utilization Inspector 1: " + (I1Busy / clock) * 100 + "%");
        System.out.println("Utilization Inspector 2: " + (I2Busy / clock) * 100 + "%");
        System.out.println("Percentage blocked Inspector 1: " + (I1Blocked / clock) * 100 + "%");
        System.out.println("Percentage blocked Inspector 2: " + (I2Blocked / clock) * 100 + "%");
        StringBuilder sb=new StringBuilder();
        sb.append(trial+",");
        sb.append(P1+",");
        sb.append((P1 / clock)+",");
        sb.append(P2+",");
        sb.append((P2 / clock)+",");
        sb.append(P3+",");
        sb.append((P3 / clock)+",");
        sb.append(((I1Busy / clock) * 100)+",");
        sb.append(((I1Blocked / clock) * 100)+",");
        sb.append(((I2Busy / clock) * 100)+",");
        sb.append(((I2Blocked / clock) * 100)+",\n");
        file.write(sb.toString());
        return new double[]{P1,P1 / clock,P2,P2 / clock,P3,P3 / clock,(I1Busy / clock) * 100,(I1Blocked / clock) * 100,(I2Busy / clock) * 100,(I2Busy / clock) * 100,(I2Blocked / clock) * 100};
    }

    public static void main(String[] args) {

        List<List<Double>> replications=new ArrayList(); //replications will contain the statistics of each replication index  -> run # ->list-> statistics
        CSVFile file=new CSVFile("SimulationReplications.csv");
        long seed=11;
        long seedIncr=7;
        int numReplications=80;
        int numTrials=0;
        ArrayList<Double> tTable=getTtable(numReplications);
        outerloop:
        for(int i=0;i<numReplications;i++){
            double []average=new double[11]; //contain the running average mean of the simulation runs
            SimModel model = new SimModel(1500,seed,i,file);
            model.start();
            Double[] averageDoubleObj=new Double[11]; //Wrapper so that each simulation run can be appended to the dataStructure
            double []temp=model.generateReport();
            for(int j=0;j<averageDoubleObj.length;j++){
                averageDoubleObj[j]=Double.valueOf(temp[j]);
            }
            replications.add(Arrays.asList(averageDoubleObj));
            for(int k=0;k<replications.size();k++){
                for(int j=0;j<averageDoubleObj.length;j++){
                    average[j]+=replications.get(k).get(j); //summation
                }
            }
            for(int k=0;k<average.length;k++){
                average[k]=average[k]/replications.size(); //average
            }
            double []sumYiminusYbarSquared=new double[11];
            for(int k=0;k<replications.size();k++){
                for(int j=0;j<average.length;j++){
                    sumYiminusYbarSquared[j]+=Math.pow(replications.get(k).get(j)-average[j],2);
                }
            }
            int count=0;
            if(i>=4&&i<80) { //5 simulation threshold 80 simulation upperbound
                for (int j = 0; j < average.length; j++) {
                    count++;
                    if(j==7) { //this is the blocked percentage of the simulation run, causes problems for the given scheduler since it rarely blocks
                        count++;
                    }else {
                        double variance = sumYiminusYbarSquared[j] / (i + 1);
                        double error = tTable.get(i) * ((Math.sqrt(variance)) / Math.sqrt(i + 1)) * 2;
                        if (average[j] * 0.2 < (error)) {
                            break;
                        }
                    }
                    if(count==9){
                        break outerloop;
                    }
                }
            }
            seed+=seedIncr;
        }
        file.close();
    }
    public static ArrayList<Double> getTtable(int replications){
        ArrayList<Double> tTable=new ArrayList<>(Arrays.asList(new Double[]{12.71,4.30,3.18,2.78,2.57,2.45,2.36,2.31,2.26,2.23,2.20,2.18,2.16,2.14,2.13,2.12,2.11,2.10,2.09,2.09,2.08,2.07,2.06,2.06,2.06,2.05,2.05,2.04}));
        for(int i=0;i<10;i++){
            tTable.add(2.04);
        }
        for(int i=0;i<20;i++){
            tTable.add(2.02);
        }
        for(int i=0;i<60;i++){
            tTable.add(2.0);
        }
        for(int i=0;i<replications;i++){
            tTable.add(1.98);
        }
        return tTable;
    }
}


