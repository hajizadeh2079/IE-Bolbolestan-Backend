package IE.server.repository;

import IE.server.repository.models.ClassDayDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassDayRepository {
    private static ClassDayRepository instance;

    private ClassDayRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ClassDay(" +
                        "code varchar(255)," +
                        "classCode varchar(255)," +
                        "`day` varchar(255)," +
                        "PRIMARY KEY (code, classCode, `day`)," +
                        "FOREIGN KEY (code, classCode) REFERENCES COURSE(code, classCode) ON DELETE CASCADE);"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static ClassDayRepository getInstance() {
        if (instance == null) {
            try {
                instance = new ClassDayRepository();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void insert(ClassDayDAO classDayDAO) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "INSERT IGNORE INTO ClassDay VALUES(?, ?, ?)"
        );
        st.setString(1, classDayDAO.getCode());
        st.setString(2, classDayDAO.getClassCode());
        st.setString(3, classDayDAO.getDay());
        try {
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        st.close();
        con.close();
    }

    public ArrayList<String> getClassDays(String code, String classCode) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(
                "SELECT day FROM ClassDay\n" +
                        "WHERE code = ? AND classCode = ?;\n"
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
            ArrayList<String> classDays = new ArrayList<String>();
            while (rs.next())
                classDays.add(rs.getString(1));
            st.close();
            con.close();
            return classDays;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }
}
