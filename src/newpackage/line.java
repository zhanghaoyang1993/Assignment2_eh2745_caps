package newpackage;
public class line {

	private double r;
    private double x;
    private double basevolt;
    private double S=1000.0;
    
    private double g;
    private double b;
    
  line(Double r,Double x,Double basevolt){
    
        this.r =r;
        this.x =x;
        this.basevolt = basevolt;
    }
    	

    
    public double getG() {
    	g=basevolt*basevolt*r/(S*(r*r+x*x));
    	return g;
    }
    
    public double getB() {
    	b=-basevolt*basevolt*x/(S*(r*r+x*x));
    	return b;
    }
}
