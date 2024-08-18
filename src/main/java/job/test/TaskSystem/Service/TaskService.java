package job.test.TaskSystem.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import job.test.TaskSystem.DAO.TaskRepository;
import job.test.TaskSystem.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<TaskDTO> getAllAuthorTasks(String email) {
        return taskRepository.findAllByAuthorEmail(email)
                .stream()
                .map(Task::toDTO)
                .toList();
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

    public TaskDTO editTask(UserDTO userDTO, TaskDTO taskDTO){
        Task task = taskRepository.findByIdAndAuthorEmail(taskDTO.id, userDTO.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        task.loadFromDTO(taskDTO);

        return taskRepository.save(task).toDTO();
    }


    public TaskDTO addTask(UserDTO userDTO, TaskDTO taskDTO) {
        Task task = taskDTO.toEntity();

        if (taskRepository.existsById(taskDTO.id)){
            throw new EntityExistsException("Task already exists");
        }

        User author = userService.getUserByEmail(userDTO.getEmail());

        task.setAuthor(author);

        return taskRepository.save(task).toDTO();
    }
}
