package job.test.TaskSystem.Service;

import jakarta.persistence.EntityNotFoundException;
import job.test.TaskSystem.DAO.TaskRepository;
import job.test.TaskSystem.Model.Task;
import job.test.TaskSystem.Model.TaskDTO;
import job.test.TaskSystem.Model.TaskStatus;
import job.test.TaskSystem.Model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllAuthorTasks(String email) {
        return taskRepository.findAllByAuthorEmail(email)
                .stream()
                .map(Task::toDTO)
                .toList();
    }

    public TaskDTO changeStatus(Long taskID, TaskStatus newStatus){
        Task task = taskRepository.findById(taskID)
                .orElseThrow(EntityNotFoundException::new);

        task.setStatus(newStatus);

        return taskRepository.save(task).toDTO();
    }

    public TaskDTO addWorker(Long taskID, UserDTO newWorker){
        Task task = taskRepository.findById(taskID)
                .orElseThrow(EntityNotFoundException::new);

        task.addWorker(newWorker.toEntity());

        return taskRepository.save(task).toDTO();
    }

    public TaskDTO removeWorker(Long taskID, UserDTO newWorker){
        Task task = taskRepository.findById(taskID)
                .orElseThrow(EntityNotFoundException::new);

        task.removeWorker(newWorker.toEntity());

        return taskRepository.save(task).toDTO();
    }

    public void deleteTask(Long taskID){
        Task task = taskRepository.findById(taskID)
                .orElseThrow(EntityNotFoundException::new);

        taskRepository.delete(task);
    }

    public TaskDTO editTask(TaskDTO dto){
        Task task = taskRepository.findById(dto.id)
                .orElseThrow(EntityNotFoundException::new);

        task.loadFromDTO(dto);

        return taskRepository.save(task).toDTO();
    }


}
