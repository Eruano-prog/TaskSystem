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

/**
 * Сервис для управления задачами.
 * Предоставляет методы для создания, обновления, удаления и получения задач,
 * а также для управления статусами и работниками задач.
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    /**
     * Получает страницу задач, созданных автором.
     *
     * @param userDTO   DTO пользователя, который является автором задач.
     * @param pageable  Параметры пагинации.
     * @return Страница задач, созданных автором.
     */
    public Page<TaskDTO> getAllAuthorTasks(UserDTO userDTO, Pageable pageable) {
        return taskRepository.findAllByAuthorEmail(userDTO.getEmail(), pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач, созданных пользователем с указанным email.
     *
     * @param email     Email пользователя, который является автором задач.
     * @param pageable  Параметры пагинации.
     * @return Страница задач, созданных пользователем.
     */
    public Page<TaskDTO> getAllUserTasks(String email, Pageable pageable) {
        return taskRepository.findAllByAuthorEmail(email, pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач, созданных пользователем с указанным email и статусом.
     *
     * @param email    Email пользователя, который является автором задач.
     * @param status   Статус задач.
     * @param pageable Параметры пагинации.
     * @return Страница задач, созданных пользователем и имеющих указанный статус.
     */
    public Page<TaskDTO> getAllUserTasksByStatus(String email, TaskStatus status, Pageable pageable) {
        return taskRepository.findAllByAuthorEmailAndStatus(email, status, pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач, созданных пользователем с указанным email и приоритетом.
     *
     * @param email    Email пользователя, который является автором задач.
     * @param priority Приоритет задач.
     * @param pageable Параметры пагинации.
     * @return Страница DTO задач, созданных пользователем и имеющих указанный приоритет.
     */
    public Page<TaskDTO> getAllUserTasksByPriority(String email, TaskPriority priority, Pageable pageable) {
        return taskRepository.findAllByAuthorEmailAndPriority(email, priority, pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач, исполнителем которой является пользователь с указанным email.
     *
     * @param email     Email пользователя, который является исполнителем задач.
     * @param pageable  Параметры пагинации.
     * @return Страница задач, созданных пользователем.
     */
    public Page<TaskDTO> getAllWorkerTasks(String email, Pageable pageable) {
        return taskRepository.findAllByWorkersEmail(email, pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач с указанным статусом, исполнителем которой является пользователь с указанным email.
     *
     * @param email     Email пользователя, который является исполнителем задач.
     * @param pageable  Параметры пагинации.
     * @return Страница задач, созданных пользователем.
     */
    public Page<TaskDTO> getAllWorkerTasksByStatus(String email, TaskStatus status, Pageable pageable) {
        return taskRepository.findAllByWorkersEmailAndStatus(email, status, pageable).map(Task::toDTO);
    }

    /**
     * Получает страницу задач с указанным приоритетом, исполнителем которой является пользователь с указанным email.
     *
     * @param email     Email пользователя, который является исполнителем задач.
     * @param pageable  Параметры пагинации.
     * @return Страница задач, созданных пользователем.
     */
    public Page<TaskDTO> getAllWorkerTasksByPriority(String email, TaskPriority priority, Pageable pageable) {
        return taskRepository.findAllByWorkersEmailAndPriority(email, priority, pageable).map(Task::toDTO);
    }

    /**
     * Изменяет статус задачи.
     *
     * @param taskID    ID задачи.
     * @param newStatus Новый статус задачи.
     * @param user      DTO пользователя, который является автором задачи.
     * @return Обновленный DTO задачи.
     * @throws EntityNotFoundException Если задача не найдена.
     */
    public TaskDTO changeStatus(Long taskID, TaskStatus newStatus, UserDTO user) throws EntityNotFoundException {
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        task.setStatus(newStatus);

        return taskRepository.save(task).toDTO();
    }

    /**
     * Добавляет исполнителя к задаче.
     *
     * @param taskID         ID задачи.
     * @param newWorkerEmail Email нового работника.
     * @param user           DTO пользователя, который является автором задачи.
     * @return Обновленный DTO задачи.
     * @throws EntityNotFoundException Если задача или работник не найдены.
     */
    public TaskDTO addWorker(Long taskID, String newWorkerEmail, UserDTO user) throws EntityNotFoundException {
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        User newWorker = userService.getUserByEmail(newWorkerEmail);

        if (task.getWorkers().stream()
                .anyMatch(worker -> worker.getEmail().equals(newWorker.getEmail()))) {
            throw new EntityExistsException("Worker " + newWorkerEmail + " already in workers list");
        }

        task.addWorker(newWorker);

        return taskRepository.save(task).toDTO();
    }

    /**
     * Удаляет работника из задачи.
     *
     * @param taskID         ID задачи.
     * @param newWorkerEmail Email работника, которого нужно удалить.
     * @param user           DTO пользователя, который является автором задачи.
     * @return Обновленный DTO задачи.
     * @throws EntityNotFoundException Если задача или работник не найдены.
     */
    public TaskDTO removeWorker(Long taskID, String newWorkerEmail, UserDTO user) throws EntityNotFoundException {
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        User newWorker = userService.getUserByEmail(newWorkerEmail);

        task.removeWorker(newWorker);

        return taskRepository.save(task).toDTO();
    }

    /**
     * Удаляет задачу.
     *
     * @param user   DTO пользователя, который является автором задачи.
     * @param taskID ID задачи.
     * @throws EntityNotFoundException Если задача не найдена.
     */
    public void deleteTask(UserDTO user, Long taskID) throws EntityNotFoundException {
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, user.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        taskRepository.delete(task);
    }

    /**
     * Редактирует задачу по её ID.
     *
     * @param userDTO DTO пользователя, который является автором задачи.
     * @param taskID  ID задачи.
     * @param title   Новый заголовок задачи.
     * @param comment Новый комментарий к задаче.
     * @return Обновленный DTO задачи.
     * @throws EntityNotFoundException Если задача не найдена.
     */
    public TaskDTO editTask(UserDTO userDTO, Long taskID, String title, String comment) throws EntityNotFoundException {
        Task task = taskRepository.findByIdAndAuthorEmail(taskID, userDTO.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        task.setTitle(title);
        task.setComment(comment);

        return taskRepository.save(task).toDTO();
    }

    /**
     * Добавляет новую задачу.
     *
     * @param userDTO  DTO пользователя, который является автором задачи.
     * @param title    Заголовок задачи.
     * @param comment  Комментарий к задаче.
     * @param priority Приоритет задачи.
     * @return DTO новой задачи.
     * @throws EntityExistsException Если задача с таким заголовком уже существует.
     */
    public TaskDTO addTask(UserDTO userDTO, String title, String comment, TaskPriority priority) throws EntityExistsException {
        if (taskRepository.existsByTitleAndAuthorEmail(title, userDTO.getEmail())) {
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
