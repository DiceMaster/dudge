CREATE OR REPLACE FUNCTION patch_from_1_to_2() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '1' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;

	-- Increment database version
    	UPDATE params SET pvalue = '2' WHERE pname = 'version';	
	-- CHANGES

	ALTER TABLE problems ADD COLUMN 
	author VARCHAR(255) NOT NULL DEFAULT '';
	-- New tables

	DROP TABLE news;
	
    	CREATE TABLE news(
	   news_id SERIAL NOT NULL
	 , author VARCHAR(255) NOT NULL
	 , adding_time TIMESTAMP NOT NULL DEFAULT now()
	 , message TEXT NOT NULL DEFAULT ''
	 , PRIMARY KEY (news_id)
	 , CONSTRAINT FK_news_1 FOREIGN KEY (author)
                  REFERENCES users (login) ON DELETE CASCADE ON UPDATE CASCADE
    	);

	--FUNCTIONS
CREATE OR REPLACE FUNCTION can_view_news(principal VARCHAR(255), news_id integer) RETURNS BOOLEAN AS '
BEGIN
	RETURN TRUE;
END;
' LANGUAGE 'plpgsql';
	
END;
$$ LANGUAGE 'plpgsql';

SELECT patch_from_1_to_2();

DROP FUNCTION IF EXISTS patch_from_1_to_2();
