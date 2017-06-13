package eh2745_assignment2;

import org.jfree.ui.RefineryUtilities;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Font;

import eh2745_assignment2.Point;
import eh2745_assignment2.Distancecalculator;

import java.util.*;

public class MainClassGui {

	private JFrame frame;
	public static String sql, query;
	public static int count = 1;
	public static int temp = 0;
	public static Connection conn = null;
	public static Statement stmt = null;
	public static Object Alldata[][], Testdata[][], Clusteroneobj[][],
			Clustertwoobj[][], Clusterthreeobj[][], Clusterfourobj[][],
			outputprint[][];
	public static Object columns[];
	public static double[] time;
	public static double[] VoltVal;
	public static double[] abgVal;

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";
	static final String DISABLE_SSL = "?useSSL=false";
	// Database credentials
	static String USER = null;
	static String PASS = null; // insert the password to SQL server

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClassGui window = new MainClassGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainClassGui() {
		// Entering of username and encrypted password
		JLabel jusername = new JLabel("Enter Username here:");
		JTextField userName = new JTextField();
		JLabel jpassword = new JLabel("Enter Password here:");
		JTextField password = new JPasswordField();
		Object[] data = { jusername, userName, jpassword, password };
		int result = JOptionPane.showConfirmDialog(null, data,
				"Enter Credentials for SQL.", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String userNameValue = userName.getText();
			String passwordValue = password.getText();
			USER = userNameValue;
			PASS = passwordValue;
		}

		try {

			Class.forName(JDBC_DRIVER);
			// connect to database
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Window for entering database name
			String databaseName = JOptionPane
					.showInputDialog("Enter name of database which you want to read from SQL");
			stmt = conn.createStatement();
			stmt.executeUpdate("USE " + databaseName);
			conn = DriverManager.getConnection(DB_URL + databaseName, USER,
					PASS);
			stmt = conn.createStatement();
			initialize();

		} catch (Exception e) {
			Object message = "Got an exception!" + "\n"
					+ "Please recheck Credentials" + "\n" + e.getMessage();
			JOptionPane.showMessageDialog(null, message);
			e.printStackTrace();
		}

	}

	public class tables extends JFrame {
		// method to plot table for values from sql database
		private static final long serialVersionUID = 1L;
		JFrame frame = new JFrame();
		private Object data;
		private Object column;

		// takes input as the data from database and the column names
		public tables(Object data[][], Object column[]) {
			this.data = data;
			this.column = column;
			JTable table = new JTable(data, column);
			JScrollPane scrollPane = new JScrollPane(table);
			frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
			frame.setSize(1500, 1500);
			frame.setTitle("***********************Data points*********************");
			frame.setLocation(200, 0);

			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			String learningSetname = JOptionPane
					.showInputDialog("Enter the learning/training set to perform kMeans algorithm:");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + learningSetname
					+ " ORDER BY time ASC");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			boolean b = rsmd.isSearchable(1);
			rs.last();
			int numberOfRows = rs.getRow();
			int j = 0;

			Alldata = new Object[numberOfRows][numberOfColumns + 1];
			// Pick all objects until there is no next object
			ResultSet rs1 = stmt.executeQuery("SELECT * FROM "
					+ learningSetname + " ORDER BY time ASC");
			double[] TIMEarr = new double[numberOfRows];
			double[] VALarr = new double[numberOfRows];
			String[] RDFIDarr = new String[numberOfRows];
			String[] NAMEarr = new String[numberOfRows];
			String[] SUBIDarr = new String[numberOfRows];
			ArrayList<Extractdata> voltList = new ArrayList<Extractdata>();
			ArrayList<Extractdata> angList = new ArrayList<Extractdata>();
			while (rs1.next()) {
				for (int k = 1; k <= numberOfColumns; k++) {
					Alldata[j][k] = rs1.getObject(k);
				}
				double TIME = rs1.getDouble("time");
				double Vol_Val = rs1.getDouble("value");
				String rdfID = rs1.getString("rdfid");
				String subID = rs1.getString("sub_rdfid");
				String name = rs1.getString("name");
				TIMEarr[j] = TIME;
				VALarr[j] = Vol_Val;
				RDFIDarr[j] = rdfID;
				NAMEarr[j] = name;
				SUBIDarr[j] = subID;
				Extractdata substation = new Extractdata(rdfID, name, TIME,
						Vol_Val, subID);
				if (j % 2 == 0) {
					voltList.add(substation);
				} else {
					angList.add(substation);
				}
				j++;
			}
			Object[] columns = { "", "RDFID", "NAME", "TIME", "VALUE",
					"SUBRDFID" };
			String number = JOptionPane
					.showInputDialog("Enter the number of buses:");
			int q=Integer.valueOf(number);
			System.out.print(q);

			// inserting the values in a new matrix
			double[][] points = new double[numberOfRows/(2*q)][2*q];

