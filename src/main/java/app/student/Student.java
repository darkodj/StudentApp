package app.student;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student implements Serializable {
    private String index;
    private String firstName;
    private String lastName;

    private String DOB;

    private String email;
    private Map<Course, Integer> courseGrade;
    private List<Course> courses;
    public Student(String index, String firstName, String lastName, String DOB, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.index = index;
        this.DOB = DOB;
        this.email = email;
        courseGrade = new HashMap<>();
        courses = new ArrayList<>();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIndex() {
        return index;
    }


    public String getEmail() {
        return email;
    }


    public String getDOB() {
        return DOB;
    }

    public Map<Course, Integer> getCourseGrade() {
        return courseGrade;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(String courseName) {
        this.courses.add(new Course(courseName));
    }
    public Course getCourse(String s){

        for(Course c: courseGrade.keySet()){
            if(c.courseName().equals(s)) return c;
        }
        return null;
    }
    public void addCourseWithGrade(Course course, int grade) {
        this.courseGrade.putIfAbsent(course, grade);
    }
    public void addGradeToCourse(Course c, int v){
        courseGrade.put(c, v);

    }
    public void setCourseGrade(Map<Course, Integer> courseGrade) {

        for (Course c : courseGrade.keySet()) {
            this.courseGrade.put(c, courseGrade.get(c));
            addCourse(c.courseName());
        }
    }

    public void printAllCourses() {
        for (Course c : courseGrade.keySet()) {
            int grade = courseGrade.get(c);
            System.out.println(c.courseName() + " " + (grade>0?grade:"No grade"));
        }
    }


    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + ", " + this.index + ", " + this.DOB;
    }
}
