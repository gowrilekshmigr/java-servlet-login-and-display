import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginDao {

	private static Connection connection = null;

	public static boolean validate(String name, String pass) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		String dbName = "COMPOSITEAPPS?useSSL=true";
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String userName = "loyalist";
		String password = "*TeamB1*";

		String url = "jdbc:sqlserver://khupragenics.database.windows.net:1433;"
				+ "database=khupragenics-db;user=loyalist@khupragenics;" + "password=" + password + ";encrypt=true;"
				+ "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		System.out.println(url);

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url);
			System.out.println(url + dbName + userName + password);

			pst = conn.prepareStatement("SELECT * FROM PATIENTS WHERE USERNAME=? and PASSWORD=?");
			pst.setString(1, name);
			pst.setString(2, pass);

			rs = pst.executeQuery();
			status = rs.next();
			System.out.println(status);
		}

		catch (Exception e) {
			System.out.println(e);
		}

		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return status;
	}

	// Create permanent connection
	public static Connection getConnection() {
		if (connection != null)
			return connection;
		else {
			try {

				String dbName = "COMPOSITEAPPS?useSSL=true";
				String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				String userName = "loyalist";
				String password = "*TeamB1*";

				String url = "jdbc:sqlserver://khupragenics.database.windows.net:1433;"
						+ "database=khupragenics-db;user=loyalist@khupragenics;" + "password=" + password
						+ ";encrypt=true;"
						+ "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

				System.out.println(url);
				Class.forName(driver);

				connection = DriverManager.getConnection(url);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return connection;
		}
	}

	public static ArrayList<Employee> getAllEmployees() {

		connection = LoginDao.getConnection();
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		Statement statement = null;
		ResultSet rs = null;

		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT TOP 100 * FROM PATIENTS");

			while (rs.next()) {
				Employee empl = new Employee();
				empl.setEmployeeID(rs.getInt("FILE_ID"));
				empl.setName(rs.getString("NAME"));
				empl.setPhone(rs.getString("PHONE_NUMBER"));
				empl.setSupervisor(rs.getString("ASSIGNED_DOCTOR"));
				empl.setUsername(rs.getString("USERNAME"));
				empl.setPassword(rs.getString("PASSWORD"));
				employeeList.add(empl);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return employeeList;
	}

	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		LoginDao login = new LoginDao();
		login.validate("driley0", "XBXA3XgR");
		System.out.println(login.getAllEmployees());
	}
}
