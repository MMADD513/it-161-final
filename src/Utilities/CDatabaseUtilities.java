// ----------------------------------------------------------------------
// Name: CDatabaseUtilities
// Abstract: All procedures that directly interact with the database
// ----------------------------------------------------------------------

// ----------------------------------------------------------------------
// Package
// ----------------------------------------------------------------------
package Utilities;


// ----------------------------------------------------------------------
// Imports
// ----------------------------------------------------------------------
import java.sql.*;						// All things SQL

import Utilities.CUserDataTypes.udtTeamType;
import net.ucanaccess.jdbc.DeleteResultSet;

public class CDatabaseUtilities
{
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Properties( never make public )
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	private static Connection m_conAdministrator = null;
	

	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// Name: OpenDatabaseConnectionMSAccessJRE7
	// Abstract: Open a connection to the database.  MS Access JRE7.
	//			JDK1.7 and JRE7.
	//
	// 			Must either have MS Access 2010 install or download and
	//			install "Microsoft Access Database Engine 2010" which can be
	//			found for free at:
	//
	//				https://www.microsoft.com/en-us/download/details.aspx?id=13255
	//
	//			Do not match to your operating system.  Download the version 
	//			(32 or 64 bit) that matches the version of your JRE.  
	// ----------------------------------------------------------------------
	public static boolean OpenDatabaseConnectionMSAccessJRE7( )
	{
		boolean blnResult = false;
		
		try
		{
			String strConnectionString = "";

			// Provider/driver (what type of database)
			strConnectionString = "jdbc:odbc:;Driver={Microsoft Access Driver (*.mdb, *.accdb)};";

			// Server name/port, IP address/port or path for file based databases like MS Access
			// System.getProperty( "user.dir" ) => Current working directory from where 
			// application was started
			strConnectionString += "DBQ=" + System.getProperty( "user.dir" ) 
								 + "\\Database\\AddressBook.accdb;";

			// User name (seems to ignore this and password)
			strConnectionString += "User ID=admin;";
			
			// Password which, by default, is empty (empty is not the same as null) for MS Access
			strConnectionString += "Password=;";
			
			// Open a connection to the database
			m_conAdministrator = DriverManager.getConnection( strConnectionString );
			
			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return blnResult;
	}

	
	// ----------------------------------------------------------------------
	// Name: OpenDatabaseConnectionMSAccessJRE8
	// Abstract: Open a connection to the database.  MS Access JRE8.
	//			The support for the JDBC-ODBC bridge has been removed from
	//			JDK1.8 and JRE8.
	//
	//			Use UCanAccess, an open-source JDBC driver, instead.
	//			Include the following jar files in your code:
	//				ucanaccess-2.0.7.jar
	//				jackcess-2.0.4.jar
	//					commons-lang-2.6.jar
	//					commons-logging-1.1.3.jar
	//				hsqldb.jar
	//
	//			To include those files select "Project / Properties / Java Build Path"
	//			from the menu.  Click on the "Libraries" tab.  Click "Add External JARs".
	//			Browse to the above jar files, which should be in a directory in your
	//			project (e.g. JDBC-to-MSAccess).  Select all five files and click
	//			"Open".  Click "OK".
	//
	//			You must also have either have MS Access 2010 install or download and
	//			install "Microsoft Access Database Engine 2010" which can be found 
	//			for free at:
	//
	//				https://www.microsoft.com/en-us/download/details.aspx?id=13255
	//
	//			Do not match to your operating system.  Download the version 
	//			(32 or 64 bit) that matches the version of your JRE.  
	// ----------------------------------------------------------------------
	public static boolean OpenDatabaseConnectionMSAccessJRE8( )
	{
		boolean blnResult = false;
		
		try
		{
			String strConnectionString = "";

			// Server name/port, IP address/port or path for file based databases like MS Access
			// System.getProperty( "user.dir" ) => Current working directory from where
			// application was started
			strConnectionString = "jdbc:ucanaccess://" + System.getProperty( "user.dir" ) 
								+ "\\Database\\AddressBook.accdb";

			// Open a connection to the database
			m_conAdministrator = DriverManager.getConnection( strConnectionString );
			
			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return blnResult;
	}

	
	// -------------------------------------------------------------------------
	// Name: OpenDatabaseConnectionSQLServer
	// Abstract: Open a connection to the database.  SQL Server.
	//	
	//	Download the drivers.  Search the internet for "Microsoft JDBC Driver 4.0 for SQL Server".  
	//	One the links should be to the Microsoft Site.  For example, 
	//		http://download.microsoft.com/download/0/2/A/02AAE597-3865-456C-AE7F-613F99F850A8/sqljdbc_4.0.2206.100_enu.exe
	//		It's a self-extracting zip file.  All it does is unzip the files.  It doesn't actually install anything.
	//	-Extract the files somewhere
	//	-Add a "Database/SQLServer" directory to your project
	//	-Copy the sqljdbc4.jar file from the enu directory into "Database/SQLServer" directory in your project.
	//		You'll need this file later.
	//	-Copy the sqljdbc_auth.dll file from enu\auth\x86 directory into the "Database/SQLServer" directory in your project.
	//		You'll need this file later.
	//	-You can delete all the other files.  You might want to save the original download file 
	//		in "Database/SQLServer" directory.		
	//	
	//	How to configure Eclipse to use the drivers for just your project:
	//	-Select/open your project in Eclipse
	//	-Choose Project/Properties from the menu
	//	-Select "Java Build Path" in the menu on the left
	//	-Click on the "Libraries" tab on the right
	//	-Click "Add External Jars..."
	//	-Browse to your "sqljdbc4.jar" file which should be in the "Database/SQLServer" folder 
	//		in your project.  If you move your project you'll need to remove the old
	//		jar file and re-add it because Eclipse uses absolute paths instead of relative.
	//	-Click OK
	//	-Click OK
	//	-Add "import com.microsoft.sqlserver.jdbc.*;		// SQL Server specific drivers
	//
	//	How to configure Java to use "Integrated Security" for SQL Server Login
	//	-Copy the sqljdbc_auth.dll from the download above to the 
	//	C:\Program Files (x86)\Java\JDK1.7.???\bin and C:\Program Files (x86)\Java\JRE7\bin directories
	//	-Restart Eclipse.
	//
	//	You may have to enable TCP connections for SQL Server.  
	//		Start/All Programs/SQL Server/Configuration Tools/SQL Server Configuration Manager
	//			SQL Server Network Configuration
	//				Protocols for MSSQLServer
	//					TCP/IP -> Enabled
	//		Stop and restart the SQL Server Service (control panel/admin tools/services/SQL Server) for changes to take affect.
	//
	//	http://stackoverflow.com/questions/6662577/connect-sql-2008-r2-with-java-in-eclipse
	// -------------------------------------------------------------------------
	
	
	// ----------------------------------------------------------------------
	// Name: CloseDatabaseConnection
	// Abstract: Close the connection to the database
	// ----------------------------------------------------------------------
	public static boolean CloseDatabaseConnection( )
	{
		boolean blnResult = false;
		
		try
		{
			// Is there a connection object?
			if( m_conAdministrator != null )
			{
				// Yes, close the connection if not closed already
				if( m_conAdministrator.isClosed( ) == false ) 
				{
					m_conAdministrator.close( );
					
					// Prevent JVM from crashing
					m_conAdministrator = null;
				}
			}
			
			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return blnResult;
	}
	
	
	// ----------------------------------------------------------------------
	// Name: LoadListBoxFromDatabase
	// Abstract: Load the target listbox with all name column values from
	//			the specified table
	// ----------------------------------------------------------------------
	public static boolean LoadListBoxFromDatabase( String strTable, 
												   String strNameColumn, 
												   CListBox lstTarget )
	{
		boolean blnResult = false;
		
		try
		{
			String strSelect = "";
			Statement sqlCommand = null;
			ResultSet rstTSource = null;
			String strName = "";
			
			// Clear list
			lstTarget.Clear( );
			
			// Build the SQL string
			strSelect = "SELECT " + strNameColumn 
					  + " FROM " + strTable 
					  + " ORDER BY " + strNameColumn;

			// Retrieve the all the records
			sqlCommand = m_conAdministrator.createStatement( );
			rstTSource = sqlCommand.executeQuery( strSelect );

			// Loop through all the records
			while( rstTSource.next( ) == true )
			{
				// Get Name from current row, which should be the first, and only, column
				strName = rstTSource.getString( 1 );

				// Add the item to the list (0 = id, strTeam = text to display, false = don't select)
				lstTarget.AddItemToList( 0, strName, false );
			}
			
			// Clean up
			rstTSource.close( );
			sqlCommand.close( );

			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return blnResult;
	}


	// -------------------------------------------------------------------------
	// Name: AddTeamToDatabase
	// Abstract: Add the team to the database
	// -------------------------------------------------------------------------
	public static boolean AddTeamToDatabase( udtTeamType udtTeam )
	{
		boolean blnResult = false;
		
		try
		{
			String strSQL = "";
			Statement sqlCommand = null;
			ResultSet rstTTeams = null;

			// Get the next highest team ID and save in suitcase so it's return to Add form
			udtTeam.intTeamID = GetNextHighestRecordID( "intTeamID", "TTeams" );
			
			// Race condition.  MS Access doesn't support locking or stored procedures.

			// Build the select string (select no records)
			strSQL = "SELECT * FROM TTeams WHERE intTeamID = -1";

			// Retrieve the all the records (should be none but we get table structure back)
			sqlCommand = m_conAdministrator.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE, 
														     ResultSet.CONCUR_UPDATABLE );
			rstTTeams = sqlCommand.executeQuery( strSQL );

			// New row using table structure returned from empty select.
			rstTTeams.moveToInsertRow( );
			rstTTeams.updateInt( "intTeamID", udtTeam.intTeamID );
			rstTTeams.updateString("strTeam", udtTeam.strTeam);
			rstTTeams.updateString("strMascot", udtTeam.strMascot);
			
			// Make the changes stick
			rstTTeams.insertRow( );
			
			// Clean up
			rstTTeams.close( );
			sqlCommand.close( );
			
			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}

		return blnResult;
	}


	private static int GetNextHighestRecordID(String strPrimaryKey, String strTable) {
		int intNextHighestRecordID = 0;
		
		try {
			String strSQL = "";
			Statement sqlCommand = null;
			ResultSet rstTable = null;
			
			strSQL = "SELECT MAX( " + strPrimaryKey + " ) + 1 AS intHighestRecordID" + " FROM " + strTable;
			
			sqlCommand = m_conAdministrator.createStatement();
			rstTable = sqlCommand.executeQuery(strSQL);
			
			if(rstTable.next()) {
				intNextHighestRecordID = rstTable.getInt("intHighestRecordID");
			}
			else {
				intNextHighestRecordID = 1;
			}
			
			rstTable.close();
			sqlCommand.close();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
		return intNextHighestRecordID;
	}


	public static boolean DeleteTeamFromDatabase(int intTeamID) {
		boolean blnResult = false;
		try {
			blnResult = DeleteRecordsFromTable(intTeamID, "intTeamID", "TTeams");
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		return blnResult;
	}


	private static boolean DeleteRecordsFromTable(int intTeamID, String strPrimaryColumn, String strTable) {
		boolean blnResult = false;
		
		try {
			String strSQL = "";
			Statement sqlCommand = null;
			
			strSQL = "DELETE FROM " + strTable + " WHERE " + strPrimaryColumn + " = " + intTeamID;
			
			sqlCommand = m_conAdministrator.createStatement();
			sqlCommand.execute(strSQL);
			
			sqlCommand.close();
			
			blnResult = true;
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		return blnResult;
	}
	
	public static boolean LoadListBoxFromDatabase (String strTable, String strPrimaryKeyColumn, String strNameColumn, CListBox lstTarget) {
		boolean blnResult = false;
		
		try {
			String strSelect = "";
			Statement sqlCommand = null;
			ResultSet rstTSource = null;
			int intID = 0;
			String strName = "";
			
			lstTarget.Clear();
			
			strSelect = "SELECT " + strPrimaryKeyColumn + ", " + strNameColumn + " FROM " + strTable + " ORDER BY " + strNameColumn;
			
			sqlCommand = m_conAdministrator.createStatement();
			rstTSource = sqlCommand.executeQuery(strSelect);
			
			while (rstTSource.next() == true) {
				intID = rstTSource.getInt(1);
				strName = rstTSource.getString(2);
				
				lstTarget.AddItemToList(intID, strName, false);
			}
			
			if (lstTarget.Length() > 0) {
				lstTarget.SetSelectedIndex(0);
			}
			
			rstTSource.close();
			sqlCommand.close();
			
			blnResult = true;
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
		return blnResult;
	}


	// -------------------------------------------------------------------------
	// Name: EditTeamInDatabase
	// Abstract: Update the information for the specified team in the database
	// -------------------------------------------------------------------------
	public static boolean EditTeamInDatabase( udtTeamType udtTeam )
	{
		boolean blnResult = false;
		
		try
		{
			String strSQL = "";
			Statement sqlCommand = null;
			ResultSet rstTTeams = null;

			// Build the select string
			strSQL = "SELECT * FROM TTeams WHERE intTeamID = " + udtTeam.intTeamID;

			// Retrieve the record
			sqlCommand = m_conAdministrator.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE, 
															 ResultSet.CONCUR_UPDATABLE );
			rstTTeams = sqlCommand.executeQuery( strSQL );

			// Edit the Team's information (should be 1 and only 1 row) 
			rstTTeams.next( );
			rstTTeams.updateString( "strTeam", udtTeam.strTeam );
			rstTTeams.updateString( "strMascot", udtTeam.strMascot );
			
			// Make the changes stick
			rstTTeams.updateRow( );

			// Clean up
			rstTTeams.close( );
			sqlCommand.close( );

			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}

		return blnResult;
	}


	// -------------------------------------------------------------------------
	// Name: GetTeamInformationFromDatabase
	// Abstract: Get data for the specified team from the database 
	// -------------------------------------------------------------------------
	public static boolean GetTeamInformationFromDatabase( udtTeamType udtTeam )
	{
		boolean blnResult = false;
		
		try
		{
			String strSQL = "";
			Statement sqlCommand = null;
			ResultSet rstTTeams = null;

			// Build the select string
			strSQL = "SELECT * FROM TTeams WHERE intTeamID = " + udtTeam.intTeamID;

			// Retrieve the record
			sqlCommand = m_conAdministrator.createStatement( );
			rstTTeams = sqlCommand.executeQuery( strSQL );

			// Get the Team's information (should be 1 and only 1 row) 
			rstTTeams.next( );
			udtTeam.strTeam = rstTTeams.getString( "strTeam" );
			udtTeam.strMascot = rstTTeams.getString( "strMascot" );

			// Clean up
			rstTTeams.close( );
			sqlCommand.close( );

			// Success
			blnResult = true;
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}

		return blnResult;
	}
}