			// save the values in one row
			double[] pointsrow = new double[2*q];
			for (int j1 = 0; j1 < numberOfRows/(2*q); j1++) {
				for (int j2 = 0; j2 < q; j2++) {
					points[j1][j2] = voltList.get(j1 * q + j2).getvalue();
					points[j1][j2 + q] = angList.get(j1 * q + j2).getvalue();
				}
			}
			for (int z = 0; z < numberOfRows/(2*q); z++) {
				for (int y = 0; y < 2*q; y++) {
					// System.out.println(points[z][y]);
				}
			}
			String testSetname = JOptionPane
					.showInputDialog("Enter the test set on which you might later like to perform kNN algorithm:");
			ResultSet rs2 = stmt.executeQuery("SELECT * FROM " + testSetname
					+ " ORDER BY time ASC");
			ResultSetMetaData rsmd2 = rs2.getMetaData();
			int numberOfColumnstest = rsmd.getColumnCount();
			boolean btest = rsmd.isSearchable(1);
			rs2.last();
			int numberOfRowstest = rs2.getRow();
			int jtest = 0;
			Testdata = new Object[numberOfRowstest][numberOfColumnstest + 1];
			// Pick all objects until there is no next object
			ResultSet rs3 = stmt.executeQuery("SELECT * FROM " + testSetname
					+ " ORDER BY time ASC");
			double[] TIMEarrtest = new double[numberOfRowstest];
			double[] VALarrtest = new double[numberOfRowstest];
			String[] RDFIDarrtest = new String[numberOfRowstest];
			String[] NAMEarrtest = new String[numberOfRowstest];
			String[] SUBIDarrtest = new String[numberOfRowstest];
			ArrayList<Extractdata> voltListtest = new ArrayList<Extractdata>();
			ArrayList<Extractdata> angListtest = new ArrayList<Extractdata>();
			while (rs3.next()) {
				for (int k = 1; k <= numberOfColumnstest; k++) {
					Testdata[jtest][k] = rs3.getObject(k);
				}
				double TIMEtest = rs3.getDouble("time");
				double Vol_Valtest = rs3.getDouble("value");
				String rdfIDtest = rs3.getString("rdfid");
				String subIDtest = rs3.getString("sub_rdfid");
				String nametest = rs3.getString("name");
				TIMEarrtest[jtest] = TIMEtest;
				VALarrtest[jtest] = Vol_Valtest;
				RDFIDarrtest[jtest] = rdfIDtest;
				NAMEarrtest[jtest] = nametest;
				SUBIDarrtest[jtest] = subIDtest;
				Extractdata substationtest = new Extractdata(rdfIDtest,
						nametest, TIMEtest, Vol_Valtest, subIDtest);
				if (jtest % 2 == 0) {
					voltListtest.add(substationtest);
				} else {
					angListtest.add(substationtest);
				}
				jtest++;
			}


			Object[] columnstest = { "", "RDFID", "NAME", "TIME", "VALUE",
					"SUBRDFID" };

			// inserting the values in a new matrix
			double[][] pointstest = new double[numberOfRows/q][q*2];

			// save the values in one row
			double[] pointsrowtest = new double[q*2];
			
			for (int j1 = 0; j1 < numberOfRowstest/(2*q); j1++) {
				for (int j2 = 0; j2 < q; j2++) {
					pointstest[j1][j2] = voltListtest.get(j1 * q + j2)
							.getvalue();
					pointstest[j1][j2 + q] = angListtest.get(j1 * q + j2)
							.getvalue();
				}
			}

			// set initial 4 points
			double[] initialmean1 = new double[2*q];
			double[] initialmean2 = new double[2*q];
			double[] initialmean3 = new double[2*q];
			double[] initialmean4 = new double[2*q];

			for (int m = 0; m < q; m++) {
				initialmean1[m] = points[0][m];
				// System.out.println(initialmean1[m]);
				initialmean2[m] = points[50][m];
				initialmean3[m] = points[100][m];
				initialmean4[m] = points[150][m];
			}
			for (int m = q; m < 2*q; m++) {
				initialmean1[m] = points[0][m];
				// System.out.println(initialmean1[m]);
				initialmean2[m] = points[50][m];
				initialmean3[m] = points[100][m];
				initialmean4[m] = points[150][m];
			}

			// variable declaration to store the old distance
			double distance1, distance2, distance3, distance4;
			// variable declaration to store new distance
			double newdistance1, newdistance2, newdistance3, newdistance4;
			// Storing the new center and corresponding variable declaration
			double[] newcenter1 = new double[2*q];
			double[] newcenter2 = new double[2*q];
			double[] newcenter3 = new double[2*q];
			double[] newcenter4 = new double[2*q];
			for (int m = 0; m < 2*q; m++) {
				newcenter1[m] = initialmean1[m];
				newcenter2[m] = initialmean2[m];
				newcenter3[m] = initialmean3[m];
				newcenter4[m] = initialmean4[m];
			}

