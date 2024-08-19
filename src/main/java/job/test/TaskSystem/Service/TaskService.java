package job.test.TaskSystem.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import job.test.TaskSystem.DAO.TaskRepository;
import job.test.TaskSystem.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Page<TaskDTO> getAllAuthorTasks(UserDTO userDTO, Pageable pageable) {
        return taskRepository.findAllByAuthorEmail(userDTO.getEmail(), pageable).map(Task::toDTO);
    }

    public Page<TaskDTO> getAllUserTasks(String email, Pageable pageable) {
        return taskRepository.findAllByAuthorEmail(email, pageable).map(Task::toDTO);
    }

    public Page<TaskDTO> getAllUserTasksByStatus(String email, TaskStatus status, Pageable pageable) {
        return taskRepository.findAllByAuthorEmailAndStatus(email, status, pageable).map(Task::toDTO);
    }

    public Page<TaskDTO> getAllUserTasksByPriority(String email, TaskPriority priority, Pageable pageable) {
        return taskRepository.findAllByAuthorEmailAndPriority(email, priority, pageable).map(Task::toDTO);
    }

    public TaskDTO changeStatus(Long taskID, TaskStatus newStatus, UserDTO user){
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        task.setStatus(newStatus);

        return taskRepository.save(task).toDTO();
    }

    public TaskDTO addWorker(Long taskID, String newWorkerEmail, UserDTO user){
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        User newWorker = userService.getUserByEmail(newWorkerEmail);

        task.addWorker(newWorker);

        return taskRepository.save(task).toDTO();
    }

    public TaskDTO removeWorker(Long taskID, String newWorkerEmail, UserDTO user){
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        User newWorker = userService.getUserByEmail(newWorkerEmail);

        task.removeWorker(newWorker);

        return taskRepository.save(task).toDTO();
    }

    public void deleteTask(UserDTO user, Long taskID){
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        taskRepository.delete(task);
    }

    public TaskDTO editTask(UserDTO userDTO, Long taskID, String title, String comment){
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, userDTO.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        task.setTitle(title);
        task.setComment(comment);

        return taskRepository.save(task).toDTO();
    }


    public TaskDTO addTask(UserDTO userDTO, String title, String comment, TaskPriority priority) {
        if (taskRepository.existsByTitleAndAuthorEmail(title, userDTO.getEmail())){
            throw new EntityExistsException("Task with this title already exists");
        }

        Task task = Task.builder()
                .id(null)
                .author(userService.getUserByEmail(userDTO.getEmail()))
                .workers(new ArrayList<>())
                .title(title)
                .status(TaskStatus.Received)
                .priority(priority)
                .comment(comment)
                .build();

        return taskRepository.save(task).toDTO();
    }


}
