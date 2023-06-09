package IE.server.repository;

import IE.server.exceptions.StudentNotFound;
import IE.server.repository.models.StudentDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;

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
                        "PRIMARY KEY (id)) ENGINE=INNODB;"
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

    public ArrayList<String> getAllIds() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT id from Student;"
        );
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<String>();
            }
            ArrayList<String> ids = new ArrayList<String>();
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
            st.close();
            con.close();
            return ids;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public StudentDAO findById(String id) throws SQLException, StudentNotFound {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT * FROM Student S WHERE S.id = ?;"
        );
        st.setString(1, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                throw new StudentNotFound();
            }
            StudentDAO studentDAO = null;
            if (rs.next()) {
                String stdId = rs.getString(1);
                String name = rs.getString(2);
                String secondName = rs.getString(3);
                String email = rs.getString(4);
                String password = rs.getString(5);
                String birthDate = rs.getString(6);
                String field = rs.getString(7);
                String faculty = rs.getString(8);
                String level = rs.getString(9);
                String status = rs.getString(10);
                String img = rs.getString(11);
                studentDAO = new StudentDAO(stdId, name, secondName, email, password, birthDate, field, faculty, level, status, img);
            }
            st.close();
            con.close();
            if (studentDAO == null) {
                throw new StudentNotFound();
            }
            return studentDAO;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public boolean doesAlreadyExist(String id, String email) throws SQLException, StudentNotFound {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT * FROM Student S WHERE S.id = ? OR S.email = ?;"
        );
        st.setString(1, id);
        st.setString(2, email);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                throw new StudentNotFound();
            }
            boolean result = false;
            if (rs.next())
                result = true;
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public String findByEmailPassword(String email, String rawPassword) throws SQLException, StudentNotFound {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT S.id, S.password FROM Student S WHERE S.email = ?;"
        );
        st.setString(1, email);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                throw new StudentNotFound();
            }
            String id = null;
            if (rs.next()) {
                String password = rs.getString(2);
                if(new BCryptPasswordEncoder().matches(rawPassword, password))
                    id = rs.getString(1);
            }
            st.close();
            con.close();
            return id;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public String findByEmail(String email) throws SQLException, StudentNotFound {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT S.id FROM Student S WHERE S.email = ?;"
        );
        st.setString(1, email);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                throw new StudentNotFound();
            }
            String id = null;
            if (rs.next())
                id = rs.getString(1);
            st.close();
            con.close();
            return id;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public void resetPassword(String id, String password) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "UPDATE Student " +
                        "SET password = ? " +
                        "WHERE id = ?;"
        );
        st.setString(1, password);
        st.setString(2, id);
        try {
            st.execute();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }
}