			// Arraylist to store the cluster
			ArrayList<Point> cluster1 = new ArrayList<Point>();
			ArrayList<Point> cluster2 = new ArrayList<Point>();
			ArrayList<Point> cluster3 = new ArrayList<Point>();
			ArrayList<Point> cluster4 = new ArrayList<Point>();
			//
			double[] value1 = new double[2*q];
			double[] value2 = new double[2*q];
			double[] value3 = new double[2*q];
			double[] value4 = new double[2*q];



			// Kmeans algorithm
			for (int integrator = 0; integrator < 10; integrator++) {
				for (int t = 0; t < numberOfRows/(2*q); t++) {
					double[] pointvalue = new double[2*q];
					// create variables to represent the distancecalculator
					for (int m = 0; m < 2*q; m++) {
						pointvalue[m] = points[t][m];

						// System.out.print("  "+pointvalue[m]);
					}
					Distancecalculator distancecalculator1 = new Distancecalculator(
							pointvalue, newcenter1);
					Distancecalculator distancecalculator2 = new Distancecalculator(
							pointvalue, newcenter2);
					Distancecalculator distancecalculator3 = new Distancecalculator(
							pointvalue, newcenter3);
					Distancecalculator distancecalculator4 = new Distancecalculator(
							pointvalue, newcenter4);
					double distanceone = distancecalculator1
							.euclideanDistance();
					double distancetwo = distancecalculator2
							.euclideanDistance();
					double distancethree = distancecalculator3
							.euclideanDistance();
					double distancefour = distancecalculator4
							.euclideanDistance();

					if (distanceone <= distancetwo
							&& distanceone <= distancethree
							&& distanceone <= distancefour) {
						cluster1.add(new Point(pointvalue));
					} else if (distancetwo <= distanceone
							&& distancetwo <= distancethree
							&& distancetwo <= distancefour) {
						cluster2.add(new Point(pointvalue));
					} else if (distancethree <= distanceone
							&& distancethree <= distancetwo
							&& distancethree <= distancefour) {
						cluster3.add(new Point(pointvalue));
					} else if (distancefour <= distanceone
							&& distancefour <= distancetwo
							&& distancefour <= distancethree) {
						cluster4.add(new Point(pointvalue));
					}
				}

				for (int x = 0; x < 2*q; x++) {
					for (int n = 0; n < cluster1.size(); n++) {
						value1[x] += cluster1.get(n).getvalue(x);
					}

					value1[x] = value1[x] / cluster1.size();
					newcenter1[x] = value1[x];
					value1[x] = 0;
				}
				for (int x = 0; x < 2*q; x++) {
					for (int n = 0; n < cluster2.size(); n++) {
						value2[x] += cluster2.get(n).getvalue(x);
					}
					value2[x] = value2[x] / cluster2.size();
					newcenter2[x] = value2[x];
					value2[x] = 0;
				}
				for (int x = 0; x < 2*q; x++) {
					for (int n = 0; n < cluster3.size(); n++) {
						value3[x] += cluster3.get(n).getvalue(x);
					}
					value3[x] = value3[x] / cluster3.size();
					newcenter3[x] = value3[x];
					value3[x] = 0;
				}
				for (int x = 0; x < 2*q; x++) {
					for (int n = 0; n < cluster4.size(); n++) {
						value4[x] += cluster4.get(n).getvalue(x);
					}
					value4[x] = value4[x] / cluster4.size();
					newcenter4[x] = value4[x];
					value4[x] = 0;
				}

				if (integrator < q) {
					cluster1.clear();
					cluster2.clear();
					cluster3.clear();
					cluster4.clear();

				}

			}

			// KNN*************************************************************************************

			// inserting the values in a new matrix with cluster number
			double[][] pointslearning = new double[numberOfRows/(2*q)][2*q+1];

			for (int x = 0; x < cluster1.size(); x++) {
				for (int y = 0; y < 2*q; y++) {
					pointslearning[x][y] = cluster1.get(x).getvalue(y);
				}
				pointslearning[x][2*q] = 1;
			}
			;

			for (int x = 0; x < cluster2.size(); x++) {
				for (int y = 0; y < 2*q; y++) {
					pointslearning[cluster1.size() + x][y] = cluster2.get(x)
							.getvalue(y);
				}
				pointslearning[cluster1.size() + x][2*q] = 2;
			}
			;

