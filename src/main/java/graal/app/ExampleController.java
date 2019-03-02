package graal.app;

import com.amazonaws.partitions.model.Partitions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

@Controller("/")
public class ExampleController {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);
    private ObjectMapper mapper = new ObjectMapper()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Get("/ping")
    public Partitions index() throws IOException {
        LOG.info("Local Test");

        InputStream stream = ExampleController.class.getClassLoader().getResourceAsStream("com/amazonaws/partitions/endpoints.json");
        final Partitions partitions = mapper.readValue(stream, Partitions.class);
        LOG.info("Local Test partitions {}", partitions);
        return partitions;
    }
}
