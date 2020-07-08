package com.avacado.stupidapps.joana.domain.task;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.avacado.stupidapps.joana.domain.JoanaExecutionHistory;

@Valid
@Document("joana_tasks")
public class JoanaTask extends JoanaTaskBase {

    @NotBlank
    @Indexed
    private String owner;

    private List<JoanaExecutionHistory> history;

    public String getOwner()
    {
      return owner;
    }

    public void setOwner(String owner)
    {
      this.owner = owner;
    }

    public List<JoanaExecutionHistory> getHistory() {
	return this.history;
    }

    public void addExecutionHistory(JoanaExecutionHistory history) {
	if (this.history == null)
	    this.history = new ArrayList<>();
	this.history.add(history);
    }

}