			for (int x = 0; x < cluster3.size(); x++) {
				for (int y = 0; y < 2*q; y++) {
					pointslearning[cluster1.size() + cluster2.size() + x][y] = cluster3
							.get(x).getvalue(y);
				}
				pointslearning[cluster1.size() + cluster2.size() + x][2*q] = 3;
			}
			;
			for (int x = 0; x < cluster4.size(); x++) {
				for (int y = 0; y < 2*q; y++) {
					pointslearning[cluster1.size() + cluster2.size()
							+ cluster3.size() + x][y] = cluster4.get(x)
							.getvalue(y);
				}
				pointslearning[cluster1.size() + cluster2.size()
						+ cluster3.size() + x][2*q] = 4;
			}
			;

			// for(int x=0;x<200;x++){
			// for(int y=0;y<19;y++){
			// System.out.print(pointslearning[x][y]+"   ");
			// }
			// System.out.println("");
			// };

			// storing pointslearning matrix data into arraylist
			ArrayList<Point> clusterone = new ArrayList<Point>();
			ArrayList<Point> clustertwo = new ArrayList<Point>();
			ArrayList<Point> clusterthree = new ArrayList<Point>();
			ArrayList<Point> clusterfour = new ArrayList<Point>();

			for (int t = 0; t < cluster1.size(); t++) {
				double[] pointvalue = new double[2*q+1];
				// create variables to represent the distancecalculator
				for (int m = 0; m < 2*q+1; m++) {
					pointvalue[m] = pointslearning[t][m];
				}

				clusterone.add(new Point(pointvalue));

			}

			for (int t = 0; t < cluster2.size(); t++) {
				double[] pointvalue = new double[2*q+1];
				// create variables to represent the distancecalculator
				for (int m = 0; m < 2*q+1; m++) {
					pointvalue[m] = pointslearning[cluster1.size() + t][m];
				}

				clustertwo.add(new Point(pointvalue));

			}

			for (int t = 0; t < cluster3.size(); t++) {
				double[] pointvalue = new double[2*q+1];
				// create variables to represent the distancecalculator
				for (int m = 0; m < 2*q+1; m++) {
					pointvalue[m] = pointslearning[cluster1.size()
							+ cluster2.size() + t][m];
				}

				clusterthree.add(new Point(pointvalue));

			}

			for (int t = 0; t < cluster4.size(); t++) {
				double[] pointvalue = new double[19];
				// create variables to represent the distancecalculator
				for (int m = 0; m < 2*q+1; m++) {
					pointvalue[m] = pointslearning[cluster1.size()
							+ cluster2.size() + cluster3.size() + t][m];
				}

				clusterfour.add(new Point(pointvalue));

			}

			// for(int n=0;n<clusterone.size();n++){
			// for(int x=0;x<19;x++){
			// System.out.print(clusterone.get(n).getvalue(x)+"   ");
			// }
			// System.out.println("");
			// }

			// calculating the distance between test set and learning set
			// build a matrix to store the distance between test points and
			// cluster set, 20*200
			double[] pointvaluetest = new double[2*q];
			double[][] distance = new double[numberOfRowstest/(2*q)][numberOfRows/(2*q)];
			for (int x = 0; x < numberOfRowstest/(2*q); x++) {
				for (int t = 0; t < cluster1.size(); t++) {
					for (int y = 0; y < 2*q; y++) {
						pointvaluetest[y] = pointstest[x][y];
					}
					Distancecalculator distancecalculator = new Distancecalculator(
							pointvaluetest, clusterone.get(t).getset());
					distance[x][t] = distancecalculator.euclideanDistance();
				}
				for (int t = 0; t < cluster2.size(); t++) {
					for (int y = 0; y < 2*q; y++) {
						pointvaluetest[y] = pointstest[x][y];
					}
					Distancecalculator distancecalculator = new Distancecalculator(
							pointvaluetest, clustertwo.get(t).getset());
					distance[x][cluster1.size() + t] = distancecalculator
							.euclideanDistance();
				}
				for (int t = 0; t < cluster3.size(); t++) {
					for (int y = 0; y < 2*q; y++) {
						pointvaluetest[y] = pointstest[x][y];
					}
					Distancecalculator distancecalculator = new Distancecalculator(
							pointvaluetest, clusterthree.get(t).getset());
					distance[x][cluster1.size() + cluster2.size() + t] = distancecalculator
							.euclideanDistance();
					// System.out.println(t);
				}
				for (int t = 0; t < cluster4.size(); t++) {
					for (int y = 0; y < 2*q; y++) {
						pointvaluetest[y] = pointstest[x][y];
					}
					Distancecalculator distancecalculator = new Distancecalculator(
							pointvaluetest, clusterfour.get(t).getset());
					distance[x][cluster1.size() + cluster2.size()
							+ cluster3.size() + t] = distancecalculator
							.euclideanDistance();
					// System.out.println(t);
					// System.out.println(distance[cluster1.size()+cluster2.size()+cluster3.size()+t]);
				}
			}
			;

			// for(int x=0;x<20;x++){
			// for(int y=0;y<200;y++){
			// System.out.print(distance[x][y]+"   ");
			// }
			// System.out.println(" ");
			// }

