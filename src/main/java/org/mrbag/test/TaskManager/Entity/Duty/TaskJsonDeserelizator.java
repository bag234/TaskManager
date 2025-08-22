package org.mrbag.test.TaskManager.Entity.Duty;

import java.io.IOException;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.mrbag.test.TaskManager.Entity.User;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class TaskJsonDeserelizator extends JsonDeserializer<Task> {

	@Override
	public Task deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode jn = p.getCodec().readTree(p);
		Task.TaskBuilder t = Task.builder();
		t.id(jn.get("id").asLong())
			.title(jn.get("title").asText())
			.description(jn.get("description").asText())
			.status(TaskStatus.valueOf(jn.get("status").asText()))
			.priority(TaskPriority.valueOf(jn.get("priority").asText()))
			.author(jn.get("author").isNull() ? null : User.builder().id(jn.get("author").asLong()).build())
			.assigner(jn.get("assigner").isNull() ? null : User.builder().id(jn.get("assigner").asLong()).build());
		return t.build();
	}

}
