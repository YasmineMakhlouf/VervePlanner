package com.csis231.api.taskTag;

import jakarta.persistence.*;

@Entity
@Table(name = "task_tags")
@IdClass(TaskTagId.class)
public class TaskTag {
    @Id
    @Column(name = "task_id")
    private Long taskId;

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    public TaskTag() {}

    public TaskTag(Long taskId, Long tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getTagId() { return tagId; }
    public void setTagId(Long tagId) { this.tagId = tagId; }
}
