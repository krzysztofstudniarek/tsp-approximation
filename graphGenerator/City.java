package graphGenerator;

/**
 * @author Krzysiek
 *
 */
public class City {
	public double lat;
	public double lon;
    public String name;
    public boolean state;
    
    /**
     * Calculates distance from other given city
     * 
     * @param node	other City
     * @return		distance
     */
    public int getDistanceFrom(City node) {
		return (int) Math.sqrt((this.lat - node.lat) * (this.lat - node.lat)
				+ (this.lon - node.lon) * (this.lon - node.lon));
	}
    
}
