package adapter.controller.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ManualConfig;
import domain.Score;
import domain.User;
import usecase.FindScore;
import usecase.FindUser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/auth")
@MultipartConfig(location = "C:\\SberJava\\cash-machine\\server")
public class AuthServlet extends HttpServlet {
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
//        Part part = req.getPart("user_data");
//        if (part != null){
//            String inputUserJsonFileName = "inputUser.json";
//            // write input json to file
//            part.write(inputUserJsonFileName);
//            // read User
//            User inputUser = getInputUserFromJson(inputUserJsonFileName);
//            // find user by login (cardNumber) and password (PIN-code)
//            Optional<User> foundedUser = findUser.findByCardNumberAndPinCode(inputUser.getCardNumber(),inputUser.getPinCode());
//            Optional<Score> foundedScore = findScore.findScoreByCardNumber(inputUser.getCardNumber());
//
//            if (foundedUser.isPresent() && foundedScore.isPresent()){
//
//                User userFromDB = foundedUser.get();
//                //userFromDB.setScore(new HashSet<Score>(){{add(foundedScore.get());}});
//                userFromDB.setScore(foundedScore.get());
//                // send response BALANCE
//                resp.getWriter()
//                        .append("\n\nCard Number: ").append(userFromDB.getCardNumber())
//                        .append("\nPassword: ").append(userFromDB.getPinCode())
//                        .append("\nBalance: ").append(userFromDB.getScore().getAmount().toString());
//            }
//            else {
//                resp.getWriter().append("Error: User not found");
//            }
//        }
//        else {
//            BufferedReader br = req.getReader();
//            StringBuilder sb = new StringBuilder();
//            int c;
//            while ((c = br.read()) != -1){
//                sb.append((char) c);
//            }
//            System.out.println(sb);
//        }
    }

    private User getInputUserFromJson(String inputUserDataPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User inputUser = objectMapper.readValue(new File(inputUserDataPath),User.class);
        return inputUser;
    }
}

