-- --------------------------------------------------------------------------------
-- Name:		Patrick Callahan
-- Class:		IT-161 Java 1
-- Abstract:	Homework 10
-- --------------------------------------------------------------------------------

-- --------------------------------------------------------------------------------
-- Options
-- --------------------------------------------------------------------------------
USE dbTest		-- Don't work in master
SET NOCOUNT ON	-- Report only errors

-- --------------------------------------------------------------------------------
-- Step #1: Create Tables
-- --------------------------------------------------------------------------------
IF OBJECT_ID( 'TTeams' ) IS NOT NULL DROP TABLE TTeams
GO

CREATE TABLE TTeams
(
	  intTeamID				INTEGER					NOT NULL
	 ,strTeam				VARCHAR(50)				NOT NULL
	 ,strMascot				VARCHAR(50)				NOT NULL
	 ,CONSTRAINT TTeams_PK PRIMARY KEY ( intTeamID )
)

-- --------------------------------------------------------------------------------
-- Step #2: Insert statements
-- --------------------------------------------------------------------------------
INSERT INTO TTeams ( intTeamID, strTeam, strMascot )
VALUES ( 1, 'Curling', 'Yeti' )

INSERT INTO TTeams ( intTeamID, strTeam, strMascot )
VALUES ( 2, 'Soccer', 'Squirrel' )

INSERT INTO TTeams ( intTeamID, strTeam, strMascot )
VALUES ( 3, 'Jai Alai', 'Chipmunk' )
