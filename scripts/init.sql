
DROP TABLE IF EXISTS CORE_APPLICATION_MSTR;
CREATE TABLE CORE_APPLICATION_MSTR (
APP_ID int NOT NULL,
APP_NAME varchar(25) NOT NULL,
APP_DESC varchar(200) NOT NULL,
PRIMARY KEY (APP_ID)
);

DELETE FROM CORE_APPLICATION_MSTR;
INSERT INTO CORE_APPLICATION_MSTR (APP_ID, APP_NAME, APP_DESC) VALUES
(1, 'BANKING', 'This is a banking app');

DROP TABLE IF EXISTS CORE_APP_AUTH_MSTR;
CREATE TABLE IF NOT EXISTS CORE_APP_AUTH_MSTR (
AUTH_ID char(36) NOT NULL,
APP_ID int NOT NULL,
APP_KEY char(36) NOT NULL,
PRIMARY KEY (AUTH_ID)
);

CREATE INDEX CORE_APP_AUTH_MSTR_APP_KEY
    ON CORE_APP_AUTH_MSTR (APP_KEY);


DELETE FROM CORE_APP_AUTH_MSTR;


DROP TABLE IF EXISTS CORE_AUDIT_LOG;
CREATE TABLE IF NOT EXISTS CORE_AUDIT_LOG (
AUDIT_ID char(36) NOT NULL,
ACTION char(10) NOT NULL,
DATA varchar(100) NOT NULL,
APP_ID int NOT NULL,
USER_ID char(36) NOT NULL,
SERVICE_NAME char(5) NOT NULL,
CRE_TIME timestamp NOT NULL,
PRIMARY KEY (AUDIT_ID)
);


DELETE FROM CORE_AUDIT_LOG;

DROP TABLE IF EXISTS CORE_AUTH_REG_MSTR;
CREATE TABLE IF NOT EXISTS CORE_AUTH_REG_MSTR (
REG_ID char(36) NOT NULL,
USER_ID char(50) NOT NULL,
APP_ID int NOT NULL,
SERVICE_NAME char(5) NOT NULL,
STATUS char(1) NOT NULL,
PRIMARY KEY (REG_ID)
) ;

CREATE INDEX CORE_AUTH_REG_MSTR_USER_ID_APP_ID
ON CORE_AUTH_REG_MSTR (USER_ID,APP_ID);

DELETE FROM CORE_AUTH_REG_MSTR;

DROP TABLE IF EXISTS CORE_AUTH_TRXN;
CREATE TABLE IF NOT EXISTS CORE_AUTH_TRXN (
AUTH_CODE char(36) NOT NULL,
CODE_CHALLENGE varchar(256) NOT NULL,
APP_ID int NOT NULL,
USER_ID char(50) NOT NULL,
OPTIONS varchar(100) NOT NULL,
AVAILABLE varchar(100) NOT NULL,
STATUS char(1) NOT NULL,
CHOSEN_SERVICE char(5) NOT NULL,
CRE_TIME timestamp NOT NULL,
EXPIRY_TIME timestamp NOT NULL,
NO_OF_ATTEMPTS mediumint(9) NOT NULL,
PRIMARY KEY (AUTH_CODE)
);

CREATE INDEX CORE_AUTH_TRXN_APP_ID_USER_ID
ON CORE_AUTH_TRXN (APP_ID,USER_ID);

DELETE FROM CORE_AUTH_TRXN;

DROP TABLE IF EXISTS CORE_USER_CHANNEL_MSTR;
CREATE TABLE IF NOT EXISTS CORE_USER_CHANNEL_MSTR (
USER_ID char(50) NOT NULL,
APP_ID int NOT NULL,
CHANNEL char(5) NOT NULL,
CHANNEL_VAL char(50) NOT NULL,
ROW_ID char(36) NOT NULL,
PRIMARY KEY (ROW_ID)
) ;

CREATE INDEX CORE_USER_CHANNEL_MSTR_USER_ID_APP_ID
ON CORE_USER_CHANNEL_MSTR (USER_ID,APP_ID);

DELETE FROM CORE_USER_CHANNEL_MSTR;

DROP TABLE IF EXISTS TOTP_MSTR;
CREATE TABLE IF NOT EXISTS TOTP_MSTR (
USER_ID char(50) NOT NULL,
APP_ID int NOT NULL,
ROW_ID char(36) NOT NULL ,
SECRET varchar(250) NOT NULL,
PRIMARY KEY (ROW_ID)
) ;
CREATE INDEX TOTP_MSTR_USER_ID_APP_ID
ON TOTP_MSTR (USER_ID,APP_ID);

DROP TABLE IF EXISTS MODULE_RECONFIRM_MSTR;
CREATE TABLE IF NOT EXISTS MODULE_RECONFIRM_MSTR (
USER_ID char(50) NOT NULL,
APP_ID int NOT NULL,
ROW_ID char(36) NOT NULL ,
STATUS char(1) NOT NULL,
PRIMARY KEY (ROW_ID)
) ;
CREATE INDEX MODULE_RECONFIRM_MSTR_USER_ID_APP_ID
ON MODULE_RECONFIRM_MSTR (USER_ID,APP_ID);

DELETE FROM TOTP_MSTR;


DELETE FROM CORE_APPLICATION_MSTR;
INSERT INTO CORE_APPLICATION_MSTR (APP_ID, APP_NAME, APP_DESC) VALUES
(1, 'Co-Auth Test app', 'This is a test application for Co-Auth');

DELETE FROM CORE_APP_AUTH_MSTR;

INSERT INTO CORE_APP_AUTH_MSTR (AUTH_ID,APP_ID, APP_KEY) VALUES
('f1b9c9c0-3f9e-4b5d-8b4d-0c2e8c0c8a4a', 1,  '11111111-1111-1111-1111-111111111111');