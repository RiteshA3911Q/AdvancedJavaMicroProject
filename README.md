# Student Marks Management System

A desktop-based **Student Marks Management System** built with Java Swing, JDBC, and MySQL. This Advanced Java Micro Project demonstrates GUI development, database connectivity, validation, result calculation, and CRUD operations.

## Features

* Java Swing GUI with **Input** and **Display** tabs
* Student ID and name management
* Five-subject marks entry
* Automatic total, percentage, and grade calculation
* Create, Read, Update, Delete operations
* Search by Student ID and refresh records
* JTable-based data display
* Automatic MySQL database/table creation
* Input validation and error dialogs
* JDBC `PreparedStatement` queries

## Technologies

| Technology        | Purpose                 |
| ----------------- | ----------------------- |
| Java              | Application development |
| Java Swing        | GUI                     |
| JDBC              | Database connectivity   |
| MySQL             | Data storage            |
| PreparedStatement | Parameterized SQL       |
| JTable            | Record display          |
| Git / GitHub      | Version control         |

## Project Structure

```text
AdvancedJavaMicroProject/
├── Database.java
├── MarksManagement.java
├── README.md
└── .gitignore
```

**`MarksManagement.java`** builds the GUI, validates input, calculates results, handles buttons, performs searches, and displays database records.
**`Database.java`** manages MySQL connections, database/table creation, inserts, updates, deletes, searches, and record retrieval.

## Architecture

```text
User → Java Swing GUI → MarksManagement.java → Database.java → JDBC → MySQL
```

The GUI collects input, the main class applies validation/result logic, and the database class handles persistent storage.

## Database Design

The application automatically creates:

```text
Database: marks_management
Table: students
```

| Column                | Type         | Description             |
| --------------------- | ------------ | ----------------------- |
| `student_id`          | VARCHAR(20)  | Primary key             |
| `student_name`        | VARCHAR(100) | Student name            |
| `subject1`–`subject5` | INT          | Marks for five subjects |
| `total`               | INT          | Total marks             |
| `percentage`          | DECIMAL(5,2) | Percentage              |
| `grade`               | VARCHAR(2)   | Final grade             |

## Result Calculation

Each subject is out of 100, so the maximum total is 500.

```text
Total = Subject1 + Subject2 + Subject3 + Subject4 + Subject5
Percentage = Total / 5
```

Example: `80 + 75 + 90 + 85 + 70 = 400` → `80.00%`.

### Grading System

|  Percentage | Grade |
| ----------: | :---: |
| 90 or above |   A+  |
|    80–89.99 |   A   |
|    70–79.99 |   B   |
|    60–69.99 |   C   |
|    50–59.99 |   D   |
|    Below 50 |   F   |

## Requirements

Install Java Development Kit (JDK), MySQL Server, MySQL Connector/J, and a terminal or Java IDE. MySQL must be running before starting the application.

## MySQL Configuration

Current settings in `Database.java`:

```text
Host: localhost
Port: 3306
Username: root
Password: empty
Database: marks_management
```

If your MySQL account uses a password, update the credentials in `Database.java`. For production software, credentials should be stored outside source code.

## JDBC Driver

The project requires **MySQL Connector/J**. The JAR is excluded by `.gitignore` and should be kept locally. The commands below use `mysql-connector-j-26.7.0.jar`; replace it with your installed version when necessary.

## Installation and Running

Clone the repository:

```bash
git clone https://github.com/RiteshA3911Q/AdvancedJavaMicroProject.git
cd AdvancedJavaMicroProject
```

Compile:

```bash
javac -cp mysql-connector-j-26.7.0.jar -d . MarksManagement.java Database.java
```

Linux/macOS:

```bash
java -cp ".:mysql-connector-j-26.7.0.jar" MarksManagement
```

Windows:

```cmd
java -cp ".;mysql-connector-j-26.7.0.jar" MarksManagement
```

## Application Interface

### Input Tab

Contains Student Information, five subject fields, calculated Result fields, and **Calculate**, **Save**, **Update**, **Delete**, and **Clear** buttons.

### Display Tab

Contains the student table, Student ID search field, **Search**, and **Refresh** buttons.

## Add a Student

1. Enter Student ID and Student Name.
2. Enter marks for all five subjects.
3. Click **Calculate** and review the result.
4. Click **Save**.
   The record is inserted into MySQL; after success, the list refreshes and the form is cleared.

## Update a Student

1. Enter an existing Student ID.
2. Enter the updated name and marks.
3. Click **Calculate**.
4. Click **Update**.
   If the ID exists, its record is updated; otherwise an error is displayed.

## Delete a Student

1. Enter the Student ID.
2. Click **Delete**.
   The matching record is removed and the table/form are refreshed.

## Search and Refresh

Use **Search** to find a specific Student ID. An empty search field loads all records. **Refresh** clears the search field and reloads the complete list.

## Validation

* Student ID cannot be empty.
* Student Name cannot be empty.
* Marks must be whole numbers between `0` and `100`.
* Update/Delete require a Student ID.
* Missing records produce an error message.

## CRUD and JDBC

| Operation                                                                                                                                                                                | Method            |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- |
| Create                                                                                                                                                                                   | `saveStudent()`   |
| Read                                                                                                                                                                                     | `getStudents()`   |
| Update                                                                                                                                                                                   | `updateStudent()` |
| Delete                                                                                                                                                                                   | `deleteStudent()` |
| `PreparedStatement` is used for insert, update, delete, and search operations. Parameterized SQL keeps user input separate from query structure and helps protect against SQL injection. |                   |

## Error Handling

The application handles invalid input and `SQLException` failures with Swing dialogs. Errors include invalid marks, missing IDs, missing records, duplicate primary keys, and database connection failures.

## Troubleshooting

**MySQL connection fails:** Check that MySQL is running and `localhost:3306`, username, and password are correct.
**JDBC driver error:** Ensure the Connector/J JAR is present and included in both compile and run commands.
**Duplicate Student ID:** `student_id` is the primary key; use **Update** for existing students.
**No records shown:** Click **Refresh** and verify data in `marks_management.students`.

## Limitations

* Fixed five-subject structure and grading rules
* No authentication or user roles
* No attendance module
* No PDF/Excel export
* Database credentials currently stored in source code
* Desktop-only Swing interface
* No external configuration system

## Future Improvements

* Login and role-based access
* Dynamic subjects
* Name search and advanced filters
* PDF mark-sheet generation
* CSV/Excel export
* Performance and grade charts
* Attendance management
* External database configuration
* Improved UI themes and reporting

## Learning Outcomes

This project provides practical experience with Core Java, OOP, Java Swing, event handling, JDBC, MySQL, SQL, `PreparedStatement`, CRUD operations, validation, exception handling, JTable, and basic application architecture.

## Workflow

```text
Start
  ↓
Connect to MySQL
  ↓
Create Database/Table if Required
  ↓
Enter Student Details + Marks
  ↓
Calculate Result
  ↓
Save / Update / Delete
  ↓
View / Search / Refresh Records
```

## Repository

https://github.com/RiteshA3911Q/AdvancedJavaMicroProject

## Author

**RiteshA3911Q** — Advanced Java Micro Project using Java Swing, JDBC, and MySQL.

## License

This repository currently does not contain a dedicated `LICENSE` file and is intended primarily for educational and academic use.
