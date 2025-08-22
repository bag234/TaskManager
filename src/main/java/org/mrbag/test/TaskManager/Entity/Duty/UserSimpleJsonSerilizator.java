package org.mrbag.test.TaskManager.Entity.Duty;

import java.io.IOException;

import org.mrbag.test.TaskManager.Entity.User;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class UserSimpleJsonSerilizator extends JsonSerializer<User> {

	@Override
	public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.getId());
	}

}
