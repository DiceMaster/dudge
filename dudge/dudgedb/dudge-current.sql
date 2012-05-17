--
-- PostgreSQL database dump
--

-- Started on 2010-04-15 20:09:59

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 348 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: -
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- TOC entry 19 (class 1255 OID 16677)
-- Dependencies: 348 3
-- Name: can_view_news(character varying, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION can_view_news(principal character varying, news_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
	RETURN TRUE;
END;
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1550 (class 1259 OID 16592)
-- Dependencies: 1854 3
-- Name: applications; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE applications (
    owner character varying(255) NOT NULL,
    contest_id integer NOT NULL,
    filing_time timestamp without time zone NOT NULL,
    message text DEFAULT ''::text NOT NULL,
    status character varying(255) NOT NULL
);


--
-- TOC entry 1552 (class 1259 OID 16630)
-- Dependencies: 1856 3
-- Name: complaints; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE complaints (
    complaint_id integer NOT NULL,
    problem_id integer NOT NULL,
    owner character varying(255) NOT NULL,
    filing_time timestamp without time zone NOT NULL,
    message text DEFAULT ''::text NOT NULL
);


--
-- TOC entry 1551 (class 1259 OID 16628)
-- Dependencies: 1552 3
-- Name: complaints_complaint_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE complaints_complaint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1913 (class 0 OID 0)
-- Dependencies: 1551
-- Name: complaints_complaint_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE complaints_complaint_id_seq OWNED BY complaints.complaint_id;


--
-- TOC entry 1546 (class 1259 OID 16510)
-- Dependencies: 3
-- Name: contest_languages; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contest_languages (
    contest_id integer NOT NULL,
    language_id character varying(255) NOT NULL
);


--
-- TOC entry 1914 (class 0 OID 0)
-- Dependencies: 1546
-- Name: TABLE contest_languages; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE contest_languages IS 'Stores information about which languages allowed in which contest.';


--
-- TOC entry 1915 (class 0 OID 0)
-- Dependencies: 1546
-- Name: COLUMN contest_languages.contest_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contest_languages.contest_id IS 'Identifier of contest.';


--
-- TOC entry 1916 (class 0 OID 0)
-- Dependencies: 1546
-- Name: COLUMN contest_languages.language_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contest_languages.language_id IS 'Identifier of language.';


--
-- TOC entry 1549 (class 1259 OID 16558)
-- Dependencies: 1853 3
-- Name: contest_problems; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contest_problems (
    contest_id integer DEFAULT 0 NOT NULL,
    problem_id integer NOT NULL,
    problem_order integer NOT NULL,
    problem_mark character varying(255) NOT NULL,    problem_cost integer NOT NULL DEFAULT 1
);


--
-- TOC entry 1917 (class 0 OID 0)
-- Dependencies: 1549
-- Name: TABLE contest_problems; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE contest_problems IS 'Stores information about what problems what contest have.';


--
-- TOC entry 1918 (class 0 OID 0)
-- Dependencies: 1549
-- Name: COLUMN contest_problems.contest_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contest_problems.contest_id IS 'Contest identifier.';


--
-- TOC entry 1919 (class 0 OID 0)
-- Dependencies: 1549
-- Name: COLUMN contest_problems.problem_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contest_problems.problem_order IS 'Order of problem in contest, starting at 1.';


--
-- TOC entry 1920 (class 0 OID 0)
-- Dependencies: 1549
-- Name: COLUMN contest_problems.problem_mark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contest_problems.problem_mark IS 'Contest-specific title of problem.';


--
-- TOC entry 1539 (class 1259 OID 16427)
-- Dependencies: 1837 1838 1839 3
-- Name: contests; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contests (
    contest_id integer NOT NULL,
    caption character varying(255) NOT NULL,
    description character varying(5000),
    con_type character varying(255) NOT NULL,
    start_time timestamp without time zone NOT NULL,
    duration integer NOT NULL,
    freeze_time integer DEFAULT 0 NOT NULL,
    is_open boolean DEFAULT false NOT NULL,
    rules text DEFAULT ''::text NOT NULL
);


--
-- TOC entry 1921 (class 0 OID 0)
-- Dependencies: 1539
-- Name: TABLE contests; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE contests IS 'Contests table.';


--
-- TOC entry 1922 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.contest_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.contest_id IS 'Contest identifier.';


--
-- TOC entry 1923 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.caption; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.caption IS 'Contest caption';


--
-- TOC entry 1924 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.description IS 'Contest description';


--
-- TOC entry 1925 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.con_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.con_type IS 'Type of contest.';


--
-- TOC entry 1926 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.start_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.start_time IS 'Contest starting date and time.';


--
-- TOC entry 1927 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.duration; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.duration IS 'Contest duration, in seconds.';


--
-- TOC entry 1928 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.freeze_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.freeze_time IS 'Time before the end of contest in seconds, after which monitor becomes non-updatable for contest participants.';


--
-- TOC entry 1929 (class 0 OID 0)
-- Dependencies: 1539
-- Name: COLUMN contests.rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN contests.rules IS 'Contest rules';


--
-- TOC entry 1538 (class 1259 OID 16425)
-- Dependencies: 1539 3
-- Name: contests_contest_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE contests_contest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1930 (class 0 OID 0)
-- Dependencies: 1538
-- Name: contests_contest_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE contests_contest_id_seq OWNED BY contests.contest_id;


--
-- TOC entry 1536 (class 1259 OID 16409)
-- Dependencies: 3
-- Name: languages; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE languages (
    language_id character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    description character varying(500) NOT NULL,
    file_extension character varying(15) NOT NULL,
    compilation_cmd character varying(32768) NOT NULL,
    execution_cmd character varying(32768) NOT NULL
);


--
-- TOC entry 1931 (class 0 OID 0)
-- Dependencies: 1536
-- Name: TABLE languages; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE languages IS 'Stores computer languages present in system.';


--
-- TOC entry 1932 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.language_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.language_id IS 'Identifier of language.';


--
-- TOC entry 1933 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.title IS 'Readable language title.';


--
-- TOC entry 1934 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.description IS 'Readable language description.';


--
-- TOC entry 1935 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.file_extension; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.file_extension IS 'File extension of language source files (with dot).';


--
-- TOC entry 1936 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.compilation_cmd; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.compilation_cmd IS 'Compilation command with substitutions.';


--
-- TOC entry 1937 (class 0 OID 0)
-- Dependencies: 1536
-- Name: COLUMN languages.execution_cmd; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN languages.execution_cmd IS 'Execution command with substitutions.';


--
-- TOC entry 1554 (class 1259 OID 16661)
-- Dependencies: 1858 1859 3
-- Name: news; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE news (
    news_id integer NOT NULL,
    author character varying(255) NOT NULL,
    adding_time timestamp without time zone DEFAULT now() NOT NULL,
    message text DEFAULT ''::text NOT NULL
);


--
-- TOC entry 1553 (class 1259 OID 16659)
-- Dependencies: 3 1554
-- Name: news_news_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE news_news_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1938 (class 0 OID 0)
-- Dependencies: 1553
-- Name: news_news_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE news_news_id_seq OWNED BY news.news_id;


--
-- TOC entry 1537 (class 1259 OID 16417)
-- Dependencies: 3
-- Name: params; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE params (
    pname character varying(255) NOT NULL,
    pvalue text NOT NULL
);


--
-- TOC entry 1939 (class 0 OID 0)
-- Dependencies: 1537
-- Name: TABLE params; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE params IS 'Database parameters.';


--
-- TOC entry 1541 (class 1259 OID 16439)
-- Dependencies: 1841 1842 1843 1844 1845 1846 1847 1848 1849 1850 3
-- Name: problems; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE problems (
    problem_id integer NOT NULL,
    owner character varying(255) DEFAULT NULL::character varying,
    title character varying(255) DEFAULT 'Problem'::character varying NOT NULL,
    is_hidden boolean DEFAULT false NOT NULL,
    description text DEFAULT ''::text NOT NULL,
    create_time timestamp without time zone NOT NULL,
    memory_limit integer DEFAULT 67108864 NOT NULL,
    cpu_time_limit integer DEFAULT 1000 NOT NULL,
    real_time_limit integer DEFAULT 60000 NOT NULL,
    output_limit integer DEFAULT 1048576 NOT NULL,
    is_healthy boolean DEFAULT false NOT NULL,
    author character varying(255) DEFAULT ''::character varying NOT NULL
);


--
-- TOC entry 1940 (class 0 OID 0)
-- Dependencies: 1541
-- Name: TABLE problems; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE problems IS 'Problems table. Contains problems present in system.';


--
-- TOC entry 1941 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.problem_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.problem_id IS 'Identifier of problem.';


--
-- TOC entry 1942 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.owner; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.owner IS 'Owner of problem.';


--
-- TOC entry 1943 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.title IS 'Title of problem.';


--
-- TOC entry 1944 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.is_hidden; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.is_hidden IS 'Define, is this problem hidden, or no.';


--
-- TOC entry 1945 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.description IS 'Description of problem, in wiki format.';


--
-- TOC entry 1946 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.memory_limit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.memory_limit IS 'Limit of virtual memory, in bytes.';


--
-- TOC entry 1947 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.cpu_time_limit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.cpu_time_limit IS 'Limit of CPU time, in milliseconds.';


--
-- TOC entry 1948 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.real_time_limit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.real_time_limit IS 'Real time limit, in milliseconds.';


--
-- TOC entry 1949 (class 0 OID 0)
-- Dependencies: 1541
-- Name: COLUMN problems.output_limit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN problems.output_limit IS 'Limit on output data, in bytes.';


--
-- TOC entry 1540 (class 1259 OID 16437)
-- Dependencies: 3 1541
-- Name: problems_problem_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE problems_problem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1950 (class 0 OID 0)
-- Dependencies: 1540
-- Name: problems_problem_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE problems_problem_id_seq OWNED BY problems.problem_id;


--
-- TOC entry 1547 (class 1259 OID 16525)
-- Dependencies: 3
-- Name: roles; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE roles (
    contest_id integer NOT NULL,
    username character varying(255) NOT NULL,
    role_type character varying(255) NOT NULL
);


--
-- TOC entry 1951 (class 0 OID 0)
-- Dependencies: 1547
-- Name: TABLE roles; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE roles IS 'Stores information about user roles and registration in contests.';


--
-- TOC entry 1952 (class 0 OID 0)
-- Dependencies: 1547
-- Name: COLUMN roles.contest_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN roles.contest_id IS 'Identifier of contest.';


--
-- TOC entry 1953 (class 0 OID 0)
-- Dependencies: 1547
-- Name: COLUMN roles.username; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN roles.username IS 'User login name.';


--
-- TOC entry 1954 (class 0 OID 0)
-- Dependencies: 1547
-- Name: COLUMN roles.role_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN roles.role_type IS 'Role of user in contest.';


--
-- TOC entry 1548 (class 1259 OID 16543)
-- Dependencies: 3
-- Name: runs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runs (
    solution_id integer NOT NULL,
    test_id integer NOT NULL,
    run_number integer NOT NULL,
    result_type character varying(255) NOT NULL,
    memory integer NOT NULL,
    cpu_time integer NOT NULL,
    real_time integer NOT NULL
);


--
-- TOC entry 1955 (class 0 OID 0)
-- Dependencies: 1548
-- Name: TABLE runs; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE runs IS 'Stores information about launched test runs.';


--
-- TOC entry 1956 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.solution_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.solution_id IS 'Solution this run belongs to.';


--
-- TOC entry 1957 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.test_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.test_id IS 'Test identifier.';


--
-- TOC entry 1958 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.run_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.run_number IS 'Run sequential number in solution.';


--
-- TOC entry 1959 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.result_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.result_type IS 'Run result type.';


--
-- TOC entry 1960 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.memory; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.memory IS 'Maximum memory size, in bytes.';


--
-- TOC entry 1961 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.cpu_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.cpu_time IS 'Run cpu time, in milliseconds.';


--
-- TOC entry 1962 (class 0 OID 0)
-- Dependencies: 1548
-- Name: COLUMN runs.real_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN runs.real_time IS 'Run real time, in milliseconds.';


--
-- TOC entry 1543 (class 1259 OID 16463)
-- Dependencies: 3
-- Name: solutions; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE solutions (
    solution_id integer NOT NULL,
    submit_time timestamp without time zone NOT NULL,
    username character varying(255) NOT NULL,
    contest_id integer NOT NULL,
    problem_id integer NOT NULL,
    language_id character varying(255) NOT NULL,
    source_code text NOT NULL,
    status character varying(255) NOT NULL,
    status_message text NOT NULL,
    compilation_time integer NOT NULL
);


--
-- TOC entry 1963 (class 0 OID 0)
-- Dependencies: 1543
-- Name: TABLE solutions; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE solutions IS 'Solutions table. Contains problem solutions submitted by users.';


--
-- TOC entry 1964 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.submit_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.submit_time IS 'Submit date and time.';


--
-- TOC entry 1965 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.username; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.username IS 'User login name.';


--
-- TOC entry 1966 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.contest_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.contest_id IS 'Identifier of contest this solution belongs to.';


--
-- TOC entry 1967 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.problem_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.problem_id IS 'Problem this solution solves.';


--
-- TOC entry 1968 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.language_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.language_id IS 'Language this solution written on.';


--
-- TOC entry 1969 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.source_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.source_code IS 'Source code of solution.';


--
-- TOC entry 1970 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.status IS 'Processing status of solution.';


--
-- TOC entry 1971 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.status_message; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.status_message IS 'Error or informational text about solution status.';


--
-- TOC entry 1972 (class 0 OID 0)
-- Dependencies: 1543
-- Name: COLUMN solutions.compilation_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN solutions.compilation_time IS 'Compilation duration in milliseconds.';


--
-- TOC entry 1542 (class 1259 OID 16461)
-- Dependencies: 3 1543
-- Name: solutions_solution_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE solutions_solution_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1973 (class 0 OID 0)
-- Dependencies: 1542
-- Name: solutions_solution_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE solutions_solution_id_seq OWNED BY solutions.solution_id;


--
-- TOC entry 1545 (class 1259 OID 16494)
-- Dependencies: 3
-- Name: tests; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE tests (
    test_id integer NOT NULL,
    problem_id integer NOT NULL,
    test_number integer NOT NULL,
    input_data text NOT NULL,
    output_data text NOT NULL
);


--
-- TOC entry 1974 (class 0 OID 0)
-- Dependencies: 1545
-- Name: TABLE tests; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE tests IS 'Contains test input and output data.';


--
-- TOC entry 1975 (class 0 OID 0)
-- Dependencies: 1545
-- Name: COLUMN tests.test_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN tests.test_id IS 'Absolute system key for test.';


--
-- TOC entry 1976 (class 0 OID 0)
-- Dependencies: 1545
-- Name: COLUMN tests.test_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN tests.test_number IS 'Test number starting at 1.';


--
-- TOC entry 1977 (class 0 OID 0)
-- Dependencies: 1545
-- Name: COLUMN tests.input_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN tests.input_data IS 'Test data.';


--
-- TOC entry 1978 (class 0 OID 0)
-- Dependencies: 1545
-- Name: COLUMN tests.output_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN tests.output_data IS 'Test output data.';


--
-- TOC entry 1544 (class 1259 OID 16492)
-- Dependencies: 1545 3
-- Name: tests_test_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tests_test_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1979 (class 0 OID 0)
-- Dependencies: 1544
-- Name: tests_test_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tests_test_id_seq OWNED BY tests.test_id;


--
-- TOC entry 1535 (class 1259 OID 16397)
-- Dependencies: 1832 1833 1834 1835 3
-- Name: users; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users (
    login character varying(255) NOT NULL,
    pwd_hash character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    real_name character varying(255) NOT NULL,
    organization character varying(255) NOT NULL,
    register_date date NOT NULL,
    age integer,
    jabber_id character varying(255) DEFAULT ''::character varying NOT NULL,
    icq_number integer,
    is_admin boolean DEFAULT false NOT NULL,
    can_create_contest boolean DEFAULT false NOT NULL,
    can_create_problem boolean DEFAULT false NOT NULL,
    stud_faculty character varying(255),
    stud_course integer,
    stud_group character varying(255));


--
-- TOC entry 1980 (class 0 OID 0)
-- Dependencies: 1535
-- Name: TABLE users; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE users IS 'Users table. Stores user accounts with their information.';


--
-- TOC entry 1981 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.login; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.login IS 'User login name.';


--
-- TOC entry 1982 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.pwd_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.pwd_hash IS 'User password hash.';


--
-- TOC entry 1983 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.email IS 'User e-mail address.';


--
-- TOC entry 1984 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.real_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.real_name IS 'User full name.';


--
-- TOC entry 1985 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.organization; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.organization IS 'Organization of user.';


--
-- TOC entry 1986 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.register_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.register_date IS 'Date and time of user registration.';


--
-- TOC entry 1987 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.age; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.age IS 'User age.';


--
-- TOC entry 1988 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.jabber_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.jabber_id IS 'Jabber ID of user in the form user@server.foo.';


--
-- TOC entry 1989 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.icq_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.icq_number IS 'ICQ UIN of user.';


--
-- TOC entry 1990 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.is_admin; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.is_admin IS 'Indicates is user have administrator privilege.';


--
-- TOC entry 1991 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.can_create_contest; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.can_create_contest IS 'Contest creation privilege.';


--
-- TOC entry 1992 (class 0 OID 0)
-- Dependencies: 1535
-- Name: COLUMN users.can_create_problem; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.can_create_problem IS 'Problem creation privilege.';


--
-- TOC entry 1855 (class 2604 OID 16633)
-- Dependencies: 1551 1552 1552
-- Name: complaint_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE complaints ALTER COLUMN complaint_id SET DEFAULT nextval('complaints_complaint_id_seq'::regclass);


--
-- TOC entry 1836 (class 2604 OID 16430)
-- Dependencies: 1539 1538 1539
-- Name: contest_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE contests ALTER COLUMN contest_id SET DEFAULT nextval('contests_contest_id_seq'::regclass);


--
-- TOC entry 1857 (class 2604 OID 16664)
-- Dependencies: 1554 1553 1554
-- Name: news_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE news ALTER COLUMN news_id SET DEFAULT nextval('news_news_id_seq'::regclass);


--
-- TOC entry 1840 (class 2604 OID 16442)
-- Dependencies: 1541 1540 1541
-- Name: problem_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE problems ALTER COLUMN problem_id SET DEFAULT nextval('problems_problem_id_seq'::regclass);


--
-- TOC entry 1851 (class 2604 OID 16466)
-- Dependencies: 1542 1543 1543
-- Name: solution_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE solutions ALTER COLUMN solution_id SET DEFAULT nextval('solutions_solution_id_seq'::regclass);


--
-- TOC entry 1852 (class 2604 OID 16497)
-- Dependencies: 1545 1544 1545
-- Name: test_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE tests ALTER COLUMN test_id SET DEFAULT nextval('tests_test_id_seq'::regclass);


--
-- TOC entry 1885 (class 2606 OID 16600)
-- Dependencies: 1550 1550 1550
-- Name: applications_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (owner, contest_id);


--
-- TOC entry 1887 (class 2606 OID 16639)
-- Dependencies: 1552 1552
-- Name: complaints_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY complaints
    ADD CONSTRAINT complaints_pkey PRIMARY KEY (complaint_id);


--
-- TOC entry 1877 (class 2606 OID 16514)
-- Dependencies: 1546 1546 1546
-- Name: contest_languages_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contest_languages
    ADD CONSTRAINT contest_languages_pkey PRIMARY KEY (contest_id, language_id);


--
-- TOC entry 1883 (class 2606 OID 16563)
-- Dependencies: 1549 1549 1549
-- Name: contest_problems_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contest_problems
    ADD CONSTRAINT contest_problems_pkey PRIMARY KEY (contest_id, problem_id);


--
-- TOC entry 1867 (class 2606 OID 16436)
-- Dependencies: 1539 1539
-- Name: contests_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contests
    ADD CONSTRAINT contests_pkey PRIMARY KEY (contest_id);


--
-- TOC entry 1863 (class 2606 OID 16416)
-- Dependencies: 1536 1536
-- Name: languages_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (language_id);


--
-- TOC entry 1889 (class 2606 OID 16671)
-- Dependencies: 1554 1554
-- Name: news_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY news
    ADD CONSTRAINT news_pkey PRIMARY KEY (news_id);


--
-- TOC entry 1865 (class 2606 OID 16424)
-- Dependencies: 1537 1537
-- Name: params_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY params
    ADD CONSTRAINT params_pkey PRIMARY KEY (pname);


--
-- TOC entry 1869 (class 2606 OID 16455)
-- Dependencies: 1541 1541
-- Name: problems_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_pkey PRIMARY KEY (problem_id);


--
-- TOC entry 1879 (class 2606 OID 16532)
-- Dependencies: 1547 1547 1547 1547
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (contest_id, username, role_type);


--
-- TOC entry 1881 (class 2606 OID 16547)
-- Dependencies: 1548 1548 1548
-- Name: runs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runs
    ADD CONSTRAINT runs_pkey PRIMARY KEY (solution_id, test_id);


--
-- TOC entry 1871 (class 2606 OID 16471)
-- Dependencies: 1543 1543
-- Name: solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY solutions
    ADD CONSTRAINT solutions_pkey PRIMARY KEY (solution_id);


--
-- TOC entry 1873 (class 2606 OID 16502)
-- Dependencies: 1545 1545
-- Name: tests_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (test_id);


--
-- TOC entry 1875 (class 2606 OID 16504)
-- Dependencies: 1545 1545 1545
-- Name: unique_test; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT unique_test UNIQUE (problem_id, test_number);


--
-- TOC entry 1861 (class 2606 OID 16408)
-- Dependencies: 1535 1535
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (login);


--
-- TOC entry 1904 (class 2606 OID 16601)
-- Dependencies: 1550 1535 1860
-- Name: fk_applications_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT fk_applications_1 FOREIGN KEY (owner) REFERENCES users(login) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1905 (class 2606 OID 16606)
-- Dependencies: 1866 1550 1539
-- Name: fk_applications_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT fk_applications_2 FOREIGN KEY (contest_id) REFERENCES contests(contest_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1906 (class 2606 OID 16640)
-- Dependencies: 1552 1868 1541
-- Name: fk_complaints_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY complaints
    ADD CONSTRAINT fk_complaints_1 FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1896 (class 2606 OID 16515)
-- Dependencies: 1539 1866 1546
-- Name: fk_contest_languages_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contest_languages
    ADD CONSTRAINT fk_contest_languages_1 FOREIGN KEY (contest_id) REFERENCES contests(contest_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1897 (class 2606 OID 16520)
-- Dependencies: 1862 1536 1546
-- Name: fk_contest_languages_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contest_languages
    ADD CONSTRAINT fk_contest_languages_2 FOREIGN KEY (language_id) REFERENCES languages(language_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1902 (class 2606 OID 16564)
-- Dependencies: 1549 1539 1866
-- Name: fk_contest_problems_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contest_problems
    ADD CONSTRAINT fk_contest_problems_1 FOREIGN KEY (contest_id) REFERENCES contests(contest_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1903 (class 2606 OID 16569)
-- Dependencies: 1549 1541 1868
-- Name: fk_contest_problems_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contest_problems
    ADD CONSTRAINT fk_contest_problems_2 FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1898 (class 2606 OID 16533)
-- Dependencies: 1866 1539 1547
-- Name: fk_contest_users_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT fk_contest_users_1 FOREIGN KEY (contest_id) REFERENCES contests(contest_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1899 (class 2606 OID 16538)
-- Dependencies: 1535 1860 1547
-- Name: fk_contest_users_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT fk_contest_users_2 FOREIGN KEY (username) REFERENCES users(login) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1907 (class 2606 OID 16672)
-- Dependencies: 1535 1860 1554
-- Name: fk_news_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY news
    ADD CONSTRAINT fk_news_1 FOREIGN KEY (author) REFERENCES users(login) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1890 (class 2606 OID 16645)
-- Dependencies: 1860 1541 1535
-- Name: fk_problems_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT fk_problems_1 FOREIGN KEY (owner) REFERENCES users(login) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 1900 (class 2606 OID 16548)
-- Dependencies: 1548 1543 1870
-- Name: fk_runs_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runs
    ADD CONSTRAINT fk_runs_1 FOREIGN KEY (solution_id) REFERENCES solutions(solution_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1901 (class 2606 OID 16553)
-- Dependencies: 1548 1545 1872
-- Name: fk_runs_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runs
    ADD CONSTRAINT fk_runs_2 FOREIGN KEY (test_id) REFERENCES tests(test_id);


--
-- TOC entry 1891 (class 2606 OID 16472)
-- Dependencies: 1860 1543 1535
-- Name: fk_solutions_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY solutions
    ADD CONSTRAINT fk_solutions_1 FOREIGN KEY (username) REFERENCES users(login) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1892 (class 2606 OID 16477)
-- Dependencies: 1543 1866 1539
-- Name: fk_solutions_2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY solutions
    ADD CONSTRAINT fk_solutions_2 FOREIGN KEY (contest_id) REFERENCES contests(contest_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1893 (class 2606 OID 16482)
-- Dependencies: 1868 1541 1543
-- Name: fk_solutions_3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY solutions
    ADD CONSTRAINT fk_solutions_3 FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1894 (class 2606 OID 16487)
-- Dependencies: 1543 1536 1862
-- Name: fk_solutions_4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY solutions
    ADD CONSTRAINT fk_solutions_4 FOREIGN KEY (language_id) REFERENCES languages(language_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1895 (class 2606 OID 16505)
-- Dependencies: 1541 1868 1545
-- Name: fk_test_inputs_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT fk_test_inputs_1 FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1912 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2010-04-15 20:09:59

--
-- PostgreSQL database dump complete
--

