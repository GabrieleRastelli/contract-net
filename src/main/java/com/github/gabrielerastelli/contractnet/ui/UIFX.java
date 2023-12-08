package com.github.gabrielerastelli.contractnet.ui;

import com.github.gabrielerastelli.contractnet.be.Simulation;
import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.contractnet.ContractNetSimulation;
import com.github.gabrielerastelli.contractnet.be.randomassignment.RandomAssignmentSimulation;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
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

    public void showSimulationPage(Integer numberOfServers, Integer numberOfThreads, Integer numberOfTasks, Integer tasksDuration, String simulationType) throws IOException {
        log.info("Number of servers: {}, number of threads: {}, number of tasks: {}, tasks duration: {}, simulation type: {}",
                numberOfServers, numberOfThreads, numberOfTasks, tasksDuration, simulationType);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simulation report");
        primaryStage.setScene(scene);

        SimulationUIController controller = loader.getController();

        primaryStage.show();

        // Run simulation on a separate thread
        new Thread(() -> {

            SimulationType type = null;
            if("ContractNet (Accept first)".equals(simulationType)) {
                type = SimulationType.CONTRACT_NET_ACCEPT_FIRST;
            } else if("ContractNet (Balanced)".equals(simulationType)) {
                type = SimulationType.CONTRACT_NET_BALANCED;
            } else if("Random A Priori".equals(simulationType)) {
                type = SimulationType.RANDOM_A_PRIORI;
            }

            Simulation simulation;
            switch (type) {
                case CONTRACT_NET_ACCEPT_FIRST:
                case CONTRACT_NET_BALANCED:
                    simulation = new ContractNetSimulation(new ArrayList<>());
                    break;
                case RANDOM_A_PRIORI:
                    simulation = new RandomAssignmentSimulation(new ArrayList<>());
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported simulation " + type);
            }
            controller.setTaskPublisher(simulation);

            List<IServer> servers = new ServerGeneratorFactory().createServerGenerator().createServers(type, numberOfServers, numberOfThreads);
            for (IServer server : servers) {
                controller.setServerPublisher(server);
            }

            StopWatch watch = new StopWatch();
            watch.start();
            try {
                simulation.startSimulation(type, new TaskGeneratorFactory().createTaskGenerator().createTasks(numberOfTasks, tasksDuration * 1000), servers);
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
            double mean = controller.setTasksExecutedMean();
            controller.setTasksExecutedVariance(mean);
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