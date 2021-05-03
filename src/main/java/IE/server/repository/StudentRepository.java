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
                        "name_ varchar(255)," +
                        "secondName varchar(255)," +
                        "email varchar(255)," +
                        "password varchar(255)," +
                        "birthDate varchar(255)," +
                        "field varchar(255)," +
                        "faculty varchar(255)," +
                        "level_ varchar(255)," +
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
                System.out.println("error in StudentRepository.create query.");
            }
        }
        return instance;
    }

    public void insert(StudentDAO student) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO Student VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        st.setString(1, student.getId());
        st.setString(2, student.getName());
        st.setString(3, student.getSecondName());
        st.setString(4, student.getEmail());
        st.setString(5, student.getPassword());
        st.setString(6, student.getBirthDate());
        st.setString(7, student.getField());
        st.setString(8, student.getFaculty());
        st.setString(9, student.getLevel());
        st.setString(10, student.getStatus());
        st.setString(11, student.getImg());
        try {
            st.execute();
        } catch (Exception e) {
            System.out.println("error in StudentRepository.insert query.");
            e.printStackTrace();
        }
        st.close();
        con.close();
    }
}