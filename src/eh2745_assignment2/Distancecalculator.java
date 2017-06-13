package eh2745_assignment2;

import java.util.ArrayList;

	public class Distancecalculator {
	    double[] distance1 = new double[18];
	    double[] distance2 = new double[18];
	    double[] difference = new double[18];

	    //Constructor
	    public Distancecalculator(double[] dataset1, double[] dataset2){
	        distance1=dataset1;
	        distance2=dataset2;
	    }
	    
	    //get the euclideanDistance of two points
	    public double euclideanDistance(){
	        double totalDistance = 0;
	        for (int i = 0; i < distance1.length/2; i++){
	            double difference =(distance1[i]*Math.cos(Math.toRadians(distance1[i+9])) - distance2[i]*Math.cos(Math.toRadians(distance2[i+9])))*(distance1[i]*Math.cos(Math.toRadians(distance1[i+9])) - distance2[i]*Math.cos(Math.toRadians(distance2[i+9])))+(distance1[i]*Math.sin(Math.toRadians(distance1[i+9])) - distance2[i]*Math.sin(Math.toRadians(distance2[i+9])))*(distance1[i]*Math.sin(Math.toRadians(distance1[i+9])) - distance2[i]*Math.sin(Math.toRadians(distance2[i+9])));
	            totalDistance += difference;
	        }
	        return Math.sqrt(totalDistance);
	    }
	    //Method to find the different in distance between two points 

	}
