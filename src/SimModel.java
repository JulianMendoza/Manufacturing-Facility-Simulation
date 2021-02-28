public class SimModel {
    private static final int BUFFER_SIZE = 2;

    private static void init(){

        Inspector I1 = new Inspector(1);
        Inspector I2 = new Inspector(1);
        Workstation w1 = new Workstation(1);
        Workstation w2 = new Workstation(2);
        Workstation w3 = new Workstation(3);

    }

    public static void main(String[] args){

        init(); //initializes 2 inspectors and 3 workstations

    }


    /**
     * This method lets inspector 1 place
     *  component in the buffer
     * @param event
     */
    private static void Inspector1(SimEvent event){

    }

    /**
     * This method lets inspector 2 place
     *  component in the buffer
     * @param event
     */
    private static void Inspector2(SimEvent event){

    }

    /**
     * this method increments the product number
     *  checks for component and
     *  buffer availability
     * @param event
     */
    private static void WorkStation(SimEvent event){

    }

    /**
     * Creates new event of event type
     * @param type
     * @param entity
     */
    private static void createEvent(SimEvent.event type, Object entity){

    }
}


