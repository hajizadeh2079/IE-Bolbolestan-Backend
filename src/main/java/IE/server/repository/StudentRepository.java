package IE.server.repository;

import IE.server.repository.models.StudentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentRepository {
    private static StudentRepository instance;

    private StudentRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Student(" +
                        "id varchar(255)," +
                        "`name` varchar(255)," +
                        "secondName varchar(255)," +
                        "email varchar(255)," +
                        "password varchar(255)," +
                        "birthDate varchar(255)," +
                        "field varchar(255)," +
                        "faculty varchar(255)," +
                        "`level` varchar(255)," +
                        "status varchar(255)," +
                        "img varchar(255)," +
                        "PRIMARY KEY (id));"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static StudentRepository getInstance() {
        if (instance == null) {
            try {
                instance = new StudentRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(StudentDAO studentDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO Student VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        st.setString(1, studentDAO.getId());
        st.setString(2, studentDAO.getName());
        st.setString(3, studentDAO.getSecondName());
        st.setString(4, studentDAO.getEmail());
        st.setString(5, studentDAO.getPassword());
        st.setString(6, studentDAO.getBirthDate());
        st.setString(7, studentDAO.getField());
        st.setString(8, studentDAO.getFaculty());
        st.setString(9, studentDAO.getLevel());
        st.setString(10, studentDAO.getStatus());
        st.setString(11, studentDAO.getImg());
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }
}