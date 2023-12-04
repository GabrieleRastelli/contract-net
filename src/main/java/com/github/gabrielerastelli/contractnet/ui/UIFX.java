package com.github.gabrielerastelli.contractnet.ui;

import com.github.gabrielerastelli.contractnet.be.Simulation;
import com.github.gabrielerastelli.contractnet.be.server.Server;
import com.github.gabrielerastelli.contractnet.be.server.factory.ServerGeneratorFactory;
import com.github.gabrielerastelli.contractnet.be.task.factory.TaskGeneratorFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/prova.fxml")));
        Parent root = loader.load();
        UIController controller = loader.getController();

        // Set up UI components

        stage.setTitle("Server Task Assignment Simulation");
        stage.setScene(new Scene(root));
        stage.show();

        // Run simulation on a separate thread
        new Thread(() -> {
            Simulation simulation = new Simulation(new ArrayList<>());
            controller.setTaskPublisher(simulation);

            List<Server> servers = new ServerGeneratorFactory().createServerGenerator().createServers();
            for (Server server : servers) {
                controller.setServerPublisher(server);
            }

            try {
                simulation.startSimulation(new TaskGeneratorFactory().createTaskGenerator().createTasks(), servers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void stop(){
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

}