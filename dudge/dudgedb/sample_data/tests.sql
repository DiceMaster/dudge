delete from public.tests;

INSERT INTO tests (problem_id , test_number, input_data , output_data)
VALUES (1 , 1 , E'1 2\n' , E'3\n'); 

INSERT INTO tests (problem_id , test_number, input_data , output_data)
VALUES (1 , 2 , E'100 1000\n' , E'1100\n'); 

INSERT INTO tests (problem_id , test_number, input_data , output_data)
VALUES (1 , 3 , E'0 50\n' , E'50\n'); 

INSERT INTO tests (problem_id , test_number, input_data , output_data)
VALUES (1 , 4 , E'-2 7\n' , E'5\n');
