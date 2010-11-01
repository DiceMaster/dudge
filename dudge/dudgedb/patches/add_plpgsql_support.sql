DROP LANGUAGE 'plpgsql';

-- CREATE FUNCTION plpgsql_call_handler() RETURNS OPAQUE AS '/usr/lib/pgsql/plpgsql.so' LANGUAGE 'C';
CREATE FUNCTION plpgsql_call_handler() RETURNS OPAQUE AS E'C:\\Program Files (x86)\\PostgreSQL\\8.4\\lib\\plpgsql.dll' LANGUAGE 'C';

CREATE LANGUAGE 'plpgsql' HANDLER plpgsql_call_handler LANCOMPILER 'PL/pgSQL';
