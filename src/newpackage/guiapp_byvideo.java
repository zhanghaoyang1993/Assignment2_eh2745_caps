package newpackage;

import java.awt.EventQueue;

import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JFormattedTextField;

import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.swing.JTable;
import javax.swing.JScrollPane;

public class guiapp_byvideo {

	private JFrame frame;
	private final JButton btnSynchronousMachine = new JButton("Synchronous Machine");

	String elementname;
	Double S = 1000.0; 

	public static String sql, query ;
	public static int i ;
	public static int count =1;
	public static int temp=0;
	public static Connection conn = null;
	public static Statement stmt = null;
	
	
	/**
	 * Launch the application.
	 */
	// JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		static final String DB_URL = "jdbc:mysql://localhost/";
		static final String DISABLE_SSL = "?useSSL=false";
		// Database credentials
		static final String USER = "root";
		static final String PASS = "root"; // insert the password to SQL server
	
		public static void extractNode (Node node,String elementname) throws NullPointerException{

			Element element = (Element) node;
			// Collect info about cim Class
			if(elementname == "BaseVoltage"){
				String rdfID = element.getAttribute("rdf:ID");
				String nominalvalue = element.getElementsByTagName("cim:"+elementname+".nominalVoltage").item(0).getTextContent();		

				System.out.println("Base Voltage"+"\n"+"rdfID: " + rdfID+"\n"+"nominal value" + nominalvalue +"\n" );

				sql = "INSERT INTO BaseVoltage " + "VALUES ('"+rdfID+"','"+nominalvalue+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(elementname == "Substation"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String region = element.getElementsByTagName("cim:Substation.Region").item(0).getAttributes().item(0).toString();

				System.out.println("Substation"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"region: "+region+"\n" );
				sql = "INSERT INTO Substation " + "VALUES ('"+rdfID+"','"+name+"','"+region+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}



			if(elementname == "Voltage Level"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String substation = element.getElementsByTagName("cim:VoltageLevel.Substation").item(0).getAttributes().item(0).toString();
				String basevoltage = element.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0).getAttributes().item(0).toString();

				System.out.println("Voltage Level"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"substation: "+substation+"\n"+"basevoltage: "+basevoltage+"\n" );
				sql = "INSERT INTO Voltage_Level " + "VALUES ('"+rdfID+"','"+ name +"','"+substation+"','"+basevoltage+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(elementname == "Generating Unit"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String maxP = element.getElementsByTagName("cim:GeneratingUnit.maxOperatingP").item(0).getTextContent();
				String minP = element.getElementsByTagName("cim:GeneratingUnit.minOperatingP").item(0).getTextContent();
				String equipmentContainer = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();

				System.out.println("Generating Unit"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"maxP: "+maxP+"\n"+"minP: "+minP+"\n"+"equipmentContainer: "+equipmentContainer+"\n" );
				sql = "INSERT INTO Generating_Unit " + "VALUES ('"+rdfID+"','"+ name +"','"+maxP+"','"+minP+"', '"+equipmentContainer+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "Synchronous Machine"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String ratedS = element.getElementsByTagName("cim:RotatingMachine.ratedS").item(0).getTextContent();
				String genUnit = element.getElementsByTagName("cim:RotatingMachine.GeneratingUnit").item(0).getAttributes().item(0).toString();
				String regControl = element.getElementsByTagName("cim:RegulatingCondEq.RegulatingControl").item(0).getAttributes().item(0).toString();
				String equipmentContainer = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();


				System.out.println("Synchronous Machine"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"ratedS: "+ratedS+"\n"+"genUnit: "+genUnit+"\n"+"regControl: "+regControl+"\n"+"equipmentContainer: "+equipmentContainer);
				sql = "INSERT INTO Synchronous_Machine " + "VALUES ('"+count++ +"','"+rdfID+"','"+ name +"','"+ratedS+"', '"+0+"','"+0+"','"+genUnit+"','"+regControl+"', '"+equipmentContainer+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "Regulating Control"){
				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();

				System.out.println("Regulating Control"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name);
				sql = "INSERT INTO Regulating_Control " + "VALUES ('"+count++ +"','"+rdfID+"','"+ name +"','"+0+"')";
				System.out.println("count is"+count);
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(elementname == "Power Transformer"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String equipmentContainer = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();

				System.out.println("Power Transformer"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"equipmentContainer: "+ equipmentContainer+"\n" );
				sql = "INSERT INTO Power_Transformer " + "VALUES ('"+rdfID+"','"+ name +"','"+equipmentContainer+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "Energy Consumer"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String equipmentContainer = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();

				System.out.println("Energy Consumer"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"equipmentContainer: "+ equipmentContainer);
				sql = "INSERT INTO Energy_Consumer " + "VALUES ('"+count++ +"','"+rdfID+"','"+ name +"', '"+ 0 +"', '"+ 0 +"','"+equipmentContainer+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "PowerTransformerEnd"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String TransformerR = element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent();
				String TransformerX = element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent();
				String Transformer = element.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0).getAttributes().item(0).toString();
				String baseVoltage = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes().item(0).toString();

				System.out.println("PowerTransformerEnd"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"Transformer.r: "+ TransformerR+"\n"+"Transformer.x: "+ TransformerX+"\n"+"Transformer_rdf:ID: "+ Transformer+"\n"+"baseVoltage_ rdf:ID: "+ baseVoltage+"\n" );
				sql = "INSERT INTO Power_Transformer_End " + "VALUES ('"+rdfID+"','"+ name +"', '"+ TransformerR +"', '"+ TransformerX +"','"+Transformer+"', '"+baseVoltage+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "Breaker"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				String equipmentContainer = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();

				System.out.println("Breaker"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name+"\n"+"equipmentContainer: "+ equipmentContainer);
				sql = "INSERT INTO Breaker " + "VALUES ('"+count++ +"','"+rdfID+"','"+ name +"', '"+ "check" +"','"+equipmentContainer+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "RatioTapChanger"){

				String rdfID = element.getAttribute("rdf:ID");
				String name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();

				System.out.println("RatioTapChanger"+"\n"+"rdfID: "+ rdfID+"\n"+"name: "+name);
				sql = "INSERT INTO Ratio_Tap_Changer " + "VALUES ('"+count++ +"','"+rdfID+"','"+ name +"','"+0+"')";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


		public static void extractSSH (Node node,String elementname) throws NullPointerException{
			Element element = (Element) node;
			// Collect info about cim Class
			if(elementname == "Synchronous Machine"){
				String P = element.getElementsByTagName("cim:RotatingMachine.p").item(0).getTextContent();
				String Q = element.getElementsByTagName("cim:RotatingMachine.q").item(0).getTextContent();		
				temp++;
				System.out.println("P: "+P+"\n"+"Q: "+Q + "\n");
				sql = "UPDATE Synchronous_Machine SET P = '"+P+"', Q = '"+Q+"' WHERE ID='"+temp+"'" ;
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			if(elementname == "Regulating Control"){
				String targetValue = element.getElementsByTagName("cim:RegulatingControl.targetValue").item(0).getTextContent();
				temp++;
				System.out.println("targetValue: "+targetValue+"\n");
				sql = "UPDATE Regulating_Control SET Target_Value = '"+targetValue+"' WHERE ID='"+temp+"' " ;

				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(elementname == "Energy Consumer"){
				String P = element.getElementsByTagName("cim:EnergyConsumer.p").item(0).getTextContent();
				String Q = element.getElementsByTagName("cim:EnergyConsumer.q").item(0).getTextContent();		
				temp++;
				System.out.println("P: "+P+"\n"+"Q: "+Q+"\n" );
				sql = "UPDATE Energy_Consumer SET P = '"+P+"', Q = '"+Q+"' WHERE ID='"+temp+"' " ;
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "Breaker"){
				String state = element.getElementsByTagName("cim:Switch.open").item(0).getTextContent();
				temp++;
				System.out.println("Switch state whether open: "+state+"\n");
				sql = "UPDATE Breaker SET State = '"+state+"' WHERE ID='"+temp+"' " ;
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(elementname == "RatioTapChanger"){
				String step = element.getElementsByTagName("cim:TapChanger.step").item(0).getTextContent();
				temp++ ;
				System.out.println("step: "+step+"\n");
				sql = "UPDATE Ratio_Tap_Changer SET Step = '"+step+"' WHERE ID='"+temp+"' " ;
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}




	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					guiapp_byvideo window = new guiapp_byvideo();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				}	
		});
	
		 
	
	}
	
	/**
	 * Create the application.
	 */
	public guiapp_byvideo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	
	public void initialize() {
		
		
		Connection conn = null;
		Statement st = null;
		try{


			File EQFile = new File("D:\\Computer Applications in Power systems\\java assignment1\\Assignment_EQ_reduced.xml");
			File SSHFile = new File("D:\\Computer Applications in Power systems\\java assignment1\\Assignment_SSH_reduced.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document eq = dBuilder.parse(EQFile);
			Document ssh = dBuilder.parse(SSHFile);
			// normalize CIM XML file
			eq.getDocumentElement().normalize();
			ssh.getDocumentElement().normalize(); 
			String[] target = {"cim:BaseVoltage","cim:Substation","cim:VoltageLevel","cim:GeneratingUnit","cim:SynchronousMachine","cim:RegulatingControl","cim:PowerTransformer","cim:EnergyConsumer","cim:PowerTransformerEnd","cim:Breaker","cim:RatioTapChanger"};

			//eq file nodes lists
			NodeList basevoltList = eq.getElementsByTagName("cim:BaseVoltage");
			NodeList subList = eq.getElementsByTagName("cim:Substation");
			NodeList voltList = eq.getElementsByTagName("cim:VoltageLevel");
			NodeList genunitList = eq.getElementsByTagName("cim:GeneratingUnit");
			NodeList genList = eq.getElementsByTagName("cim:SynchronousMachine");
			NodeList regcon = eq.getElementsByTagName("cim:RegulatingControl");
			NodeList powtrans = eq.getElementsByTagName("cim:PowerTransformer");
			NodeList engcon = eq.getElementsByTagName("cim:EnergyConsumer");
			NodeList powtransend = eq.getElementsByTagName("cim:PowerTransformerEnd");
			NodeList breaker = eq.getElementsByTagName("cim:Breaker");
			NodeList ratchanger = eq.getElementsByTagName("cim:RatioTapChanger");
			
			//ssh file node lists
			NodeList genListssh = ssh.getElementsByTagName("cim:SynchronousMachine");
			NodeList regconssh = ssh.getElementsByTagName("cim:RegulatingControl");
			NodeList engconssh = ssh.getElementsByTagName("cim:EnergyConsumer");
			NodeList breakerssh = ssh.getElementsByTagName("cim:Breaker");
			NodeList ratchangerssh = ssh.getElementsByTagName("cim:RatioTapChanger");
			
			//ACLines
			NodeList aclines=eq.getElementsByTagName("cim:ACLineSegment");
			//shunt capacitor
			NodeList capacitor=eq.getElementsByTagName("cim:LinearShuntCompensator");

			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open a connection
			System.out.println("Connecting to SQL server...");
			conn = DriverManager.getConnection(DB_URL+DISABLE_SSL, USER, PASS);

			// execute a query to create database
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			String sql = "CREATE DATABASE IF NOT EXISTS PowerSystem"; // create database Students
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully..."+"\n");

			// Connect to the created database power system and create table breaker
			conn = DriverManager.getConnection(DB_URL + "PowerSystem"+DISABLE_SSL, USER, PASS);
			sql = "USE PowerSystem";
			stmt.executeUpdate(sql);
			sql = "SET foreign_key_checks = 0 ;";
			stmt.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS BaseVoltage";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS BaseVoltage(RDFID VARCHAR(50) NOT NULL, Nominal_value DOUBLE, PRIMARY KEY(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			sql = "DROP TABLE IF EXISTS Substation";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Substation(RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Region_rdfID VARCHAR(50), PRIMARY KEY(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;

			sql = "DROP TABLE IF EXISTS Voltage_Level";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Voltage_Level(RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Substation_rdfID VARCHAR(75), Base_Voltage_rdfID VARCHAR(75), PRIMARY KEY(RDFID),FOREIGN KEY(Substation_rdfID) REFERENCES Substation(RDFID),FOREIGN KEY(base_Voltage_rdfID) REFERENCES BaseVoltage(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;

			sql = "DROP TABLE IF EXISTS Generating_Unit";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Generating_Unit(RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), MAXP DOUBLE, MINP DOUBLE, Equipment_Container_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(Equipment_Container_rdfID) REFERENCES Substation(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;

			sql = "DROP TABLE IF EXISTS Synchronous_Machine";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Synchronous_Machine(ID INT, RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), RatedS DOUBLE, P DOUBLE, Q DOUBLE, genunit_rdfID VARCHAR(75), regcontrol_rdfID VARCHAR(75), Equipment_Container_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(genunit_rdfID) REFERENCES Generating_Unit(RDFID), FOREIGN KEY(regcontrol_rdfID) REFERENCES Regulating_Control(RDFID), FOREIGN KEY(Equipment_Container_rdfID) REFERENCES Voltage_Level(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;

			sql = "DROP TABLE IF EXISTS Regulating_Control";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Regulating_Control(ID INT, RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Target_Value DOUBLE, PRIMARY KEY(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			sql = "DROP TABLE IF EXISTS Power_Transformer";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Power_Transformer(RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Equipment_Container_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(Equipment_Container_rdfID) REFERENCES Substation(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			sql = "DROP TABLE IF EXISTS Energy_Consumer";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Energy_Consumer(ID INT, RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), P DOUBLE, Q DOUBLE, Equipment_Container_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(Equipment_Container_rdfID) REFERENCES Voltage_Level(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			sql = "DROP TABLE IF EXISTS Power_Transformer_End";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Power_Transformer_End(RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Transformer_r DOUBLE, Transformer_x DOUBLE, Transformer_rdfID VARCHAR(75), base_Voltage_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(Transformer_rdfID) REFERENCES Power_Transformer(RDFID),FOREIGN KEY(base_Voltage_rdfID) REFERENCES BaseVoltage(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			sql = "DROP TABLE IF EXISTS Breaker";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Breaker(ID INT, RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), State VARCHAR(30), Equipment_Container_rdfID VARCHAR(75), PRIMARY KEY(RDFID), FOREIGN KEY(Equipment_Container_rdfID) REFERENCES Voltage_Level(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;

			sql = "DROP TABLE IF EXISTS Ratio_Tap_Changer";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Ratio_Tap_Changer(ID INT, RDFID VARCHAR(50) NOT NULL, Name VARCHAR(20), Step DOUBLE, PRIMARY KEY(RDFID))"; // create table Breaker with corresponding attributes
			stmt.executeUpdate(sql) ;


			for (int i = 0; i < basevoltList.getLength(); i++) {
				extractNode (basevoltList.item(i),"BaseVoltage");
			}
			for (int i = 0; i < subList.getLength(); i++) {
				extractNode (subList.item(i),"Substation");
			}
			for (int i = 0; i < voltList.getLength(); i++) {
				extractNode (voltList.item(i),"Voltage Level");
			}
			for (int i = 0; i < genunitList.getLength(); i++) {
				extractNode (genunitList.item(i),"Generating Unit");
			}
			for (int i = 0; i < genList.getLength(); i++) {
				extractNode (genList.item(i),"Synchronous Machine");
				extractSSH (genListssh.item(i),"Synchronous Machine");
				Element synmachine =(Element) genList.item(i);
				String machineresource = synmachine.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).toString();
				for(int a =0; a < voltList.getLength(); a++ ){
					Element voltlevel = (Element) voltList.item(a);
					String rdfID = voltlevel.getAttribute("rdf:ID");
					if (machineresource == rdfID){
						Element voltLevel = (Element) voltList.item(a);
						String voltlevelrdf = voltLevel.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0).getAttributes().item(0).toString();
						for(int b =0; b < basevoltList.getLength(); b++ ){
							Element basevolt = (Element) basevoltList.item(b);
							String basevoltid = basevolt.getAttribute("rdfID");
							if ( voltlevelrdf == basevoltid){
								System.out.println(basevoltid);
							}
						}
					}
				}
			}


			for (int i = 0; i < regcon.getLength(); i++) {
				extractNode (regcon.item(i),"Regulating Control");
				extractSSH (regconssh.item(i),"Regulating Control");
			}
			for (int i = 0; i < powtrans.getLength(); i++) {
				extractNode (powtrans.item(i),"Power Transformer");
			}
			for (int i = 0; i < engcon.getLength(); i++) {
				extractNode (engcon.item(i),"Energy Consumer");
				extractSSH (engconssh.item(i),"Energy Consumer");
			}
			for (int i = 0; i < powtransend.getLength(); i++) {
				extractNode (powtransend.item(i),"PowerTransformerEnd");
			}
			for (int i = 0; i < breaker.getLength(); i++) {
				extractNode (breaker.item(i),"Breaker");
				extractSSH (breakerssh.item(i),"Breaker");
			}
			for (int i = 0; i < ratchanger.getLength(); i++) {
				extractNode (ratchanger.item(i),"RatioTapChanger");
				extractSSH (ratchangerssh.item(i),"RatioTapChanger");
			}
			

			//save b and g for transformer
			
			double[] basevolt=new double[4];

			for(int i=0; i<basevoltList.getLength(); i++){
				Element element = (Element) basevoltList.item(i);
				String basevoltages = element.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent();
				basevolt[i]=Double.parseDouble(basevoltages);
				
			    
			}
			
			double[] transr=new double[6];

			for(int i=0; i<powtransend.getLength(); i++){
				Element element = (Element) powtransend.item(i);
				String transrs = element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent();
				transr[i]=Double.parseDouble(transrs);
				
			    
			}
			
			double[] transx=new double[6];

			for(int i=0; i<powtransend.getLength(); i++){
				Element element = (Element) powtransend.item(i);
				String transrx = element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent();
				transx[i]=Double.parseDouble(transrx);
				
			    
			}
			
			double[] tsg= new double[6];
			double[] tsb= new double[6];
			
			for(int i=0; i<powtransend.getLength(); i++){
				Element element = (Element) powtransend.item(i);
				String tG = element.getElementsByTagName("cim:PowerTransformerEnd.g").item(0).getTextContent();
				String tB = element.getElementsByTagName("cim:PowerTransformerEnd.b").item(0).getTextContent();
				tsg[i]=Double.parseDouble(tG);
				tsb[i]=Double.parseDouble(tB);
				

			}
			
			
			
			
			ArrayList<transformer> transformerList = new ArrayList<transformer>();
			double[] transformerr={transr[0],transr[2],transr[4]};
			double[] transformerx={transx[0],transx[2],transx[4]};
			double[] transformerb={tsb[0],tsb[2],tsb[4]};
			double[] transformerg={tsg[0],tsg[2],tsg[4]};
			double[] transformerbv={basevolt[3],basevolt[2],basevolt[1]};
			
			double[] tg= new double[3];
			double[] tb= new double[3];

			
			
			for(int i=0; i<powtrans.getLength(); i++){
				
				 int s =1000;
				 transformerList.add(new transformer(transformerr[i],transformerx[i],transformerbv[i]));
				 

				 
				 tg[i]=transformerbv[i]*transformerbv[i]*transformerg[i]/s+transformerList.get(i).getG();
				 
				 tb[i]=transformerbv[i]*transformerbv[i]*transformerb[i]/s+transformerList.get(i).getB();
				 

				 
				}
			
			
			//get b and g from energy consumer
			double[] loadg= new double[3];
			double[] loadb= new double[3];
			double s =1000;
			for(int i=0; i<engconssh.getLength(); i++){
				Element element = (Element) engconssh.item(i);
				String loadp = element.getElementsByTagName("cim:EnergyConsumer.p").item(0).getTextContent();
				loadg[i]=Double.parseDouble(loadp)/s;
				
			    
			}
			for(int i=0; i<engconssh.getLength(); i++){
				Element element = (Element) engconssh.item(i);
				String loadq = element.getElementsByTagName("cim:EnergyConsumer.q").item(0).getTextContent();
				loadb[i]=Double.parseDouble(loadq)/s;
				
			    
			}
			//b and g for shunt capacitor
			
					double[] cg= new double[2];
					double[] cb= new double[2];
					double[] cbv= new double[2];
					cbv[0]=basevolt[3];
					cbv[1]=basevolt[1];
					for(int i=0; i<capacitor.getLength(); i++){
						Element element = (Element) capacitor.item(i);
						String cgs = element.getElementsByTagName("cim:LinearShuntCompensator.gPerSection").item(0).getTextContent();
						cg[i]=cbv[i]*cbv[i]*Double.parseDouble(cgs)/s;
						
					    
					}
					for(int i=0; i<capacitor.getLength(); i++){
						Element element = (Element) capacitor.item(i);
						String cbs = element.getElementsByTagName("cim:LinearShuntCompensator.bPerSection").item(0).getTextContent();
						cb[i]=cbv[i]*cbv[i]*Double.parseDouble(cbs)/s;
						
					    
					}
			//extract r and x for ACLines
			double[] liner= new double[2];
	        double[] linex=new double [2];
	        double[] lsg=new double [2];
	        double[] lsb=new double [2];
	        double[] lg=new double [2];
	        double[] lb=new double [2];
	        double linebv=basevolt[2];
	        
	        ArrayList<line> lineList = new ArrayList<line>();
			for(int i=0; i<aclines.getLength(); i++){
				Element element = (Element) aclines.item(i);
				String R = element.getElementsByTagName("cim:ACLineSegment.r").item(0).getTextContent();
				liner[i]=Double.parseDouble(R); 
			    
				String X = element.getElementsByTagName("cim:ACLineSegment.x").item(0).getTextContent();
				linex[i]=Double.parseDouble(X);
				
				String G = element.getElementsByTagName("cim:ACLineSegment.gch").item(0).getTextContent();
				lsg[i]=Double.parseDouble(G); 
				
				String B = element.getElementsByTagName("cim:ACLineSegment.bch").item(0).getTextContent();
				lsb[i]=Double.parseDouble(B); 
				
			    
				 lineList.add(new line(liner[i],linex[i],linebv));
				 lg[i]=basevolt[2]*basevolt[2]*lsg[i]/s+lineList.get(i).getG();
				 
				 lb[i]=basevolt[2]*basevolt[2]*lsb[i]/s+lineList.get(i).getB();
				 
			}
			
			
			//states of breakers
			
			String[] state=new String[9];
			double[] statefactor=new double[9];
			
			for(int i=0;i<breakerssh.getLength();i++){
				Element element = (Element) breakerssh.item(i);
				state[i]=element.getElementsByTagName("cim:Switch.open").item(0).getTextContent();
				
				
				if(state[i].equals("false")){
					statefactor[i]=1.0;
				}
				else{
					statefactor[i]=0.0;
				}
			}
			
	        //build Ybus
			double[] Yg= new double[25];
			double[] Yb= new double[25];
			Yg[0]=statefactor[7]*statefactor[0]*tg[0]+cg[1];
			Yb[0]=statefactor[7]*statefactor[0]*tb[0]+cb[1];
			Yg[1]=0;
			Yb[1]=0;
			Yg[2]=0;
			Yb[2]=0;
			Yg[3]=0;
			Yb[3]=0;
			Yg[4]=-statefactor[7]*statefactor[0]*tg[0];
			Yb[4]=-statefactor[7]*statefactor[0]*tb[0];
			Yg[5]=0;
			Yb[5]=0;
			Yg[6]=statefactor[6]*statefactor[4]*lg[0]+statefactor[5]*statefactor[2]*lg[1]+loadg[0];
			Yb[6]=statefactor[6]*statefactor[4]*lb[0]+statefactor[5]*statefactor[2]*lb[1]+loadb[0];
			Yg[7]=-(statefactor[6]*statefactor[4]*lg[0]+statefactor[5]*statefactor[2]*lg[1]);
			Yb[7]=-(statefactor[6]*statefactor[4]*lb[0]+statefactor[5]*statefactor[2]*lb[1]);
			Yg[8]=0;
			Yb[8]=0;
			Yg[9]=0;
			Yb[9]=0;
			Yg[10]=0;
			Yb[10]=0;
			Yg[11]=-(statefactor[4]*statefactor[6]*lg[0]+statefactor[2]*statefactor[5]*lg[1]);;
			Yb[11]=-(statefactor[4]*statefactor[6]*lb[0]+statefactor[2]*statefactor[5]*lb[1]);
			Yg[12]=statefactor[2]*statefactor[5]*lg[1]+statefactor[4]*statefactor[6]*lg[0]+statefactor[1]*statefactor[3]*tg[1]+loadg[1];
			Yb[12]=statefactor[2]*statefactor[5]*lb[1]+statefactor[4]*statefactor[6]*lb[0]+statefactor[1]*statefactor[3]*tb[1]+loadb[1];
			Yg[13]=0;
			Yb[13]=0;
			Yg[14]=-statefactor[1]*statefactor[3]*tg[1];
			Yb[14]=-statefactor[1]*statefactor[3]*tb[1];
			Yg[15]=0;
			Yb[15]=0;
			Yg[16]=0;
			Yb[16]=0;
			Yg[17]=0;
			Yb[17]=0;
			Yg[18]=statefactor[8]*tg[2];
			Yb[18]=statefactor[8]*tb[2];
			Yg[19]=-statefactor[8]*tg[2];
			Yb[19]=-statefactor[8]*tb[2];
			Yg[20]=Yg[4];
			Yb[20]=Yb[4];
			Yg[21]=0;
			Yb[21]=0;
			Yg[22]=Yg[14];
			Yb[22]=Yg[14];
			Yg[23]=Yg[19];
			Yb[23]=Yb[19];
			Yg[24]=statefactor[7]*statefactor[0]*tg[0]+statefactor[1]*statefactor[3]*tg[1]+statefactor[8]*tg[2]+loadg[2]+cg[0];
			Yb[24]=statefactor[7]*statefactor[0]*tb[0]+statefactor[1]*statefactor[3]*tb[1]+statefactor[8]*tb[2]+loadb[2]+cb[0];
			
			
              
				String[] Y = new String[25] ;
				String[] Ybs = new String[25];
				String[] Ygs = new String[25];
				for(int i=0;i<25;i++){
					Ybs[i]= Double.toString(Yb[i]);
					Ygs[i]= Double.toString(Yg[i]);
					Y[i] = " "+Yg[i]+ "+" +"("+Yb[i]+"i"+") ";
				}
				
				for(int i=0;i<5;i++){
					System.out.print(Y[i]);
				}
				    System.out.print("\n");
				for(int i=5;i<10;i++){
					System.out.print(Y[i]);
				}
				    System.out.print("\n");
				for(int i=10;i<15;i++){
					System.out.print(Y[i]);
				}
					System.out.print("\n");    
				for(int i=15;i<20;i++){
					System.out.print(Y[i]);
				}
					System.out.print("\n");		    
				for(int i=20;i<25;i++){
					System.out.print(Y[i]);
				}
					System.out.print("\n");
				
		
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open a connection
			System.out.println("Connecting to SQL server...");
			conn = DriverManager.getConnection(DB_URL+DISABLE_SSL, USER, PASS);

			// execute a query to create database
			System.out.println("Creating database...");
			st = conn.createStatement();
			String sql1 = "CREATE DATABASE IF NOT EXISTS PowerSystem"; // create database Students
			st.executeUpdate(sql1);
			System.out.println("Database created successfully...");
			sql1 = "USE PowerSystem";
			st.executeUpdate(sql1);
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		String query = "SELECT * FROM BaseVoltage";
		Statement st0 = conn.createStatement(); 
		// execute the query, and get a java result set
		ResultSet rs = st0.executeQuery(query);
		String[] BVRDFID = new String[20] ;
		String[] BVNOMVAL = new String[20] ;
		int i =0 ;
		while(rs.next()){

			String RDFID= rs.getString("RDFID");
			String NOMVAL = rs.getString("Nominal_value");
			
			BVRDFID[i] = RDFID ;
			BVNOMVAL[i]= NOMVAL;
			i++ ;
		}


		String[] BaseVoltsrdf = {"The RDFID of the BaseVoltages are as follows :",
				BVRDFID[0], BVRDFID[1], BVRDFID[2], BVRDFID[3], BVRDFID[4], BVRDFID[5], BVRDFID[6], BVRDFID[7],BVRDFID[8], BVRDFID[9], 
				BVRDFID[10], BVRDFID[11], BVRDFID[12], BVRDFID[13], BVRDFID[14], BVRDFID[15], BVRDFID[16], BVRDFID[17],BVRDFID[18], BVRDFID[19]};
		String[] BaseVoltsNomval = {"The Nominal Values are:",
				BVNOMVAL[0], BVNOMVAL[1], BVNOMVAL[2],BVNOMVAL[3], BVNOMVAL[4], BVNOMVAL[5], BVNOMVAL[6],BVNOMVAL[7],BVNOMVAL[8],BVNOMVAL[9],
				BVNOMVAL[10], BVNOMVAL[11],BVNOMVAL[12],BVNOMVAL[13], BVNOMVAL[14], BVNOMVAL[15], BVNOMVAL[16],BVNOMVAL[17],BVNOMVAL[18],BVNOMVAL[19]};
		final JPanel listPanel1 = new JPanel();
		listPanel1.setVisible(true);
		JLabel listLbl = new JLabel("Base Voltage data");
		JList Voltsrdf = new JList(BaseVoltsrdf);
		JList Voltsnom = new JList(BaseVoltsNomval);
		listPanel1.add(listLbl);
		listPanel1.add(Voltsrdf);
		listPanel1.add(Voltsnom);
		JButton btnBaseVoltage = new JButton("Base Voltage");
		btnBaseVoltage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl, listPanel1);
			}
		});
		btnBaseVoltage.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnBaseVoltage.setBounds(56, 164, 229, 29);
		frame.getContentPane().add(btnBaseVoltage);
		
		
		String query1 = "SELECT * FROM Substation";
		
		ResultSet rs1 = st.executeQuery(query1);
		String[] SRDFID = new String[20] ;
		String[] SNAME = new String[20] ;
		String[] SRREGIONRDFS = new String[20];
		int i1 =0 ;
		while(rs1.next()){

			String RDFID= rs1.getString("RDFID");
			String NAME = rs1.getString("Name");
			String SREGIONRDFID = rs1.getString("Region_rdfID");
			
			SRDFID[i1] = RDFID ;
			SNAME[i1]= NAME;
			SRREGIONRDFS[i1]= SREGIONRDFID ;
			i1++ ;
		}
		
		String[] Substationrdf = {"The RDFID of the Substation are as follows :",
				SRDFID[0], SRDFID[1],SRDFID[2], SRDFID[3],SRDFID[4], SRDFID[5],SRDFID[6], SRDFID[7],SRDFID[8], SRDFID[9],
				SRDFID[10], SRDFID[11],SRDFID[12], SRDFID[13],SRDFID[14], SRDFID[15],SRDFID[16], SRDFID[17],SRDFID[18], SRDFID[19]};
		String[] Substationname = {"The Names of substations are:",
				SNAME[0],SNAME[1],SNAME[2],SNAME[3],SNAME[4],SNAME[5],SNAME[6],SNAME[7],SNAME[8],SNAME[9],
				SNAME[10],SNAME[11],SNAME[12],SNAME[13],SNAME[14],SNAME[15],SNAME[16],SNAME[17],SNAME[18],SNAME[19]};
		String[] Sregrdfid = {"The Names of substations are:",
				SRREGIONRDFS[0],SRREGIONRDFS[1],SRREGIONRDFS[2],SRREGIONRDFS[3],SRREGIONRDFS[4],SRREGIONRDFS[5],SRREGIONRDFS[6],SRREGIONRDFS[7],SRREGIONRDFS[8],SRREGIONRDFS[9],
				SRREGIONRDFS[10],SRREGIONRDFS[11],SRREGIONRDFS[12],SRREGIONRDFS[13],SRREGIONRDFS[14],SRREGIONRDFS[15],SRREGIONRDFS[16],SRREGIONRDFS[17],SRREGIONRDFS[18],SRREGIONRDFS[19]};
		
		
		final JPanel listPanel2 = new JPanel();
		listPanel2.setVisible(true);
		JLabel listLbl2 = new JLabel("Substation Data");
		JList Substationrdf1 = new JList(Substationrdf);
		JList Substationname1 = new JList(Substationname);
		JList Sregrdfid1 = new JList(Sregrdfid);
	
		listPanel2.add(listLbl2);
		listPanel2.add(Substationrdf1);
		listPanel2.add(Substationname1);
		listPanel2.add(Sregrdfid1);
		
		JButton btnSubstation = new JButton("Substation");
		btnSubstation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl2, listPanel2);
			}
		});
		btnSubstation.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnSubstation.setBounds(56, 257, 237, 29);
		frame.getContentPane().add(btnSubstation);
		
		
		
		String query2 = "SELECT * FROM Voltage_Level";
		
		ResultSet rs2 = st.executeQuery(query2);
		String[] VRDFIDarr = new String[20] ;
		String[] VNAME1arr = new String[20] ;
		String[] VSRDFIDarr = new String[20];
		String[] VBVRDFIDarr = new String[20];
		int i2 =0 ;
		while(rs2.next()){
			String VRDFID= rs2.getString("RDFID");
			String VNAME = rs2.getString("Name");
			String SREGIONRDFID = rs2.getString("Substation_rdfID");
			String SBVRDFID = rs2.getString("Base_Voltage_rdfID");
			
			VRDFIDarr[i2] = VRDFID ;
			VNAME1arr[i2]= VNAME;
			VSRDFIDarr[i2]= SREGIONRDFID ;
			VBVRDFIDarr[i2]=SBVRDFID ;
			i2++ ;
		}
		
		String[] bvrdf = {"The RDFID of the Voltage Level are as follows :",
				VRDFIDarr[0],VRDFIDarr[1],VRDFIDarr[2],VRDFIDarr[3],VRDFIDarr[4],VRDFIDarr[5],VRDFIDarr[6],VRDFIDarr[7],VRDFIDarr[8],VRDFIDarr[9],
				VRDFIDarr[10],VRDFIDarr[11],VRDFIDarr[12],VRDFIDarr[13],VRDFIDarr[14],VRDFIDarr[15],VRDFIDarr[16],VRDFIDarr[17],VRDFIDarr[18],VRDFIDarr[19]};
		String[] bvname = {"The Voltage Levels are:",
				VNAME1arr[0],VNAME1arr[1],VNAME1arr[2],VNAME1arr[3],VNAME1arr[4],VNAME1arr[5],VNAME1arr[6],VNAME1arr[7],VNAME1arr[8],VNAME1arr[9],
				VNAME1arr[10],VNAME1arr[11],VNAME1arr[12],VNAME1arr[13],VNAME1arr[14],VNAME1arr[15],VNAME1arr[16],VNAME1arr[17],VNAME1arr[18],VNAME1arr[19]};
		String[] Srdfid = {"The rdf id of corresponding Substations are:",
				VSRDFIDarr[0],VSRDFIDarr[1],VSRDFIDarr[2],VSRDFIDarr[3],VSRDFIDarr[4],VSRDFIDarr[5],VSRDFIDarr[6],VSRDFIDarr[7],VSRDFIDarr[8],VSRDFIDarr[9],
				VSRDFIDarr[10],VSRDFIDarr[11],VSRDFIDarr[12],VSRDFIDarr[13],VSRDFIDarr[14],VSRDFIDarr[15],VSRDFIDarr[16],VSRDFIDarr[17],VSRDFIDarr[18],VSRDFIDarr[19]};
		String[] bvrdfid = {"The rdf id of corresponding Base Voltages are:",
				VBVRDFIDarr[0],VBVRDFIDarr[1],VBVRDFIDarr[2],VBVRDFIDarr[3],VBVRDFIDarr[4],VBVRDFIDarr[5],VBVRDFIDarr[6],VBVRDFIDarr[7],VBVRDFIDarr[8],VBVRDFIDarr[9],
				VBVRDFIDarr[10],VBVRDFIDarr[11],VBVRDFIDarr[12],VBVRDFIDarr[13],VBVRDFIDarr[14],VBVRDFIDarr[15],VBVRDFIDarr[16],VBVRDFIDarr[17],VBVRDFIDarr[18],VBVRDFIDarr[19]};
		
		
		final JPanel listPanel3 = new JPanel();
		listPanel2.setVisible(true);
		JLabel listLbl3 = new JLabel("Voltage Level Data");
		JList bvrdf1 = new JList(bvrdf);
		JList bvname1 = new JList(bvname);
		JList Srdfid1 = new JList(Srdfid);
		JList bvrdfid1 = new JList(bvrdfid);
		listPanel3.add(listLbl3);
		listPanel3.add(bvname1);
		listPanel3.add(bvrdf1);
		listPanel3.add(Srdfid1);
		listPanel3.add(bvrdfid1);
		JButton btnNewButton = new JButton("Voltage Level");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl3, listPanel3);
			}
		});
		btnNewButton.setBounds(56, 339, 237, 29);
		frame.getContentPane().add(btnNewButton);
		
		
		String query3 = "SELECT * FROM Generating_Unit";
		
		ResultSet rs3 = st.executeQuery(query3);
		String[] GURDFIDarr = new String[20] ;
		String[] GUNAMEarr = new String[20] ; 
		String[] GUMAXParr = new String[20];
		String[] GUMINParr = new String[20];
		String[] GUECRDFIDarr = new String[20];
		
		int i3 =0 ;
		while(rs3.next()){
			String GURDFID= rs3.getString("RDFID");
			String GUNAME = rs3.getString("Name");
			String GUMAXP = rs3.getString("MAXP");
			String GUMINP = rs3.getString("MINP");
			String GUECRDFID = rs3.getString("Equipment_Container_rdfID");
			
			GURDFIDarr[i3] = GURDFID ;
			GUNAMEarr[i3]= GUNAME;
			GUMAXParr[i3]= GUMAXP ;
			GUMINParr[i3]= GUMINP ;
			GUECRDFIDarr[i3]= GUECRDFID ;
			i3++ ;
		}
		
		String[] gurdf = {"The RDFID of the Generating units are as follows :",
				GURDFIDarr[0],GURDFIDarr[1],GURDFIDarr[2],GURDFIDarr[3],GURDFIDarr[4],GURDFIDarr[5],GURDFIDarr[6],GURDFIDarr[7],GURDFIDarr[8],GURDFIDarr[9],
				GURDFIDarr[10],GURDFIDarr[11],GURDFIDarr[12],GURDFIDarr[13],GURDFIDarr[14],GURDFIDarr[15],GURDFIDarr[16],GURDFIDarr[17],GURDFIDarr[18],GURDFIDarr[19]};
		String[] guname = {"The Generating Units are:",
				GUNAMEarr[0],GUNAMEarr[1],GUNAMEarr[2],GUNAMEarr[3],GUNAMEarr[4],GUNAMEarr[5],GUNAMEarr[6],GUNAMEarr[7],GUNAMEarr[8],GUNAMEarr[9],
				GUNAMEarr[10],GUNAMEarr[11],GUNAMEarr[12],GUNAMEarr[13],GUNAMEarr[14],GUNAMEarr[15],GUNAMEarr[16],GUNAMEarr[17],GUNAMEarr[18],GUNAMEarr[19]};
		String[] gumaxp = {"The MAXP of Generating Units are:",
				GUMAXParr[0],GUMAXParr[1],GUMAXParr[2],GUMAXParr[3],GUMAXParr[4],GUMAXParr[5],GUMAXParr[6],GUMAXParr[7],GUMAXParr[8],GUMAXParr[9],
				GUMAXParr[10],GUMAXParr[11],GUMAXParr[12],GUMAXParr[13],GUMAXParr[14],GUMAXParr[15],GUMAXParr[16],GUMAXParr[17],GUMAXParr[18],GUMAXParr[19]};
		String[] guminp = {"The MINP of Generating Units are:",
				GUMINParr[0],GUMINParr[1],GUMINParr[2],GUMINParr[3],GUMINParr[4],GUMINParr[5],GUMINParr[6],GUMINParr[7],GUMINParr[8],GUMINParr[9],
				GUMINParr[10],GUMINParr[11],GUMINParr[12],GUMINParr[13],GUMINParr[14],GUMINParr[15],GUMINParr[16],GUMINParr[17],GUMINParr[18],GUMINParr[19]};
		String[] guecrdfid = {"The RDFID OF Equipment Container of Generating Units are:",
				GUECRDFIDarr[0],GUECRDFIDarr[1],GUECRDFIDarr[2],GUECRDFIDarr[3],GUECRDFIDarr[4],GUECRDFIDarr[5],GUECRDFIDarr[6],GUECRDFIDarr[7],GUECRDFIDarr[8],GUECRDFIDarr[9],
				GUECRDFIDarr[10],GUECRDFIDarr[11],GUECRDFIDarr[12],GUECRDFIDarr[13],GUECRDFIDarr[14],GUECRDFIDarr[15],GUECRDFIDarr[16],GUECRDFIDarr[17],GUECRDFIDarr[18],GUECRDFIDarr[19]};
		
		
		final JPanel listPanel4 = new JPanel();
		listPanel4.setVisible(true);
		JLabel listLbl4 = new JLabel("Generating Unit Data");
		JList gurdf1 = new JList(gurdf);
		JList guname1 = new JList(guname);
		JList gumaxp1 = new JList(gumaxp);
		JList guminp1 = new JList(guminp);
		JList guecrdfid1 = new JList(guecrdfid);
		listPanel4.add(listLbl4);
		listPanel4.add(gurdf1);
		listPanel4.add(guname1);
		listPanel4.add(gumaxp1);
		listPanel4.add(guminp1);
		listPanel4.add(guecrdfid1);
		
		JButton btnNewButton_1 = new JButton("Generating Unit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl4, listPanel4);
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnNewButton_1.setBounds(56, 507, 237, 29);
		frame.getContentPane().add(btnNewButton_1);
		
		
		String query4 = "SELECT * FROM Synchronous_Machine";
		
		ResultSet rs4 = st.executeQuery(query4);
		String[] SMRDFIDarr = new String[20] ;
		String[] SMNAMEarr = new String[20] ; 
		String[] SMRATEDSarr = new String[20];
		String[] SMParr = new String[20];
		String[] SMQarr = new String[20];
        String[] SMGURDFarr = new String[20];
		
		int i4 =0 ;
		while(rs4.next()){
			String SMRDFID = rs4.getString("RDFID");
			String SMNAME = rs4.getString("Name");
			String SMRATEDS = rs4.getString("RatedS");
			String SMP = rs4.getString("P");
			String SMQ = rs4.getString("Q");
            String SMGURDF = rs4.getString("genunit_rdfID");
			
			SMRDFIDarr[i4] = SMRDFID ;
			SMNAMEarr[i4]= SMNAME; 
			SMRATEDSarr[i4]= SMRATEDS ;
			SMParr[i4]= SMP ;
			SMQarr[i4]= SMQ ;
            SMGURDFarr[i4]= SMGURDF ;
			i4++ ;
		}
		
		String[] smrdf = {"The RDFID of the Synchronous Machines are as follows :",
				SMRDFIDarr[0],SMRDFIDarr[1],SMRDFIDarr[2],SMRDFIDarr[3],SMRDFIDarr[4],SMRDFIDarr[5],SMRDFIDarr[6],SMRDFIDarr[7],SMRDFIDarr[8],SMRDFIDarr[9],
				SMRDFIDarr[10],SMRDFIDarr[11],SMRDFIDarr[12],SMRDFIDarr[13],SMRDFIDarr[14],SMRDFIDarr[15],SMRDFIDarr[16],SMRDFIDarr[17],SMRDFIDarr[18],SMRDFIDarr[19]};
		String[] smname = {"The Synchronous machine names:",
				SMNAMEarr[0],SMNAMEarr[1],SMNAMEarr[2],SMNAMEarr[3],SMNAMEarr[4],SMNAMEarr[5],SMNAMEarr[6],SMNAMEarr[7],SMNAMEarr[8],SMNAMEarr[9],
				SMNAMEarr[10],SMNAMEarr[11],SMNAMEarr[12],SMNAMEarr[13],SMNAMEarr[14],SMNAMEarr[15],SMNAMEarr[16],SMNAMEarr[17],SMNAMEarr[18],SMNAMEarr[19]};
		String[] smrateds = {"The Rated S of synchronous Machine are:",
				SMRATEDSarr[0],SMRATEDSarr[1],SMRATEDSarr[2],SMRATEDSarr[3],SMRATEDSarr[4],SMRATEDSarr[5],SMRATEDSarr[6],SMRATEDSarr[7],SMRATEDSarr[8],SMRATEDSarr[9],
				SMRATEDSarr[10],SMRATEDSarr[11],SMRATEDSarr[12],SMRATEDSarr[13],SMRATEDSarr[14],SMRATEDSarr[15],SMRATEDSarr[16],SMRATEDSarr[17],SMRATEDSarr[18],SMRATEDSarr[19]};
		String[] smp = {"P of synchronous machine are:",
				SMParr[0],SMParr[1],SMParr[2],SMParr[3],SMParr[4],SMParr[5],SMParr[6],SMParr[7],SMParr[8],SMParr[9],
				SMParr[10],SMParr[11],SMParr[12],SMParr[13],SMParr[14],SMParr[15],SMParr[16],SMParr[17],SMParr[18],SMParr[19]};
		String[] smq = {"Q of synchronous machine are:",
				SMQarr[0],SMQarr[1],SMQarr[2],SMQarr[3],SMQarr[4],SMQarr[5],SMQarr[6],SMQarr[7],SMQarr[8],SMQarr[9],
				SMQarr[10],SMQarr[11],SMQarr[12],SMQarr[13],SMQarr[14],SMQarr[15],SMQarr[16],SMQarr[17],SMQarr[18],SMQarr[19]};
        String[] smgurdf = {"The RDFID OF Equipment Container of Generating Units are:",
        		SMGURDFarr[0],SMGURDFarr[1],SMGURDFarr[2],SMGURDFarr[3],SMGURDFarr[4],SMGURDFarr[5],SMGURDFarr[6],SMGURDFarr[7],SMGURDFarr[8],SMGURDFarr[9],
        		SMGURDFarr[10],SMGURDFarr[11],SMGURDFarr[12],SMGURDFarr[13],SMGURDFarr[14],SMGURDFarr[15],SMGURDFarr[16],SMGURDFarr[17],SMGURDFarr[18],SMGURDFarr[19]};
		
		
		final JPanel listPanel5 = new JPanel();
		listPanel5.setVisible(true);
		JLabel listLbl5 = new JLabel("Synchronous Machine Data");
		JList smrdf1 = new JList(smrdf);
		JList smname1 = new JList(smname);
		JList smrated1 = new JList(gumaxp);
		JList smp1 = new JList(smp);
		JList smq1 = new JList(smq);
        JList smgurdf1 = new JList(smgurdf);
        
		listPanel5.add(smrdf1);
		listPanel5.add(smname1);
		listPanel5.add(smrated1);
		listPanel5.add(smp1);
		listPanel5.add(smq1);
		listPanel5.add(smgurdf1);
		JList list = new JList();
		list.setBounds(93, 61, 1, 1);
		frame.getContentPane().add(list);
		btnSynchronousMachine.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnSynchronousMachine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl5, listPanel5);
			}
		});
		btnSynchronousMachine.setBounds(609, 164, 288, 29);
		frame.getContentPane().add(btnSynchronousMachine);
		
		
		String query5 = "SELECT * FROM Regulating_Control";
		
		ResultSet rs5 = st.executeQuery(query5);
		String[] RCRDFIDarr = new String[20] ;
		String[] RCNAMEarr = new String[20] ; 
		String[] RCTVarr = new String[20];
		
		int i5 =0 ;
		while(rs5.next()){
			String RCRDFID = rs5.getString("RDFID");
			String RCNAME = rs5.getString("Name");
			String RCTV = rs5.getString("Target_Value");
			
			RCRDFIDarr[i5] = RCRDFID ;
			RCNAMEarr[i5]= RCNAME ;
			RCTVarr[i5]= RCTV ;
			i5++ ;
		}
		
		String[] rcrdf = {"The RDFID of the Regulating Control are as follows :",
				RCRDFIDarr[0],RCRDFIDarr[1],RCRDFIDarr[2],RCRDFIDarr[3],RCRDFIDarr[4],RCRDFIDarr[5],RCRDFIDarr[6],RCRDFIDarr[7],RCRDFIDarr[8],RCRDFIDarr[9],
				RCRDFIDarr[10],RCRDFIDarr[11],RCRDFIDarr[12],RCRDFIDarr[13],RCRDFIDarr[14],RCRDFIDarr[15],RCRDFIDarr[16],RCRDFIDarr[17],RCRDFIDarr[18],RCRDFIDarr[19]};
		String[] rcname = {"The Regulating Control names:",
				RCNAMEarr[0],RCNAMEarr[1],RCNAMEarr[2],RCNAMEarr[3],RCNAMEarr[4],RCNAMEarr[5],RCNAMEarr[6],RCNAMEarr[7],RCNAMEarr[8],RCNAMEarr[9],
				RCNAMEarr[10],RCNAMEarr[11],RCNAMEarr[12],RCNAMEarr[13],RCNAMEarr[14],RCNAMEarr[15],RCNAMEarr[16],RCNAMEarr[17],RCNAMEarr[18],RCNAMEarr[19]};
		String[] rctv = {"The Rated S of synchronous Machine are:",
				RCTVarr[0],RCTVarr[1],RCTVarr[2],RCTVarr[3],RCTVarr[4],RCTVarr[5],RCTVarr[6],RCTVarr[7],RCTVarr[8],RCTVarr[9],
				RCTVarr[10],RCTVarr[11],RCTVarr[12],RCTVarr[13],RCTVarr[14],RCTVarr[15],RCTVarr[16],RCTVarr[17],RCTVarr[18],RCTVarr[19]};
		
		
		final JPanel listPanel6 = new JPanel();
		listPanel6.setVisible(true);
		JLabel listLbl6 = new JLabel("Regulating Control Data");
		JList rcrdf1 = new JList(rcrdf);
		JList rcname1 = new JList(rcname);
		JList rctv1 = new JList(rctv);
		listPanel6.add(listLbl6);
		listPanel6.add(rcrdf1);
		listPanel6.add(rcname1);
		listPanel6.add(rctv1);
		
		JButton btnRegulatingControl = new JButton("Regulating Control");
		btnRegulatingControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl6, listPanel6);
			}
		});
		btnRegulatingControl.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnRegulatingControl.setBounds(609, 257, 288, 29);
		frame.getContentPane().add(btnRegulatingControl);
		
		
		String query6 = "SELECT * FROM Power_Transformer";
		
		ResultSet rs6 = st.executeQuery(query6);
		String[] PTRDFIDarr = new String[20] ;
		String[] PTNAMEarr = new String[20] ; 
		String[] PTECarr = new String[20];
		
		int i6 =0 ;
		while(rs6.next()){
			String PTRDFID = rs6.getString("RDFID");
			String PTNAME = rs6.getString("Name");
			String PTEC = rs6.getString("Equipment_Container_rdfID");
			
			PTRDFIDarr[i6] = PTRDFID ;
			PTNAMEarr[i6]= PTNAME;
			PTECarr[i6]= PTEC ;
			i6++ ;
		}
		
		String[] ptrdf = {"The RDFID of the Power Tranfformers are as follows :",
				PTRDFIDarr[0],PTRDFIDarr[1],PTRDFIDarr[2],PTRDFIDarr[3],PTRDFIDarr[4],PTRDFIDarr[5],PTRDFIDarr[6],PTRDFIDarr[7],PTRDFIDarr[8],PTRDFIDarr[9],
				PTRDFIDarr[10],PTRDFIDarr[11],PTRDFIDarr[12],PTRDFIDarr[13],PTRDFIDarr[14],PTRDFIDarr[15],PTRDFIDarr[16],PTRDFIDarr[17],PTRDFIDarr[18],PTRDFIDarr[19]};
		String[] ptname = {"The Power Transformer names:",
				PTNAMEarr[0],PTNAMEarr[1],PTNAMEarr[2],PTNAMEarr[3],PTNAMEarr[4],PTNAMEarr[5],PTNAMEarr[6],PTNAMEarr[7],PTNAMEarr[8],PTNAMEarr[9],
				PTNAMEarr[10],PTNAMEarr[11],PTNAMEarr[12],PTNAMEarr[13],PTNAMEarr[14],PTNAMEarr[15],PTNAMEarr[16],PTNAMEarr[17],PTNAMEarr[18],PTNAMEarr[19]};
		String[] ptec = {"The rdf ID of Power Transformer Equipment Container are:",
				PTECarr[0],PTECarr[1],PTECarr[2],PTECarr[3],PTECarr[4],PTECarr[5],PTECarr[6],PTECarr[7],PTECarr[8],PTECarr[9],
				PTECarr[10],PTECarr[11],PTECarr[12],PTECarr[13],PTECarr[14],PTECarr[15],PTECarr[16],PTECarr[17],PTECarr[18],PTECarr[19]};
		
		
		final JPanel listPanel7 = new JPanel();
		listPanel7.setVisible(true);
		JLabel listLbl7 = new JLabel("Power Transformer Data");
		JList ptrdf1 = new JList(ptrdf);
		JList ptname1 = new JList(ptname);
		JList ptec1 = new JList(ptec);
		listPanel7.add(listLbl7);
		listPanel7.add(ptrdf1);
		listPanel7.add(ptname1);
		listPanel7.add(ptec1);
		JButton btnPowerTransformer = new JButton("Power Transformer");
		btnPowerTransformer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl7, listPanel7);
			}
		});
		btnPowerTransformer.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnPowerTransformer.setBounds(609, 340, 288, 29);
		frame.getContentPane().add(btnPowerTransformer);
		
		
		String query10 = "SELECT * FROM Ratio_Tap_Changer";
		
		ResultSet rs10 = st.executeQuery(query10);
		String[] RTCRDFarr = new String[20] ;
		String[] RTCNAMEarr = new String[20] ; 
		String[] RTCSTEParr = new String[20];
					
		int i10 =0 ;
		while(rs10.next()){
			String RTCRDF = rs10.getString("RDFID");
			String RTCNAME = rs10.getString("Name");
			String RTCSTEP = rs10.getString("Step");
			
			RTCRDFarr[i10] = RTCRDF ;
			RTCNAMEarr[i10]= RTCNAME;
			RTCSTEParr[i10]= RTCSTEP ;
			i10++ ;
		}
		
		String[] rtcrdf = {"The RDFID of the Ratio Tap Changers are as follows :",
				RTCRDFarr[0],RTCRDFarr[1],RTCRDFarr[2],RTCRDFarr[3],RTCRDFarr[4],RTCRDFarr[5],RTCRDFarr[6],RTCRDFarr[7],RTCRDFarr[8],RTCRDFarr[9],
				RTCRDFarr[10],RTCRDFarr[11],RTCRDFarr[12],RTCRDFarr[13],RTCRDFarr[14],RTCRDFarr[15],RTCRDFarr[16],RTCRDFarr[17],RTCRDFarr[18],RTCRDFarr[19]};
		String[] rtcname = {"The Ratio Tap Changer names:",
				RTCNAMEarr[0],RTCNAMEarr[1],RTCNAMEarr[2],RTCNAMEarr[3],RTCNAMEarr[4],RTCNAMEarr[5],RTCNAMEarr[6],RTCNAMEarr[7],RTCNAMEarr[8],RTCNAMEarr[9],
				RTCNAMEarr[10],RTCNAMEarr[11],RTCNAMEarr[12],RTCNAMEarr[13],RTCNAMEarr[14],RTCNAMEarr[15],RTCNAMEarr[16],RTCNAMEarr[17],RTCNAMEarr[18],RTCNAMEarr[19]};
		String[] rtcstep  = {"The Ratio Tap Changer Step",
				RTCSTEParr[0],RTCSTEParr[1],RTCSTEParr[2],RTCSTEParr[3],RTCSTEParr[4],RTCSTEParr[5],RTCSTEParr[6],RTCSTEParr[7],RTCSTEParr[8],RTCSTEParr[9],
				RTCSTEParr[10],RTCSTEParr[11],RTCSTEParr[12],RTCSTEParr[13],RTCSTEParr[14],RTCSTEParr[15],RTCSTEParr[16],RTCSTEParr[17],RTCSTEParr[18],RTCSTEParr[19]};			
		final JPanel listPanel11 = new JPanel();
		listPanel11.setVisible(true);
		JLabel listLbl11 = new JLabel("Ratio Tap Changer Data");
		JList rtcrdf1 = new JList(rtcrdf);
		JList rtcname1 = new JList(rtcname);
		JList rtcstep1 = new JList(rtcstep);
		listPanel11.add(listLbl11);
		listPanel11.add(rtcrdf1);
		listPanel11.add(rtcname1); 
		listPanel11.add(rtcstep1);
		JButton btnRatioTapChanger = new JButton("Ratio Tap Changer");
		btnRatioTapChanger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl11, listPanel11);
			}
		});
		btnRatioTapChanger.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnRatioTapChanger.setBounds(56, 605, 237, 29);
		frame.getContentPane().add(btnRatioTapChanger);
		
		

		String query7 = "SELECT * FROM Energy_Consumer";
		
		ResultSet rs7 = st.executeQuery(query7);
		String[] ECRDFIDarr = new String[20] ;
		String[] ECNAMEarr = new String[20] ; 
		String[] ECParr = new String[20];
		String[] ECQarr = new String[20];
		String[] ECECarr = new String[20];
		
		
		int i7 =0 ;
		while(rs7.next()){
			String ECRDFID = rs7.getString("RDFID");
			String ECNAME = rs7.getString("Name");
			String ECP = rs7.getString("P");
			String ECQ = rs7.getString("Q");
			String ECEC = rs7.getString("Equipment_Container_rdfID");
			
			ECRDFIDarr[i7] = ECRDFID ;
			ECNAMEarr[i7]= ECNAME;
			ECParr[i7]= ECP ;
			ECQarr[i7]= ECQ ;
			ECECarr[i7]= ECEC ;
			i7++ ;
		}
		
		String[] ecrdf = {"The RDFID of the Energy Consumers are as follows :",
				ECRDFIDarr[0],ECRDFIDarr[1],ECRDFIDarr[2],ECRDFIDarr[3],ECRDFIDarr[4],ECRDFIDarr[5],ECRDFIDarr[6],ECRDFIDarr[7],ECRDFIDarr[8],ECRDFIDarr[9],
				ECRDFIDarr[10],ECRDFIDarr[11],ECRDFIDarr[12],ECRDFIDarr[13],ECRDFIDarr[14],ECRDFIDarr[15],ECRDFIDarr[16],ECRDFIDarr[17],ECRDFIDarr[18],ECRDFIDarr[19]};
		String[] ecname = {"The Energy Consumer names:",
				ECNAMEarr[0],ECNAMEarr[1],ECNAMEarr[2],ECNAMEarr[3],ECNAMEarr[4],ECNAMEarr[5],ECNAMEarr[6],ECNAMEarr[7],ECNAMEarr[8],ECNAMEarr[9],
				ECNAMEarr[10],ECNAMEarr[11],ECNAMEarr[12],ECNAMEarr[13],ECNAMEarr[14],ECNAMEarr[15],ECNAMEarr[16],ECNAMEarr[17],ECNAMEarr[18],ECNAMEarr[19]};
		String[] ecp  = {"The active power consumed by Energy Consumer",
				ECParr[0],ECParr[1],ECParr[2],ECParr[3],ECParr[4],ECParr[5],ECParr[6],ECParr[7],ECParr[8],ECParr[9],
				ECParr[10],ECParr[11],ECParr[12],ECParr[13],ECParr[14],ECParr[15],ECParr[16],ECParr[17],ECParr[18],ECParr[19]};
		String[] ecq = {"The reactive power consumed by Energy Consumer",
				ECQarr[0],ECQarr[1],ECQarr[2],ECQarr[3],ECQarr[4],ECQarr[5],ECQarr[6],ECQarr[7],ECQarr[8],ECQarr[9],
				ECQarr[10],ECQarr[11],ECQarr[12],ECQarr[13],ECQarr[14],ECQarr[15],ECQarr[16],ECQarr[17],ECQarr[18],ECQarr[19]};
		String[] ecec = {"The rdf ID of Energy Consumer Equipment Container",
				ECECarr[0],ECECarr[1],ECECarr[2],ECECarr[3],ECECarr[4],ECECarr[5],ECECarr[6],ECECarr[7],ECECarr[8],ECECarr[9],
				ECECarr[10],ECECarr[11],ECECarr[12],ECECarr[13],ECECarr[14],ECECarr[15],ECECarr[16],ECECarr[17],ECECarr[18],ECECarr[19]};
		
		
		
		final JPanel listPanel8 = new JPanel();
		listPanel8.setVisible(true);
		JLabel listLbl8 = new JLabel("Energy Consumer Data");
		JList ecrdf1 = new JList(ecrdf);
		JList ecname1 = new JList(ecname);
		JList ecp1 = new JList(ecp);
		JList ecq1 = new JList(ecq);
		JList ecec1 = new JList(ecec);

		listPanel8.add(listLbl8);
		listPanel8.add(ecrdf1);
		listPanel8.add(ecname1);
		listPanel8.add(ecp1);
		listPanel8.add(ecq1);
		listPanel8.add(ecec1);
		JButton btnEnergyConsumer = new JButton("Energy Consumer");
		btnEnergyConsumer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl8, listPanel8);
			}
		});
		btnEnergyConsumer.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnEnergyConsumer.setBounds(609, 423, 288, 29);
		frame.getContentPane().add(btnEnergyConsumer);
		
		

		String query8 = "SELECT * FROM Power_Transformer_End";
		
		ResultSet rs8 = st.executeQuery(query8);
		String[] PTERDFarr = new String[20] ;
		String[] PTENAMEarr = new String[20] ; 
		String[] PTERarr = new String[20];
		String[] PTEXarr = new String[20];
		String[] PTETXRDFarr = new String[20];
		String[] PTEBVRDFarr = new String[20];			
		
		int i8 =0 ;
		while(rs8.next()){
			String PTERDF = rs8.getString("RDFID");
			String PTENAME = rs8.getString("Name");
			String PTER = rs8.getString("Transformer_r");
			String PTEX = rs8.getString("Transformer_x");
			String PTETXRDF = rs8.getString("Transformer_rdfID");
			String PTEBVRDF = rs8.getString("base_Voltage_rdfID");
			
			PTERDFarr[i8] = PTERDF ;
			PTENAMEarr[i8]= PTENAME;
			PTERarr[i8]= PTER ;
			PTEXarr[i8]= PTEX ;
			PTETXRDFarr[i8]= PTETXRDF ;
			PTEBVRDFarr[i8]= PTEBVRDF ;
			i8++ ;
		}
		
		String[] pterdf = {"The RDFID of the Power Transformer End are as follows :",
				PTERDFarr[0],PTERDFarr[1],PTERDFarr[2],PTERDFarr[3],PTERDFarr[4],PTERDFarr[5],PTERDFarr[6],PTERDFarr[7],PTERDFarr[8],PTERDFarr[9],
				PTERDFarr[10],PTERDFarr[11],PTERDFarr[12],PTERDFarr[13],PTERDFarr[14],PTERDFarr[15],PTERDFarr[16],PTERDFarr[17],PTERDFarr[18],PTERDFarr[19]};
		String[] ptename = {"The PowerTransformerEnd names:",
				PTENAMEarr[0],PTENAMEarr[1],PTENAMEarr[2],PTENAMEarr[3],PTENAMEarr[4],PTENAMEarr[5],PTENAMEarr[6],PTENAMEarr[7],PTENAMEarr[8],PTENAMEarr[9],
				PTENAMEarr[10],PTENAMEarr[11],PTENAMEarr[12],PTENAMEarr[13],PTENAMEarr[14],PTENAMEarr[15],PTENAMEarr[16],PTENAMEarr[17],PTENAMEarr[18],PTENAMEarr[19]};
		String[] pter  = {"The resistance of Power Transformer End",
				PTERarr[0],PTERarr[1],PTERarr[2],PTERarr[3],PTERarr[4],PTERarr[5],PTERarr[6],PTERarr[7],PTERarr[8],PTERarr[9],
				PTERarr[10],PTERarr[11],PTERarr[12],PTERarr[13],PTERarr[14],PTERarr[15],PTERarr[16],PTERarr[17],PTERarr[18],PTERarr[19]};
		String[] ptex = {"The reactance of power Transformer End",
				PTEXarr[0],PTEXarr[1],PTEXarr[2],PTEXarr[3],PTEXarr[4],PTEXarr[5],PTEXarr[6],PTEXarr[7],PTEXarr[8],PTEXarr[9],
				PTEXarr[10],PTEXarr[11],PTEXarr[12],PTEXarr[13],PTEXarr[14],PTEXarr[15],PTEXarr[16],PTEXarr[17],PTEXarr[18],PTEXarr[19]};
		String[] ptetxrdf = {"The rdf ID of Equipment Container corresponding to Power Transformer",
				PTETXRDFarr[0],PTETXRDFarr[1],PTETXRDFarr[2],PTETXRDFarr[3],PTETXRDFarr[4],PTETXRDFarr[5],PTETXRDFarr[6],PTETXRDFarr[7],PTETXRDFarr[8],PTETXRDFarr[9],
				PTETXRDFarr[10],PTETXRDFarr[11],PTETXRDFarr[12],PTETXRDFarr[13],PTETXRDFarr[14],PTETXRDFarr[15],PTETXRDFarr[16],PTETXRDFarr[17],PTETXRDFarr[18],PTETXRDFarr[19]};
		String[] ptebvrdf = {"The rdf ID of Equipment Container corresponding to the Base Voltage",
				PTEBVRDFarr[0],PTEBVRDFarr[1],PTEBVRDFarr[2],PTEBVRDFarr[3],PTEBVRDFarr[4],PTEBVRDFarr[5],PTEBVRDFarr[6],PTEBVRDFarr[7],PTEBVRDFarr[8],PTEBVRDFarr[9],
				PTEBVRDFarr[10],PTEBVRDFarr[11],PTEBVRDFarr[12],PTEBVRDFarr[13],PTEBVRDFarr[14],PTEBVRDFarr[15],PTEBVRDFarr[16],PTEBVRDFarr[17],PTEBVRDFarr[18],PTEBVRDFarr[19]};
		
		
		
		final JPanel listPanel9 = new JPanel();
		listPanel9.setVisible(true);
		JLabel listLbl9 = new JLabel("Power Transformer End");
		JList pterdf1 = new JList(pterdf);
		JList ptename1 = new JList(ptename);
		JList pter1 = new JList(pter);
		JList ptex1 = new JList(ptex);
		JList ptetxrdf1 = new JList(ptetxrdf);
		JList ptebvrdf1 = new JList(ptebvrdf);
		listPanel9.add(listLbl9);
		listPanel9.add(pterdf1);
		listPanel9.add(ptename1);
		listPanel9.add(pter1);
		listPanel9.add(ptex1);
		listPanel9.add(ptetxrdf1);
		listPanel9.add(ptebvrdf1);
		JButton btnPowerTransformerEnd = new JButton("Power Transformer End");
		btnPowerTransformerEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl9, listPanel9);
			}
		});
		btnPowerTransformerEnd.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnPowerTransformerEnd.setBounds(609, 517, 288, 29);
		frame.getContentPane().add(btnPowerTransformerEnd);
		
		
		
		String query9 = "SELECT * FROM Breaker";
		
		ResultSet rs9 = st.executeQuery(query9);
		String[] BRDFarr = new String[20] ;
		String[] BNAMEarr = new String[20] ; 
		String[] BSTATEarr = new String[20];
		String[] BECRDFarr = new String[20];		
		
		int i9 =0 ;
		while(rs9.next()){
			String BRDF = rs9.getString("RDFID");
			String BNAME = rs9.getString("Name");
			String BSTATE = rs9.getString("State");
			String BECRDF = rs9.getString("Equipment_Container_rdfID");
			
			BRDFarr[i9] = BRDF ;
			BNAMEarr[i9]= BNAME;
			BSTATEarr[i9]= BSTATE ;
			BECRDFarr[i9]= BECRDF ;
			i9++ ;
		}
		
		String[] brdf = {"The RDFID of the Breakers are as follows :",
				BRDFarr[0],BRDFarr[1],BRDFarr[2],BRDFarr[3],BRDFarr[4],BRDFarr[5],BRDFarr[6],BRDFarr[7],BRDFarr[8],BRDFarr[9],
				BRDFarr[10],BRDFarr[11],BRDFarr[12],BRDFarr[13],BRDFarr[14],BRDFarr[15],BRDFarr[16],BRDFarr[17],BRDFarr[18],BRDFarr[19]};
		String[] bname = {"The Breaker names:",
				BNAMEarr[0],BNAMEarr[1],BNAMEarr[2],BNAMEarr[3],BNAMEarr[4],BNAMEarr[5],BNAMEarr[6],BNAMEarr[7],BNAMEarr[8],BNAMEarr[9],
				BNAMEarr[10],BNAMEarr[11],BNAMEarr[12],BNAMEarr[13],BNAMEarr[14],BNAMEarr[15],BNAMEarr[16],BNAMEarr[17],BNAMEarr[18],BNAMEarr[19] };
		String[] bstate  = {"The State of the breaker if it is open",
				BSTATEarr[0],BSTATEarr[1],BSTATEarr[2],BSTATEarr[3],BSTATEarr[4],BSTATEarr[5],BSTATEarr[6],BSTATEarr[7],BSTATEarr[8],BSTATEarr[9],
				BSTATEarr[10],BSTATEarr[11],BSTATEarr[12],BSTATEarr[13],BSTATEarr[14],BSTATEarr[15],BSTATEarr[16],BSTATEarr[17],BSTATEarr[18],BSTATEarr[19] };
		String[] becrdf = {"The Breaker Equipment Container rdfID",
				BECRDFarr[0],BECRDFarr[1],BECRDFarr[2],BECRDFarr[3],BECRDFarr[4],BECRDFarr[5],BECRDFarr[6],BECRDFarr[7],BECRDFarr[8],BECRDFarr[9],
				BECRDFarr[10],BECRDFarr[11],BECRDFarr[12],BECRDFarr[13],BECRDFarr[14],BECRDFarr[15],BECRDFarr[16],BECRDFarr[17],BECRDFarr[18],BECRDFarr[19]};
					
		final JPanel listPanel10 = new JPanel();
		listPanel10.setVisible(true);
		JLabel listLbl10 = new JLabel("Breakers Data");
		JList brdf1 = new JList(brdf);
		JList bname1 = new JList(bname);
		JList bstate1 = new JList(bstate);
		JList becrdf1 = new JList(becrdf);
		listPanel10.add(listLbl10);
		listPanel10.add(brdf1);
		listPanel10.add(bname1); 
		listPanel10.add(bstate1);
		listPanel10.add(becrdf1);
		
		JButton btnBreaker = new JButton("Breaker");
		btnBreaker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLbl10, listPanel10);
			}
		});
		btnBreaker.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnBreaker.setBounds(56, 423, 237, 29);
		frame.getContentPane().add(btnBreaker);
		
		JLabel lblComputerApplicationsIn = new JLabel("Computer Applications in Power System");
		lblComputerApplicationsIn.setFont(new Font("Times New Roman", Font.BOLD, 31));
		lblComputerApplicationsIn.setForeground(new Color(0, 0, 128));
		lblComputerApplicationsIn.setBounds(187, 28, 549, 50);
		frame.getContentPane().add(lblComputerApplicationsIn);
		
		JLabel lblCimObjects = new JLabel("Click on the Cim objects or Y bus matrix to view details");
		lblCimObjects.setFont(new Font("Times New Roman", Font.BOLD, 23));
		lblCimObjects.setBounds(197, 83, 612, 50);
		frame.getContentPane().add(lblCimObjects);
		
		
		
		
		String[] YCOL1 = {Y[0], Y[5],Y[10], Y[15],Y[20]};
		String[] YCOL2 = {Y[1], Y[6],Y[11], Y[16],Y[21]};
		String[] YCOL3 = {Y[2], Y[7],Y[12], Y[17],Y[22]};
		String[] YCOL4 = {Y[3], Y[8],Y[13], Y[18],Y[23]};
		String[] YCOL5 = {Y[4], Y[9],Y[14], Y[19],Y[24]}; 
		
		final JPanel listPanelY = new JPanel();
		listPanelY.setVisible(true);
		JLabel listLblY = new JLabel("Y bus matrix is ");
		JList YCOL11 = new JList(YCOL1);
		JList YCOL22 = new JList(YCOL2);
		JList YCOL33 = new JList(YCOL3);
		JList YCOL44 = new JList(YCOL4);
		JList YCOL55 = new JList(YCOL5);
		listPanelY.add(listLblY);
		listPanelY.add(YCOL11);
		listPanelY.add(YCOL22);
		listPanelY.add(YCOL33);
		listPanelY.add(YCOL44);
		listPanelY.add(YCOL55);
		
		JButton btnYBusMatrix = new JButton("Y bus matrix Elements");
		btnYBusMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(listLblY, listPanelY);
			}
		});
		btnYBusMatrix.setFont(new Font("Times New Roman", Font.BOLD, 21));
		btnYBusMatrix.setBounds(609, 607, 288, 29);
		frame.getContentPane().add(btnYBusMatrix);
	}
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
}
	
}
