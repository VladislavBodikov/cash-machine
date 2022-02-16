package adapter.controller.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ManualConfig;
import domain.Score;
import domain.User;
import usecase.FindScore;
import usecase.FindUser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/test")
public class TestConnectonServlet extends HttpServlet {
    private FindUser findUser;
    private FindScore findScore;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ManualConfig manualConfig = new ManualConfig();
        this.findUser = manualConfig.findUser();
        this.findScore = manualConfig.findScore();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        // read REQUEST
        String userInputData = readRequestBody(req);

        User inputUser = getInputUserFromJson(userInputData);
        // Step 1. find score in DB
        Optional<Score> foundedScore = findScore.findScoreByCardNumber(inputUser.getScore().getCardNumber());
        if (foundedScore.isPresent()){
            // Step 2. find user by score.userId
            Optional<User> foundedUser = findUser.findById(foundedScore.get().getUserId());
            if (foundedUser.isPresent()){
                User userFromDB = foundedUser.get();
                userFromDB.setScore(foundedScore.get());
                String responseJson = userToJson(userFromDB);
                // send response User with full data from DB
                resp.getWriter().write(responseJson);
            }
        }
        else {
            resp.getWriter().write("Wrong PIN-code");
        }
    }

    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder userInputData = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while( (str = br.readLine()) != null ){
            userInputData.append(str);
        }
        return userInputData.toString();
    }

    private User getInputUserFromJson(String inputString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputString,User.class);
    }
    private String userToJson(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
}
