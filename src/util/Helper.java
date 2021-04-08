package util;

import simulation.SimModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Helper {
    public static ArrayList<Double> getTtable(int replications) {
        ArrayList<Double> tTable = new ArrayList<>(Arrays.asList(new Double[]{12.71, 4.30, 3.18, 2.78, 2.57, 2.45, 2.36, 2.31, 2.26, 2.23, 2.20, 2.18, 2.16, 2.14, 2.13, 2.12, 2.11, 2.10, 2.09, 2.09, 2.08, 2.07, 2.06, 2.06, 2.06, 2.05, 2.05, 2.04}));
        for (int i = 0; i < 10; i++) {
            tTable.add(2.04);
        }
        for (int i = 0; i < 20; i++) {
            tTable.add(2.02);
        }
        for (int i = 0; i < 60; i++) {
            tTable.add(2.0);
        }
        for (int i = 0; i < replications; i++) {
            tTable.add(1.98);
        }
        return tTable;
    }

    public static void setUp(int seedBound, int seedIncBound, int lowerThreshold, int numReplications, boolean optimized) {
        Random random = new Random();
        List<List<Double>> replications = new ArrayList(); //replications will contain the statistics of each replication index  -> run # ->list-> statistics
        CSVFile file;
        if (optimized) {
            file = new CSVFile("SimulationReplicationsOptimized.csv");
        } else {
            file = new CSVFile("SimulationReplications.csv");
        }
        long seed = random.nextInt(seedBound) + 1;
        long seedIncr = random.nextInt(seedIncBound) + 1;
        int numTrials = 0;
        ArrayList<Double> tTable = Helper.getTtable(numReplications);
        outerLoop:
        for (int i = 0; i < numReplications; i++) {
            double[] average = new double[11]; //contain the running average mean of the simulation runs
            SimModel model = new SimModel(3000, seed, i, file, optimized);
            model.start();
            Double[] averageDoubleObj = new Double[11]; //Wrapper so that each simulation run can be appended to the dataStructure
            double[] temp = model.generateReport();
            for (int j = 0; j < averageDoubleObj.length; j++) {
                averageDoubleObj[j] = Double.valueOf(temp[j]);
            }
            replications.add(Arrays.asList(averageDoubleObj));
            for (int k = 0; k < replications.size(); k++) {
                for (int j = 0; j < averageDoubleObj.length; j++) {
                    average[j] += replications.get(k).get(j); //summation
                }
            }
            for (int k = 0; k < average.length; k++) {
                average[k] = average[k] / replications.size(); //average
            }
            double[] sumYiminusYbarSquared = new double[11];
            for (int k = 0; k < replications.size(); k++) {
                for (int j = 0; j < average.length; j++) {
                    sumYiminusYbarSquared[j] += Math.pow(replications.get(k).get(j) - average[j], 2);
                }
            }
            int count = 0;
            if (i >= lowerThreshold && i < numReplications - 1) {
                for (int j = 0; j < average.length; j++) {
                    count++;
                    if (j == 7 || j == 9) { //this is the blocked percentage of the simulation run, causes problems for the given scheduler since it rarely blocks
                        count++;
                    } else {
                        double variance = sumYiminusYbarSquared[j] / (i + 1);
                        double error = tTable.get(i) * ((Math.sqrt(variance)) / Math.sqrt(i + 1)) * 2;
                        if (average[j] * 0.2 < (error)) {
                            break;
                        }
                    }
                    if (count == 9) {
                        break outerLoop;
                    }
                }
            }
            seed += seedIncr;
        }
        file.close();
    }
}
