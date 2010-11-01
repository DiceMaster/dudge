CREATE OR REPLACE FUNCTION patch_from_2_to_3() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '2' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;

	-- Increment database version
    	UPDATE params SET pvalue = '3' WHERE pname = 'version';	
	-- CHANGES

	ALTER TABLE contests
	ADD COLUMN rules text NOT NULL DEFAULT '';
	COMMENT ON COLUMN contests.rules IS 'Contest rules';
	
END;
$$ LANGUAGE 'plpgsql';	

SELECT patch_from_2_to_3();

DROP FUNCTION IF EXISTS patch_from_2_to_3();
