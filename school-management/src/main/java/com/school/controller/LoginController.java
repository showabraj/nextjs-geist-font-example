package com.school.controller;

import com.school.dao.UserDAO;
import com.school.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("student", "teacher", "admin");
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String role = roleComboBox.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.authenticate(username, password, role);
        if (user != null) {
            openDashboard(user.getRole());
            ((Stage) loginButton.getScene().getWindow()).close();
        } else {
            showAlert("Login Failed", "Invalid username, password, or role.");
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String role = roleComboBox.getValue();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUp" + capitalize(role) + ".fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sign Up - " + capitalize(role));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open sign up page.");
        }
    }

    private void openDashboard(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + capitalize(role) + "Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(capitalize(role) + " Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
