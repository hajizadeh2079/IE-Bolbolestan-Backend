package IE.server.repository;

import IE.server.repository.models.CourseDAO;
import IE.server.repository.models.WeeklyScheduleDAO;

import java.sql.*;
import java.util.ArrayList;

public class WeeklyScheduleRepository {
    private static WeeklyScheduleRepository instance;

    private WeeklyScheduleRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS WeeklySchedule(" +
                        "id varchar(255)," +
                        "code varchar(255)," +
                        "classCode varchar(255)," +
                        "status int," +
                        "PRIMARY KEY (id, code, status)," +
                        "FOREIGN KEY (code, classCode) REFERENCES COURSE(code, classCode) ON DELETE CASCADE," +
                        "FOREIGN KEY (id) REFERENCES STUDENT(id) ON DELETE CASCADE);"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static WeeklyScheduleRepository getInstance() {
        if (instance == null) {
            try {
                instance = new WeeklyScheduleRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(WeeklyScheduleDAO weeklyScheduleDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO WeeklySchedule VALUES(?, ?, ?, ?)"
        );
        st.setString(1, weeklyScheduleDAO.getId());
        st.setString(2, weeklyScheduleDAO.getCode());
        st.setString(3, weeklyScheduleDAO.getClassCode());
        st.setString(4, String.valueOf(weeklyScheduleDAO.getStatus()));
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public int calcSignedUp(String code, String classCode) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT COUNT(*)\n" +
                        "FROM WeeklySchedule\n" +
                        "WHERE code = ? AND classCode = ? AND (status = 1 OR status = 2)"
        );
        st.setString(1, code);
        st.setString(2, classCode);
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return 0;
            }
            rs.next();
            int signedUp = Integer.parseInt(rs.getString(1));
            st.close();
            con.close();
            return signedUp;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<CourseDAO> getWeeklyScheduleById(String id, int status) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st;
        st = con.prepareStatement(
                "SELECT c.*\n" +
                        "FROM weeklyschedule w join course c on c.code = w.code and c.classCode = w.classCode\n" +
                        "WHERE w.id = ? AND w.status = ?;"
        );
        st.setString(1, id);
        st.setString(2, String.valueOf(status));
        try {
            ResultSet rs = st.executeQuery();
            if (rs == null) {
                st.close();
                con.close();
                return new ArrayList<CourseDAO>();
            }
            ArrayList<CourseDAO> courses = new ArrayList<CourseDAO>();
            while (rs.next()) {
                String code = rs.getString(1);
                String classCode = rs.getString(2);
                String name = rs.getString(3);
                int units = Integer.parseInt(rs.getString(4));
                String type = rs.getString(5);
                String instructor = rs.getString(6);
                int capacity = Integer.parseInt(rs.getString(7));
                String classTimeStart = rs.getString(8);
                String classTimeEnd = rs.getString(9);
                String examTimeStart = rs.getString(10);
                String examTimeEnd = rs.getString(11);
                courses.add(new CourseDAO(code, classCode, name, instructor, units, type, classTimeStart, classTimeEnd, examTimeStart, examTimeEnd, capacity));
            }
            st.close();
            con.close();
            return courses;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public void delete(String id, String code, String classCode, int status) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "DELETE FROM weeklyschedule\n" +
                        "WHERE id = ? AND code = ? AND classCode = ? AND status = ?;"
        );
        st.setString(1, id);
        st.setString(2, code);
        st.setString(3, classCode);
        st.setString(4, String.valueOf(status));
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public void submitPlan(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.addBatch(
                "DELETE FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND (status = 1 OR status = 2);\n"
        );
        st.addBatch(
                "UPDATE weeklyschedule\n" +
                        "SET status = 3\n" +
                        "WHERE id = " + id + " AND status = 4;\n"
        );
        st.addBatch(
                "INSERT INTO weeklyschedule (id, code, classCode, status)\n" +
                        "SELECT id, code, classCode, 1\n" +
                        "FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND status = 3;\n"
        );
        st.addBatch(
                "INSERT INTO weeklyschedule (id, code, classCode, status)\n" +
                        "SELECT id, code, classCode, 2\n" +
                        "FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND status = 5;"
        );
        try {
            st.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public void resetPlan(String id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.addBatch(
                "DELETE FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND (status = 3 OR status = 4 OR status = 5);\n"
        );
        st.addBatch(
                "INSERT INTO weeklyschedule (id, code, classCode, status)\n" +
                        "SELECT id, code, classCode, 3\n" +
                        "FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND status = 1;\n"
        );
        st.addBatch(
                "INSERT INTO weeklyschedule (id, code, classCode, status)\n" +
                        "SELECT id, code, classCode, 5\n" +
                        "FROM weeklyschedule\n" +
                        "WHERE id = " + id + " AND status = 2;"
        );
        try {
            st.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public void waitListToFinalizedCourse() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.addBatch(
                "UPDATE course c\n" +
                        "SET capacity = capacity + (SELECT COUNT(*)\n" +
                        "                            FROM weeklyschedule w\n" +
                        "                            WHERE c.code = w.code AND c.classCode = w.classCode AND w.status = 2)"
        );
        st.addBatch(
                "CREATE TABLE IF NOT EXISTS Temp(\n" +
                        "    id varchar(255),\n" +
                        "    code varchar(255),\n" +
                        "    classCode varchar(255),\n" +
                        "    PRIMARY KEY (id, code),\n" +
                        "    FOREIGN KEY (code, classCode) REFERENCES COURSE(code, classCode) ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (id) REFERENCES STUDENT(id) ON DELETE CASCADE);\n"
        );
        st.addBatch(
                "INSERT INTO Temp\n" +
                        "SELECT id, code, classCode\n" +
                        "FROM weeklyschedule\n" +
                        "WHERE status = 2;\n"
        );
        st.addBatch(
                "UPDATE weeklyschedule\n" +
                        "SET status = 3\n" +
                        "WHERE status = 5 AND (id, code, classCode) IN (SELECT * FROM Temp);\n"
        );
        st.addBatch("DROP TABLE Temp;");
        st.addBatch(
                "UPDATE weeklyschedule\n" +
                        "SET status = 1\n" +
                        "WHERE status = 2;\n"
        );
        try {
            st.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }
}
