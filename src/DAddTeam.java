import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Utilities.*;
import Utilities.CMessageBox.enuIconType;
import Utilities.CUserDataTypes.*;

public class DAddTeam extends JDialog implements ActionListener {

	private JLabel m_lblTeam = null;
	private CTextBox m_txtTeam = null;
	private JLabel m_lblMascot = null;
	private CTextBox m_txtMascot = null;
	private JLabel m_lblRequiredField = null;
	private JButton m_btnOK = null;
	private JButton m_btnCancel = null;

	
	public DAddTeam (JFrame frmParent) {
		super (frmParent, true);
		
		try {
			Initialize();
			
			AddControls();
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
	}
	
	private void Initialize() {
		try {
			int intHeight = 175;
			int intWidth = 285;
			
			setTitle ("Add Team");
			
			setSize (intWidth, intHeight);
			
			CUtilities.CenterOwner(this);
			
			setResizable(false);
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
	}
	
	private void AddControls() {
		try
		{				
			// Manually position and size controls
			CUtilities.ClearLayoutManager( this );
		
			// Team
			m_lblTeam = CUtilities.AddLabel( this, "Team:*", 25, 20 );
			m_txtTeam = CUtilities.AddTextBox( this, "", 21, 75, 25, 185, 50 );
	
			// Mascot
			m_lblMascot = CUtilities.AddLabel( this, "Mascot:*", 55, 20 );
			m_txtMascot = CUtilities.AddTextBox( this, "", 51, 75, 25, 185, 50 );
	
			// Required
			m_lblRequiredField = CUtilities.AddRequiredFieldLabel( this, 77, 70 );

			// m_btnOK
			m_btnOK = CUtilities.AddButton( this, this, "OK", 'O', 100, 30, 30, 100 );
	
			// m_btnCancel
			m_btnCancel = CUtilities.AddButton( this, this, "Cancel", 'C', 100, 155, 30, 100 );
		}
		catch( Exception expError )
		{
			// Display Error Message
			CUtilities.WriteLog( expError );
		}
	}
	
	
	private void btnOK_Click() {
		try {
			if (IsValidData() == true) {
				if (SaveData() == true) {
					setVisible(false);
					
					CDatabaseUtilities.LoadListBoxFromDatabase( "TTeams", "intTeamID", "strTeam", FMain.m_lstTeams );
				}
			}
		}
		catch (Exception e) {
			
		}
	}
	
	private void btnCancel_Click() {
		try {
			setVisible(false);
		}
		catch (Exception e) {
			
		}
	}
	
	private boolean SaveData( )
	{
		boolean blnResult = false;
		
		try
		{
			// Make a suitcase for moving data
			udtTeamType udtNewTeam = new CUserDataTypes( ).new udtTeamType( );
			
			// Load suitcase with data from form
			udtNewTeam.intTeamID = 0;
			udtNewTeam.strTeam = m_txtTeam.getText();
			udtNewTeam.strMascot = m_txtMascot.getText();
			
			// We are busy
			CUtilities.SetBusyCursor( this, true );
			
			// Try to save the data
			blnResult = CDatabaseUtilities.AddTeamToDatabase( udtNewTeam );
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
		
		return blnResult;
	}
	
	private boolean IsValidData( )
	{
		// Assume data is good.  Easier to code that way.
		boolean blnIsValidData = true;
		
		try
		{
			String strErrorMessage = "Please correct the following error(s):\n";
			
			// Trim textboxes
			CUtilities.TrimAllFormTextBoxes( this );
			
			// Team
			if( m_txtTeam.getText( ).equals( "" ) == true )
			{
				strErrorMessage += "-Team cannot be blank\n";
				blnIsValidData = false;
			}

			if(m_txtMascot.getText().equals("") == true) {
				strErrorMessage += "-Mascot cannot be blank\n";
				blnIsValidData = false;
			}
			
			// Bad data?
			if( blnIsValidData == false )
			{
				// Yes, warn the user
				CMessageBox.Show( this, strErrorMessage, 
								  getTitle( ) + " Error", enuIconType.Warning );
			}
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return blnIsValidData;
	}
	
	@Override
	public void actionPerformed(ActionEvent aeSource) {
		try {
			if(aeSource.getSource() == m_btnOK) {
				btnOK_Click();
			}
			else if(aeSource.getSource() == m_btnCancel) {
				btnCancel_Click();
			}
			
		}
		catch (Exception e) {
			CUtilities.WriteLog(e);
		}
		
	}
	
}
