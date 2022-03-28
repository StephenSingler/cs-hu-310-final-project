/* Put your final project reporting queries here */
USE cs_hu_310_final_project;

-- Calculate the GPA for a student given a student_id (use student_id=1)
SELECT
	students.first_name,
	students.last_name,
	COUNT(class_registrations.class_registration_id) AS number_of_classes,
	SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points_earned,
	AVG(convert_to_grade_point(grades.letter_grade)) AS GPA
FROM
	students,
	grades,
	class_registrations
WHERE
	students.student_id = class_registrations.student_id AND grades.grade_id = class_registrations.grade_id AND students.student_id = 1;


-- Calculate the GPA for each student (across all classes and all terms)
SELECT
	students.first_name,
	students.last_name,
	COUNT(class_registrations.class_registration_id) AS number_of_classes,
	SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points_earned,
	AVG(convert_to_grade_point(grades.letter_grade)) AS GPA
FROM
	students,
	grades,
	class_registrations
WHERE
	students.student_id = class_registrations.student_id AND grades.grade_id = class_registrations.grade_id
GROUP BY 
	students.student_id;

-- Calculate the avg GPA for each class
SELECT 
	classes.code, 
    classes.name, 
    COUNT(class_registrations.grade_id) AS number_of_grades, 
    SUM(convert_to_grade_point(grades.letter_grade)) AS "total_grade_points_earned", 
    AVG(convert_to_grade_point(grades.letter_grade)) AS "AVG GPA"
FROM
	grades
INNER JOIN 
	class_registrations ON grades.grade_id = class_registrations.grade_id
INNER JOIN
	class_sections ON class_registrations.class_section_id = class_sections.class_section_id
INNER JOIN
	classes ON classes.class_id = class_sections.class_id
GROUP BY 
	classes.class_id;

-- Calculate the avg GPA for each class and term
SELECT 
	classes.code,
    classes.name,
    terms.name,
    COUNT(class_registrations.grade_id) AS number_of_grades, 
    SUM(convert_to_grade_point(grades.letter_grade)) AS "total_grade_points_earned", 
    AVG(convert_to_grade_point(grades.letter_grade)) AS "AVG GPA"
FROM
	grades
INNER JOIN 
	class_registrations ON grades.grade_id = class_registrations.grade_id
INNER JOIN
	class_sections ON class_registrations.class_section_id = class_sections.class_section_id
INNER JOIN
	classes ON classes.class_id = class_sections.class_id
INNER JOIN
	terms ON class_sections.term_id = terms.term_id
GROUP BY 
	classes.code, classes.name, terms.name;

-- List all the classes being taught by an instructor (use instructor_id = 1)
SELECT
	instructors.first_name,
    instructors.last_name,
    academic_titles.title,
    classes.code,
    classes.name,
    terms.name
FROM
	class_sections
INNER JOIN
	instructors ON class_sections.instructor_id = instructors.instructor_id
INNER JOIN
	academic_titles ON instructors.academic_title_id = academic_titles.academic_title_id
INNER JOIN
	classes ON class_sections.class_id = classes.class_id
INNER JOIN
	terms ON class_sections.term_id = terms.term_id
WHERE
	class_sections.instructor_id = 1;

-- List all classes with terms & instructor
SELECT
    classes.code,
    classes.name,
    terms.name,
    instructors.first_name,
    instructors.last_name
FROM 
	class_sections
INNER JOIN
    classes ON class_sections.class_id = classes.class_id
INNER JOIN
    instructors ON class_sections.instructor_id = instructors.instructor_id
INNER JOIN
    terms ON class_sections.term_id = terms.term_id

-- Calculate the remaining space left in a class
SELECT 
	classes.code, 
    classes.name, 
    terms.name as term, 
    COUNT(class_registrations.student_id) AS enrolled_students, 
    ANY_VALUE(classes.maximum_students - COUNT(class_registrations.student_id)) AS space_remaining 
FROM 
	classes
INNER JOIN
	class_sections ON classes.class_id = class_sections.class_id 
INNER JOIN
	class_registrations ON class_sections.class_section_id = class_registrations.class_section_id
INNER JOIN
	terms ON class_sections.term_id = terms.term_id
group by 
	classes.code, terms.name, classes.name;