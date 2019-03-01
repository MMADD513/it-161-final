// ----------------------------------------------------------------------
// Name: DEditTeam
// Abstract: Edit a team
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
public class DEditTeam extends JDialog implements ActionListener
												 ,WindowListener
{
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Controls
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Declare the controls in the order they appear (top to bottom, left to right)
	@SuppressWarnings("unused")
	private JLabel m_lblTeam = null;
	private CTextBox m_txtTeam = null;

	@SuppressWarnings("unused")
	private JLabel m_lblMascot = null;
	private CTextBox m_txtMascot = null;

	@SuppressWarnings("unused")
	private JLabel m_lblRequiredField = null;
	
	private JButton m_btnOK = null;
	private JButton m_btnCancel = null;

	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Properties
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Never make public properties.  
	// Make protected or private with public get/set methods
	private int m_intTeamToEditID = 0;
	private boolean m_blnResult = false;
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	
	// ----------------------------------------------------------------------
	// Name: DEditTeam
	// Abstract: the default constructor
	// ----------------------------------------------------------------------
	public DEditTeam( JFrame frmParent, int intTeamToEditID )
	{
		super( frmParent, true );	// true = modal
		
		try
		{
			// Save ID for loading and saving
			m_intTeamToEditID = intTeamToEditID;
			
			Initialize( );
			
			AddControls( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
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
			int intHeight = 175;
			int intWidth = 285;
			
			// Title
			setTitle( "Edit Team" );
			
			// Size
			setSize( intWidth, intHeight );
			
			// Center screen
			CUtilities.CenterOwner( this );
			
			// No resize
			setResizable( false );
			
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
	// Name: paint
	// Abstract: Override the paint event to draw grid marks.
	// ----------------------------------------------------------------------
	public void paint( Graphics g )
	{
		super.paint( g );
		
		try
		{
			//CUtilities.DrawGridMarks( this, g );
		}
		catch( Exception expError )
		{
			// Display Error Message
			CUtilities.WriteLog( expError );
		}
	}
	
	
	// ----------------------------------------------------------------------
	// Name: AddControls
	// Abstract: Add all the controls to the frame
	// ----------------------------------------------------------------------
	private void AddControls( )
	{
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
	


	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// WindowListener
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// Name: windowOpened
	// Abstract: Load the form controls with values for record from database.
	// ----------------------------------------------------------------------
	public void windowOpened( WindowEvent weSource )
	{
		try
		{
			udtTeamType udtTeam = new CUserDataTypes( ).new udtTeamType( );
			boolean blnResult = false;
			
			// Which team to load?
			udtTeam.intTeamID = m_intTeamToEditID;
			
			// We are busy
			CUtilities.SetBusyCursor( this, true );

			// Get values
			blnResult = CDatabaseUtilities.GetTeamInformationFromDatabase( udtTeam );
			
			// Did it work?
			if( blnResult == true )
			{
				// Yes, load form with values
				m_txtTeam.setText( udtTeam.strTeam );
				m_txtMascot.setText( udtTeam.strMascot );
			}
			else
			{
				// No, warn user and ...
				CMessageBox.Show( this,  "Unable to load team information.\n"
									   + "The form will now close.", 
									   "Edit Team Error",
									   enuIconType.Error );
				
				// close
				setVisible( false );
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

	
	// Don't care
	public void windowClosed( WindowEvent weSource ) { }
	public void windowClosing( WindowEvent weSource ) { }
    public void windowIconified( WindowEvent weSource ) { }
    public void windowDeiconified( WindowEvent weSource ) { }
    public void windowActivated( WindowEvent weSource ) { }
    public void windowDeactivated( WindowEvent weSource ) { }


    // ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// ActionListener
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
				 if( aeSource.getSource( ) == m_btnOK )	    	btnOK_Click( );
			else if( aeSource.getSource( ) == m_btnCancel )	    btnCancel_Click( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}


	// ----------------------------------------------------------------------
	// Name: btnOK_Click
	// Abstract: Edit the team in the database
	// ----------------------------------------------------------------------
	private void btnOK_Click( )
	{
		try
		{
			// Is the form data good?
			if( IsValidData( ) == true )
			{
				// Did it save to the database?
				if( SaveData( ) == true )
				{
					// Yes, success
					m_blnResult = true;
					
					// All done
					setVisible( false );
				}
			}
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}
	
	
	// ----------------------------------------------------------------------
	// Name: IsValidData
	// Abstract: Check all the data and warn the user if it's bad
	// ----------------------------------------------------------------------
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

			// Mascot
			if( m_txtMascot.getText( ).equals( "" ) == true )
			{
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
	
	
	// ----------------------------------------------------------------------
	// Name: SaveData
	// Abstract: Get the data off of the form and save it to the database
	// ----------------------------------------------------------------------
	private boolean SaveData( )
	{
		boolean blnResult = false;
		
		try
		{
			// Make a suitcase for moving data
			udtTeamType udtNewTeam = new CUserDataTypes( ).new udtTeamType( );
			
			// Load suitcase with data from form
			udtNewTeam.intTeamID = m_intTeamToEditID;
			udtNewTeam.strTeam = m_txtTeam.getText( );
			udtNewTeam.strMascot = m_txtMascot.getText( );
			
			// We are busy
			CUtilities.SetBusyCursor( this, true );
			
			// Try to save the data
			blnResult = CDatabaseUtilities.EditTeamInDatabase( udtNewTeam );
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
	
	
	// ----------------------------------------------------------------------
	// Name: btnCancel_Click
	// Abstract: Cancel the Edit team
	// ----------------------------------------------------------------------
	private void btnCancel_Click( )
	{
		try
		{
			setVisible( false );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
	}


	// ----------------------------------------------------------------------
	// Name: GetResult
	// Abstract: Get result flag indicating if the edit was successful.
	// ----------------------------------------------------------------------
	public boolean GetResult( )
	{
		try
		{		
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return m_blnResult;
	}


	// ----------------------------------------------------------------------
	// Name: GetNewTeamInformation
	// Abstract: Get the new information from the form.
	// ----------------------------------------------------------------------
	public udtTeamType GetNewTeamInformation( )
	{
		udtTeamType udtTeam = null;

		try
		{		
			udtTeam = new CUserDataTypes( ).new udtTeamType( );
			
			udtTeam.intTeamID = m_intTeamToEditID;
			udtTeam.strTeam = m_txtTeam.getText( );
			udtTeam.strMascot = m_txtMascot.getText( );
		}
		catch( Exception excError )
		{
			// Display Error Message
			CUtilities.WriteLog( excError );
		}
		
		return udtTeam;
	}
	
	
}