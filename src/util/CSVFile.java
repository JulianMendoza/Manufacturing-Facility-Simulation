package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CSVFile {
    private String fileName;
    private PrintWriter printWriter;
    public CSVFile(String fileName) {
        this.fileName = fileName;
        create();
    }
    private void create(){
        try{
            printWriter=new PrintWriter(new File(fileName));
            printWriter.write(headings());
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    private String headings(){
        StringBuilder sb=new StringBuilder();
        sb.append("Trial,Product1 Tally,Product1 Throughput,Product2 Tally,Product2 Throughput,Product 3 Tally,Product3 Throughput,Utilization I1,Percentage BlockedI1,Utilization I2,PercentageBlockedI2,SEED\n");
        //sb.append("Time,Inspector1Busy,Inspector2Busy,WorkStation1Busy,W1Buffer0,Workstation2Busy,W2Buffer0,W2Buffer1,W3Busy,W3Buffer0,W3Buffer1,Component1Produced,Component2Produced,Component3Produced\n");
        //sb.append("0.00,1,1,1,2,0,2,2,0,2,2,0,0,0\n");
        return sb.toString();
    }
    public void write(String str){
        printWriter.write(str);
    }
    public void close(){
        printWriter.close();
    }

}
