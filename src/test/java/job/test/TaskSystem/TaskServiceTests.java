package job.test.TaskSystem;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import job.test.TaskSystem.DAO.TaskRepository;
import job.test.TaskSystem.Model.*;
import job.test.TaskSystem.Service.TaskService;
import job.test.TaskSystem.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private UserDTO userDTO;
    private Task task;
    private User user;

    @BeforeEach
    public void setUp() {
        userDTO = UserDTO.builder()
                .nickName("testUser")
                .email("test@example.com")
                .build();
        user = User.builder()
                .nickname("testUser")
                .email("test@example.com")
                .password("password")
                .role("User")
                .build();
        task = Task.builder()
                .id(1L)
                .author(user)
                .workers(new ArrayList<>())
                .title("Test Task")
                .status(TaskStatus.Received)
                .priority(TaskPriority.High)
                .comment("Test Comment")
                .build();
    }

    @Test
    public void testGetAllAuthorTasks() {
        List<Task> tasks = List.of(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAllByAuthorEmail(anyString(), any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDTO> result = taskService.getAllAuthorTasks(userDTO, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
    }

    @Test
    public void testChangeStatus() {
        when(taskRepository.findByIdAndAuthorEmail(anyLong(), anyString())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.changeStatus(1L, TaskStatus.In_progress, userDTO);

        assertNotNull(result);
        assertEquals(TaskStatus.In_progress, TaskStatus.valueOf(result.getStatus()));
    }

    @Test
    public void testChangeStatus_TaskNotFound() {
        when(taskRepository.findByIdAndAuthorEmail(anyLong(), anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.changeStatus(1L, TaskStatus.In_progress, userDTO);
        });
    }

    @Test
    public void testAddTask_Success() {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(taskRepository.existsByTitleAndAuthorEmail(anyString(), anyString())).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.addTask(userDTO, "Test Task", "Test Comment", TaskPriority.High);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    public void testAddTask_TaskAlreadyExists() {
        when(taskRepository.existsByTitleAndAuthorEmail(anyString(), anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            taskService.addTask(userDTO, "New Task", "New Comment", TaskPriority.High);
        });
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.findByIdAndAuthorEmail(anyLong(), anyString())).thenReturn(Optional.of(task));

        taskService.deleteTask(userDTO, 1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void testDeleteTask_TaskNotFound() {
        when(taskRepository.findByIdAndAuthorEmail(anyLong(), anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.deleteTask(userDTO, 1L);
        });
    }
}
