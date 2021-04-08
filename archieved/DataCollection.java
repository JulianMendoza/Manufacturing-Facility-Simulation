package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;
public class DataCollection {
    private static final String[] FILEPATHS=new String[]{"/data/servinsp1.dat","/data/servinsp22.dat","/data/servinsp23.dat","/data/ws1.dat","/data/ws2.dat","/data/ws3.dat"};
    private static final String PATH= Paths.get("").toAbsolutePath().toString();
    private HashMap<Integer,ArrayList<Double>> map;
    public DataCollection(){
        map=new HashMap<>();
    }
    public void generate(){
        for(int i=0;i<FILEPATHS.length;i++){
            map.put(i,readFile(PATH+FILEPATHS[i]));
        }
        computeStats();
    }
    private ArrayList<Double> readFile(String path){
        ArrayList<Double> d=new ArrayList<>();
        try{
            File file=new File(path);
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
              d.add(scanner.nextDouble());
            }
        }catch(FileNotFoundException e){
            System.out.println("Invalid filepath.");
            e.printStackTrace();
        }finally{
            return d;
        }
    }
    private void computeStats(){
       for(Integer i:map.keySet()){
           int size=map.get(i).size();
           double max=Collections.max(map.get(i));
           double min=Collections.min(map.get(i));
           int binSize=(int)Math.sqrt(size);
           double frequency=(max-min)/binSize;
           JSONObject stats=new JSONObject();
           stats.put("id",i);
           stats.put("size",size);
           stats.put("bins",binSize);
           stats.put("mean",map.get(i).stream().mapToDouble(f -> f.doubleValue()).sum()/size);
           stats.put("max",max);
           stats.put("min",min);
           stats.put("frequency",frequency);
           for(int k=1;k<=binSize;k++){
               int count=0;
               for(double d:map.get(i)){
                   if(d<min+(frequency*k)&&d>=min+frequency*(k-1)){
                       count++;
                   }
               }
               //last input
               if(k==binSize){
                   count++;
               }
               stats.put("bin"+k,count);
           }
           String path="/data/"+i+".json";
           writeJSON(path,stats);
       }
    }
    private void writeJSON(String path,JSONObject o){
        try{
            FileWriter writer=new FileWriter(PATH+path);
            writer.write(o.toString());
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        DataCollection d=new DataCollection();
        d.generate();
    }
}
