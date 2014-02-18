package graphGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import org.json.JSONObject;

/**
 * @author Krzysiek
 *
 */
public class DistanceMatrixGenerator {
    
    private Tuple[][] TimeDistanceMatrix;
    
    private Vector<City> cities;
    private int size;
    
    /**
     * @param size Size of the graph to generate
     */
    public DistanceMatrixGenerator(int size){
        this.cities = new Vector<City>();
        this.size = size;
        this.TimeDistanceMatrix = new Tuple[size][size];
    }
    
    /**
     * Gets cities list form specified file
     * 
     * @param filePath 					path to the file
     * @throws FileNotFoundException	
     * @throws IOException	
     */
    public void getCitiesFromFile(String filePath) throws FileNotFoundException, IOException{

            System.out.print("Start of reading cities to memory\n");
            
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String nextLine = br.readLine();
            
            while (nextLine != null && cities.size()<size) {
                String[] parts = nextLine.split(";");
                //System.out.println(nextLine);
                if (parts.length != 3) {
                    System.out.println("Line: " + nextLine + " does not have the correct number of components");
                    System.exit(2);
                }
                
                City c = new City();
                c.lat = Double.parseDouble(parts[0]);
                c.lon = Double.parseDouble(parts[1]);
                c.name = parts[2];
                
                Tuple t;
                boolean ok = true;
                
                for(int j=0; j<cities.size(); j++){
                	if((t = this.getDistance(c, this.cities.get(j)))!= null){
                		this.TimeDistanceMatrix[j][cities.size()] = t;
                		this.TimeDistanceMatrix[cities.size()][j] = t;
                	}else{
                		ok = false;
                		System.out.print("\n\n\n\nNOT OK\n\n\n\n");
                		break;
                	}
                }
                
                if(ok){
                	Tuple tu = new Tuple();
                	
                	tu.a = c;
                	tu.b = c;
                	tu.distance = 0;
                	tu.time = 0;
                	
                	this.TimeDistanceMatrix[cities.size()][cities.size()] = tu;
   
                	cities.add(c);
                }               
                nextLine = br.readLine();
                System.out.print("\n"+cities.size()+"\n");
            }
            br.close();
        
    }
    
    /**
     * Generates cities form file with the specific formating
     * 
     * @param fileName					specified file name
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void generateCitiesInput(String fileName) throws FileNotFoundException, IOException{
        System.out.print("Start of generaing cities input\n");
            
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            BufferedWriter wr = new BufferedWriter(new FileWriter("usa.cities"));
            wr.write("");
            String nextLine = br.readLine();
            String s = "";
            while (nextLine != null) {
                String[] parts = nextLine.split("\\|");
                
                try{
                    Double.parseDouble(parts[9]);
                    Double.parseDouble(parts[10]);
                    
                    s = parts[9] +";" + parts[10] + ";" + parts[1] + "\n";
                    wr.append(s);
                
                } catch(NumberFormatException e){
                    System.out.print(nextLine + " NOT TAKEN CAUSE WRONG INPUT");
                } catch(IndexOutOfBoundsException e){
                    System.out.print(nextLine + " NOT TAKEN CAUSE WRONG INPUT");
                }
                
                
                nextLine = br.readLine();
            }
            br.close();
            wr.close();
            
            System.out.print("End of generaing cities input\n");
    }
    
    /**
     * Writes down generated matrix to file
     * 
     * @param filePath		path to file that should be overrided
     * @throws IOException
     */
    public void WriteDistanceMatrixToFile(String filePath) throws IOException{
    	String s ="";
    	File f = new File(filePath);
    	if(!f.exists()){
    		f.createNewFile();
    	}
        
    	BufferedWriter w = new BufferedWriter(new FileWriter(f));
    	
    	for(int i =0; i<cities.size(); i++){
    		s += cities.get(i).name + ";";
    	}
    	
    	s+= "\n";
    	
    	w.write(s);
    	
        for(int i = 0; i<cities.size(); i++){
        	s = "";
            for(int j = 0; j<cities.size(); j++){
                s+= this.TimeDistanceMatrix[i][j].distance + ";";
            }
            s+="\n";
            w.append(s);
        }
        
        w.close(); 
    }
    
    /**
     * Getting distance and travel time between two cities from http://router.project-osrm.org
     * 
     * @param a		First city
     * @param b		Second city
     * @return		distance and time between those cities
     * @throws IOException
     */
    private Tuple getDistance(City a, City b) throws IOException {
        System.out.println("Calculating distance between " + a.name + " and " + b.name);
        String url = "http://router.project-osrm.org/"
                + "viaroute?loc=" + a.lat + "," + a.lon + "&"
                + "loc=" + b.lat + "," + b.lon + "&"
                + "instructions=false&geometry=false";
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Apmon's City routing grid2");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                new BufferedInputStream(conn.getInputStream())));
        try {
            JSONObject jsonObj = new JSONObject(br.readLine());
            Tuple result = new Tuple();
            if(jsonObj.getInt("status") != 207){
	            
	            result.distance = jsonObj.getJSONObject("route_summary").getDouble("total_distance")/1000;
	            result.time = jsonObj.getJSONObject("route_summary").getInt("total_time");
	            result.a = a;
	            result.b = b;
	           
            }else{
            	return null;
            }
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Vector<City> getCities(){
        return this.cities;
    }
    
    public Tuple[][] getTimeDistanceMatrix(){
        return this.TimeDistanceMatrix;
    }

    /**
     * Draws distance matrix
     * 
     * @return string with distance matrix drawed
     */
    public String drawDistanceMatrix(){
        String s ="";
        
        for(int i = 0; i<cities.size(); i++){
            for(int j = 0; j<cities.size(); j++){
                s+= this.TimeDistanceMatrix[i][j].distance + " km---";
            }
            s+="\n";
        }
        
        return s;
        
    }
    
    public int[][] getDistanceMatrix(){
        int[][] s = new int[cities.size()][cities.size()];
        
        for(int i = 0; i<cities.size(); i++){
            for(int j = 0; j<cities.size(); j++){
                s[i][j]= (int) this.TimeDistanceMatrix[i][j].distance;
            }
        }
        
        return s;
        
    }
    
    public String drawTimeMatrix(){
        String s ="";
        
        for(int i = 0; i<cities.size(); i++){
            for(int j = 0; j<cities.size(); j++){
                s+= String.format("%d:%02d:%02d", (this.TimeDistanceMatrix[i][j].time / 3600), (this.TimeDistanceMatrix[i][j].time / 60 % 60), this.TimeDistanceMatrix[i][j].time % 60) + "---";
            }
            s+="\n";
        }
        
        return s;
        
    }
    
    
    /**
     * runns the matrix generator
     * 
     * @return
     */
    public int[][] run() {
        System.out.print("There are ");
        
        try {
           // dmg.generateCitiesInput("tmp.txt");
            
            System.out.print("There are "+this.getCities().size()+" in the memory before reading\n");
            this.getCitiesFromFile("usa.cities");
            System.out.print("There are "+this.getCities().size()+" in the memory after reading");
            
           // this.calculateDistanceMatrix();
            
            System.out.print("\n----------DISTANCE MATRIX----------\n\n"+this.drawDistanceMatrix());
            
          //  System.out.print("\n----------TIME MATRIX----------\n\n"+this.drawTimeMatrix());
            
            return this.getDistanceMatrix();
            
        } catch (FileNotFoundException ex) {
            System.out.printf(ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.printf(ex.getMessage());
            return null;
        }
    }
}
