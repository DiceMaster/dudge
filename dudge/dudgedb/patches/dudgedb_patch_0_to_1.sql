CREATE OR REPLACE FUNCTION patch_from_0_to_1() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '0' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;
	
	-- CHANGES

	ALTER TABLE contests ADD COLUMN is_open boolean NOT NULL DEFAULT FALSE;
	ALTER TABLE problems ADD COLUMN is_healthy boolean NOT NULL DEFAULT FALSE;

	-- New tables
	CREATE TABLE applications (
   	   owner VARCHAR(255) NOT NULL
	 , contest_id INTEGER NOT NULL
    	 , filing_time TIMESTAMP NOT NULL
    	 , message TEXT NOT NULL DEFAULT ''
    	 , status VARCHAR(255) NOT NULL 
	 , PRIMARY KEY (owner , contest_id)
	 , CONSTRAINT FK_applications_1 FOREIGN KEY (owner)
                  REFERENCES users (login) ON DELETE CASCADE ON UPDATE CASCADE
         , CONSTRAINT FK_applications_2 FOREIGN KEY (contest_id)
                  REFERENCES contests (contest_id) ON DELETE CASCADE ON UPDATE CASCADE
    	);

    	CREATE TABLE news(
	   new_id SERIAL NOT NULL
	 , owner VARCHAR(255) NOT NULL
	 , adding_time TIMESTAMP NOT NULL
	 , message TEXT NOT NULL DEFAULT ''
	 , PRIMARY KEY (new_id)
	 , CONSTRAINT FK_news_1 FOREIGN KEY (owner)
                  REFERENCES users (login) ON DELETE CASCADE ON UPDATE CASCADE
    	);

    	CREATE TABLE complaints(
	   complaint_id SERIAL NOT NULL
	 , problem_id INTEGER NOT NULL
	 , owner VARCHAR(255) NOT NULL
	 , filing_time TIMESTAMP NOT NULL
	 , message TEXT NOT NULL DEFAULT ''
	 , PRIMARY KEY (complaint_id)
	 , CONSTRAINT FK_complaints_1 FOREIGN KEY (problem_id)
                  REFERENCES problems (problem_id) ON DELETE CASCADE ON UPDATE CASCADE
    	);

	ALTER TABLE problems DROP CONSTRAINT FK_problems_1;

	ALTER TABLE problems ADD CONSTRAINT FK_problems_1 FOREIGN KEY (owner) REFERENCES users (login) ON UPDATE CASCADE ON DELETE SET NULL;
	
	-- Conversion of logins
	UPDATE users SET login = translate(lower(login), ' ', '_');
	
	-- Increment database version
    	UPDATE params SET pvalue = '1' WHERE pname = 'version';
END;
$$ LANGUAGE 'plpgsql';

SELECT patch_from_0_to_1();

DROP FUNCTION IF EXISTS patch_from_0_to_1();
