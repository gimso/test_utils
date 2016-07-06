package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FotaQueries {
	JDBCUtil jdbc = new JDBCUtil();

	/**
	 * Get from Fota DB the current version registered under Device table
	 * 
	 * @param plugId
	 * @return
	 */
	public String getCurrentVersionByDeviceId(String plugId) {
		String sql = "SELECT current_package_version FROM fota.FotaGUI_device where id='" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("current_package_version");
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
	 * Change current version in db, per plug id.
	 */
	public void updateCurrentVersionByPlugId(String version, String plugId) {
		String sql = "UPDATE fota.FotaGUI_device SET current_package_version='" + version + "' WHERE id='" + plugId + "'";
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
	 * Deletes from Fota DB the current version registered under Device table
	 * 
	 * @param plugId
	 */
	public void deleteCurrentVersionByDeviceId(String plugId) {
		String sql = "UPDATE fota.FotaGUI_device SET current_package_version = '' WHERE id = '" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the package version by the package id from package table
	 * 
	 * @param packageId
	 * @return
	 */
	public String getPendingVersionNameById(String deviceId) {
		String packageId = getPendingVersionIdByDeviceId(deviceId);
		String sql = "SELECT version FROM fota.FotaGUI_package where id='" + packageId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("version");
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
	 * Get the existing pending version id from package table in fota db
	 * 
	 * @param
	 * @return pending version id
	 */
	public String getPendingVersionIdByDeviceId(String deviceId) {
		String sql = "SELECT pending_package_id FROM fota.FotaGUI_device where id='" + deviceId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("pending_package_id");
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
	 * Get the existing pending version id from package table in fota db
	 * @param version
	 * @return id
	 */
	public String getPackageId(String version) {
		String sql = "SELECT id FROM fota.FotaGUI_package where version ='" + version + "'";
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

	/**
	 * Add pending version to db, per plug id.
	 */
	public void insertPendingVersionByPlugId(String version, String plugId) {
		String pendingPackageId = getPackageId(version);
		String sql = "UPDATE fota.FotaGUI_device SET pending_package_id=" + pendingPackageId + " WHERE id='" + plugId + "'";
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
	 * Deletes from Fota DB the pending version registered under Device table
	 * 
	 * @param plugId
	 */
	public void deletePendingVersionByDeviceId(String plugId) {
		String sql = "UPDATE fota.FotaGUI_device SET pending_package_id = null WHERE id = '" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
				System.out.println(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get from Fota DB the last_state registered under Device table
	 * @param plugId
	 * @return
	 */
	public String getLastStateByDeviceId(String plugId) {
		String sql = "SELECT last_state FROM fota.FotaGUI_device where id='" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet resultset = statement.executeQuery(sql);
				while (resultset.next()) {
					return resultset.getString("last_state");
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
	 * Deletes from Fota DB the last_state from under Device table
	 * @param plugId
	 */
	public void deleteLastStateByDeviceId(String plugId) {
		String sql = "UPDATE fota.FotaGUI_device SET last_state= '' WHERE id = '" + plugId + "'";
		try (Connection connection = jdbc.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
