package eh2745_assignment2;

public class distancecompare {
	
	 double distance1;
     double[] distance2;
	 int k;
	 
	//Constructor
	    public distancecompare(double distance1, double [] distance2,int k){
	        this.distance1=distance1;
	        this.distance2=distance2;
	        this.k=k;
	    }
	    
	  //get the euclideanDistance of two points
	    public double comparator(){
	    	int j=0;
	    	for(int i=0;i<distance2.length;i++){
	    		if(distance1>=distance2[i]){
	    			j++;
	    		}
	    	}
	    	if(j<=k){
	    		return 1;
	    	}
	    	else{
	    		return 0;
	    	}
	    }
     
}