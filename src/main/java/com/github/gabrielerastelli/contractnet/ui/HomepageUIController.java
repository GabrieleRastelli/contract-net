package com.github.gabrielerastelli.contractnet.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.text.Text;

import java.io.IOException;

public class HomepageUIController {

    private UIFX mainApp;

    @FXML
    private CheckBox randomNumberOfServersCheckBox;

    @FXML
    private Text numberOfServersText;

    @FXML
    private Spinner<Integer> numberOfServersSpinner;

    @FXML
    private CheckBox randomNumberOfThreadsCheckBox;

    @FXML
    private Text numberOfThreadsText;

    @FXML
    private Spinner<Integer> numberOfThreadsSpinner;

    @FXML
    private CheckBox randomNumberOfTasksCheckBox;

    @FXML
    private Text numberOfTasksText;

    @FXML
    private Spinner<Integer> numberOfTasksSpinner;

    @FXML
    private CheckBox randomTaskDurationCheckBox;

    @FXML
    private Text taskDurationText;

    @FXML
    private Spinner<Integer> taskDurationSpinner;

    @FXML
    private ComboBox<String> simulationTypeComboBox;

    public void setMainApp(UIFX mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void showSimulationPage() throws IOException {
        mainApp.showSimulationPage(numberOfServersSpinner.getValue(), numberOfThreadsSpinner.getValue(),
                numberOfTasksSpinner.getValue(), taskDurationSpinner.getValue(), simulationTypeComboBox.getValue());
    }

    @FXML
    private void initialize() {
        randomNumberOfServersCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            numberOfServersText.setVisible(!newValue);
            numberOfServersSpinner.setVisible(!newValue);
        });

        randomNumberOfThreadsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            numberOfThreadsText.setVisible(!newValue);
            numberOfThreadsSpinner.setVisible(!newValue);
        });

        randomNumberOfTasksCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            numberOfTasksText.setVisible(!newValue);
            numberOfTasksSpinner.setVisible(!newValue);
        });

        randomTaskDurationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            taskDurationText.setVisible(!newValue);
            taskDurationSpinner.setVisible(!newValue);
        });

        simulationTypeComboBox.setItems(FXCollections.observableArrayList(
                "ContractNet (Accept first)",
                "ContractNet (Balanced)",
                "Random A Priori",
                "Round Robin",
                "Decentralized"
        ));

        simulationTypeComboBox.getSelectionModel().select(0);
    }

}
