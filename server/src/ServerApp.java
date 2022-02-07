import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.time.*;

public class ServerApp {

	final static String url = "jdbc:postgresql://localhost:5432/maedb";
	final static String user = "mae";
	final static String password = "mae2021";
	
	//private IP of my phone in my local network
	static String clientIP = "192.168.178.61";
	
	//server is sending on this port
	final static int serverPORT = 49500;
	
	//server is receiving on this port
	final static int clientPORT = 49501;
	static Server server;
	
    public static void main(String[] args) {
    	
        try (Connection con = DriverManager.getConnection(url, user, password)) {
        	
        	while(true) {
        		
        		int UID = 0;
    			int PID = 0;
    			String DateTime = "";
    			String NewName = "";
    			String answer = "";
    			double Timeframe = 0;
    			
        		System.out.println("Server: listening for Message...");
        		String msg = Server.receiveMessage(clientPORT);
        		if (msg.equals("server-error: exception while receiving msg")) {
        			break;
        		}
        		
        		String[] request = msg.split(";");
        		
        		switch (request[0]) {
        			case "scanQR":
        				UID = Integer.parseInt(request[1]);
        				PID = Integer.parseInt(request[2]);
        				DateTime = request[3];
        				Timeframe = addVisit(con, UID, PID, DateTime);
        				answer = String.valueOf(Timeframe);
        				Server.sendMessage(answer, clientIP, serverPORT);
        				break;
        				
        			case "newUser":
        				NewName = request[1];
        				UID = addUser(con, NewName);
        				answer = String.valueOf(UID);
        				Server.sendMessage(answer, clientIP, serverPORT);
        				break;
        				
        			case "setName":
        				UID = Integer.parseInt(request[1]);
        				NewName = request[2];
        				setName(con, UID, NewName);
        				break;
        				
        			case "loadVisits":
        				UID = Integer.parseInt(request[1]);
        				ArrayList<Visit> visits = loadVisits(con, UID);
        				answer = "";
        				for(int i=0; i<visits.size(); i++) {
        					answer +=
    							visits.get(i).getVID() + ";" +
        						visits.get(i).getPID() + ";" +
								visits.get(i).getPlaceName() + ";" +
								visits.get(i).getDateTime() + ";" +
								visits.get(i).getTimeframe() + "%";
        				}
        				Server.sendMessage(answer, clientIP, serverPORT);
        				break;
        				
        			case "loadMetPeople":
        				PID = Integer.parseInt(request[1]);
        				DateTime = request[2];
        				ArrayList<String> metPeople = loadMetPeople(con, PID, DateTime);
        				answer = "";
        				for(int i=0; i<metPeople.size(); i++) {
        					answer += metPeople.get(i) + ";";
        				}
        				Server.sendMessage(answer, clientIP, serverPORT);
        				break;
        		}
        	}

        } catch (SQLException ex) {
            System.out.println("sql-exception: " + ex);
        } catch (IOException ex) {
        	System.out.println("io-exception: " + ex);
        }
    }
    
    /* Example:
     * addVisit(con, 1, 2, "2021-12-22 13:00:00");
     */
    private static double addVisit(Connection con, int UID, int PID, String DateTime) throws SQLException {
    	
    	try {
    	
	    	Statement st = con.createStatement();
	    	st.executeUpdate(
				"insert into visits (uid, pid, datetime) values (" +
				+ UID + ", " +
				+ PID + ", " +
				"'" + DateTime + "');"
			);
	    	st.close();
	    	
	    	String query =
	    			"select Timeframe from Places where PID = ?;";
	    	PreparedStatement ps = con.prepareStatement(query);
	    	ps.setInt(1, PID);
	    	ResultSet rs = ps.executeQuery();
	    	if (rs.next()) {
	    		double result = Double.parseDouble(rs.getString(1));
	    		ps.close();
	    		return result;
	    	} else {
	    		throw new SQLException();
	    	}
	    	
    	} catch (SQLException ex) {
    		System.out.println("sql-exception: " + ex);
    		return 0.0;
    	}
    }
    
