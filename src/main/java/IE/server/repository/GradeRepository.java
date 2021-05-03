package IE.server.repository;

import IE.server.controllers.models.GradeDTO;
import IE.server.exceptions.StudentNotFound;
import IE.server.repository.models.GradeDAO;
import IE.server.repository.models.StudentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GradeRepository {
    private static GradeRepository instance;

    private GradeRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Grade(" +
                        "id varchar(255)," +
                        "code varchar(255)," +
                        "term int," +
                        "grade int," +
                        "PRIMARY KEY (id, code, term)," +
                        "FOREIGN KEY (code) REFERENCES COURSE(code) ON DELETE CASCADE," +
                        "FOREIGN KEY (id) REFERENCES STUDENT(id) ON DELETE CASCADE);"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static GradeRepository getInstance() {
        if (instance == null) {
            try {
                instance = new GradeRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(GradeDAO gradeDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO Grade VALUES(?, ?, ?, ?)"
        );
        st.setString(1, gradeDAO.getId());
        st.setString(2, gradeDAO.getCode());
        st.setString(3, String.valueOf(gradeDAO.getTerm()));
        st.setString(4, String.valueOf(gradeDAO.getGrade()));
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public double calcGPA(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT SUM(temp3) / SUM(units) AS GPA\n" +
                    "FROM (SELECT units, SUM(grade * units) AS temp3\n" +
                    "      FROM (SELECT c.units, g.grade\n" +
                    "              FROM grade g join course c on c.code = g.code\n" +
                    "              WHERE g.id = ?) AS temp\n" +
                    "      GROUP BY units) AS temp2;"
        );
        st.setString(1, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return -1;
            }
            st.close();
            con.close();
            return Double.parseDouble(rs.getString(1));
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public int calcTPU(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT SUM(units) AS TPU\n" +
                    "FROM grade g join course c on c.code = g.code\n" +
                    "WHERE g.id = ? AND g.grade >= 10;"
        );
        st.setString(1, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return 0;
            }
            st.close();
            con.close();
            return Integer.parseInt(rs.getString(1));
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public int getMaxTermById(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT MAX(term) FROM grade WHERE id = ?;"
        );
        st.setString(1, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return 0;
            }
            st.close();
            con.close();
            return Integer.parseInt(rs.getString(1));
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<GradeDTO> getReportCard(String id, int term) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT g.code, c.name, c.units, g.grade\n" +
                    "FROM grade g join course c on c.code = g.code\n" +
                    "WHERE g.term = ? AND g.id = ?;\n"
        );
        st.setString(1, String.valueOf(term));
        st.setString(2, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<GradeDTO>();
            }
            st.close();
            con.close();
            ArrayList<GradeDTO> gradesHistory = new ArrayList<GradeDTO>();
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                int units = Integer.parseInt(rs.getString(3));
                int grade = Integer.parseInt(rs.getString(4));
                gradesHistory.add(new GradeDTO(code, name, units, grade));
            }
            return gradesHistory;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public double getReportCardGPA(String id, int term) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT SUM(temp3) / SUM(units) AS GPA\n" +
                        "FROM (SELECT units, SUM(grade * units) AS temp3\n" +
                        "      FROM (SELECT c.units, g.grade\n" +
                        "              FROM grade g join course c on c.code = g.code\n" +
                        "              WHERE g.term = ? AND g.id = ?) AS temp\n" +
                        "      GROUP BY units) AS temp2;"
        );
        st.setString(1, String.valueOf(term));
        st.setString(2, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return -1;
            }
            st.close();
            con.close();
            return Double.parseDouble(rs.getString(1));
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }
}
