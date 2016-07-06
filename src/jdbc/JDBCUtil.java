package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.FirmwareVersion;
import beans.SimStatus;
import global.PropertiesUtil;

/**
 * This class is Utility for JDBC queries from
 * 
 * @author Yehuda Ginsburg
 *
 */
public class JDBCUtil {


	public JDBCUtil() {
	}

	/**
	 * Initializing Connection to QA MYSQL Database, by the URL and the
	 * user-password.
	 * 
	 * @return Connection implementing by MYSQL
	 */
	public Connection getConnection() {
		String PASSWORD = PropertiesUtil.getInstance().getProperty("PASSWORD");
		String USER = PropertiesUtil.getInstance().getProperty("USER");
		String MYSQL_JDBC_DRIVER = PropertiesUtil.getInstance().getProperty("MYSQL_JDBC_DRIVER");
		String MYSQL_PORT = PropertiesUtil.getInstance().getProperty("MYSQL_PORT");
		String DB_URL = PropertiesUtil.getInstance().getProperty("DB_URL");
		String URL = "jdbc:mysql://" + DB_URL + ":" + MYSQL_PORT;

		try {
			Class.forName(MYSQL_JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("The mysql.jdbc.driver dependency/jar not found");
			e.printStackTrace();
		}
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.err.println("Could not create a DB connection to " + DB_URL
					+ "  with User:" + USER + " and Password:" + PASSWORD
					+ "\n" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * JDBC-SQL Query get the status of SIM's from the inventory_sim,
	 * inventory_simboard, inventory_simunit, usage_allocation and usage_plug
	 * tables, getting the SIM 'name' (like MOE:1:0), plug id, if Forced, if
	 * Allowed and whats the RSIM type (SIM/USIM)
	 * 
	 * @return list of SimStatus
	 */
	public List<SimStatus> getAllSimsStatus() {
		String QA_BOARD_SLOT = PropertiesUtil.getInstance().getProperty("QA_BOARD_SLOT");

		List<SimStatus> list = new ArrayList<SimStatus>();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT concat_ws(':', isunit.name, isboard.slot, isim.offset) AS 'SIM Name' ");
		builder.append(", isim.allowed AS 'Allowed' ");
		builder.append(", ifnull(ua.plug_id, 'None') AS 'Plug ID' ");
		builder.append(", ifnull(up.id, 'No') AS 'Forced' ");
		builder.append(", isim.allowed AS 'Allowed' ");
		builder.append(", isim.is_gsm AS '2G'");
		builder.append(", isim.is_umts AS '3G'");
		builder.append(", isim.imsi AS 'IMSI'");
		builder.append(", isim.geo_location_id AS 'geo_location_id'");
		builder.append(" FROM persist.inventory_sim AS isim ");
		builder.append(" JOIN persist.inventory_simboard AS isboard ON isboard.id = isim.board_id ");
		builder.append(" JOIN persist.inventory_simunit AS isunit ON isboard.unit_id = isunit.id ");
		builder.append(" LEFT JOIN persist.usage_allocation AS ua ON ua.sim_id = isim.id ");
		builder.append(" LEFT JOIN persist.usage_plug AS up ON up.sim_force_id = isim.id ");
       builder.append("WHERE isboard.slot = "+QA_BOARD_SLOT+" ;");

		String simNameColumnName = "SIM Name";
		String plugIdColumnName = "Plug ID";
		String forcedColumnName = "Forced";
		String allowedColumnName = "Allowed";
		String usimColumnName = "3G"; 
		String simColumnName = "2G"; 
		String imsiColumnName = "IMSI";
		String geoLocationColumnName = "geo_location_id";

		
		String sql = builder.toString();
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					list.add(new SimStatus
								(resultset.getString(simNameColumnName), 
								 resultset.getString(plugIdColumnName), 
								 resultset.getString(forcedColumnName),
								 resultset.getString(imsiColumnName),
								 resultset.getInt(allowedColumnName), 
								 resultset.getInt(usimColumnName), 
								 resultset.getInt(simColumnName),				 
								 resultset.getInt(geoLocationColumnName)
								 
								 )
							);
				}
				if (!list.isEmpty())
					return list;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * Check for plug in allocations
	 * @param plugId
	 * @return SimStatus if plug allocated
	 */
	public SimStatus getPlugSimStatus(String plugId) {
		String QA_BOARD_SLOT = PropertiesUtil.getInstance().getProperty("QA_BOARD_SLOT");
		
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT concat_ws(':', isunit.name, isboard.slot, isim.offset) AS 'SIM Name' ");
		builder.append(", isim.allowed AS 'Allowed' ");
		builder.append(", ifnull(ua.plug_id, 'None') AS 'Plug ID' ");
		builder.append(", ifnull(up.id, 'No') AS 'Forced' ");
		builder.append(", isim.allowed AS 'Allowed' ");
		builder.append(", isim.is_gsm AS '2G'");
		builder.append(", isim.is_umts AS '3G'");
		builder.append(", isim.imsi AS 'IMSI'");
		builder.append(", isim.geo_location_id AS 'geo_location_id'");
		builder.append(" FROM persist.inventory_sim AS isim ");
		builder.append(" JOIN persist.inventory_simboard AS isboard ON isboard.id = isim.board_id ");
		builder.append(" JOIN persist.inventory_simunit AS isunit ON isboard.unit_id = isunit.id ");
		builder.append(" LEFT JOIN persist.usage_allocation AS ua ON ua.sim_id = isim.id ");
		builder.append(" LEFT JOIN persist.usage_plug AS up ON up.sim_force_id = isim.id ");
		builder.append(" WHERE isboard.slot = " + QA_BOARD_SLOT);
		builder.append(" AND ua.plug_id = '" + plugId + "' ;");
		
		String simNameColumnName = "SIM Name";
		String plugIdColumnName = "Plug ID";
		String forcedColumnName = "Forced";
		String allowedColumnName = "Allowed";
		String usimColumnName = "3G"; 
		String simColumnName = "2G"; 
		String imsiColumnName = "IMSI";
		String geoLocationColumnName = "geo_location_id";
		
		String sql = builder.toString();
		
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return new SimStatus
							(resultset.getString(simNameColumnName), 
							 resultset.getString(plugIdColumnName), 
							 resultset.getString(forcedColumnName),
							 resultset.getString(imsiColumnName),
							 resultset.getInt(allowedColumnName), 
							 resultset.getInt(usimColumnName), 
							 resultset.getInt(simColumnName),				 
							 resultset.getInt(geoLocationColumnName)
								 );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * JDBC-SQL Query get all allocated plug id's from DB
	 * 
	 * @return list of plug id's allocated
	 */
	public List<String> getAllocatedPlug() {
		List<String> plugIds = new ArrayList<String>();

		String sql = "SELECT plug_id FROM persist.usage_allocation";
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					plugIds.add(resultset.getString("plug_id"));
				}
				return plugIds;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * JDBC-SQL Query to get all forced plug's from usage_plug table
	 * 
	 * @return String List of forced plugId
	 */
	public List<String> getAllForcedPlugs() {
		String sql = "SELECT id FROM persist.usage_plug WHERE sim_force_id IS NOT NULL";

		List<String> plugIds = new ArrayList<String>();

		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					plugIds.add(resultset.getString("id"));
				}

				return !plugIds.isEmpty() ? plugIds : null;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Unforce All plug allocations from usage_plug table
	 */
	public void unforceAllAllocations() {
		String sql = "UPDATE persist.usage_plug SET sim_force_id = NULL";

		try (Connection connection = getConnection()) {
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
	 * Get all firmware version url's
	 * @return List(String) of each url
	 */
	private List<String> getAllFwUrl() {
		String sql = "SELECT fw_url FROM persist.inventory_firmwareversion;";

		List<String> allFwUrl = new ArrayList<String>();

		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					allFwUrl.add(resultset.getString("fw_url"));
				}

				return !allFwUrl.isEmpty() ? allFwUrl : null;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check if firmware-version-url exist
	 * @param fwUrl
	 * @return true if exist
	 */
	public boolean isFwUrlExist(String fwUrl) {
		List<String> allFwUrl = getAllFwUrl();
		if (allFwUrl == null)
			return false;
		for (String s : allFwUrl) {
			if (s.toLowerCase().contains(fwUrl.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Parse all firmware with the url, config and version as FirmwareVersion objects and add them into list
	 * @return List<FirmwareVersion>
	 */
	public List<FirmwareVersion> getFirmwareVersion() {
		List<FirmwareVersion> firmwareVersions = new ArrayList<FirmwareVersion>();

		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT fwUrl.fw_url, fwConfig.fw_update_config, fwVersion.fw_update_version ");
		builder.append(" FROM persist.inventory_firmwareversion AS fwUrl ");
		builder.append(" JOIN persist.inventory_fwconfig AS fwConfig ");
		builder.append(" ON fwUrl.fw_update_config_id = fwConfig.id ");
		builder.append(" JOIN persist.inventory_fwversion AS fwVersion ");
		builder.append(" ON fwUrl.fw_update_version_id = fwVersion.id ");

		String sql = builder.toString();

		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					String fwConfig = resultset.getString("fw_update_config");
					String fwVersion = resultset.getString("fw_update_version");
					String fwUrl = resultset.getString("fw_url");
					firmwareVersions.add(new FirmwareVersion(fwConfig,
							fwVersion, fwUrl));
				}

				return !firmwareVersions.isEmpty() ? firmwareVersions : null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Delete All firmware version if one of the foreign keys (config/version) is null, 
	 * this is a workaround because when trying to delete that from the Persist GUI it throws Error 500
	 */
	public void deleteAllFirmwareVersionsWhereIsNull() {

		String select = "SELECT id FROM persist.inventory_firmwareversion WHERE `fw_update_config_id` IS NULL;";
		try (Connection connection = getConnection()) {
			List<Integer> ids = new ArrayList<Integer>();
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(select);
				while (resultset.next()) {
					ids.add(resultset.getInt("id"));
				}
			} catch (SQLException e) {
				throw e;
			}
			
			if (!ids.isEmpty()) {
				for (Integer i : ids) {
					try (Statement statement = connection.createStatement()) {
						String delete = "DELETE FROM `persist`.`inventory_firmwareversion` WHERE id = " + i + ";";
						statement.execute(delete);
					} catch (SQLException e) {
						throw e;
					}
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

		
	public Boolean createAllocationInDB(String plugNu, String simId) {
		
		String insertQuery = String.format("INSERT INTO persist.usage_allocation (plug_id, sim_id,created_at, last_contact_at) VALUES (%s,%s,'0000-00-00 00:00:00','0000-00-00 00:00:00')", plugNu, simId);
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				int response = statement.executeUpdate(insertQuery);
				System.out.println("response +" + response);
				if (response == 1)
				{
					return true;
				}
				
				else 
				{
					System.out.println(response);
					return false;
				}
				
			}			
			catch (SQLException e) {
			throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Map<String, Object>getSimByImsi(String imsi) {
		
		Map<String, Object> sims = new HashMap<String, Object>();

		String sql = "SELECT * FROM persist.inventory_sim WHERE inventory_sim.imsi = " + imsi + " " + "AND datetime_detached IS NULL";
		System.out.println("SQL: "  + sql);
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					sims.put("imsi", resultset.getString("imsi"));
					sims.put("geo_location", resultset.getString("geo_location_id"));
					sims.put("is_gsm", resultset.getInt("is_gsm"));
					sims.put("is_umts", resultset.getInt("is_umts"));
					
					
				}
				return sims;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	
}

		
