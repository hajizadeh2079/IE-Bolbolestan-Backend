package IE.server.repository;

import IE.server.repository.models.GradeDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
