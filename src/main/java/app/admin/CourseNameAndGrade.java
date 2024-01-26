package app.admin;

public record CourseNameAndGrade(String courseName, String courseGrade) {
	@Override
	public String courseGrade() {
		return Integer.parseInt(courseGrade) > 0 ? courseGrade : "Nema ocenu";
	}
}
