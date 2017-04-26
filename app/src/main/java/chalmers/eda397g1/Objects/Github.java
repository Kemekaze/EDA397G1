package chalmers.eda397g1.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nume on 2017-04-25
 */

public class Github implements Serializable {
    private List<BacklogItem> backlogItems;
    private String columnId;
    private String fullName;
    private String projectId;
    private String repoId;

    public Github(List<BacklogItem> backlogItems, String columnId, String fullName, String projectId, String repoId) {
        this.backlogItems = backlogItems;
        this.columnId = columnId;
        this.fullName = fullName;
        this.projectId = projectId;
        this.repoId = repoId;
    }

    public List<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }
}
