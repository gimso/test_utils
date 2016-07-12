package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsageQueries {
	JDBCUtil jdbc = new JDBCUtil();
	
	/**
	 * Gets plug id by timestamp that's saved under UsageGUI_datarecord table.
	 * @param userId
	 * @return
	 */
	public int getPlugIdByUserId(String userId) {
			String sql = "SELECT plug_id FROM UsageDB.UsageGUI_datarecord WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("timestamp");
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
	 * Gets usage by user_id
	 * @param userId
	 * @return
	 */
	public int getUsageByPlugId(String userId) {
			String sql = "SELECT usage FROM UsageDB.UsageGUI_datarecord WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("usage");
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
	 * Gets mcc by user_id
	 * @param userId
	 * @return
	 */
	public int getMccByPlugId(String userId) {
			String sql = "SELECT mcc FROM UsageDB.UsageGUI_datarecord WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("mcc");
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
	 * Gets mnc by user_id
	 * @param userId
	 * @return
	 */
	public int getMncByPlugId(String userId) {
			String sql = "SELECT mnc FROM UsageDB.UsageGUI_datarecord WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("mnc");
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
	 * Gets rsim_id (imsi) by userId
	 * @param userId
	 * @return
	 */
	public int getImsiByPlugId(String userId) {
			String sql = "SELECT rsim_id FROM UsageDB.UsageGUI_datarecord WHERE user_id= '" + userId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("rsim_id");
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
	 * Gets user_id by user name
	 * @param userName
	 * @return
	 */
	public int getUserIdByUserName(String userName) {
			String sql = "SELECT id FROM UsageDB.UsageGUI_enduser WHERE username= '" + userName + "'";
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
	 * Gets user_id by plug_id
	 * @param plugId
	 * @return
	 */
	public int getUserIdByPlugId(String plugId) {
		String sql = "SELECT user_id FROM UsageDB.UsageGUI_datarecord WHERE plug_id= '" + plugId + "'";
			try (Connection connection = jdbc.getConnection()) {
				try (Statement statement = connection.createStatement()) {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						return resultset.getInt("user_id");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return -1;
	}
}
