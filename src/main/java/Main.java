import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {
  //База данных с данными по времени работы:
  private static final String CONN = "jdbc:mysql://95.181.157.159:3306/MayningPCStatus?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
  private static final String USER = "MayningPCStatus";
  private static final String PASS = "aW6x?42f";

  private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Status"
      + " ("
      + " id int(6) NOT NULL AUTO_INCREMENT,"
      + " day varchar(255) NOT NULL,"
      + " month varchar(255) NOT NULL,"
      + " year varchar(255) NOT NULL,"
      + " time varchar(255) NOT NULL,"
      + " PRIMARY KEY (id),"
      + " UNIQUE KEY text (time)"
      + ")";

  public static void main(String[] args) {
    try {
      createTable();
      Connection conn = DriverManager.getConnection(CONN, USER, PASS);
        for (; ; ) {
          Document doc = Jsoup.connect("https://megolox.ru/mayning/")
              .data("query", "Java")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
              .cookie("auth", "token")
              .get();
          Elements h1Elements = doc.select("h1");
          Elements h2Elements = doc.select("h1");
          String h1 = h1Elements.text();
          String h2 = h2Elements.text();

          Date dateNow = new Date();
          SimpleDateFormat formatForDateDay = new SimpleDateFormat("d");
          SimpleDateFormat formatForDateMonth = new SimpleDateFormat("M");
          SimpleDateFormat formatForDateYear = new SimpleDateFormat("yyyy");
          SimpleDateFormat formatForDateTime = new SimpleDateFormat("H:m:s");

          String DayDate = formatForDateDay.format(dateNow);
          String DayMonth = formatForDateMonth.format(dateNow);
          String DayYear = formatForDateYear.format(dateNow);
          String DayTime = formatForDateTime.format(dateNow);

          String query =
              "INSERT INTO Status " + "(id, day, month, year, time)" + " values (?, ?, ?, ?, ?)";

          PreparedStatement preparedStmt = conn.prepareStatement(query);
          preparedStmt.setInt(1, num() + 1); //Получаем "длину" таблицы и прибавляем 1
          preparedStmt.setString(2, DayDate);
          preparedStmt.setString(3, DayMonth);
          preparedStmt.setString(4, DayYear);
          preparedStmt.setString(5, DayTime);
          preparedStmt.execute(); //Записываем данные в БД
          System.out.println("save");
          Thread.sleep(Long.parseLong(h1));
        }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void createTable() {
    try {
      //Время работы
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
