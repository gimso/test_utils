package application.dbManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import application.config.ConfigurationManager;

/**
 * This class is Utility for JDBC queries from cloud.
 * @author Yehuda Ginsburg
 * @author Tamir Sagi
 * 
 *
 */
public class JDBCUtil implements DbHandler {

	public static final String DB_PERSIST_TRIP = "persist.trip_trip";
	public static final String DB_PERSIST_PLUG = "persist.usage_plug";
	public static final String DB_PERSIST_ACCESS_NUMBER = "persist.inventory_accessnumber";
	public static final String DB_PERSIST_TRIP_COLUMN_PLUG_ID = "plug_id";
	
	private static JDBCUtil jdbcManager;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private static final String key_USER = "USER";
	private static final String key_PASSWORD = "PASSWORD";
	private static final String key_MYSQL_JDBC_DRIVER = "MYSQL_JDBC_DRIVER";
	private static final String key_DB_URL = "DB_URL";
	private static final String key_DB_PORT = "MYSQL_PORT";
	

	private JDBCUtil() {
		connectToDB();
	}
	
	
	public static JDBCUtil getDbManager(){
		if(jdbcManager ==null)
			jdbcManager = new JDBCUtil();
		return jdbcManager;
	}

	
	/**
	 * Initializing Connection to QA MYSQL Database, by the URL and the
	 * user-password.
	 * 
	 * @return Connection implementing by MYSQL
	 */
	@Override
	public void connectToDB() {
		String user = ConfigurationManager.get(key_USER);
		String password = ConfigurationManager.get(key_PASSWORD);
		String mysql_jdbc_driver = ConfigurationManager.get(key_MYSQL_JDBC_DRIVER);
		String mysql_port = ConfigurationManager.get(key_DB_PORT);
		String db_url = ConfigurationManager.get(key_DB_URL);
		String url = "jdbc:mysql://" + db_url + ":" + mysql_port;
		try {
			Class.forName(mysql_jdbc_driver).newInstance();
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not create a DB connection to " + db_url
					+ "  with User:" + user + " and Password:" + password
					+ "\n" + e.getMessage());
		} catch (InstantiationException e) {
			System.err.println("Could not create a DB connection " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("Could not create a DB connection " + e.getMessage());
		}
	}

	
	
	/**
	 * Get USer PAssword by phone number
	 */
	@Override
	public String getUserPassword(String homeNumber) {
		if(connection == null)
			connectToDB();
		String sql = "SELECT password FROM persist.usage_user WHERE home_number = ?";
		try{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, homeNumber);
			ResultSet resultset = statement.executeQuery();
			if (resultset.next()) {
				return resultset.getString("password");
			}
			return "";

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	
	
	
	@Override
	public void closeDB() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}


	@Override
	public boolean isExist(String table, String column,String toFind) {
		if(connection == null)
			connectToDB();
		String query = "SELECT * FROM " + table + " WHERE " + column + " = " + toFind;
		try{
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(query);
			return resultSet.next();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}


	@Override
	public void removeTrip(String table,String coloumnToCompare,String deviceID) {
		if(isExist(table,coloumnToCompare, deviceID)){
			String query = "DELETE FROM " + table + " WHERE " + coloumnToCompare + " = " + deviceID;
			try{
				PreparedStatement p_Statement = (PreparedStatement)connection.prepareCall(query);
				p_Statement.execute();
				System.out.println("JDBC:DELETED");
			}catch (SQLException e) {
				System.err.println("removeTrip JDBC " + e.getMessage());
		}
		}
	}


	
	@Override
	public void changeAccessNumberState(String table,String accessNumber,boolean isAllowed) {
		if(connection == null)
			connectToDB();
		String query = "UPDATE " + table +" SET allowed = ? WHERE number = " + accessNumber;
	    PreparedStatement preparedStmt;
		try {
			preparedStmt = connection.prepareStatement(query);
			int allow = (isAllowed) ? 1 : 0;
			preparedStmt.setInt(1, allow);
			preparedStmt.executeUpdate();
			 System.out.println("changeAccessNumberState DB - changeAccessNumberState done set to : " + isAllowed);
		} catch (SQLException e) {
			System.err.println("changeAccessNumberState JDBC " + e.getMessage());
		}
		
	}


	@Override
	public void changeHsimState(String plugID,boolean setActive) {
		if(connection == null)
			connectToDB();
		String query = "UPDATE " + DB_PERSIST_PLUG +" SET use_HSIM_in_home_country = ? WHERE id = " + plugID;
		PreparedStatement preparedStmt;
		try {
			preparedStmt = connection.prepareStatement(query);
			int allow = (setActive) ? 1 : 0;
			preparedStmt.setInt(1, allow);
			preparedStmt.executeUpdate();
			System.out.println("changeHsimState DB - changeAccessNumberState done set to : " + setActive);
		} catch (SQLException e) {
			System.err.println("changeHsimState JDBC " + e.getMessage());
		}
	}
	

}