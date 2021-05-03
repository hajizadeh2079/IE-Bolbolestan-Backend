package IE.server.repository;

import IE.server.repository.models.PrerequisiteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
