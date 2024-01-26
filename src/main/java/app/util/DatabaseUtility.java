package app.util;

import app.student.Course;
import app.student.Student;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUtility {
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_USERNAME = "admin";
	private static final String DB_PASSWORD = "admin";
	
	public DatabaseUtility() {
	}
	
	public static Connection connectTo(String dbName) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL + dbName, DB_USERNAME, DB_PASSWORD);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static String loginTo(Connection connection, String username, String password) throws SQLException {
		PreparedStatement psLogin = connection.prepareStatement("select * from login_data where username = ? and " +
				"password = ?");
		psLogin.setString(1, username);
		psLogin.setString(2, password);
		
		ResultSet rsLogin = psLogin.executeQuery();
		if (!rsLogin.next()) return null;
		String res = rsLogin.getString("account_type");
		rsLogin.close();
		return res;
	}
	
	public static Student extractStudentData(Connection con, String studentID) throws SQLException {
		final String sqlStudentByID = "select * from students where student_id = ?";
		final String sqlCoursesById = "select * from courseswithstudentid where student_id = ?";
		PreparedStatement psStudentByID = con.prepareStatement(sqlStudentByID);
		PreparedStatement psCoursesByID = con.prepareStatement(sqlCoursesById);
		
		psStudentByID.setString(1, studentID);
		psCoursesByID.setString(1, studentID);
		
		ResultSet rsStudentByID = psStudentByID.executeQuery();
		ResultSet rsCoursesByID = psCoursesByID.executeQuery();
		Student student = null;
		if (rsStudentByID.next()) {
			student = new Student(
					rsStudentByID.getString("student_id"),
					rsStudentByID.getString("firstName"),
					rsStudentByID.getString("lastName"),
					rsStudentByID.getString("DOB"),
					rsStudentByID.getString("email")
			);
		}
		String courseListRaw = "";
		if (rsCoursesByID.next()) {
			courseListRaw = rsCoursesByID.getString("courses_list");
		}
		Map<Course, Integer> courses = processCourseData(courseListRaw);
		if (student != null) student.setCourseGrade(courses);
		rsStudentByID.close();
		rsCoursesByID.close();
		return student;
	}
	
	public static void updateCourseDB(String studentID, Map<Course, Integer> map) {
		try (Connection connection = DatabaseUtility.connectTo("test")) {
			String sql = "update test.courseswithstudentid set courses_list = ? where student_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			//System.out.println(wrapCourseData(map));
			preparedStatement.setString(1,wrapCourseData(map));
			preparedStatement.setString(2,studentID);
			int executeUpdate = preparedStatement.executeUpdate();
			//System.out.println(executeUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static Map<Course, Integer> processCourseData(String string) {
		String[] courseWithGrade = string.split(":");
		Map<Course, Integer> courses = new HashMap<>();
		
		for (String s : courseWithGrade) {
			String[] t = s.split(",");
			courses.putIfAbsent(new Course(t[0]), Integer.valueOf(t[1]));
		}
		
		return courses;
	}
	
	private static String wrapCourseData(Map<Course, Integer> map) {
		StringBuilder result = new StringBuilder();
		for (Course c : map.keySet()) {
			result.append(c.courseName()).append(",").append(map.get(c));
			result.append(":");
		}
		int i = result.lastIndexOf(":");
		result.charAt(i);
		result.deleteCharAt(i);
		return result.toString();
	}
	
	
}
