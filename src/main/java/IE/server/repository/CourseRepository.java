package IE.server.repository;

import IE.server.repository.models.CourseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseRepository {
    private static CourseRepository instance;

    private CourseRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Course(" +
                        "code varchar(255)," +
                        "classCode varchar(255)," +
                        "`name` varchar(255)," +
                        "units int," +
                        "`type` varchar(255)," +
                        "instructor varchar(255)," +
                        "capacity int," +
                        "classTimeStart varchar(255)," +
                        "classTimeEnd varchar(255)," +
                        "examTimeStart varchar(255)," +
                        "examTimeEnd varchar(255)," +
                        "PRIMARY KEY (code, classCode));"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static CourseRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CourseRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(CourseDAO courseDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO Course VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        st.setString(1, courseDAO.getCode());
        st.setString(2, courseDAO.getClassCode());
        st.setString(3, courseDAO.getName());
        st.setString(4, String.valueOf(courseDAO.getUnits()));
        st.setString(5, courseDAO.getType());
        st.setString(6, courseDAO.getInstructor());
        st.setString(7, String.valueOf(courseDAO.getCapacity()));
        st.setString(8, courseDAO.getClassTimeStart());
        st.setString(9, courseDAO.getClassTimeEnd());
        st.setString(10, courseDAO.getExamTimeStart());
        st.setString(11, courseDAO.getExamTimeEnd());
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }
}
