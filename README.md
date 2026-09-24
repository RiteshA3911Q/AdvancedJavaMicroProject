```markdown
# Student Marks Management System

A simple Java Swing application for managing student marks using MySQL and JDBC. This project is designed for a diploma-level Advanced Java microproject, focusing on a clean GUI and basic CRUD operations.

## Features
- **GUI Interface**: Clean and organized layout with two tabs for input and display
- **Database Integration**: Automatic creation of MySQL database and table
- **CRUD Operations**: Create, Read, Update, Delete student records
- **Result Calculation**: Total, percentage, and grade calculation
- **Search & Refresh**: Search students by ID and refresh displayed records

## Project Structure
```
MarksManagement/
├── MarksManagement.java
└── Database.java
```

## Setup Instructions
1. **Database Setup**:
   - Ensure MySQL server is running
   - Create a new database called `marks_management`
   - The application will automatically create the `students` table

2. **Compile & Run**:
   ```bash
   javac -d . MarksManagement.java Database.java
   java MarksManagement
   ```

## Usage
### Input Tab
- Enter student details and marks (0-100)
- Click **Calculate** to get total, percentage, and grade
- Use **Save**, **Update**, or **Delete** to manage records
- **Clear** to reset input fields

### Display Tab
- Search students by ID and refresh the table
- View all student records with marks, totals, and grades

## Notes
- This project uses only Java Swing and JDBC with no external libraries
- Database operations are handled in `Database.java`
- GUI layout uses `GridBagLayout` for proper alignment
- All functionality is contained within the two provided files

## License
MIT
```