    /* Example:
     * System.out.println("test: " + addUser(con, "TestPerson2"));
     */
    private static int addUser(Connection con, String Name) throws SQLException {
    	
    	try {
    	
	    	Statement st = con.createStatement();
	    	st.executeUpdate(
				"insert into Users (name) values ('" + Name + "');"
			);
	    	st.close();
	    	
	    	String query =
				"select max(UID) from Users where Name = ?;";
	    	
	    	PreparedStatement ps = con.prepareStatement(query);
	    	
	    	ps.setString(1, Name);
	    	
	    	ResultSet rs = ps.executeQuery();
	    	if (rs.next()) {
	    		int result = Integer.parseInt(rs.getString(1));
	    		ps.close();
	    		return result;
	    	} else {
	    		ps.close();
	    		throw new SQLException();
	    	}
	    	
    	} catch (SQLException ex) {
    		System.out.println("sql-exception: " + ex);
    		return 0;
    	}
    }
    
    /* Example:
     * setName(con, 4, "TestPerson3");
     */
    private static void setName(Connection con, int UID, String Name) throws SQLException {
    	
    	try {
    	
    	Statement st = con.createStatement();
    	st.executeUpdate(
			"update users set Name = '" + Name +"' where UID = " + UID + ";"
		);
    	st.close();
    	
    	} catch (SQLException ex) {
    		System.out.println("sql-exception: " + ex);
    	}
    }
    
    /* Example:
     * 	ArrayList<Visit> tmp = loadVisits(con, 1);
		for (int i = 0; i < tmp.size(); i++) {
			System.out.println("Row" + i + ": " +
					tmp.get(i).getPID() + " " +
					tmp.get(i).getPlaceName() + " " +
					tmp.get(i).getDate() + " " +
					tmp.get(i).getTime() + " " +
					tmp.get(i).getTimeframe()
					);
		}
     */
    private static ArrayList<Visit> loadVisits(Connection con, int UID) throws SQLException {
    	
    	try {
	    	
	    	String query =
				"select VID, PID, Name, DateTime, Timeframe " +
				"from Visits join Places using (PID) " +
				"where UID = ?" +
				"order by DateTime;";
	        	
	    	PreparedStatement ps = con.prepareStatement(query);
	    	ps.setInt(1, UID);
	    	ResultSet rs = ps.executeQuery();
	    	
	    	ResultSetMetaData rsmd = rs.getMetaData();
	    	int columnCount = rsmd.getColumnCount();
	    	ArrayList<Visit> visits = new ArrayList<Visit>();
	    	int VID = 0;
	    	int PID = 0;
	    	String PlaceName = "";
	    	String DateTime = "";
	    	double Timeframe = 0;
	    	
	    	while (rs.next()) {
	    		for (int i = 1; i <= columnCount; i++) {
	    			switch (i) {
	    			case 1:
	    				VID = Integer.parseInt(rs.getString(i));
	    				break;
	    			case 2:
	    				PID = Integer.parseInt(rs.getString(i));
	    				break;
	    			case 3:
	    				PlaceName = rs.getString(i);
	    				break;
	    			case 4:
	    				DateTime = rs.getString(i);
	    				break;
	    			case 5:
	    				Timeframe = Double.parseDouble(rs.getString(i));
	    				break;
	    			}
	    		}
	    		Visit newVisit = new Visit(VID, PID, PlaceName, DateTime, Timeframe);
	    		visits.add(newVisit); 
	    	}
	    	ps.close();
			return visits;
			
    	} catch (SQLException ex) {
    		System.out.println("sql-exception: " + ex);
    		return new ArrayList<Visit>();
    	}
    }
    
