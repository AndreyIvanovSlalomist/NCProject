package ru.nc.musiclib.controller.fxController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import ru.nc.musiclib.utils.PasswordUtils;
import ru.nc.musiclib.model.Role;

import java.util.concurrent.Callable;

import static ru.nc.musiclib.utils.LoaderFX.getStage;

public class AuthorizationController {
    public PasswordField password;
    @FXML
    private TextField login;
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
        login.setOnMouseClicked(event -> clear());
        password.setOnMouseClicked(event -> clear());
        signIn.setOnAction(event -> {
            clear();
            String loginText = login.getText().trim();
            String passwordText = password.getText().trim();
            if (loginText.isEmpty()) {
                loginErrorLabel.setText("Введите логин!");
                setBorder(login);
            } else if (passwordText.isEmpty()) {
                passwordErrorLabel.setText("Введите пароль!");
                setBorder(password);
            } else {
                checkUser(loginText, passwordText);
            }
        });
        signUp.setOnAction(event -> {
            clear();
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

    private void checkUser(String login, String password) {
        if (ClientUtils.signInUser(clientSocket, login, password)) {
            signInUser(login);
        } else {
            loginErrorLabel.setText("Неверный логин или пароль!");
        }
    }

    private void signInUser(String login) {
        Role role = ClientUtils.getRole(clientSocket, login);
        if (role != null) {
            Stage stage = getStage(FxController.class, (Callable<FxController>) () -> new FxController(clientSocket, role, login), "/fxml/main.fxml", false, "Музыкальная библиотека");
            stage.setOnShown(event -> this.login.getScene().getWindow().hide());
            stage.show();
        }
    }

    private void signUpUser(String login, String password) {
        Object object = ClientUtils.signUpUser(clientSocket, login, PasswordUtils.hashPassword(password));
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

    AuthorizationController(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
