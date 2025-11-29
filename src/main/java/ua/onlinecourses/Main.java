package ua.onlinecourses;

import ua.onlinecourses.config.AppConfig;
import ua.onlinecourses.exception.DataSerializationException;
import ua.onlinecourses.exception.InvalidDataException;
import ua.onlinecourses.model.*;
import ua.onlinecourses.persistence.PersistenceManager;
import ua.onlinecourses.repository.CourseRepository;
import ua.onlinecourses.repository.InstructorRepository;
import ua.onlinecourses.repository.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        try {
            AppConfig config = new AppConfig();
            PersistenceManager manager = new PersistenceManager(config);

            demonstrateValidStudentCreation();
            demonstrateInvalidStudentCreation();
            
            demonstrateValidCourseCreation();
            demonstrateInvalidCourseCreation();
            
            demonstrateValidInstructorCreation();
            demonstrateInvalidInstructorCreation();
            
            demonstrateValidModuleCreation();
            demonstrateInvalidModuleCreation();
            
            demonstrateRepositoryWithValidation(config, manager);

        } catch (DataSerializationException e) {
            logger.log(Level.SEVERE, "Serialization error: " + e.getMessage());
        }
    }

    private static void demonstrateValidStudentCreation() {

        try {
            Student student1 = new Student("Lesia", "Melnyk", "lesia.melnyk@chnu.edu.ua", LocalDate.of(2023, 9, 1));
            logger.log(Level.INFO, "Created student: {0} {1}", new Object[]{student1.firstName(), student1.lastName()});
            
            Student student2 = new Student("Ivan", "Bondaryk", "ivan.bondaryk@chnu.edu.ua", LocalDate.of(2024, 1, 15));
            logger.log(Level.INFO, "Created student: {0} {1}", new Object[]{student2.firstName(), student2.lastName()});
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, "Unexpected validation error: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateInvalidStudentCreation() {

        try {
            logger.log(Level.INFO, "Attempting to create student with empty name and invalid email...");
            new Student("", "Melnyk", "invalid-email", LocalDate.of(2023, 9, 1));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create student with future enrollment date...");
            new Student("Lesia", "Melnyk", "lesia.melnyk@chnu.edu.ua", LocalDate.now().plusYears(2));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create student with multiple invalid fields...");
            new Student("", "", "not-an-email", LocalDate.now().minusYears(10));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateValidCourseCreation() {

        try {
            Course course1 = new Course("Java Programming", "Learn Java basics and OOP", 5, LocalDate.now().plusMonths(2));
            logger.log(Level.INFO, "Created course: {0} ({1} credits)", new Object[]{course1.title(), course1.credits()});
            
            Course course2 = new Course("Data Structures", "Advanced algorithms", 4, LocalDate.now().plusMonths(3));
            logger.log(Level.INFO, "Created course: {0} ({1} credits)", new Object[]{course2.title(), course2.credits()});
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, "Unexpected validation error: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateInvalidCourseCreation() {

        try {
            logger.log(Level.INFO, "Attempting to create course with empty title and invalid credits...");
            new Course("", "Description", 10, LocalDate.now().plusMonths(1));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create course with start date too far in future...");
            new Course("Java", "Description", 3, LocalDate.now().plusYears(2));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create course with zero credits and empty description...");
            new Course("Python", "", 0, LocalDate.now().plusMonths(1));
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateValidInstructorCreation() {

        
        try {
            Instructor instructor1 = new Instructor("Igor", "Bylat", 34);
            logger.log(Level.INFO, "Created instructor: {0} {1} (expertise: {2})", 
                    new Object[]{instructor1.firstName(), instructor1.lastName(), instructor1.expertise()});
            
            Instructor instructor2 = new Instructor("Denys", "Malyk", 20);
            logger.log(Level.INFO, "Created instructor: {0} {1} (expertise: {2})", 
                    new Object[]{instructor2.firstName(), instructor2.lastName(), instructor2.expertise()});
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, "Unexpected validation error: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateInvalidInstructorCreation() {

        try {
            logger.log(Level.INFO, "Attempting to create instructor with empty name...");
            new Instructor("", "Smith", 15);
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create instructor with invalid expertise (0)...");
            new Instructor("John", "Doe", 0);
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create instructor with expertise > 60...");
            new Instructor("Jane", "Doe", 100);
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateValidModuleCreation() {

        try {
            myModule module1 = new myModule("Introduction", "Basic concepts and fundamentals");
            logger.log(Level.INFO, "Created module: {0}", module1.title());
            
            myModule module2 = new myModule("Advanced Topics", "Deep dive into complex subjects");
            logger.log(Level.INFO, "Created module: {0}", module2.title());
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, "Unexpected validation error: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateInvalidModuleCreation() {

        try {
            logger.log(Level.INFO, "Attempting to create module with empty title...");
            new myModule("", "Valid content");
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create module with title too long (>30 chars)...");
            new myModule("This title is way too long for a module name", "Content");
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        
        try {
            logger.log(Level.INFO, "Attempting to create module with empty title and content...");
            new myModule("", "");
        } catch (InvalidDataException e) {
            logger.log(Level.WARNING, "Validation error caught: {0}", e.getMessage());
        }
        logger.log(Level.INFO, "");
    }

    private static void demonstrateRepositoryWithValidation(AppConfig config, PersistenceManager manager) 
            throws DataSerializationException {

        StudentRepository studentRepo = new StudentRepository();
        CourseRepository courseRepo = new CourseRepository();
        InstructorRepository instructorRepo = new InstructorRepository();
        
        List<Student> validStudents = new ArrayList<>();
        validStudents.add(new Student("Lesia", "Melnyk", "lesia.melnyk@chnu.edu.ua", LocalDate.of(2023, 9, 1)));
        validStudents.add(new Student("Liliya", "Fivko", "liliya.fivko@student.ua", LocalDate.of(2023, 9, 5)));
        validStudents.add(new Student("Ivan", "Bondaryk", "ivan.bondaryk@chnu.edu.ua", LocalDate.of(2023, 9, 3)));
        
        for (Student student : validStudents) {
            studentRepo.add(student);
        }
        logger.log(Level.INFO, "Added {0} valid students to repository", studentRepo.size());
        
        List<Course> validCourses = new ArrayList<>();
        validCourses.add(new Course("Java Programming", "Java Basics", 5, LocalDate.now().plusMonths(1)));
        validCourses.add(new Course("Data Structures", "Algorithms", 3, LocalDate.now().plusMonths(2)));
        validCourses.add(new Course("Web Development", "HTML and CSS", 4, LocalDate.now().plusMonths(3)));
        
        for (Course course : validCourses) {
            courseRepo.add(course);
        }
        logger.log(Level.INFO, "Added {0} valid courses to repository", courseRepo.size());
        
        List<Instructor> validInstructors = new ArrayList<>();
        validInstructors.add(new Instructor("Igor", "Bylat", 34));
        validInstructors.add(new Instructor("Denys", "Malyk", 20));
        validInstructors.add(new Instructor("Inessa", "Kir", 39));
        
        for (Instructor instructor : validInstructors) {
            instructorRepo.add(instructor);
        }
        logger.log(Level.INFO, "Added {0} valid instructors to repository", instructorRepo.size());
        
        logger.log(Level.INFO, "");
        logger.log(Level.INFO, "Saving validated objects to files...");
        manager.save(studentRepo.getAll(), "students", Student.class, "JSON");
        manager.save(courseRepo.getAll(), "courses", Course.class, "JSON");
        manager.save(instructorRepo.getAll(), "instructors", Instructor.class, "JSON");
        logger.log(Level.INFO, "All objects saved successfully!");
        
        logger.log(Level.INFO, "");
    }
}
