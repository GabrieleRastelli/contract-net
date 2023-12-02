package com.github.gabrielerastelli.contractnet;

import com.github.gabrielerastelli.contractnet.server.Server;
import com.github.gabrielerastelli.contractnet.server.ServerGenerator;
import com.github.gabrielerastelli.contractnet.server.factory.ServerGeneratorFactory;
import com.github.gabrielerastelli.contractnet.task.Task;
import com.github.gabrielerastelli.contractnet.task.TaskGenerator;
import com.github.gabrielerastelli.contractnet.task.factory.TaskGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainServiceTests {

    @InjectMocks
    MainService mainService;

    @Mock
    ServerGeneratorFactory serverGeneratorFactory;

    @Mock
    ServerGenerator serverGenerator;

    @Mock
    TaskGeneratorFactory taskGeneratorFactory;

    @Mock
    TaskGenerator taskGenerator;

    @Test
    public void oneTaskAndOneServer() throws Exception {
        when(serverGenerator.createServers()).thenReturn(List.of(new Server("0.0.0.0", 1)));
        when(serverGeneratorFactory.createServerGenerator()).thenReturn(serverGenerator);

        when(taskGenerator.createTasks()).thenReturn(List.of(new Task(UUID.randomUUID().toString(), 3000)));
        when(taskGeneratorFactory.createTaskGenerator()).thenReturn(taskGenerator);

        mainService.startSimulation();
    }

    @Test
    public void twoTasksAndOneServer() throws Exception {
        when(serverGenerator.createServers()).thenReturn(List.of(new Server("0.0.0.0", 1)));
        when(serverGeneratorFactory.createServerGenerator()).thenReturn(serverGenerator);

        when(taskGenerator.createTasks()).thenReturn(List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 1000)));
        when(taskGeneratorFactory.createTaskGenerator()).thenReturn(taskGenerator);

        mainService.startSimulation();
    }

    @Test
    public void oneTaskAndTwoServer() throws Exception {
        when(serverGenerator.createServers()).thenReturn(List.of(new Server("0.0.0.0", 1),
                new Server("1.1.1.1", 1)));
        when(serverGeneratorFactory.createServerGenerator()).thenReturn(serverGenerator);

        when(taskGenerator.createTasks()).thenReturn(List.of(new Task(UUID.randomUUID().toString(), 3000)));
        when(taskGeneratorFactory.createTaskGenerator()).thenReturn(taskGenerator);

        mainService.startSimulation();
    }

    @Test
    public void twoTasksAndTwoServer() throws Exception {
        when(serverGenerator.createServers()).thenReturn(List.of(new Server("0.0.0.0", 1),
                new Server("1.1.1.1", 1)));
        when(serverGeneratorFactory.createServerGenerator()).thenReturn(serverGenerator);

        when(taskGenerator.createTasks()).thenReturn(List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 1000)));
        when(taskGeneratorFactory.createTaskGenerator()).thenReturn(taskGenerator);

        mainService.startSimulation();
    }

    @Test
    public void sixTasksAndTwoServer() throws Exception {
        when(serverGenerator.createServers()).thenReturn(List.of(new Server("0.0.0.0", 1),
                new Server("1.1.1.1", 1)));
        when(serverGeneratorFactory.createServerGenerator()).thenReturn(serverGenerator);

        when(taskGenerator.createTasks()).thenReturn(List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 1000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000)));
        when(taskGeneratorFactory.createTaskGenerator()).thenReturn(taskGenerator);

        mainService.startSimulation();
    }
}
