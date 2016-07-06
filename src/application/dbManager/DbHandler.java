package application.dbManager;

import java.sql.SQLException;

public interface DbHandler {

	/**
	 * method connects to db
	 * @throws SQLException 
	 */
	public void connectToDB();
	
	
	/**
	 * method closes connection to db
	 * @throws SQLException 
	 */
	public void closeDB() throws SQLException;
	
	
	/**
	 * r
	* @param table - The table of trip
	 * @param column - column to compare
	 * @param toFind - value
	 * @return whether the value exist in db
	 */
	public boolean isExist(String table, String column,String toFind);
	
	
	/**
	 * remove specific trip from persist
	 * @param table - The table of trip
	 * @param coloumnToCompare - column to compare
	 * @param deviceID - value
	 */
	public void removeTrip(String table,String coloumnToCompare,String deviceID) ;
	
	
	/**
	 * 
	 * @param homeNumber
	 * @return user password
	 */
	public String getUserPassword(String homeNumber);
	
	
	/**
	 * change access number state 
	 * @param isAllowed
	 */
	public void changeAccessNumberState(String table,String accessNumber,boolean isAllowed);
	
	
	/**
	 * change Hsim state 
	 * @param setActive
	 */
	public void changeHsimState(String plugID,boolean setActive);

}
