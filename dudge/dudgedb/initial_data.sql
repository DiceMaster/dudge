INSERT INTO users (login, pwd_hash, real_name , organization , register_date , age , icq_number , email, is_admin)
VALUES ('admin', MD5('admin'), 'Admin' , 'Adminov' , '2007-08-15' , 30 , 777777777 , 'none@none.no', 'TRUE');

INSERT INTO languages (language_id, title, description, file_extension, compilation_cmd, execution_cmd)
VALUES ('gcc', 'GNU C++', 'GNU C++ Compiler', '.cpp',
'g++ -static -static-libgcc -pipe -O0 -o ${PROG.EXENAME} ${PROG.SRCNAME}',
'${PROG.TESTDIR}/${PROG.EXENAME}');

INSERT INTO languages (language_id, title, description, file_extension, compilation_cmd, execution_cmd)
VALUES ('java', 'Java', 'Sun JDK 6', '.java',
'javac ${PROG.SRCNAME}',
'java -cp ${PROG.TESTDIR} "${PROG.TESTDIR}/${PROG.NAME}"');

INSERT INTO languages (language_id, title, description, file_extension, compilation_cmd, execution_cmd)
VALUES ('fpc', 'Object Pascal (FPC)', 'Free Pascal Compiler', '.pas',
'fpc -XS -Xt -Os -Sd -WC -o${PROG.NAME} ${PROG.SRCNAME}',
'${PROG.TESTDIR}/${PROG.EXENAME}');

INSERT INTO languages (language_id, title, description, file_extension, compilation_cmd, execution_cmd)
VALUES ('csharp', 'C#', 'Microsoft Visual C# 2010 Express', '.cs',
'csc.exe /out:${PROG.EXENAME} /target:winexe /main:DudgeProgram /nologo C:\dudgestuff\ndudge\dudgedb\csharplauncher\launcher.cs ${PROG.SRCNAME}',
'${PROG.TESTDIR}/${PROG.EXENAME}');


INSERT INTO contests (caption, description, con_type, start_time, duration)
VALUES ('Example ACM contest' , 'Test ACM contest.' , 'ACM', 'yesterday', 0);

INSERT INTO params (pname, pvalue)
VALUES ('default_contest', (SELECT last_value FROM contests_contest_id_seq));

INSERT INTO params (pname, pvalue)
VALUES ('version', '4');