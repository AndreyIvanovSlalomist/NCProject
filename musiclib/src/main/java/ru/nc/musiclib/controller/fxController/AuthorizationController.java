package ru.nc.musiclib.controller.fxController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.nc.musiclib.net.client.ClientSocket;
import ru.nc.musiclib.utils.ClientUtils;
import ru.nc.musiclib.utils.ConstProtocol;
import ru.nc.musiclib.utils.Role;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.Callable;

import static ru.nc.musiclib.utils.LoaderFX.getStage;

public class AuthorizationController {
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
            if (loginText.isEmpty()) {
                loginErrorLabel.setText("Введите логин!");
                setBorder(login);
            } else if (passwordText.isEmpty()) {
                passwordErrorLabel.setText("Введите пароль!");
                setBorder(password);
            } else {
                clear();
                signInUser(loginText, passwordText);
            }
        });
        signUp.setOnAction(event -> {
            String loginText = login.getText().trim();
            String passwordText = password.getText().trim();
            if (loginText.equals("")) {
                loginErrorLabel.setText("Введите логин!");
                setBorder(login);
            } else if (passwordText.equals("")) {
                passwordErrorLabel.setText("Введите пароль!");
                setBorder(password);
            } else if (checkPassword(passwordText)) {
                signUpUser(loginText, passwordText);
            }
        });
    }

    private void connect() {
        if (clientSocket == null) {
            clientSocket = ClientUtils.connect();
        }
    }

    private void signInUser(String login, String password) {
        connect();
        Object object = ClientUtils.signInUser(clientSocket, login, password);
        if (object == ConstProtocol.errorUser) {
                loginErrorLabel.setText("Неверный логин или пароль!");
        } else if (object instanceof Role) {
            Role role = (Role) object;
            Stage stage = getStage(FxController.class, (Callable<FxController>) () -> new FxController(clientSocket, role), "/fxml/main.fxml", false, "Музыкальная библиотека");
            stage.setOnShown(event -> {
                this.login.getScene().getWindow().hide();
            });
            stage.show();
        }
    }

    private void signUpUser(String login, String password) {
        connect();
        Object object = ClientUtils.signUpUser(clientSocket, login, password);
        if (object instanceof String) {
            String answer = (String) object;
            if (answer.equals("OK")) {
                ClientUtils.alertSelectedItem(null, "Музыкальная библиотека", "Регистрация прошла успешно!\nЧтобы продолжить авторизуйтесь.");
                this.login.clear();
                this.password.clear();
            } else {
                loginErrorLabel.setText("Этот логин уже занят");
                setBorder(this.login);
            }
        }
    }

    private boolean checkPassword(String str) {
        if (str.length() < 4 || str.length() > 10) {
            passwordErrorLabel.setText("Длина пароля должна быть от 4 до 10 символов!");
            setBorder(password);
            return false;
        } else
            return true;
    }

    private void setBorder(TextField field) {
        field.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
    }

    private void clear() {
        passwordErrorLabel.setText("");
        loginErrorLabel.setText("");
        password.setBorder(null);
        login.setBorder(null);
    }

    private String hash(String password) {
        String hashedPassword = null;
        try {
            byte[] salt = getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
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