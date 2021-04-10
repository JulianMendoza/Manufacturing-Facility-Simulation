package simulation;

import util.Helper;


public class Main {
    public static void main(String[] arg) {
        Helper.setUp(10000,200,6,500,5000,"SimulationReplicationsOptimized.csv",true);
        Helper.setUp(10000,200,6,500,5000,"SimulationReplications.csv",false);
    }
}
