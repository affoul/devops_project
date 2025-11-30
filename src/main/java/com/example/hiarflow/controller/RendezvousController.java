package com.example.hiarflow.controller;

import com.example.hiarflow.GenericDAO;
import com.example.hiarflow.model.Coiffeur;
import com.example.hiarflow.model.Rendezvous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class RendezvousController {

    @FXML private TableView<Rendezvous> rendezvousTable;
    @FXML private TableColumn<Rendezvous, Integer> idColumn;
    @FXML private TableColumn<Rendezvous, String> nomClientColumn;
    @FXML private TableColumn<Rendezvous, String> prenomClientColumn;
    @FXML private TableColumn<Rendezvous, String> telephoneColumn;
    @FXML private TableColumn<Rendezvous, java.time.LocalDate> dateColumn;
    @FXML private TableColumn<Rendezvous, LocalTime> heureColumn;

    @FXML private TextField nomClientField;
    @FXML private TextField prenomClientField;
    @FXML private TextField telephoneField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;

    @FXML private Label welcomeLabel;

    private GenericDAO<Rendezvous> rendezvousDAO;
    private ObservableList<Rendezvous> rendezvousList;
    private Coiffeur currentCoiffeur;

    @FXML
    public void initialize() {
        rendezvousDAO = new GenericDAO<>("com.example.hiarflow.model.Rendezvous");
        rendezvousList = FXCollections.observableArrayList();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        prenomClientColumn.setCellValueFactory(new PropertyValueFactory<>("prenomClient"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephoneClient"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRdv"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heureRdv"));

        // Écouteur de sélection
        rendezvousTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        fillForm(newSelection);
                    }
                });
    }

    public void setCurrentCoiffeur(Coiffeur coiffeur) {
        this.currentCoiffeur = coiffeur;
        if (currentCoiffeur != null) {
            welcomeLabel.setText("Bienvenue " + currentCoiffeur.getNom());
            loadRendezvous();
        }
    }

    private void loadRendezvous() {
        rendezvousList.clear();
        if (currentCoiffeur != null) {
            List<Rendezvous> allRdv = rendezvousDAO.getAll();
            for (Rendezvous rdv : allRdv) {
                if (rdv.getCoiffeur() != null && rdv.getCoiffeur().getId().equals(currentCoiffeur.getId())) {
                    rendezvousList.add(rdv);
                }
            }
            rendezvousTable.setItems(rendezvousList);
        } else {
            showAlert("Erreur", "Aucun coiffeur connecté. Veuillez vous reconnecter.");
        }
    }

    @FXML
    private void handleAddRendezvous() {
        if (!validateFields()) {
            return;
        }

        if (currentCoiffeur == null) {
            showAlert("Erreur", "Aucun coiffeur connecté. Veuillez vous reconnecter.");
            return;
        }

        Rendezvous rdv = new Rendezvous();
        rdv.setNomClient(nomClientField.getText());
        rdv.setPrenomClient(prenomClientField.getText());
        rdv.setTelephoneClient(telephoneField.getText());
        rdv.setDateRdv(datePicker.getValue());
        rdv.setHeureRdv(LocalTime.parse(heureField.getText()));
        rdv.setCoiffeur(currentCoiffeur);

        rendezvousDAO.save(rdv);
        clearFields();
        loadRendezvous();
    }

    @FXML
    private void handleUpdateRendezvous() {
        Rendezvous selected = rendezvousTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à modifier.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        selected.setNomClient(nomClientField.getText());
        selected.setPrenomClient(prenomClientField.getText());
        selected.setTelephoneClient(telephoneField.getText());
        selected.setDateRdv(datePicker.getValue());
        selected.setHeureRdv(LocalTime.parse(heureField.getText()));

        rendezvousDAO.update(selected);
        clearFields();
        loadRendezvous();
    }

    private void fillForm(Rendezvous rdv) {
        nomClientField.setText(rdv.getNomClient());
        prenomClientField.setText(rdv.getPrenomClient());
        telephoneField.setText(rdv.getTelephoneClient());
        datePicker.setValue(rdv.getDateRdv());
        heureField.setText(rdv.getHeureRdv().toString());
    }

    @FXML
    private void handleDeleteRendezvous() {
        Rendezvous selected = rendezvousTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à supprimer.");
            return;
        }

        rendezvousDAO.delete(selected);
        loadRendezvous();
        clearFields();
    }

    private boolean validateFields() {
        if (nomClientField.getText().isEmpty() ||
                prenomClientField.getText().isEmpty() ||
                telephoneField.getText().isEmpty() ||
                datePicker.getValue() == null ||
                heureField.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return false;
        }

        try {
            LocalTime.parse(heureField.getText());
        } catch (Exception e) {
            showAlert("Erreur", "Format de l'heure incorrect. Utilisez HH:mm (ex : 14:30).");
            return false;
        }

        return true;
    }

    private void clearFields() {
        nomClientField.clear();
        prenomClientField.clear();
        telephoneField.clear();
        datePicker.setValue(null);
        heureField.clear();
        rendezvousTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hiarflow/login.fxml"));
            Parent root = loader.load();

            currentCoiffeur = null;

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de connexion.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}