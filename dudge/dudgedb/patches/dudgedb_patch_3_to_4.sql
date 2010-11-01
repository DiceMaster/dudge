CREATE OR REPLACE FUNCTION patch_from_3_to_4() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '3' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;

	-- Increment database version
    	UPDATE params SET pvalue = '4' WHERE pname = 'version';	
	-- CHANGES

	ALTER TABLE contest_problems
	ADD COLUMN problem_cost integer NOT NULL DEFAULT 1;
	COMMENT ON COLUMN contest_problems.problem_cost IS 'Problem cost in parrots';
	
END;
$$ LANGUAGE 'plpgsql';	

SELECT patch_from_3_to_4();

DROP FUNCTION IF EXISTS patch_from_3_to_4();
