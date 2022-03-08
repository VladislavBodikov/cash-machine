import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import domain.Score;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import org.junit.jupiter.api.Test;
import usecase.FindScore;
import usecase.FindUser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class InitDataFromSqlScriptTest {

    private final String DB_URL = "jdbc:h2:~/my_db";

    private final String USER = "sa";
    private final String PASS = "";

    FindUser findUser = new FindUser(new UserRepositoryH2DBImpl());
    FindScore findScore = new FindScore(new ScoreRepositoryH2DBImpl());

    @Test
    void initSqlScript() throws SQLException, IOException {
        Connection connection = getConnection().orElseThrow(SQLException::new);
        String sql = readSqlScript("src/main/resources/initData.sql");
//        String sql = readSqlScript("src/main/resources/insertUser.sql");
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        connection.commit();

        preparedStatement.close();
        connection.close();
    }
    @Test
    void printValues(){
        List<Score> scores = findScore.findAll();
        List<User> users = findUser.findAll();
        System.out.println("Users : \n");
        users.forEach(System.out::println);
        System.out.println("Scores : \n");
        scores.forEach(System.out::println);
    }
    @Test
    void readUsers() throws SQLException {
        Connection connection = getConnection().orElseThrow(SQLException::new);
        String sql = "SELECT * FROM USERS";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

    private Optional<Connection> getConnection() {
        try {
            return Optional.ofNullable(DriverManager.getConnection(DB_URL, USER, PASS));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String readSqlScript(String pathToScript) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathToScript));

        StringBuilder buildSql = new StringBuilder();

        String line = "";
        while ((line = br.readLine()) != null){
            buildSql.append(line);
        }

        return buildSql.toString();
    }
}
