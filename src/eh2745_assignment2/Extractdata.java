package eh2745_assignment2;

public class Extractdata {
	 String name, rdfID, subID;
	    double time;
	    double value;

	    
	    Extractdata(String rdfID, String name, double time, double value, String subID) {
	        this.rdfID = rdfID;
	        this.name = name;
	        this.time = time;
	        this.value = value;
	        this.subID = subID;
	    }
	    public String getname() {
	        return name;
	    }
	    public String getrdfID() {
	        return rdfID;
	    }
	    public String getsubID() {
	        return subID;
	    }  //return Time
	    public double gettime() {
	        return time;
	    }
	    public double getvalue() {
	        return value;
	    }


	}
