import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

  private static final String CONN = "jdbc:mysql://95.181.157.159:3306/MayningPCStatus?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
  private static final String USER = "MayningPCStatus";
  private static final String PASS = "aW6x?42f";
  private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Status"
      + " ("
      + " id int(6) NOT NULL AUTO_INCREMENT,"
      + " text varchar(255) NOT NULL,"
      + " time TIMESTAMP NOT NULL,"
      + " PRIMARY KEY (id),"
      + " UNIQUE KEY text (time)"
      + ")";

  public static void main(String[] args) {
    try {
      createTable();
      Connection conn = DriverManager.getConnection(CONN, USER, PASS);
      for (; ; ) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String text = "Майнинг ферма работает";
        String dateADD = formatForDateNow.format(dateNow);
        String query = "INSERT INTO Status " + "(id, text, time)" + " values (?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, num() + 1); // Получаем "длину" таблицы и прибавляем 1
        preparedStmt.setString(2, text);
        preparedStmt.setString(3, dateADD); //SQL ошибка будет
        preparedStmt.execute(); //Записываем данные в БД
        System.err.println("Заметка сохранена!");
        // System.out.println(dateADD);
        Thread.sleep(5000);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void createTable() {
    try {
      Connection conn = DriverManager.getConnection(CONN, USER, PASS);
      PreparedStatement preparedStatement = conn.prepareStatement(SQL_CREATE);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static int num() {
    try {
      Connection conn = DriverManager.getConnection(CONN, USER, PASS);
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) AS id FROM Status");
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
