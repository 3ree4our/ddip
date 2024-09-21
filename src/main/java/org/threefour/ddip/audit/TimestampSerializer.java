package org.threefour.ddip.audit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampSerializer extends JsonSerializer<Timestamp> {
  @Override
  public void serialize(Timestamp timestamp, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(timestamp.toInstant().toString());
  }
}
