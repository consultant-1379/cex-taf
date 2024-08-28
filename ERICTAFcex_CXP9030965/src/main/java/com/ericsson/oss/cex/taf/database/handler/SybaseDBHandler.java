package com.ericsson.oss.cex.taf.database.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to interact with sybase db
 * @author xananan
 *
 */
public class SybaseDBHandler {
	
	private Connection conn;
	
	private static final String DRIVER = "com.sybase.jdbc3.jdbc.SybDriver";
	
	private static final String URL = "jdbc:sybase:Tds:";

	public SybaseDBHandler(final String hostName, final String port,
			final String dbName, final String userName, final String password)
			throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		conn = DriverManager.getConnection(URL + hostName + ":" + port + "/"
				+ dbName, userName, password);
	}
	
	/** 
	 * @param queryString
	 * @return result of sql query {@link ResultSet}
	 * @throws SQLException
	 */
	public ResultSet executeQuery(final String queryString) throws SQLException {
		final Statement stmt = this.conn.createStatement();
		return stmt.executeQuery(queryString);
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if(conn != null) {
			conn.close();
		}
	}
	
	public static void main(String[] args) {
		SybaseDBHandler sybaseDBHandler = null;
		try {
			sybaseDBHandler = new SybaseDBHandler("atvts509.athtem.eei.ericsson.se", "5025", "cexdb", "sa", "sybase11");
			ResultSet result = sybaseDBHandler.executeQuery("select * from FDN where ID=3874");
			while(result.next()) {
				System.out.println(result.getInt(1));
			}
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		} finally{
			try {
				if(sybaseDBHandler != null){
					sybaseDBHandler.closeConnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