			// the number of the closest point
			System.out.println("cluster 1 size: " + cluster1.size());
			System.out.println("cluster 2 size: " + cluster2.size());
			System.out.println("cluster 3 size: " + cluster3.size());
			System.out.println("cluster 4 size: " + cluster4.size());

			Object messagesize = "***************** Cluster 1 size: "
					+ cluster1.size() + "***************** \n"
					+ "***************** Cluster 2 size: " + cluster2.size()
					+ "***************** \n \n"
					+ "***************** Cluster 3 size: " + cluster3.size()
					+ "***************** \n \n"
					+ "***************** Cluster 4 size: " + cluster4.size()
					+ "***************** \n \n";
			JOptionPane.showMessageDialog(null, messagesize);

			// create table Clusteronedata with corresponding attributes
			sql = "DROP TABLE IF EXISTS Clusteronedata";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Clusteronedata(CLUSTER_NUMBER double, V1 double, ANG1 double, V2 double, ANG2 double, V3 double, ANG3 double, V4 double, ANG4 double, V5 double, ANG5 double, V6 double, ANG6 double, V7 double, ANG7 double, V8 double, ANG8 double, V9 double, ANG9 double)"; // create
																																																																												// table
																																																																												// Breaker
																																																																												// with
																																																																												// corresponding
																																																																												// attributes
			stmt.executeUpdate(sql);
			for (int n = 0; n < clusterone.size(); n++) {
				sql = "INSERT INTO Clusteronedata " + "VALUES ( '"
						+ clusterone.get(n).getvalue(18) + "', '"
						+ clusterone.get(n).getvalue(0) + "', '"
						+ clusterone.get(n).getvalue(9) + "', '"
						+ clusterone.get(n).getvalue(1) + "', '"
						+ clusterone.get(n).getvalue(10) + "', '"
						+ clusterone.get(n).getvalue(2) + "', '"
						+ clusterone.get(n).getvalue(11) + "', '"
						+ clusterone.get(n).getvalue(3) + "', '"
						+ clusterone.get(n).getvalue(12) + "', '"
						+ clusterone.get(n).getvalue(4) + "', " + "'"
						+ clusterone.get(n).getvalue(13) + "', '"
						+ clusterone.get(n).getvalue(5) + "', '"
						+ clusterone.get(n).getvalue(14) + "', '"
						+ clusterone.get(n).getvalue(6) + "', '"
						+ clusterone.get(n).getvalue(15) + "', '"
						+ clusterone.get(n).getvalue(7) + "', '"
						+ clusterone.get(n).getvalue(16) + "', '"
						+ clusterone.get(n).getvalue(8) + "', '"
						+ clusterone.get(n).getvalue(17) + "')";
				stmt.executeUpdate(sql);
			}
			ResultSet rsc1 = stmt.executeQuery("SELECT * FROM Clusteronedata");
			ResultSetMetaData rsmdc1 = rsc1.getMetaData();
			int numberOfColumnsc1 = rsmdc1.getColumnCount();
			rsc1.last();
			int numberOfRowsc1 = rsc1.getRow();
			int jc1 = 0;
			Clusteroneobj = new Object[numberOfRowsc1][numberOfColumnsc1 + 1];
			// Pick all objects until there is no next object
			ResultSet rsc11 = stmt.executeQuery("SELECT * FROM Clusteronedata");
			while (rsc11.next()) {
				for (int k1 = 1; k1 <= numberOfColumnsc1; k1++) {
					Clusteroneobj[jc1][k1] = rsc11.getObject(k1);
				}
				jc1++;
			}
			Object[] columnsc = { "", "CLUSTER_NUMBER", "V1", "ANG1", "V2",
					"ANG2", "V3", "ANG3", "V4", "ANG4", "V5", "ANG5", "V6",
					"ANG6", "V7", "ANG7", "V8", "ANG8", "V9", "ANG9" };

