package IE.server.repository;

import IE.server.controllers.models.GradeDTO;
import IE.server.repository.models.GradeDAO;
import IE.server.repository.models.GradeUnitDAO;

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

    public ArrayList<GradeUnitDAO> getLastGrades(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT g.grade, c.units\n" +
                        "FROM grade g join course c on c.code = g.code\n" +
                        "WHERE g.id = ? AND g.code NOT IN (SELECT g2.code\n" +
                        "                                          FROM grade g2\n" +
                        "                                          WHERE g2.id = g.id AND g2.code = g.code AND g2.term > g.term);"
        );
        st.setString(1, id);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<GradeUnitDAO>();
            }
            ArrayList<GradeUnitDAO> lastGrades = new ArrayList<GradeUnitDAO>();
            while (rs.next()) {
                int grade = Integer.parseInt(rs.getString(1));
                int units = Integer.parseInt(rs.getString(2));
                lastGrades.add(new GradeUnitDAO(grade, units));
            }
            st.close();
            con.close();
            return lastGrades;
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
            int tpu = 0;
            if (rs.next())
                tpu = Integer.parseInt(rs.getString(1));
            st.close();
            con.close();
            return tpu;
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
            int maxTerm = 0;
            if (rs.next())
                maxTerm = Integer.parseInt(rs.getString(1));
            st.close();
            con.close();
            return maxTerm;
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
            ArrayList<GradeDTO> gradesHistory = new ArrayList<GradeDTO>();
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                int units = Integer.parseInt(rs.getString(3));
                int grade = Integer.parseInt(rs.getString(4));
                gradesHistory.add(new GradeDTO(code, name, units, grade));
            }
            st.close();
            con.close();
            return gradesHistory;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<GradeUnitDAO> getTermGrades(String id, int term) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT g.grade, c.units\n" +
                        "FROM grade g join course c on c.code = g.code\n" +
                        "WHERE g.id = ? AND g.term = ?;"
        );
        st.setString(1, id);
        st.setString(2, String.valueOf(term));
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<GradeUnitDAO>();
            }
            ArrayList<GradeUnitDAO> termGrades = new ArrayList<GradeUnitDAO>();
            while (rs.next()) {
                int grade = Integer.parseInt(rs.getString(1));
                int units = Integer.parseInt(rs.getString(2));
                termGrades.add(new GradeUnitDAO(grade, units));
            }
            st.close();
            con.close();
            return termGrades;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public int getLastGrade(String id, String code) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT g.grade\n" +
                        "FROM grade g\n" +
                        "WHERE g.id = ? AND g.code = ? AND\n" +
                        "      g.code NOT IN (SELECT g2.code\n" +
                        "                    FROM grade g2\n" +
                        "                    WHERE g2.id = g.id AND g2.code = g.code AND g2.term > g.term);"
        );
        st.setString(1, id);
        st.setString(2, code);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return 0;
            }
            int grade = 0;
            if (rs.next())
                grade = Integer.parseInt(rs.getString(1));
            st.close();
            con.close();
            return grade;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }
}
