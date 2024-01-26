package app.admin;

import app.student.Student;
import app.util.DatabaseUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private Button btnSignOut;
    @FXML
    private Button btnShowStudent;
    @FXML
    private ListView<Student> listView = new ListView<>();
    private final Window win = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);


    public List<Student> loadData() throws SQLException {
        Connection connection = DatabaseUtility.connectTo("test");
        final String sqlStudent = "select student_id from students";

        PreparedStatement statement = connection.prepareStatement(sqlStudent);
        ResultSet resultSet = statement.executeQuery();

        List<Student> list = new ArrayList<>();

        while (resultSet.next()) {
            Student student = DatabaseUtility.extractStudentData(
                    connection,
                    resultSet.getString("student_id")
            );
            list.add(student);
        }
        resultSet.close();
        connection.close();
        return list;
    }

    public void addToView() throws SQLException {
        List<Student> list = loadData();
        listView.getItems().clear();
        listView.getItems().addAll(list);
        focus();
    }

    public void printSelectedListItem() {
        Student s = listView.getSelectionModel().getSelectedItem();
        if (s == null) {
            System.out.println("Nista nije selektovano");
            return;
        }
        s.printAllCourses();
        focus();
    }

    private void focus() {
        listView.getSelectionModel().clearSelection();
        win.requestFocus();
    }

    public void signOut() throws IOException {
        FXMLLoader mainScene = new FXMLLoader(
                getClass().getResource("/app/app/MainView.fxml"));
        Stage mainStage = (Stage) btnSignOut.getScene().getWindow();
        mainStage.setTitle("Student Management System");
        mainStage.setScene(new Scene(mainScene.load()));

    }

    ////////////////////////////////////////////////////
    //                Student PopUp Window            //
    ////////////////////////////////////////////////////


    public void showStudentPopup() throws IOException {

        FXMLLoader studentPopup = new FXMLLoader(getClass()
                .getResource("/app/admin/StudentPopUpInfoView.fxml"));
        Scene studentPopupScene = new Scene(studentPopup.load());
        Stage studentPopupStage = new Stage();
        studentPopupStage.initModality(Modality.APPLICATION_MODAL);
        studentPopupStage.initOwner(btnShowStudent.getScene().getWindow());
        studentPopupStage.setTitle("Student INFO");
        studentPopupStage.setScene(studentPopupScene);
        StudentPopUpController studentPopUpController = studentPopup.getController();
        Student s = listView.getSelectionModel().getSelectedItem();
        if(s != null) {
            studentPopupStage.show();
            studentPopUpController.initData(s);
        }
        else noStudentSelected();



    }


    private void noStudentSelected() {
        Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.CLOSE);
        alert.setContentText("No student selected!");
        alert.setTitle("Error");
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addToView();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