			sql = "DROP TABLE IF EXISTS Clustertwodata";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Clustertwodata(CLUSTER_NUMBER double, V1 double, ANG1 double, V2 double, ANG2 double, V3 double, ANG3 double, V4 double, ANG4 double, V5 double, ANG5 double, V6 double, ANG6 double, V7 double, ANG7 double, V8 double, ANG8 double, V9 double, ANG9 double)"; // create
																																																																												// table
																																																																												// Breaker
																																																																												// with
																																																																												// corresponding
																																																																												// attributes
			stmt.executeUpdate(sql);
			for (int n = 0; n < clustertwo.size(); n++) {
				sql = "INSERT INTO Clustertwodata " + "VALUES ( '"
						+ clustertwo.get(n).getvalue(18) + "', '"
						+ clustertwo.get(n).getvalue(0) + "', '"
						+ clustertwo.get(n).getvalue(9) + "', '"
						+ clustertwo.get(n).getvalue(1) + "', '"
						+ clustertwo.get(n).getvalue(10) + "', '"
						+ clustertwo.get(n).getvalue(2) + "', '"
						+ clustertwo.get(n).getvalue(11) + "', '"
						+ clustertwo.get(n).getvalue(3) + "', '"
						+ clustertwo.get(n).getvalue(12) + "', '"
						+ clustertwo.get(n).getvalue(4) + "', " + "'"
						+ clustertwo.get(n).getvalue(13) + "', '"
						+ clustertwo.get(n).getvalue(5) + "', '"
						+ clustertwo.get(n).getvalue(14) + "', '"
						+ clustertwo.get(n).getvalue(6) + "', '"
						+ clustertwo.get(n).getvalue(15) + "', '"
						+ clustertwo.get(n).getvalue(7) + "', '"
						+ clustertwo.get(n).getvalue(16) + "', '"
						+ clustertwo.get(n).getvalue(8) + "', '"
						+ clustertwo.get(n).getvalue(17) + "')";
				stmt.executeUpdate(sql);
			}
			ResultSet rsc2 = stmt.executeQuery("SELECT * FROM Clustertwodata");
			ResultSetMetaData rsmdc2 = rsc2.getMetaData();
			int numberOfColumnsc2 = rsmdc2.getColumnCount();
			rsc2.last();
			int numberOfRowsc2 = rsc2.getRow();
			int jc2 = 0;
			Clustertwoobj = new Object[numberOfRowsc2][numberOfColumnsc2 + 1];
			// Pick all objects until there is no next object
			ResultSet rsc21 = stmt.executeQuery("SELECT * FROM Clustertwodata");
			while (rsc21.next()) {
				for (int k2 = 1; k2 <= numberOfColumnsc2; k2++) {
					Clustertwoobj[jc2][k2] = rsc21.getObject(k2);
				}
				jc2++;
			}

			sql = "DROP TABLE IF EXISTS Clusterthreedata";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Clusterthreedata(CLUSTER_NUMBER double, V1 double, ANG1 double, V2 double, ANG2 double, V3 double, ANG3 double, V4 double, ANG4 double, V5 double, ANG5 double, V6 double, ANG6 double, V7 double, ANG7 double, V8 double, ANG8 double, V9 double, ANG9 double)"; // create																																																																						// attributes
			stmt.executeUpdate(sql);
			for (int n = 0; n < clusterthree.size(); n++) {
				sql = "INSERT INTO Clusterthreedata " + "VALUES ( '"
						+ clusterthree.get(n).getvalue(18) + "', '"
						+ clusterthree.get(n).getvalue(0) + "', '"
						+ clusterthree.get(n).getvalue(9) + "', '"
						+ clusterthree.get(n).getvalue(1) + "', '"
						+ clusterthree.get(n).getvalue(10) + "', '"
						+ clusterthree.get(n).getvalue(2) + "', '"
						+ clusterthree.get(n).getvalue(11) + "', '"
						+ clusterthree.get(n).getvalue(3) + "', '"
						+ clusterthree.get(n).getvalue(12) + "', '"
						+ clusterthree.get(n).getvalue(4) + "', " + "'"
						+ clusterthree.get(n).getvalue(13) + "', '"
						+ clusterthree.get(n).getvalue(5) + "', '"
						+ clusterthree.get(n).getvalue(14) + "', '"
						+ clusterthree.get(n).getvalue(6) + "', '"
						+ clusterthree.get(n).getvalue(15) + "', '"
						+ clusterthree.get(n).getvalue(7) + "', '"
						+ clusterthree.get(n).getvalue(16) + "', '"
						+ clusterthree.get(n).getvalue(8) + "', '"
						+ clusterthree.get(n).getvalue(17) + "')";
				stmt.executeUpdate(sql);
			}
			ResultSet rsc3 = stmt
					.executeQuery("SELECT * FROM Clusterthreedata");
			ResultSetMetaData rsmdc3 = rsc3.getMetaData();
			int numberOfColumnsc3 = rsmdc3.getColumnCount();
			rsc3.last();
			int numberOfRowsc3 = rsc3.getRow();
			int jc3 = 0;
			Clusterthreeobj = new Object[numberOfRowsc3][numberOfColumnsc3 + 1];
			// Pick all objects until there is no next object
			ResultSet rsc31 = stmt
					.executeQuery("SELECT * FROM Clusterthreedata");
			while (rsc31.next()) {
				for (int k3 = 1; k3 <= numberOfColumnsc3; k3++) {
					Clusterthreeobj[jc3][k3] = rsc31.getObject(k3);
				}
				jc3++;
			}

