import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DropTable {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/BD_ERP?useSSL=false";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {

            System.out.println("Dropping table tipo_direccion_seq...");
            stmt.executeUpdate("DROP TABLE IF EXISTS tipo_direccion_seq");
            System.out.println("Table dropped successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
