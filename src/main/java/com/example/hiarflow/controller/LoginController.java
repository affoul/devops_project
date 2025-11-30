package com.example.hiarflow.controller;

import com.example.hiarflow.GenericDAO;
import com.example.hiarflow.model.Coiffeur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField nomField;
    @FXML private PasswordField motDePasseField;

    public static Coiffeur currentCoiffeur;

    @FXML
    private void handleLogin() {
        String nom = nomField.getText().trim();
        String motDePasse = motDePasseField.getText().trim();

        if (nom.isEmpty() || motDePasse.isEmpty()) {
            showAlert("Erreur", "Le nom et le mot de passe sont requis");
            return;
        }

        try {
            GenericDAO<Coiffeur> coiffeurDAO = new GenericDAO<>("com.example.hiarflow.model.Coiffeur");
            currentCoiffeur = coiffeurDAO.searchUser(nom, motDePasse);

            if (currentCoiffeur != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hiarflow/rendezvous.fxml"));
                Parent root = loader.load();

                // Passer le coiffeur connecté au contrôleur des rendez-vous
                RendezvousController rendezvousController = loader.getController();
                rendezvousController.setCurrentCoiffeur(currentCoiffeur);
                rendezvousController.setCurrentCoiffeur(currentCoiffeur);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Gestion des Rendez-vous");
                stage.show();

                ((Stage) nomField.getScene().getWindow()).close();
            } else {
                showAlert("Erreur", "Nom ou mot de passe incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la connexion.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}