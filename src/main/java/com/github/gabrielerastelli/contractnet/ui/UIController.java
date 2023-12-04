package com.github.gabrielerastelli.contractnet.ui;

import com.github.gabrielerastelli.contractnet.be.server.Server;
import com.github.gabrielerastelli.contractnet.interfaces.ServerPublisher;
import com.github.gabrielerastelli.contractnet.interfaces.ServerUpdateListener;
import com.github.gabrielerastelli.contractnet.interfaces.TaskPublisher;
import com.github.gabrielerastelli.contractnet.interfaces.TaskUpdateListener;
import com.github.gabrielerastelli.contractnet.ui.model.ServerWorkload;
import com.github.gabrielerastelli.contractnet.ui.model.TaskStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UIController implements TaskUpdateListener, ServerUpdateListener {

    @FXML
    TableView<TaskStatus> taskTableView;

    @FXML
    TableColumn<TaskStatus, String> taskId;

    @FXML
    TableColumn<TaskStatus, String> status;

    @FXML
    TableView<ServerWorkload> serverTableView;

    @FXML
    TableColumn<ServerWorkload, String> serverIp;

    @FXML
    TableColumn<ServerWorkload, Integer> currentWorkload;

    @FXML
    ComboBox<String> serverWorkloadComboBox;

    @FXML
    LineChart<Number, Number> serverWorkloadLineChart;

    final ObservableList<TaskStatus> tasks = FXCollections.observableArrayList();

    final ObservableList<ServerWorkload> servers = FXCollections.observableArrayList();

    final ObservableList<String> serverIpsList = FXCollections.observableArrayList();

    final ObservableList<XYChart.Series<Number, Number>> workloadSeriesList = FXCollections.observableArrayList();

    public void initialize() {
        taskId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskId()));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        taskTableView.setItems(tasks);

        serverIp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServerIp()));
        currentWorkload.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCurrentWorkload()).asObject());

        serverTableView.setItems(servers);

        serverWorkloadComboBox.setItems(serverIpsList);

        serverWorkloadLineChart.getYAxis().setAutoRanging(false);
        ((NumberAxis) serverWorkloadLineChart.getYAxis()).setUpperBound(5);
        ((NumberAxis) serverWorkloadLineChart.getYAxis()).setLowerBound(0);
        ((NumberAxis) serverWorkloadLineChart.getYAxis()).setTickUnit(1.0);
        ((NumberAxis) serverWorkloadLineChart.getYAxis()).setMinorTickVisible(false);

    }

    public void setTaskPublisher(TaskPublisher taskPublisher) {
        taskPublisher.addUpdateListener(this);
    }

    @Override
    public void onUpdate(String taskId, String status) {
        Platform.runLater(() -> {
            log.debug("Notified about task: {}", taskId);

            // Find the index of the TaskStatus object with the specified taskId
            TaskStatus key = new TaskStatus(taskId, null);
            int index = Collections.binarySearch(tasks, key, Comparator.comparing(TaskStatus::getTaskId));

            if (index >= 0) {
                // Retrieve the TaskStatus object and update its status
                TaskStatus taskToUpdate = tasks.get(index);
                taskToUpdate.setStatus(status);

                // Notify the ObservableList of the change
                taskTableView.getItems().set(index, taskToUpdate);
            } else {
                TaskStatus taskToUpdate = new TaskStatus(taskId, status);
                taskTableView.getItems().add(taskToUpdate);
                tasks.sort(Comparator.comparing(TaskStatus::getTaskId));
            }
        });
    }

    public void setServerPublisher(ServerPublisher serverPublisher) {
        serverPublisher.addUpdateListener(this);
    }

    @Override
    public void onUpdate(Server server, int currentWorkload) {
        Platform.runLater(() -> {
            log.debug("Notified about Server: {} workload: {}", server.getIp(), currentWorkload);

            ServerWorkload key = new ServerWorkload(server.getIp(), null);
            int index = Collections.binarySearch(servers, key, Comparator.comparing(ServerWorkload::getServerIp));

            if (index >= 0) {
                ServerWorkload serverWorkloadToUpdate = servers.get(index);
                serverWorkloadToUpdate.setCurrentWorkload(currentWorkload);

                serverTableView.getItems().set(index, serverWorkloadToUpdate);
            } else {
                ServerWorkload serverWorkloadToUpdate = new ServerWorkload(server.getIp(), currentWorkload);
                serverTableView.getItems().add(serverWorkloadToUpdate);
                servers.sort(Comparator.comparing(ServerWorkload::getServerIp));
            }

            XYChart.Series<Number, Number> series = getSeriesById(server.getIp());
            if(series == null) {
                series = new XYChart.Series<>();
                series.setName(server.getIp());
                series.getData().add(new XYChart.Data<>(series.getData().size(), currentWorkload));
                workloadSeriesList.add(series);
            } else {
                series.getData().add(new XYChart.Data<>(series.getData().size(), currentWorkload));
            }

            if (!serverIpsList.contains(server.getIp())) {
                serverIpsList.add(server.getIp());
            }

        });
    }

    @FXML
    private void handleDropdownAction() {
        String selectedId = serverWorkloadComboBox.getValue();
        serverWorkloadLineChart.getData().clear();
        serverWorkloadLineChart.getData().add(getSeriesById(selectedId));
    }

    private XYChart.Series<Number, Number> getSeriesById(String id) {
        for (XYChart.Series<Number, Number> series : workloadSeriesList) {
            if (series.getName().equals(id)) {
                return series;
            }
        }
        return null;
    }
}