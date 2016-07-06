package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import global.PropertiesUtil;
import global.TimeAndDateConvertor;
/**
 * This class is designated RestAPI DB queries for Telemetry project use
 * @author Dana
 *
 */
public class TelemetryQueries {
	JDBCUtil jdbc = new JDBCUtil();
	private static final String COOKIE = "63o32329jvzsf6042f2p439yk7cbkty5";
	private static final String OUTGOING_ACCESS_NUMBER = "1900000000";
	private static final String GCM_KEY = "APA91bG5tGEgpZieDbHy8yKDhOiBH6yJSQ9VJpg7TDLsn6vOkK5V_1q6cczV5-z3lZAnd9d1KpxvxrhCxFhtklyBHRwOLVcgk51Hg69q7GTo0nbnCh7Pp0ny7iLwGdRttk47ml_Uvgp";
	public static final String PLAN_NAME = "DailyPlanA";
	private String date;
	
	/**
	 * Gets cookie from user
	 * @param userName
	 * @return cookie 
	 */
	public String getCookieFromUser(String userName) {
		String sql = "SELECT cookie FROM persist.usage_user WHERE name= '" + userName + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("cookie");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Injects cookie into user
	 * @param userId
	 */
	public void injectCookieIntoUser(int userId) {
		String sql = "UPDATE persist.usage_user SET cookie='" + COOKIE + "' WHERE id='" + userId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets user id
	 * Used for getTripIdByUserId()
	 * @param userName
	 * @return user id
	 */
	public int getUserId(String userName) {
		String sql = "SELECT id FROM persist.usage_user WHERE name= '" + userName + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Gets trip id by user name. 
	 * If user doesn't exist- prints error.
	 * @param userName
	 * @return trip id
	 */
	public int getTripIdByUserName(String userName) {
		int userId = getUserId(userName);
		if (userId == -1) {
			System.err.println("User doesn't exist");
		} else {
			String sql = "SELECT id FROM persist.trip_trip WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("id");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	
	/**
	 * Delete trip by trip id, for trip create test
	 * @param tripId
	 */
	public void deleteTripById(int tripId){
		
		String sql = "DELETE FROM persist.trip_trip WHERE id = " + tripId;
		
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The billing marker is a the date according to which the data usage sent from the app is updated.
	 * the date must be earlier than the timestamp sent within the json.
	 * when a trip is created, the billing marker = null, therefore must be set
	 */
	public void updateBillingMarkerDate(int tripId){
		date = TimeAndDateConvertor.convertDateToCloudTripString(new Date());
		String sql = "UPDATE persist.trip_trip SET billing_time_marker = '" + date + "' WHERE id = " + tripId;
		try(Connection connection = jdbc.getConnection()){
			try (Statement statement = connection.createStatement()){
				statement.executeUpdate(sql);
				System.out.println(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Gets existing allocation
	 * Used for createAllocation()
	 * @param plugId
	 * @return allocation id
	 */
	public int getExistingAllocation(String plugId) {
		String sql = "SELECT id FROM persist.usage_allocation WHERE plug_id= '" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Informs that allocation doesn't exist.
	 * Creates dummy allocation with auto-generated id using archived allocations. 
	 * @param plugId
	 * @return allocation id
	 */
	private int createAllocation(String plugId) {
		System.err.println("Allocation doesn't exist, creating new");
		String sql = "INSERT INTO persist.usage_allocation (plug_id, sim_id, created_at,last_contact_at,outgoing_access_number_id,geolocation_id,dsim_mnc,plug_battery, plug_reception) "
				+ "VALUES( '" + plugId + "', '" +  getSimId()
				+ "', '2016-01-10 09:50:35', '2016-01-19 05:51:05'," + getAccessNumId() + ", '425', '1', '2', '72')";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = statement.getGeneratedKeys();
				while (rs.next()) {
					System.out.println("generated key: " + rs.getInt(1));
					return rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	/**
	 * Checks if there is existing allocation
	 * If allocation doesn't exist it creates new allocation using the same plug id and sets the new allocation id into trip.
	 * If allocation exists
	 */
	public void injectAllocationIntoTrip() {
		int allocationId = getExistingAllocation("000010002024");
		int tripId = getTripIdByUserName(PropertiesUtil.getInstance().getProperty("USAGE_USER"));
		if (allocationId == -1) {
			allocationId = createAllocation("000010002024");
		}
			String sql = "UPDATE persist.trip_trip SET allocation_id = '" + allocationId
					+ "', overall_allocations = '1' WHERE id = '" + tripId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Gets the sim id from allocation: used for getImsi()
	 * @param allocationId
	 * @return sim id
	 */
	private int getSimIdFromAllocation(int allocationId) {
		String sql = "SELECT sim_id FROM persist.usage_allocation WHERE id = '" + allocationId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("sim_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Get the imsi from the sim id of a specific allocation
	 * @param allocationId
	 * @return imsi
	 */
	 public int getImsi(int allocationId) {
		int simId = getSimIdFromAllocation(allocationId);
		String sql = "SELECT imsi FROM persist.inventory_sim WHERE id = '" + simId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("imsi");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	 
	 /**
	  * Injects an imsi into sim table (persist.inventory_sim)
	  */
	 public void injectImsiIntoSim(String imsi){
		 String sql = "UPDATE persist.inventory_sim SET imsi = " + imsi + " WHERE id = " + getSimId();
		 try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	
	 /**
	  * Gets user's password, for verification code test
	  * @param userId
	  * @return user's password
	  */
	public String getPasswordByUserId(int userId){
		
		String sql = "SELECT password FROM persist.usage_user WHERE id = '" + userId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("password");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Setting ALL the access numbers to "allow=false" status
	 */
	public void disallowAllAccessNumbers() {
		String sql = "UPDATE `persist`.`inventory_accessnumber` SET allowed = 0 WHERE id > 0";
		try(Connection connection = jdbc.getConnection()){
			try (Statement statement = connection.createStatement()){
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * Setting ALL the access numbers back to "allow=true" status
	 */
	public void allowAllAccessNumbers() {
		String sql = "UPDATE `persist`.`inventory_accessnumber` SET allowed = 1 WHERE id > 0";
		try(Connection connection = jdbc.getConnection()){
			try (Statement statement = connection.createStatement()){
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Inject a hard-coded GCM to use for GcmTest
	 */
	public void injectGcmKeyIntoUser(){
		int userId = getUserId(PropertiesUtil.getInstance().getProperty("USAGE_USER"));
		String sql = "UPDATE persist.usage_user SET gcm_key='" + GCM_KEY + "' WHERE id = " + userId;
		try(Connection connection = jdbc.getConnection()){
			try (Statement statement = connection.createStatement()){
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Delete GCM from user
	 */
	public void deleteGcmKeyByUser(){
		int userId = getUserId(PropertiesUtil.getInstance().getProperty("USAGE_USER"));
		String sql = "UPDATE persist.usage_user SET gcm_key='" + " " + "' WHERE id = " + userId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get simgroup id, to be used in getSimId method
	 */
	private int getSimGroupId(String simGroup){
		//get the sim group id
		String sql = "SELECT id FROM persist.inventory_simgroup WHERE name='" + simGroup + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Get sim id
	 */
	public int getSimId(){
		//get the sim group id
		String sql = "SELECT id FROM persist.inventory_sim WHERE group_id = " + getSimGroupId("Unassigned");
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;	
	}
	
	
	/**
	 * Get plan id by plan name from plans table
	 * Used for deleteTripPlanHistory()
	 * @param plan name
	 * @return plan id
	 */
	public int getPlanId(String planName) {
		String sql = "SELECT id FROM persist.usage_plan WHERE name= '" + planName + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	/**
	 * Delete the tripPlanHistory of a certain plan, by plan id
	 */
	public void deleteTripPlanHistory(int planId) {
		String sql = "DELETE FROM persist.trip_tripplanhistory WHERE plan_id = " + planId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete allocation
	 * @param allocationId
	 */
	public void deleteAllocation(int allocationId){
		String sql = "DELETE FROM persist.usage_allocation WHERE id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the Rsim number from telephonyallocation table
	 * @param allocationId
	 * @return Rsim
	 */
	public String getRsimNumber(int allocationId){
		String sql = "SELECT in_rsim_number FROM persist.usage_telephonyallocation WHERE allocation_id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("in_rsim_number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the incoming access number from telephonyallocation table
	 * @param allocationId
	 * @return incoming access number
	 */
	public String getIncomingNum(int allocationId){
		String sql = "SELECT in_did FROM persist.usage_telephonyallocation WHERE allocation_id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("in_did");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the outgoing access number from telephonyallocation table
	 * @param allocationId
	 * @return outgoing access number
	 */
	public String getOutgoingDidNum(int allocationId){
		String sql = "SELECT out_did FROM persist.usage_telephonyallocation WHERE allocation_id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("out_did");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the destNumber from telephonyallocation table
	 * @param allocationId
	 * @return incoming access number
	 */
	public String getDestinationNum(int allocationId){
		String sql = "SELECT dest_number FROM persist.usage_telephonyallocation WHERE allocation_id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("dest_number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the outgoing access number id from allocation table, used for getOutgoingNum()
	 * @param allocationId
	 * @return out-access number id
	 */
	public int getOutAccessNumIdByAllocation(int allocationId){
		String sql = "SELECT outgoing_access_number_id FROM persist.usage_allocation WHERE id = " + allocationId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getInt("outgoing_access_number_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Gets the outgoing access number from accessnumber table by allocation id 
	 * @param allocationId
	 * @return
	 */
	public String getOutgoingNum(int allocationId){
		int outAccessNumId = getOutAccessNumIdByAllocation(allocationId);
		String sql = "SELECT number FROM persist.inventory_accessnumber WHERE id = " + outAccessNumId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * Gets a specific access number id from accessnumber table
	 * @return Outgoing access number id
	 */
	public String getAccessNumId(){
		String sql = "SELECT id FROM persist.inventory_accessnumber WHERE number = " + OUTGOING_ACCESS_NUMBER;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getDigestFromSim(int allocationId){
		int simId = getSimIdFromAllocation(allocationId);
		String sql = "SELECT digest FROM persist.inventory_sim WHERE id = " + simId;
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("digest");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
