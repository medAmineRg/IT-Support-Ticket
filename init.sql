-- Switch to Pluggable Database (PDB)
ALTER SESSION SET CONTAINER = XEPDB1;

-- Create the user inside the PDB
CREATE USER iticket IDENTIFIED BY iticket25;

-- Grant system privileges
GRANT CREATE SESSION TO iticket;
GRANT CONNECT TO iticket;
GRANT RESOURCE TO iticket;
GRANT DBA TO iticket;
GRANT UNLIMITED TABLESPACE TO iticket;

-- Set default tablespace and quota
ALTER USER iticket DEFAULT TABLESPACE USERS;
ALTER USER iticket QUOTA UNLIMITED ON USERS;
