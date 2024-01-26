package app.app;

import app.student.StudentController;
import app.util.DatabaseUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblDBConnStatus;
    @FXML
    private Label lblLoginEmptyFields;
    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private Connection connection;
    private String username;

    public void onLogin() throws IOException, SQLException {

        if (txtUsername.getText().isEmpty() && txtPassword.getText().isEmpty()) {
            lblLoginEmptyFields.setText("PLEASE ENTER USERNAME AND PASSWORD TO LOGIN");
            lblLoginEmptyFields.setTextFill(Color.rgb(255, 0, 0));
        } else if (txtUsername.getText().isEmpty()) {
            lblLoginEmptyFields.setText("PLEASE ENTER USERNAME TO LOGIN");
            lblLoginEmptyFields.setTextFill(Color.rgb(255, 0, 0));

        } else if (txtPassword.getText().isEmpty()) {
            lblLoginEmptyFields.setText("PLEASE ENTER PASSWORD TO LOGIN");
            lblLoginEmptyFields.setTextFill(Color.rgb(255, 0, 0));
        } else {
            lblLoginEmptyFields.setText("");
            username = txtUsername.getText().toLowerCase();
            String password = txtPassword.getText().toLowerCase();
            String accountType = DatabaseUtility.loginTo(
                    connection, username, password
            );
            if (accountType != null) loadScene(accountType);
            else loginError();
        }
    }

    public void loadScene(String scene) throws SQLException, IOException {
        switch (scene) {
            case "ADMIN" -> {
                Stage adminStage = (Stage) btnLogin.getScene().getWindow();
                FXMLLoader adminScene = new FXMLLoader(
                        getClass().getResource("/app/admin/AdminView.fxml"));
                adminStage.setScene(new Scene(adminScene.load()));
                adminStage.setTitle("ADMIN PANEL");
                connection.close();
            }
            case "STUDENT" -> {

//                FileUtility.writeObject(DatabaseUtility.extractStudentData(connection, username));
                Stage studentStage = (Stage) btnLogin.getScene().getWindow();
                FXMLLoader studentLoader = new FXMLLoader(
                        getClass().getResource("/app/student/StudentView.fxml"));
                studentStage.setScene(new Scene(studentLoader.load()));
                StudentController studentController = studentLoader.getController();
                studentController.initData(DatabaseUtility.extractStudentData(connection,username));
                studentStage.setTitle("STUDENT PANEL");
                connection.close();
            }
        }
    }

    public void loginError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Wrong username/password");
        alert.setTitle("Error");
        alert.show();
    }

    public void DBConMsg(boolean b) {
        String message = b ? "connected" : "connection failed";
        Color c = b ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0);
        lblDBConnStatus.setText(message);
        lblDBConnStatus.setTextFill(c);
        btnLogin.setDisable(!b);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DatabaseUtility.connectTo("test");
        boolean connected = connection != null;
        DBConMsg(connected);
//        try {
//            FileUtility.createFile();
//            FileUtility.doe();
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void DEBUG_ADMIN() throws SQLException, IOException {
        loadScene("ADMIN");
    }

    public void DEBUG_STUDENT() throws SQLException, IOException {
        txtUsername.setText("mi00000");
        txtPassword.setText("1111");
        onLogin();
    }
}