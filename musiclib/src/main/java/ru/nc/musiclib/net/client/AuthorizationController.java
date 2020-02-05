package ru.nc.musiclib.net.client;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Alert;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.scene.control.Label;
        import javafx.scene.layout.Border;
        import javafx.scene.layout.BorderStroke;
        import javafx.scene.layout.BorderStrokeStyle;
        import javafx.scene.layout.BorderWidths;
        import javafx.scene.paint.Color;
        import javafx.stage.Stage;
        import javafx.stage.StageStyle;
        import ru.nc.musiclib.logger.MusicLibLogger;
        import ru.nc.musiclib.net.ConstProtocol;
        import ru.nc.musiclib.net.Role;

        import java.io.IOException;
        import java.net.InetAddress;
        import java.net.UnknownHostException;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.security.SecureRandom;

public class AuthorizationController {
    private final static MusicLibLogger logger = new MusicLibLogger(AuthorizationController.class);
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private Button signIn;
    @FXML
    private Button signUp;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    private ClientSocket clientSocket;

    @FXML
    void initialize() {
        login.setOnMouseClicked(event -> {
            clear();
        });
        password.setOnMouseClicked(event -> {
            clear();
        });
        signIn.setOnAction(event -> {
            String loginText = login.getText().trim();
            String passwordText = password.getText().trim();
            if(loginText.equals("")){
                loginErrorLabel.setText("Введите логин!");
                setBorder(login);
            }else if(passwordText.equals("")){
                passwordErrorLabel.setText("Введите пароль!");
                setBorder(password);
            }else{
                clear();
                signInUser(loginText,passwordText);
            }
        });
        signUp.setOnAction(event -> {
            String loginText = login.getText().trim();
            String passwordText = password.getText().trim();
            if(loginText.equals("")){
                loginErrorLabel.setText("Введите логин!");
                setBorder(login);
            }else if(passwordText.equals("")){
                passwordErrorLabel.setText("Введите пароль!");
                setBorder(password);
            }else if(checkPassword(passwordText)){
                signUpUser(loginText,passwordText);
            }
        });
    }
    private void connect(){
        if (clientSocket == null) {
            try {
                clientSocket = new ClientSocket(InetAddress.getLocalHost(), 4444);
            } catch (UnknownHostException e) {
                logger.error("Ошибка: Неизвестен хост");
            }
        }
    }

    private void signInUser(String login, String password){
        connect();
        try {
            clientSocket.getOos().writeObject(ConstProtocol.checkUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток!");
        }
        Object object = null;
        try {
            object = clientSocket.getOis().readObject();
            if(object instanceof ConstProtocol){
                ConstProtocol cp = (ConstProtocol) object;
                if(cp == ConstProtocol.errorUser) {
                  loginErrorLabel.setText("Неверный логин или пароль!");
                }
            }else if(object instanceof Role){
                Role role = (Role) object;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                Parent root = loader.load();
                FxController controller = loader.getController();
                controller.setCurrentRole(role);
                controller.setConnection(clientSocket);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("Музыкальная библиотека");
                stage.setScene(new Scene(root));
                stage.setOnShown(event -> {
                    this.login.getScene().getWindow().hide();
                    controller.refresh();
                });
                stage.show();
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка чтения из потока!");
        }


    }
    private void signUpUser(String login, String password){
        connect();
        try {
            clientSocket.getOos().writeObject(ConstProtocol.addUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток!");
        }
        Object object = null;
        try {
            object = clientSocket.getOis().readObject();
            if(object instanceof String){
                String answer = (String) object;
                if(answer.equals("OK")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Музыкальная библиотека");
                    alert.setHeaderText(null);
                    alert.setContentText("Регистрация прошла успешно!\nЧтобы продолжить авторизуйтесь.");
                    alert.setOnShowing(event -> {
                        this.login.clear();
                        this.password.clear();
                    });
                    alert.showAndWait();

                }else{
                    loginErrorLabel.setText("Этот логин уже занят");
                    setBorder(this.login);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.info("Ошибка чтения из потока");
        }
    }

    private boolean checkPassword(String str){
        if(str.length()<4||str.length()>10) {
            passwordErrorLabel.setText("Длина пароля должна быть от 4 до 10 символов!");
            setBorder(password);
            return false;
        }else
            return true;
    }
    private void setBorder(TextField field){
        field.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
    }
    private void clear(){
        passwordErrorLabel.setText("");
        loginErrorLabel.setText("");
        password.setBorder(null);
        login.setBorder(null);
    }
    private String hash(String password){
        String hashedPassword = null;
        try {
            byte[] salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i =0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA256PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

}
