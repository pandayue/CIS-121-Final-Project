package edu.upenn.cis.cis121.project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * 
 * @author Tess Rinearson - tessr@seas.upenn.edu
 *
 */

public class DBWrapper {
	
	private Connection _conn;

	public DBWrapper(String dbUser, String dbPass, String dbSID, String dbHost, 
			int port)
	{
		
		try 
		{
			_conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	// returns the user_ids of all friends of the user with the input user_id
	public int[] getFriends(int user_id)
	{
		ArrayList<Integer> friendlist = new ArrayList<Integer>();
		try {
			String query = "select user_id1 from Friends where user_id2=" + 
					user_id + "union select user_id2 from Friends where " + 
					"user_id1=" + user_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	Integer fid = new Integer(rs.getInt(1));
		    	friendlist.add(fid);
		    }
		    
		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
		}
		
		return convertToPrimitives(friendlist);
	}
	
	// returns the place_ids of all the places liked by the user with user_id
	public int[] getLikes(int user_id)
	{
		ArrayList<Integer> likelist = new ArrayList<Integer>();
		try {
			String query = "select place_id from Likes where user_id=" + 
					user_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	Integer lid = new Integer(rs.getInt(1));
		    	likelist.add(lid);
		    }
		    
		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
		}
		
		return convertToPrimitives(likelist);
		
	}
	
	/**
	 * Get the type_id given a place_id
	 * @param place_id
	 * @return the type_id of the place. returns -1 if not found
	 */
	
	public int getType(int place_id)
	{
		int ptid = -1;
		try {
			String query = "select type_id from Places where place_id=" + 
					place_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	ptid = rs.getInt("type_id");
		    }
		    
		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
		}
		return ptid;
	}
	
	
	// returns an array of the form [lat,lon] representing the location
	// of the place with place_id
	public double[] getLocation(int place_id)
	{
		double[] loc = new double[2];
		try {
			String query = "select latitude, longitude from Places where " +
					"place_id = " + place_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	loc[0] = rs.getInt("latitude");
		    	loc[1] = rs.getInt("longitude");
		    }
		    
		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
		}
		
		return loc;
	}
	
	public double[] getUserLocation(int user_id)
	{
		double[] loc = new double[2];
		try {
			String query = "select latitude, longitude from Users where " +
					"user_id = " + user_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	loc[0] = rs.getInt("latitude");
		    	loc[1] = rs.getInt("longitude");
		    }
		    
		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
		}
		
		return loc;
	}
	
	public boolean userExists(int user_id)
	{
		try {
			String query = "select count(user_id) from Users where user_id="
					+ user_id;
			
			Statement st = _conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    
		    while (rs.next()) {
		    	int res = rs.getInt(1);
			    if(res == 1)
			    {
			    	return true;
			    }
			    else
			    {
			    	return false;
			    }
		    }

		    rs.close();
		    st.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.toString());
			return false;
		}
		
		return false;
		
	}
	
	public boolean isFriend(int user1, int user2)
	{
		int[] friends = getFriends(user1);
		for(int ii = 0; ii < friends.length; ii++)
		{
			if(friends[ii] == user2) return true;
		}
		return false;
	}
	
	/**
	 * Converts an ArrayList of Integers to an array of ints.
	 * @param list ArrayList of Integers
	 * @return an array of ints
	 */
	
	private int[] convertToPrimitives(ArrayList<Integer> list)
	{
		int[] res = new int[list.size()];
		for(int ii = 0; ii < res.length; ii++)
		{
			res[ii] = list.get(ii).intValue();
		}
		
		return res;
	}
	  
		/**
		 * Close the database connection.
		 * @throws SQLException
		 */
		public void closeDBConnection() {
			try {
				DBUtils.closeDBConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace(System.err);
			}
		}

}
