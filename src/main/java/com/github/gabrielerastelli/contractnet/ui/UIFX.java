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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UIFX extends Application {

    Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        showHomePage();
    }

    private void showHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/homepage.fxml")));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Homepage");
        primaryStage.setScene(scene);

        HomepageUIController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.show();
    }

    public void showSimulationPage(Integer numberOfServers, Integer numberOfTasks, Integer tasksDuration, String simulationType) throws IOException {
        log.info("Number of servers: {}, number of tasks: {}, tasks duration: {}, simulation type: {}",
                numberOfServers, numberOfTasks, tasksDuration, simulationType);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simulation report");
        primaryStage.setScene(scene);

        SimulationUIController controller = loader.getController();

        primaryStage.show();

        // Run simulation on a separate thread
        new Thread(() -> {
            Simulation simulation = new Simulation(new ArrayList<>());
            controller.setTaskPublisher(simulation);

            List<Server> servers = new ServerGeneratorFactory().createServerGenerator().createServers();
            for (Server server : servers) {
                controller.setServerPublisher(server);
            }

            StopWatch watch = new StopWatch();
            watch.start();
            try {
                simulation.startSimulation(new TaskGeneratorFactory().createTaskGenerator().createTasks(), servers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            watch.stop();

            long seconds = watch.getTime() / 1000;
            long remainingMilliseconds = watch.getTime() % 1000;

            controller.updateTotalExecutionTime(String.format("%d:%d",
                    seconds,
                    remainingMilliseconds)
            );
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