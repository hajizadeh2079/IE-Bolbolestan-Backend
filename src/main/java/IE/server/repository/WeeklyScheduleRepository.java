package IE.server.repository;

import IE.server.repository.models.CourseDAO;
import IE.server.repository.models.WeeklyScheduleDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
