package IE.server.repository;

import IE.server.repository.models.WeeklyScheduleDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                        "PRIMARY KEY (id, code)," +
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
}
