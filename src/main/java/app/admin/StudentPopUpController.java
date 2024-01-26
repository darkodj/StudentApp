package app.admin;

import app.student.Course;
import app.student.Student;
import app.util.DatabaseUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentPopUpController {
	@FXML
	TableView<CourseNameAndGrade> tbStudentData;
	@FXML
	TableColumn<CourseNameAndGrade, String> tbcSubject;
	@FXML
	TableColumn<CourseNameAndGrade, String> tbcGrade;
	private Student student = null;
	@FXML
	private Label lblSPStudentDOB;
	@FXML
	private Label lblSPStudentEmail;
	@FXML
	private Label lblSPStudentFirstName;
	@FXML
	private Label lblSPStudentLastName;
	@FXML
	private Label lblSPStudentIndex;
	@FXML
	private TextField tfAddCourse;
	@FXML
	private TextField tfAddGrade;
	public void initData(Student s) {
		student = s;
		lblSPStudentFirstName.setText(s.getFirstName());
		lblSPStudentLastName.setText(s.getLastName());
		lblSPStudentDOB.setText(s.getDOB());
		lblSPStudentEmail.setText(s.getEmail());
		lblSPStudentIndex.setText(s.getIndex());
		
		tbcSubject.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		tbcGrade.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
		populateTable();
		
	}
	
	public void updateGrade() {
		CourseNameAndGrade cg = tbStudentData.getSelectionModel().getSelectedItem();
		if (cg != null) {
			AtomicInteger v = new AtomicInteger();
			TextInputDialog inputGrade = new TextInputDialog("Enter grade");
			inputGrade.setHeaderText("Grade:");
			Optional<String> res = inputGrade.showAndWait();
			res.ifPresent(grade -> v.set(Integer.parseInt(grade)));
			
			Course c = student.getCourse(cg.courseName());
			if (c != null && (v.get() > 5 && v.get() < 11)){
				student.addGradeToCourse(c, v.get());
				writeGradeToDB(student);
				student = updateStudentRef();
			}
			tbStudentData.getItems().clear();
			populateTable();
		}
		
	}
	private Student updateStudentRef(){
		Connection connection = DatabaseUtility.connectTo("test");
		try {
			return DatabaseUtility.extractStudentData(connection,student.getIndex());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	private void writeGradeToDB(Student s){
		DatabaseUtility.updateCourseDB(s.getIndex(),s.getCourseGrade());
	}
	private void populateTable() {
		
		ObservableList<CourseNameAndGrade> coursesAndGradesList =
				FXCollections.observableArrayList(retrieveCourseGrades());
		tbStudentData.setItems(coursesAndGradesList);
	}
	
	private ArrayList<CourseNameAndGrade> retrieveCourseGrades() {
		ArrayList<CourseNameAndGrade> courseList = new ArrayList<>();
		Map<Course, Integer> courseMap = student.getCourseGrade();
		for (Course c : courseMap.keySet()) {
			courseList.add(new CourseNameAndGrade(c.courseName(), String.valueOf(courseMap.get(c))));
		}
		return courseList;
	}
	
	
}
