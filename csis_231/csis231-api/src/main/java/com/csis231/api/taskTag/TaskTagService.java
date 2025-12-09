package com.csis231.api.taskTag;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskTagService {
    private final TaskTagRepository repo;

    public TaskTagService(TaskTagRepository repo) {
        this.repo = repo;
    }

    public List<TaskTag> getAllTaskTags() {
        return repo.findAll();
    }

    public TaskTag getTaskTagById(Long taskId, Long tagId) {
        return repo.findById(new TaskTagId(taskId, tagId))
                .orElseThrow(() -> new RuntimeException("TaskTag not found"));
    }

    public TaskTag createTaskTag(TaskTag taskTag) {
        return repo.save(taskTag);
    }

    public TaskTag updateTaskTag(Long taskId, Long tagId, TaskTag taskTag) {
        TaskTag existing = repo.findById(new TaskTagId(taskId, tagId))
                .orElseThrow(() -> new RuntimeException("TaskTag not found"));

        existing.setTaskId(taskTag.getTaskId());
        existing.setTagId(taskTag.getTagId());

        return repo.save(existing);
    }

    public void deleteTaskTag(Long taskId, Long tagId) {
        TaskTagId id = new TaskTagId(taskId, tagId);
        if (!repo.existsById(id)) {
            throw new RuntimeException("TaskTag not found");
        }
        repo.deleteById(id);
    }
}
