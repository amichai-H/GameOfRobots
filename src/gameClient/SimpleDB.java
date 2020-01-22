	package gameClient;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.Date;
	import java.util.HashMap;

	/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
		public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		public static final String jdbcUser = "student";
		public static final String jdbcUserPassword = "OOP2020student";
	//	private static String jdbcAdmin = "doadmin";
		//private static String jdbcAdminPassword = "guvvx0m0g955vix3";
		private static int[][] results = {{0, 145, 290}, {1, 450, 580}, {3, 750, 580}, {5, 570, 500}, {9, 510, 580}, {11, 1050, 580}, {13, 310, 580}, {16, 235, 290}, {19, 250, 580}, {20, 200, 290}, {23, 1000, 1140}};

		/**
		 * Simple main for demonstrating the use of the Data-base
		 *
		 * @param args
		 */
		public static void main(String[] args) {
			//System.out.println(getRank(315149500, 0));
			//System.out.println(allId(0));
		//writeRes(0,0,124,5);
//		System.out.println(getLevel(315149500));
//			int id1 = 315149500;  // "dummy existing ID
//			int level = 0;
//			allUsers();
//			printLog();
//			String kml = getKML(id1,level);
//			System.out.println("***** KML file example: ******");
//			System.out.println(kml);
		}

		/**
		 * simply prints all the games as played by the users (in the database).
		 */
		public static void printLog() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection =
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT * FROM Logs;";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);

				while (resultSet.next()) {
					System.out.println("Id: " + resultSet.getInt("UserID") + "," + resultSet.getInt("levelID") + "," + resultSet.getInt("moves") + "," + resultSet.getDate("time"));
				}
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		/**
		 * this function returns the KML string as stored in the database (userID, level);
		 *
		 * @param id
		 * @param level
		 * @return
		 */
		public static String getKML(int id, int level) {
			String ans = null;
			String allCustomersQuery = "SELECT * FROM Users where userID=" + id + ";";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection =
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				if (resultSet != null && resultSet.next()) {
					ans = resultSet.getString("kml_" + level);
				}
			} catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return ans;
		}

		public static int allUsers() {
			int ans = 0;
			String allCustomersQuery = "SELECT * FROM Users;";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection =
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				while (resultSet.next()) {
					System.out.println("Id: " + resultSet.getInt("UserID"));
					ans++;
				}
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return ans;
		}
//		static boolean writeRes(int id, int level, int moves, int grade) {
//			boolean ans = false;
//			long now = (new Date()).getTime();
//			String query = " insert into Logs (UserID, LevelID, time, moves, score) values (" + id + ", " + level + ", CURRENT_TIMESTAMP," + moves + " , " + grade + ")";
//
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcAdmin, jdbcAdminPassword);
//				Statement statement = connection.createStatement();
//				statement.executeUpdate(query);
//			} catch (SQLException var10) {
//				System.out.println("SQLException: " + var10.getMessage());
//				System.out.println("Vendor Error: " + var10.getErrorCode());
//			} catch (ClassNotFoundException var11) {
//				var11.printStackTrace();
//			}
//
//			return ans;
//		}

		public static String getRank(int id, int level) {
			HashMap<Integer, Boolean> myHash = new HashMap<>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT UserID, score, moves FROM Logs WHERE score>=" + results[level][1] + " AND moves<= " + results[level][2] + " AND UserID<>0 AND  UserID<> 999 AND levelID=" + results[level][0] + " ORDER BY score DESC; ";
//					String allCustomersQuery = "select distinct on (UserID) * from Logs order by UserID, score desc;";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				int count = 0;
				int myF = 0;
				String myscore = "";
				boolean first = true;
				while (resultSet.next()) {
					if (resultSet.getInt("UserID") == id && first) {
						myF = count;
						myscore = "Id: " + resultSet.getInt("UserID") + " score: " + resultSet.getInt("score") + " moves: " + resultSet.getInt("moves");
						first = false;
					}

					System.out.println("Id: " + resultSet.getInt("UserID")+ " score: "+resultSet.getInt("score")+" moves: "+ resultSet.getInt("moves"));
					if (myHash.get(resultSet.getInt("UserID")) == null) {
						myHash.put(resultSet.getInt("UserID"), true);
						count++;
					}
				}
				return "Rank of ID - "+id+" is: " + myF + " From  " + count + " " + myscore;
			} catch (Exception e) {
				return e.toString();
			}

			//return null;
		}
		public static String allId(int id) {
			try {
				int counter = 0;
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT * FROM Logs WHERE UserID=" + id +"  ; ";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				while (resultSet.next()) {
					counter++;
					System.out.println(resultSet.getInt("UserID") + " score : " + resultSet.getInt("score") + " moves:  " + resultSet.getInt("moves"));


				}
				return "ID: " + id + " play " + counter + " times.";

			} catch (Exception e){
				return e.toString();
			}
		}

		/**
		 * this method return the current level that the id is on
		 * @param id -  of the player
		 * @return
		 */

		public static String getLevel(int id) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				boolean b = true;
				int coun = 0;
				int i = 0;
				while (b) {
					String allCustomersQuery = "SELECT * FROM Logs WHERE UserID=" + id + " AND score >=" + results[i][1] + " AND levelID=" + results[i][0] + " AND moves<=" + results[i][2] + "  ; ";
					ResultSet resultSet = statement.executeQuery(allCustomersQuery);
					while (resultSet.next()) {
						if (resultSet.getInt("UserID") == id) {
							coun = results[i][0];
							if (results[i][0] == 23) {
								System.out.println(resultSet.getInt("UserID") + " score : " + resultSet.getInt("score") + " moves:  " + resultSet.getInt("moves"));
							}
						} else {
							b = false;
						}
					}
					if (i < results.length - 1) {
						i++;
					} else {
						b = false;
					}

				}
				return " Your("+id+") level is: " +coun + "";

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		public static String getCounter(int id) {
			try {
				int counter = 0;
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
					String allCustomersQuery = "SELECT * FROM Logs WHERE UserID=" + id +"  ; ";
					ResultSet resultSet = statement.executeQuery(allCustomersQuery);
					while (resultSet.next()) {
						counter++;

					}
					return "ID: " + id + " play " + counter + " times.";

			} catch (Exception e){
				return e.toString();
			}
		}
	}
		