			sql = "DROP TABLE IF EXISTS Clusterfourdata";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Clusterfourdata(CLUSTER_NUMBER double, V1 double, ANG1 double, V2 double, ANG2 double, V3 double, ANG3 double, V4 double, ANG4 double, V5 double, ANG5 double, V6 double, ANG6 double, V7 double, ANG7 double, V8 double, ANG8 double, V9 double, ANG9 double)"; // create
																																																																												// table
																																																																												// Breaker
																																																																												// with
																																																																												// corresponding
																																																																												// attributes
			stmt.executeUpdate(sql);
			for (int n = 0; n < clusterfour.size(); n++) {
				sql = "INSERT INTO Clusterfourdata " + "VALUES ( '"
						+ clusterfour.get(n).getvalue(18) + "', '"
						+ clusterfour.get(n).getvalue(0) + "', '"
						+ clusterfour.get(n).getvalue(9) + "', '"
						+ clusterfour.get(n).getvalue(1) + "', '"
						+ clusterfour.get(n).getvalue(10) + "', '"
						+ clusterfour.get(n).getvalue(2) + "', '"
						+ clusterfour.get(n).getvalue(11) + "', '"
						+ clusterfour.get(n).getvalue(3) + "', '"
						+ clusterfour.get(n).getvalue(12) + "', '"
						+ clusterfour.get(n).getvalue(4) + "', " + "'"
						+ clusterfour.get(n).getvalue(13) + "', '"
						+ clusterfour.get(n).getvalue(5) + "', '"
						+ clusterfour.get(n).getvalue(14) + "', '"
						+ clusterfour.get(n).getvalue(6) + "', '"
						+ clusterfour.get(n).getvalue(15) + "', '"
						+ clusterfour.get(n).getvalue(7) + "', '"
						+ clusterfour.get(n).getvalue(16) + "', '"
						+ clusterfour.get(n).getvalue(8) + "', '"
						+ clusterfour.get(n).getvalue(17) + "')";
				stmt.executeUpdate(sql);
			}
			ResultSet rsc4 = stmt.executeQuery("SELECT * FROM Clusterfourdata");
			ResultSetMetaData rsmdc4 = rsc4.getMetaData();
			int numberOfColumnsc4 = rsmdc4.getColumnCount();
			rsc4.last();
			int numberOfRowsc4 = rsc4.getRow();
			int jc4 = 0;
			Clusterfourobj = new Object[numberOfRowsc4][numberOfColumnsc4 + 1];
			// Pick all objects until there is no next object
			ResultSet rsc41 = stmt
					.executeQuery("SELECT * FROM Clusterfourdata");
			while (rsc41.next()) {
				for (int k4 = 1; k4 <= numberOfColumnsc4; k4++) {
					Clusterfourobj[jc4][k4] = rsc41.getObject(k4);
				}
				jc4++;
			}

			frame = new JFrame();
			frame.getContentPane().setBackground(Color.WHITE);
			frame.setBounds(550, 300, 900, 648);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);

