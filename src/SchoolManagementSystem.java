import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This application will keep track of things like what classes are offered by
 * the school, and which students are registered for those classes and provide
 * basic reporting. This application interacts with a database to store and
 * retrieve data.
 */
public class SchoolManagementSystem {

    public static void getAllClassesByInstructor(String first_name, String last_name) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {

                //execute query
                sqlStatement = connection.createStatement();

                String sql = "SELECT instructors.first_name, instructors.last_name, academic_titles.title, classes.code, classes.name, terms.name AS term FROM class_sections INNER JOIN instructors ON class_sections.instructor_id = instructors.instructor_id INNER JOIN academic_titles ON instructors.academic_title_id = academic_titles.academic_title_id INNER JOIN classes ON class_sections.class_id = classes.class_id INNER JOIN terms ON class_sections.term_id = terms.term_id WHERE instructors.first_name = \"" + first_name +  "\" AND instructors.last_name = \"" + last_name + "\";";

                ResultSet results = sqlStatement.executeQuery(sql);

                System.out.println("First name | Last Name | Title | Code | Name | Term");
                System.out.println("----------------------------------------------------");

                //loop over results to print
                while (results.next()) {
                    String firstName = results.getString("first_name");
                    String lastName = results.getString("last_name");
                    String title = results.getString("title");
                    String code = results.getString("code");
                    String name = results.getString("name");
                    String term = results.getString("term");
                    System.out.println(firstName + " | " + lastName + " | " + title + " | " + code + " | " + name + " | " + term);
                }
                results.close();
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void submitGrade(String studentId, String classSectionID, String grade) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            //convert string to integer
            int id = Integer.parseInt(studentId);
            int sectionId = Integer.parseInt(classSectionID);
            connection = Database.getDatabaseConnection();
            if (connection != null) {
                //call createStatement
                sqlStatement = connection.createStatement();
                String sql = "UPDATE grades SET letter_grade='" + grade + "' WHERE grade_id = "
                        + "(SELECT grade_id FROM class_registration WHERE student_id=" + id + " AND class_section_id="
                        + sectionId + ")";
                //excecute update query
                sqlStatement.executeUpdate(sql);
                System.out.println("Grade has heen submitted!");
            } else {
                System.out.println("Connection failed!");
            }
            
        } catch (SQLException sqlException) {
            System.out.println("Failed to submit grade");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void registerStudent(String studentId, String classSectionID) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {

                //execute query
                sqlStatement = connection.createStatement();

                String sql = "INSERT INTO class_registrations (student_id, class_section_id) VALUES ('%s', '%s');";
                sql = String.format(sql, studentId, classSectionID);

                int results = sqlStatement.executeUpdate(sql);

                System.out.println("Class Registration ID | Student ID | Class Section ID");
                System.out.println("-------------------------------------------------------");
                System.out.println(results + " | " + studentId + " | " + classSectionID);

            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to register student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void deleteStudent(String studentId) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {

                //execute query
                sqlStatement = connection.createStatement();

                String sql = "DELETE FROM students WHERE student_id = %s;";
                sql = String.format(sql, studentId);

                sqlStatement.executeUpdate(sql);

                System.out.println("Student with id: " + studentId + " was deleted");

            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void createNewStudent(String firstName, String lastName, String birthdate) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {

                //execute query
                sqlStatement = connection.createStatement();

                String sql = "INSERT INTO students (first_name, last_name, birthdate) VALUES ('%s', '%s', '%s');";
                sql = String.format(sql, firstName, lastName, birthdate);

                int results = sqlStatement.executeUpdate(sql);

                System.out.println("Student ID | First Name | Last Name | Birthdate");
                System.out.println("-------------------------------------------------");
                System.out.println(results + " | " + firstName + " | " + lastName + " | " + birthdate);

            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to create student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void listAllClassRegistrations() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {

                //execute query
                sqlStatement = connection.createStatement();

                String sql = "SELECT students.student_id, class_sections.class_section_id, students.first_name, students.last_name, classes.code, classes.name, terms.name AS term, grades.letter_grade FROM class_registrations INNER JOIN students ON class_registrations.student_id = students.student_id INNER JOIN class_sections ON class_sections.class_section_id = class_registrations.class_section_id INNER JOIN classes ON classes.class_id = class_sections.class_id INNER JOIN terms ON terms.term_id = class_sections.term_id INNER JOIN grades ON grades.grade_id = class_registrations.grade_id GROUP BY students.student_id, class_sections.class_section_id ORDER BY grades.grade_id;";

                ResultSet results = sqlStatement.executeQuery(sql);

                System.out.println("Student ID | class_section_id | First Name | Last Name | Code | Name | Term | Letter Grade");
                System.out.println("-------------------------------------------------------------------------------------------");

                //loop over results to print
                while (results.next()) {
                    int studentID = results.getInt("student_id");
                    int classSectionID = results.getInt("class_section_id");
                    String firstName = results.getString("first_name");
                    String lastName = results.getString("last_name");
                    String code = results.getString("code");
                    String name = results.getString("name");
                    String term = results.getString("term");
                    String grade = results.getString("letter_grade");
                    System.out.println(studentID + " | " + classSectionID + " | " + firstName + " | " + lastName + " | " + code + " | " + name + " | " + term + " | " + grade);
                }
                results.close();
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClassSections() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {
                sqlStatement = connection.createStatement();
                // executeQuery: runs a SELECT statement and returns the results.

                String sql = "SELECT class_sections.class_section_id, classes.code, classes.name, terms.name AS term FROM class_sections INNER JOIN classes ON classes.class_id = class_sections.class_id INNER JOIN terms ON class_sections.term_id = terms.term_id GROUP BY class_sections.class_section_id;";

                ResultSet results = sqlStatement.executeQuery(sql);
                System.out.println("Class Section ID | Code | Name | term");
                System.out.println("------------------------------------------------");
                // Loop over the results
                while (results.next()) {
                    int classId = results.getInt("class_section_id");
                    String code = results.getString("code");
                    String name = results.getString("name");
                    String term = results.getString("term");
                    System.out.println(classId + " | " + code + " | " + name + " | " + term);
                }
                results.close();
            } else {
                System.out.println("Connection Failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClasses() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) {
                sqlStatement = connection.createStatement();
                // executeQuery: runs a SELECT statement and returns the results.
                ResultSet results = sqlStatement.executeQuery("SELECT * FROM classes");
                System.out.println("Class ID | Code | Name | Description");
                System.out.println("------------------------------------------------");
                // Loop over the results
                while (results.next()) {
                    int classId = results.getInt("class_id");
                    String code = results.getString("code");
                    String name = results.getString("name");
                    String description = results.getString("description");
                    System.out.println(classId + " | " + code + " | " + name + " | " + description);
                }
                results.close();
            } else {
                System.out.println("Connection Failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void listAllStudents() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            // Connect to database
            connection = Database.getDatabaseConnection();
            if (connection != null) { 
                sqlStatement = connection.createStatement();
                // executeQuery: runs a SELECT statement and returns the results.
                ResultSet results = sqlStatement.executeQuery("SELECT * FROM students");
                System.out.println("Student ID | First Name | Last Name | Birthdate");
                System.out.println("------------------------------------------------");
                // Loop over the results
                while (results.next()) {
                    int studentID = results.getInt("student_id");
                    String firstName = results.getString("first_name");
                    String lastName = results.getString("last_name");
                    Date birthDate = results.getDate("birthdate");
                    System.out.println(studentID + " | " + firstName + " | " + lastName + " | " + birthDate);
                }
                results.close();
            } else {
                System.out.println("Connection Failed!");
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /***
     * Splits a string up by spaces. Spaces are ignored when wrapped in quotes.
     *
     * @param command - School Management System cli command
     * @return splits a string by spaces.
     */
    public static List<String> parseArguments(String command) {
        List<String> commandArguments = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) commandArguments.add(m.group(1).replace("\"", ""));
        return commandArguments;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the School Management System");
        System.out.println("-".repeat(80));

        Scanner scan = new Scanner(System.in);
        String command = "";

        do {
            System.out.print("Command: ");
            command = scan.nextLine();
            ;
            List<String> commandArguments = parseArguments(command);
            command = commandArguments.get(0);
            commandArguments.remove(0);

            if (command.equals("help")) {
                System.out.println("-".repeat(38) + "Help" + "-".repeat(38));
                System.out.println("test connection \n\tTests the database connection");

                System.out.println("list students \n\tlists all the students");
                System.out.println("list classes \n\tlists all the classes");
                System.out.println("list class_sections \n\tlists all the class_sections");
                System.out.println("list class_registrations \n\tlists all the class_registrations");
                System.out.println("list instructor <first_name> <last_name>\n\tlists all the classes taught by that instructor");


                System.out.println("delete student <studentId> \n\tdeletes the student");
                System.out.println("create student <first_name> <last_name> <birthdate> \n\tcreates a student");
                System.out.println("register student <student_id> <class_section_id>\n\tregisters the student to the class section");

                System.out.println("submit grade <studentId> <class_section_id> <letter_grade> \n\tcreates a student");
                System.out.println("help \n\tlists help information");
                System.out.println("quit \n\tExits the program");
            } else if (command.equals("test") && commandArguments.get(0).equals("connection")) {
                Database.testConnection();
            } else if (command.equals("list")) {
                if (commandArguments.get(0).equals("students")) listAllStudents();
                if (commandArguments.get(0).equals("classes")) listAllClasses();
                if (commandArguments.get(0).equals("class_sections")) listAllClassSections();
                if (commandArguments.get(0).equals("class_registrations")) listAllClassRegistrations();

                if (commandArguments.get(0).equals("instructor")) {
                    getAllClassesByInstructor(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("create")) {
                if (commandArguments.get(0).equals("student")) {
                    createNewStudent(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("register")) {
                if (commandArguments.get(0).equals("student")) {
                    registerStudent(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("submit")) {
                if (commandArguments.get(0).equals("grade")) {
                    submitGrade(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("delete")) {
                if (commandArguments.get(0).equals("student")) {
                    deleteStudent(commandArguments.get(1));
                }
            } else if (!(command.equals("quit") || command.equals("exit"))) {
                System.out.println(command);
                System.out.println("Command not found. Enter 'help' for list of commands");
            }
            System.out.println("-".repeat(80));
        } while (!(command.equals("quit") || command.equals("exit")));
        System.out.println("Bye!");
    }
}

