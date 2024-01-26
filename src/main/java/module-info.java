module app.studentmsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens app.app to javafx.fxml;
    exports app.app;
    exports app.admin;
    opens app.admin to javafx.fxml;
    exports app.student;
    opens app.student to javafx.fxml;

}