package app.student;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class StudentController implements Initializable {
    @FXML
    //private final Window win = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
    private Student student;

    public void printStudentInfo() {

        System.out.println(
                student.getIndex() + " "
                        + student.getFirstName() + " "
                        + student.getLastName() + " "
                        + student.getEmail()
        );
        List<Course> c = student.getCourses();
        for(Course o : c){
            System.out.print(o.courseName()+" ");
        }
        System.out.println();
    }


    public void initData(Student s){
        this.student = s;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        try {
//
//            student = (Student) FileUtility.readObject();
//            FileUtility.delete();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }
}
