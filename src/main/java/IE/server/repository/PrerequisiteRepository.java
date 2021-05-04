package IE.server.repository;

import IE.server.repository.models.PrerequisiteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrerequisiteRepository {
    private static PrerequisiteRepository instance;

    private PrerequisiteRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Prerequisite(" +
                        "code varchar(255)," +
                        "classCode varchar(255)," +
                        "prerequisite varchar(255)," +
                        "PRIMARY KEY (code, classCode, prerequisite)," +
                        "FOREIGN KEY (code, classCode) REFERENCES COURSE(code, classCode) ON DELETE CASCADE," +
                        "FOREIGN KEY (prerequisite) REFERENCES COURSE(code) ON DELETE CASCADE);"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static PrerequisiteRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PrerequisiteRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(PrerequisiteDAO prerequisiteDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO Prerequisite VALUES(?, ?, ?)"
        );
        st.setString(1, prerequisiteDAO.getCode());
        st.setString(2, prerequisiteDAO.getClassCode());
        st.setString(3, prerequisiteDAO.getPrerequisite());
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public ArrayList<String> getPrerequisitesNames(String code, String classCode) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT c.name\n" +
                        "FROM prerequisite p join course c on p.prerequisite = c.code\n" +
                        "WHERE p.code = ? and p.classCode = ?;"
        );
        st.setString(1, code);
        st.setString(2, classCode);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<String>();
            }
            ArrayList<String> prerequisitesNames = new ArrayList<String>();
            while (rs.next())
                prerequisitesNames.add(rs.getString(1));
            st.close();
            con.close();
            return prerequisitesNames;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<String> getPrerequisites(String code, String classCode) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT p.prerequisite\n" +
                        "FROM prerequisite p\n" +
                        "WHERE p.code = ? and p.classCode = ?;\n"
        );
        st.setString(1, code);
        st.setString(2, classCode);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<String>();
            }
            ArrayList<String> prerequisites = new ArrayList<String>();
            while (rs.next())
                prerequisites.add(rs.getString(1));
            st.close();
            con.close();
            return prerequisites;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }
}
