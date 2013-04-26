CREATE OR REPLACE FUNCTION patch_from_5_to_6() RETURNS VOID AS $$
DECLARE
	db_version text;
BEGIN
	-- Test database version

	SELECT INTO db_version pvalue FROM params WHERE pname = 'version';
	IF db_version != '5' THEN
		RAISE EXCEPTION 'Incompatible database version.'; 
	END IF;

	-- Increment database version
    	UPDATE params SET pvalue = '6' WHERE pname = 'version';	
	-- CHANGES

	DROP TABLE opaque_sessions;
	
	CREATE TABLE opaque_sessions (
	    osession_id bigint NOT NULL,
	    solution_id integer,
	    created timestamp with time zone DEFAULT now() NOT NULL,
	    originalsession_id bigint,
	    steps integer
	);

	ALTER TABLE ONLY opaque_sessions
	    ADD CONSTRAINT opaque_sessions_pkey PRIMARY KEY (osession_id);

	ALTER TABLE ONLY opaque_sessions
	    ADD CONSTRAINT opaque_sessions_solution_id_fkey FOREIGN KEY (solution_id)
	    REFERENCES solutions(solution_id);
	
END;
$$ LANGUAGE 'plpgsql';

SELECT patch_from_5_to_6();

DROP FUNCTION IF EXISTS patch_from_5_to_6();
