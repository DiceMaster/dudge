CREATE OR REPLACE FUNCTION patch_from_4_to_5() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '4' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;

	-- Increment database version
    	UPDATE params SET pvalue = '5' WHERE pname = 'version';	
	-- CHANGES

	ALTER TABLE users
	ADD COLUMN stud_faculty character varying(255);
	COMMENT ON COLUMN users.stud_faculty IS 'Student faculty';
	ALTER TABLE users
	ADD COLUMN stud_course integer;
	COMMENT ON COLUMN users.stud_course IS 'Student course';
	ALTER TABLE users
	ADD COLUMN stud_group character varying(255);
	COMMENT ON COLUMN users.stud_group IS 'Student group';

	
END;
$$ LANGUAGE 'plpgsql';	

SELECT patch_from_4_to_5();

DROP FUNCTION IF EXISTS patch_from_4_to_5();