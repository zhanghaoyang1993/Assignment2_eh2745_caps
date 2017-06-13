package newpackage;

public class transformer {

	private double r;
    private double x;
    private double basevolt;
    private double S=1000.0;
    
    private double g;
    private double b;
    
  transformer(Double r,Double x,Double basevolt){
    
        this.r =r;
        this.x =x;
        this.basevolt = basevolt;
    }
    	
    //transfer r and x to g and b:1/(r+ix)=r/(r^2+x^2)-i*x/(r^2+x^2)=b+ig
    
    
    public double getG() {
    	g=basevolt*basevolt*r/(S*(r*r+x*x));
    	return g;
    }
    
    public double getB() {
    	b=-basevolt*basevolt*x/(S*(r*r+x*x));
    	return b;
    }
}
