package eh2745_assignment2;

import java.util.Set;



public class Point {
    // point
	double[] x1 = new double[18];


    public Point(double [] set1) {

        this.x1 = set1;
               
    }
    public double[] getset() {
        return x1;
    }
    public double getvalue(int i) {
        return x1[i];
    }
}