    /* Example:
     * 	ArrayList<String> tmp = loadMetPeople(con, 1, "2021-12-21 12:55:00");
    	for (int i = 0; i<tmp.size(); i++) {
    		System.out.println(tmp.get(i));
    	}
     */
    public static ArrayList<String> loadMetPeople(Connection con, int PID, String DateTimeBegin) throws SQLException {
    	
    	try {
	    	
	    	String query =
				"select timeframe " +
				"from places " +
				"where PID = ?;";
	        	
	    	PreparedStatement psTimeframe = con.prepareStatement(query);
	    	psTimeframe.setInt(1, PID);
	    	ResultSet rsTimeframe = psTimeframe.executeQuery();
	    	double timeframe = 0;
	    	if (rsTimeframe.next()) {
	    		timeframe = Double.parseDouble(rsTimeframe.getString(1));
	    		psTimeframe.close();
	    	} else {
	    		psTimeframe.close();
	    		throw new SQLException();
	    	}
	    	
	    	query =
				"select distinct users.name " +
				"from visits " +
				"join users using (UID) " +
				"join places using (PID)" +
				"where PID = ? and " +
				"(datetime between " +
				"TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') and " +
				"TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'))";
	        
	    	String timeFrameBegin = getTimeframeBegin(DateTimeBegin, timeframe);
	    	String timeFrameEnd = getTimeframeEnd(DateTimeBegin, timeframe);
	    	PreparedStatement ps = con.prepareStatement(query);
	    	ps.setInt(1, PID);
	    	ps.setString(2, timeFrameBegin);
	    	ps.setString(3, timeFrameEnd);
	    	ResultSet rs = ps.executeQuery();
	    	
	    	ArrayList<String> names = new ArrayList<String>();
	    	
	    	while (rs.next()) {
	    		names.add(rs.getString(1));
	    	}
	    	ps.close();
			return names;
			
    	} catch (SQLException ex) {
    		System.out.println("sql-exception: " + ex);
    		System.out.println("returning empty list of met people");
    		return new ArrayList<String>();
    	}
		
    }
    
    /*
     * Output: 	<date> + "T" + <time>
     * Example: "2021-12-27 13:00:00"
     */
    public static String getTimeframeBegin(String DateTimeString, double Timeframe) {
    	LocalDateTime dateTime = toLocalDateTime(DateTimeString);
    	
    	long secondsToAdd = (long) (Timeframe * 60 * 60);
    	LocalDateTime timeframeBegin = dateTime.plusSeconds((long) secondsToAdd*(-1));
    	
    	String tmp = timeframeBegin.toString();
    	if (tmp.length()==16) {
    		tmp = tmp + ":00";
    	}
    	String result =
    			tmp.substring(0, 10) + " " +
				tmp.substring(11);
    	
    	return result;
    }
    
    /*
     * Output: 	<date> + "T" + <time>
     * Example: "2021-12-27 13:00:00"
     */
    public static String getTimeframeEnd(String DateTimeString, double Timeframe) {
    	LocalDateTime dateTime = toLocalDateTime(DateTimeString);
    	
    	long secondsToAdd = (long) (Timeframe * 60 * 60);
    	LocalDateTime timeframeEnd = dateTime.plusSeconds((long) secondsToAdd);
    	
    	String tmp = timeframeEnd.toString();
    	if (tmp.length()==16) {
    		tmp = tmp + ":00";
    	}
    	String result =
    			tmp.substring(0, 10) + " " +
				tmp.substring(11);
    	
    	return result;
    }
    
    public static LocalDateTime toLocalDateTime(String DateTimeString) {
    	int year = Integer.parseInt(DateTimeString.substring(0, 4));
    	int month = Integer.parseInt(DateTimeString.substring(5, 7));
    	int day = Integer.parseInt(DateTimeString.substring(8, 10));
    	LocalDate date = LocalDate.of(year, month, day);
    	
    	int hour = Integer.parseInt(DateTimeString.substring(11,13));
    	int minute = Integer.parseInt(DateTimeString.substring(14, 16));
    	int second = Integer.parseInt(DateTimeString.substring(17));
    	LocalTime time = LocalTime.of(hour, minute, second);
    	
    	return LocalDateTime.of(date, time);
    }
}