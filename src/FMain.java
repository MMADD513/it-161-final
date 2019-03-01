// ----------------------------------------------------------------------
// Name: FMain
// Abstract: Final
// ----------------------------------------------------------------------


// ----------------------------------------------------------------------
// Imports
// ----------------------------------------------------------------------
import java.awt.*;						// Basic windows functionality
import java.awt.event.*;				// Event processing
import javax.swing.*;					// Supplemental windows functionality
import Utilities.*;
import Utilities.CMessageBox.enuIconType;
import Utilities.CUserDataTypes.udtTeamType;

@SuppressWarnings("serial")
public class FMain extends JFrame implements ActionListener,
											 WindowListener
{
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Controls
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	// Declare variables in the order that they appear
	public static CListBox m_lstTeams = null;
	private JButton m_btnAdd = null;
	private JButton m_btnEdit = null;
	private JButton m_btnDelete = null;
	private JButton m_btnClose = null;
	private JMenuBar m_mbMainMenu = null;
	private JMenu m_mnuFile = null;
	private JMenuItem m_mniFileExit = null;
	private JMenu m_mnuManage = null;
	private JMenuItem m_mniManageAdd = null;
	private JMenuItem m_mniManageDelete = null;
	private JMenuItem m_mniManageEdit = null;
	private JMenu m_mnuHelp = null;
	private JMenuItem m_mniHelpAbout = null;

	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Properties
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Never make public properties.  
	// Make protected or private with public get/set methods

	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	
	// ----------------------------------------------------------------------
	// Name: FMain
	// Abstract: the default constructor
	// ----------------------------------------------------------------------
	public FMain( )
	{
		try
		{
			Initialize( );
			
			AddControls( );
			
			BuildMenu();
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}
	


	private void BuildMenu() {

		try {
			m_mbMainMenu = CUtilities.AddMenuBar(this);
			
			m_mnuFile = CUtilities.AddMenu(m_mbMainMenu, "File", 'F');
				m_mniFileExit = CUtilities.AddMenuItem(m_mnuFile, this, "Exit", 'X', 'X');
				
			m_mnuManage = CUtilities.AddMenu(m_mbMainMenu, "Manage", 'M');
				m_mniManageAdd = CUtilities.AddMenuItem(m_mnuManage, this, "Add", 'A', 'A');
				m_mniManageEdit = CUtilities.AddMenuItem(m_mnuManage, this, "Edit", 'E', 'E');
				m_mniManageDelete = CUtilities.AddMenuItem(m_mnuManage, this, "Delete", 'D', 'D');
				
			m_mnuHelp = CUtilities.AddMenu(m_mbMainMenu, "Help", 'H');
				m_mniHelpAbout = CUtilities.AddMenuItem(m_mnuHelp, this, "About", 'B');
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
	}



	// ----------------------------------------------------------------------
	// Name: Initialize
	// Abstract: Set the form properties
	// ----------------------------------------------------------------------
	private void Initialize( )
	{
		try
		{
			int intHeight = 460;	
			int intWidth = 515;
			
			// Title
			setTitle( "Homework 15 by MM" );
			
			// Size
			setSize( intWidth, intHeight );
			
			// Center screen
			CUtilities.CenterScreen( this );
			
			// No resize
			setResizable( false );
			
			// Exit application close (instead of just hiding/visible = false)
			setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			
			// Listen for window events
			this.addWindowListener( this );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}
	
	
	
	// ----------------------------------------------------------------------
	// Name: AddControls
	// Abstract: Add all the form controls
	// ----------------------------------------------------------------------
	public void AddControls( )
	{
		try
		{
			// Clear layout manager so we can manually size and position controls
			this.setLayout( null );
	
			// Load


			// Teams List
			m_lstTeams						= CUtilities.AddListBox( this,
																	65, 20, 280, 250 );
			
			// m_btnAdd
			m_btnAdd = CUtilities.AddButton(this, this, "Add", 'A', 110, 390, 30, 100);
			
			// m_btnEdit
			m_btnEdit = CUtilities.AddButton(this, this, "Edit", 'E', 185, 390, 30, 100);
			
			// m_btnDelete
			m_btnDelete = CUtilities.AddButton(this, this, "Delete", 'D', 260, 390, 30, 100);
			
			// m_btnClose
			m_btnClose = CUtilities.AddButton(this, this, "Close", 'C', 355, 390, 30, 100);
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}
	
	
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Window Listener
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// Name: windowOpened
	// Abstract: The window is opened.  Triggered by setVisible( true ).
	// ----------------------------------------------------------------------
	public void windowOpened( WindowEvent weSource )
	{
		try
		{
			// We are busy
			CUtilities.SetBusyCursor( this, true );

			// Can we connect to the database?
			if( CDatabaseUtilities.OpenDatabaseConnectionMSAccessJRE8( ) == false )
			{
				
				// No, warn the user ...
				CMessageBox.Show( "Database connection error.\n" +
								  "The application will close.\n", 
								  getTitle( ) + " Error" );

				
				// and close the form/application
				this.dispose( );
			}
			
			CDatabaseUtilities.LoadListBoxFromDatabase( "TTeams", "intTeamID", "strTeam", m_lstTeams );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		finally
		{
			// We are NOT busy
			CUtilities.SetBusyCursor( this, false );
		}
	}
	
	
	// ----------------------------------------------------------------------
	// Name: windowClosed
	// Abstract: Close the connection to the database
	//			Triggered when this.dispose( ) but NOT by clicking X button
	//			in the Window Title Bar
	// ----------------------------------------------------------------------
	public void windowClosed( WindowEvent weSource )
	{
		try
		{
			CleanUp( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}

	
	// ----------------------------------------------------------------------
	// Name: windowClosing
	// Abstract: Close the connection to the database
	//			Triggered clicking X button in the Window Title Bar
	//			but NOT by this.dispose( )
	// ----------------------------------------------------------------------
	public void windowClosing( WindowEvent weSource )
	{
		try
		{
			CleanUp( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}

	
	// ----------------------------------------------------------------------
	// Name: CleanUp
	// Abstract: Close the connection to the database
	// ----------------------------------------------------------------------
	public void CleanUp( )
	{
		try
		{
			CDatabaseUtilities.CloseDatabaseConnection( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}


	// Don't care
	public void windowActivated( WindowEvent weSource ) { }
	public void windowDeactivated( WindowEvent weSource ) { }
	public void windowIconified( WindowEvent weSource ) { }
	public void windowDeiconified( WindowEvent weSource ) { }	
	
	
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Action Listener
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// Name: actionPerformed
	// Abstract: Event handler for control click events
	// ----------------------------------------------------------------------
	@Override
	public void actionPerformed( ActionEvent aeSource )
	{
		try
		{
			// VB.Net Event Procedure Names: <Control Name>_<Event Type>
			
			if (aeSource.getSource() == m_btnAdd) {
				btnAdd_Click();
			}
			else if (aeSource.getSource() == m_btnDelete) {
				btnDelete_Click();
			}
			else if (aeSource.getSource() == m_btnEdit) {
				btnEdit_Click();
			}
			else if (aeSource.getSource() == m_btnClose) {
				btnClose_Click();
			}
			else if (aeSource.getSource() == m_mniFileExit) {
				mniFileExit_Click();
			}
			else if (aeSource.getSource() == m_mniManageAdd) {
				mniManageAdd_Click();
			}
			else if (aeSource.getSource() == m_mniManageEdit) {
				mniManageEdit_Click();
			}
			else if (aeSource.getSource() == m_mniManageDelete) {
				mniManageDelete_Click();
			}
			else if (aeSource.getSource() == m_mniHelpAbout) {
				mniHelpAbout_Click();
			}
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}

	
	private void mniManageEdit_Click() {
		
		try {
			btnEdit_Click();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}



	private void mniHelpAbout_Click() {
		
		try {
			CMessageBox.Show(this, "My first Java database application\n" + "by MM\n" + "\n" + "Team And Players\n" + "Created by \n" + "Michael Maddin\n", "About", enuIconType.Information);
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}



	private void mniManageDelete_Click() {
		
		try {
			btnDelete_Click();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}



	private void mniManageAdd_Click() {
		
		try {
			btnAdd_Click();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}



	private void mniFileExit_Click() {

		try {
			this.dispose();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}



	private void btnClose_Click() {
		this.dispose();
		
	}



	private void btnEdit_Click() {

		try {
			int intSelectedListIndex = 0;
			CListItem liSelectedTeam = null;
			int intSelectedTeamID = 0;
			DEditTeam dlgEditTeam = null;
			udtTeamType udtNewTeam = null;
			
			intSelectedListIndex = m_lstTeams.GetSelectedIndex();
			
			if (intSelectedListIndex < 0) {
				CMessageBox.Show(this, "You must select a team to edit.", "Edit Team Error", enuIconType.Warning);
			}
			else {
				liSelectedTeam = m_lstTeams.GetSelectedItem();
				intSelectedTeamID = liSelectedTeam.GetValue();
				
				System.out.println(intSelectedTeamID);
				
				dlgEditTeam = new DEditTeam(this, intSelectedTeamID);
				
				dlgEditTeam.setVisible(true);
				
				if (dlgEditTeam.GetResult() == true) {
					udtNewTeam = dlgEditTeam.GetNewTeamInformation();
					
					m_lstTeams.RemoveAt(intSelectedListIndex);
					
					CDatabaseUtilities.LoadListBoxFromDatabase( "TTeams", "intTeamID", "strTeam", m_lstTeams );
				}
			}
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
	}



	// ----------------------------------------------------------------------
	// Name: btnDelete_Click
	// Abstract: Delete a team
	// ----------------------------------------------------------------------
	private void btnDelete_Click( )
	{
		try
		{
			int intSelectedListIndex = 0;
			CListItem liSelectedTeam = null;
			int intSelectedTeamID = 0;
			String strSelectedTeam = "";
			int intConfirm = 0;
			boolean blnResult = false;
			
			// Get the selected index from the list
			intSelectedListIndex = m_lstTeams.GetSelectedIndex( );

			// Is something selected?
			if( intSelectedListIndex < 0 )
			{
				// No, warn the user
				CMessageBox.Show( this, "You must select a Team to delete.", 
								  		"Delete Team Error" );
			}
			else
			{
				// Yes, so get the selected list item ID and name
				liSelectedTeam = m_lstTeams.GetSelectedItem( );
				intSelectedTeamID = liSelectedTeam.GetValue( );
				strSelectedTeam = liSelectedTeam.GetName( );
				
				// Confirm delete
				intConfirm = CMessageBox.Confirm( this, "Are you sure?", 
												  "Delete Team: " + strSelectedTeam );
	
				// Delete?
				if( intConfirm == CMessageBox.intRESULT_YES )
				{
					// Yes, we are busy
					CUtilities.SetBusyCursor( this, true );
					
					// Attempt to delete
					blnResult = CDatabaseUtilities.DeleteTeamFromDatabase( intSelectedTeamID );
					
					// Did it work?
					if( blnResult == true )
					{
						// Yes, remove record.  Next closest record automatically selected.
						m_lstTeams.RemoveAt( intSelectedListIndex );
						
						CDatabaseUtilities.LoadListBoxFromDatabase( "TTeams", "intTeamID", "strTeam", m_lstTeams );
					}
				}
			}	
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		finally
		{
			// We are NOT busy
			CUtilities.SetBusyCursor( this, false );
		}
	}




	private void btnAdd_Click() {
		try {
			DAddTeam dlgAddTeam = new DAddTeam(this);
			
			dlgAddTeam.setVisible(true);
			
			
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
	}

}









