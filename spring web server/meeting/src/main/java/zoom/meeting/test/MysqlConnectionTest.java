package zoom.meeting.test;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class MysqlConnectionTest {
    public static void main(String[] args) {

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://localhost/zoom";

            conn = DriverManager.getConnection(url, "javaUser", "java");

            log.info("DB연결 성공");
        } catch (ClassNotFoundException e) {
            log.error("드라이버 로딩 실패 {}",e);
        } catch (SQLException e){
            log.error("에러: {}",e);
        }
        finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
