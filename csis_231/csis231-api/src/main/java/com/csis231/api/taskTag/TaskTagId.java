package com.csis231.api.taskTag;

import java.io.Serializable;
import java.util.Objects;

public class TaskTagId implements Serializable {
    private Long taskId;
    private Long tagId;

    public TaskTagId() {}
    public TaskTagId(Long taskId, Long tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskTagId)) return false;
        TaskTagId that = (TaskTagId) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, tagId);
    }
}
