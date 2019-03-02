package graal.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.partitions.PartitionsLoader;
import com.amazonaws.partitions.model.Partitions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.http.annotation.*;

@Controller("/")
public class ExampleController {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);
    private ObjectMapper mapper = new ObjectMapper()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Get("/ping")
    public String index() throws IOException {
        LOG.info("Local Test");

        String json = "{\n" +
                "  \"partitions\" : [ {\n" +
                "    \"defaults\" : {\n" +
                "      \"hostname\" : \"{service}.{region}.{dnsSuffix}\",\n" +
                "      \"protocols\" : [ \"https\" ],\n" +
                "      \"signatureVersions\" : [ \"v4\" ]\n" +
                "    }\n" +
                "  }]\n" +
                "}";

        ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());
        final Partitions partitions = mapper.readValue(stream, Partitions.class);
        LOG.info("Local Test partitions {}", partitions);
        return "{\"pong\":true, \"graal\": true}";
    }
}