			JButton btnNewButton = new JButton("Show learning set");
			btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			btnNewButton.setBackground(SystemColor.activeCaption);
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tables table = new tables(Alldata, columns);
					try {
						rs.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			btnNewButton.setBounds(79, 195, 240, 29);
			frame.getContentPane().add(btnNewButton);

			JLabel lblEhComputerApplications = new JLabel(
					"EH2745 COMPUTER APPLICATIONS IN POWER SYSTEM");
			lblEhComputerApplications.setFont(new Font("Times New Roman",
					Font.PLAIN, 33));
			lblEhComputerApplications.setBounds(15, 35, 863, 71);
			frame.getContentPane().add(lblEhComputerApplications);

			JButton btnShowTestSet = new JButton("Show Test Set");
			btnShowTestSet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tables table = new tables(Testdata, columnstest);
				}
			});
			btnShowTestSet.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			btnShowTestSet.setBackground(SystemColor.activeCaption);
			btnShowTestSet.setBounds(516, 195, 240, 29);
			frame.getContentPane().add(btnShowTestSet);

			JButton btnNewButton_1 = new JButton("Cluster 1");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tables table = new tables(Clusteroneobj, columnsc);

				}
			});
			btnNewButton_1.setBounds(15, 286, 210, 67);
			frame.getContentPane().add(btnNewButton_1);

			JButton btnCluster = new JButton("Cluster 2");
			btnCluster.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tables table = new tables(Clustertwoobj, columnsc);
				}
			});
			btnCluster.setBounds(15, 385, 210, 67);
			frame.getContentPane().add(btnCluster);

			JButton btnNewButton_2 = new JButton("Cluster 3");
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tables table = new tables(Clusterthreeobj, columnsc);
				}
			});
			btnNewButton_2.setBounds(278, 385, 175, 67);
			frame.getContentPane().add(btnNewButton_2);

			JButton btnCluster_1 = new JButton("Cluster 4");
			btnCluster_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tables table = new tables(Clusterfourobj, columnsc);
				}
			});
			btnCluster_1.setBounds(278, 286, 175, 67);
			frame.getContentPane().add(btnCluster_1);

			JButton btnPerformKnnAlgorithm = new JButton(
					"Perform kNN Algorithm to the Test Set");
			btnPerformKnnAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					// Entering of k VALUE NO.
					JLabel jKvalue = new JLabel(
							"Enter the k-Value for the kNN algorithm here:");
					JTextField KValue = new JTextField();
					Object[] data = { jKvalue, KValue };
					int resultpopup = JOptionPane.showConfirmDialog(null, data,
							"kNN-Algorithm", JOptionPane.OK_CANCEL_OPTION);

					if (resultpopup == JOptionPane.OK_OPTION) {
						String KValuedata = KValue.getText();
						String temp = KValuedata;
						int k = Integer.parseInt(temp);
						Object[][] outputprint = new Object[numberOfRowstest/(2*q)][1];
						// for cluster1
						for (int t = 0; t < numberOfRowstest/(2*q); t++) {
							// the number of the showing time
							double c1 = 0;
							double c2 = 0;
							double c3 = 0;
							double c4 = 0;
							double[] pointvalue = new double[numberOfRows/(2*q)];

							for (int x = 0; x < numberOfRows/(2*q); x++) {
								pointvalue[x] = distance[t][x];
							}
							for (int x = 0; x < clusterone.size(); x++) {
								distancecompare distancecompare1 = new distancecompare(
										distance[t][x], pointvalue, k);
								c1 += distancecompare1.comparator();
							}
							for (int x = 0; x < clustertwo.size(); x++) {
								distancecompare distancecompare2 = new distancecompare(
										distance[t][clusterone.size() + x],
										pointvalue, k);
								c2 += distancecompare2.comparator();
							}
							for (int x = 0; x < clusterthree.size(); x++) {
								distancecompare distancecompare3 = new distancecompare(
										distance[t][clusterone.size()
												+ clustertwo.size() + x],
										pointvalue, k);
								c3 += distancecompare3.comparator();
							}
							for (int x = 0; x < clusterfour.size(); x++) {
								distancecompare distancecompare4 = new distancecompare(
										distance[t][clusterone.size()
												+ clustertwo.size()
												+ clusterthree.size() + x],
										pointvalue, k);
								c4 += distancecompare4.comparator();
							}
							if (c1 >= c2 && c1 >= c3 && c1 >= c4) {
								outputprint[t][0] = "The time stamp "
										+ (t + 1)
										+ " data of test set belongs to Cluster 1 ";
								System.out.println(1);
							} else if (c2 >= c1 && c2 >= c3 && c2 >= c4) {
								outputprint[t][0] = "The time stamp "
										+ (t + 1)
										+ " data of test set belongs to Cluster 2 ";
								System.out.println(2);
							} else if (c3 >= c1 && c3 >= c2 && c3 >= c4) {
								outputprint[t][0] = "The time stamp "
										+ (t + 1)
										+ " data of test set belongs to Cluster 3 ";
								System.out.println(3);
							} else if (c4 >= c1 && c4 >= c2 && c4 >= c1) {
								outputprint[t][0] = "The time stamp "
										+ (t + 1)
										+ " data of test set belongs to Cluster 4 ";
								System.out.println(4);
							}
						}
						// Object messagekval = "Answer"+"\n" +k ;
						// System.out.println(outputprint[1][0]);
						Object messagekval = "Test Data Classification in Clusters "
								+ "\n" + "\n" + "\n"
								+ outputprint[0][0]
								+ "\n"
								+ outputprint[1][0]
								+ "\n"
								+ outputprint[2][0]
								+ "\n"
								+ outputprint[3][0]
								+ "\n"
								+ outputprint[4][0]
								+ "\n"
								+ outputprint[5][0]
								+ "\n"
								+ outputprint[6][0]
								+ "\n"
								+ outputprint[7][0]
								+ "\n"
								+ outputprint[8][0]
								+ "\n"
								+ outputprint[9][0]
								+ "\n"
								+ outputprint[10][0]
								+ "\n"
								+ outputprint[11][0]
								+ "\n"
								+ outputprint[12][0]
								+ "\n"
								+ outputprint[13][0]
								+ "\n"
								+ outputprint[14][0]
								+ "\n"
								+ outputprint[15][0]
								+ "\n"
								+ outputprint[16][0]
								+ "\n"
								+ outputprint[17][0]
								+ "\n"
								+ outputprint[18][0]
								+ "\n"
								+ outputprint[19][0];

						JOptionPane.showMessageDialog(null, messagekval);
						Object[] col = { " ", };
						tables table = new tables(outputprint, col);
					}
				}

			});
			btnPerformKnnAlgorithm.setBounds(494, 286, 369, 166);
			frame.getContentPane().add(btnPerformKnnAlgorithm);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}