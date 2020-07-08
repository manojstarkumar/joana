package com.avacado.stupidapps.joana.domain.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;

@Document("joana_tasks_execution")
@Valid
public class JoanaTaskExecution extends JoanaTaskBase {

    @NotBlank
    private JoanaTaskInitiator triggeredBy;

    @NotBlank
    private String parentTask;

    @Valid
    @Nullable
    private List<JoanaTaskComment> comments;

    @NotNull
    private JoanaStates state;

    @NotNull
    private Date startedAt;

    private Date completedAt;

    @NotNull
    private JoanaExecutionMode executionMode;

    public void setStartedAt(Date start) {
	if(start != null)
	    this.startedAt = (Date) start.clone();
    }

    public Date getStartedAt() {
	if(this.startedAt != null)
	    return (Date) this.startedAt.clone();
	return null;
    }

    public void setCompletedAt(Date finish) {
	if(finish != null)
	    this.completedAt = (Date) finish.clone();
    }

    public Date getCompletedAt() {
	if(this.completedAt != null)
	    return (Date) this.completedAt.clone();
	return null;
    }

    public JoanaTaskInitiator getTriggeredBy() {
	return triggeredBy;
    }

    public void setTriggeredBy(JoanaTaskInitiator triggeredBy) {
	this.triggeredBy = triggeredBy;
    }

    public String getParentTask() {
	return parentTask;
    }

    public void setParentTask(String parentTask) {
	this.parentTask = parentTask;
    }

    public List<JoanaTaskComment> getComments() {
	return comments;
    }

    public void addComment(String userName, String comments) {
	if (this.comments == null)
	    this.comments = new ArrayList<>();
	JoanaTaskComment taskComment = new JoanaTaskComment();
	taskComment.setComment(comments);
	taskComment.setUserName(userName);
	taskComment.setCommentDate(new Date());
	taskComment.setPosition(this.comments.size() + 1);
	this.comments.add(taskComment);
    }

    public JoanaStates getState() {
	return state;
    }

    public void setState(JoanaStates state) {
	this.state = state;
    }
    
    public JoanaExecutionMode getExecutionMode() {
	return executionMode;
    }

    public void setExecutionMode(JoanaExecutionMode executionMode) {
	this.executionMode = executionMode;
    }

    public static class JoanaTaskComment {
	private int position;

	@NotBlank
	private String comment;
	
	private String userName;
	
	private Date commentDate;

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public String getComment() {
	    return comment;
	}

	public void setComment(String comment) {
	    this.comment = comment;
	}

	public String getUserName() {
	    return userName;
	}

	public void setUserName(String userName) {
	    this.userName = userName;
	}

	public Date getCommentDate() {
	    if(commentDate != null)
		return (Date) commentDate.clone();
	    return null;
	}

	public void setCommentDate(Date commentDate) {
	    if(commentDate != null)
		this.commentDate = (Date) commentDate.clone();
	}
    }
    
    public static class JoanaTaskInitiator {
	private String name;
	private String initiatorId;
	
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getInitiatorId() {
	    return initiatorId;
	}
	public void setInitiatorId(String initiatorId) {
	    this.initiatorId = initiatorId;
	}
	
	public static JoanaTaskInitiator build() {
	    return new JoanaTaskInitiator();
	}
	
	public JoanaTaskInitiator withName(String name) {
	    this.setName(name);
	    return this;
	}
	
	public JoanaTaskInitiator withInitiatorId(String initiatorId) {
	    this.setInitiatorId(initiatorId);
	    return this;
	}
    }
    
    public static JoanaTaskExecution fromTask(JoanaTask task) {
	JoanaTaskExecution taskExecution = new JoanaTaskExecution();
	taskExecution.setName(task.getName());
	taskExecution.setParentTask(task.getId());
	task.getActions().forEach(action -> taskExecution.addAction(action));
	task.getReviewers().forEach(reviewer -> {
	    JoanaUser user = new JoanaUser();
	    user.setEmail(reviewer.getEmail());
	    user.setName(reviewer.getName());
	    taskExecution.addReviewer(user);
	});
	return taskExecution;
    }
}
