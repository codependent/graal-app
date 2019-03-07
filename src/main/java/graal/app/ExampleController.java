package graal.app;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.http.apache.client.impl.ApacheConnectionManagerFactory;
import com.amazonaws.http.apache.client.impl.ApacheHttpClientFactory;
import com.amazonaws.http.apache.client.impl.ConnectionManagerAwareHttpClient;
import com.amazonaws.http.client.ConnectionManagerFactory;
import com.amazonaws.http.client.HttpClientFactory;
import com.amazonaws.http.conn.ClientConnectionManagerFactory;
import com.amazonaws.http.settings.HttpClientSettings;
import com.amazonaws.partitions.model.Partitions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;

@Controller("/")
public class ExampleController {

    private static final Log LOG = LogFactory.getLog(ExampleController.class);
    private ObjectMapper mapper = new ObjectMapper()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final HttpClientFactory<ConnectionManagerAwareHttpClient> httpClientFactory = new
            ApacheHttpClientFactory();

    private HttpClient httpClient;

    public ExampleController() {
        this.httpClient = httpClientFactory.create(HttpClientSettings.adapt(new ClientConfiguration()));
        LOG.info("init");
        final ConnectionManagerFactory<HttpClientConnectionManager> cmFactory = new ApacheConnectionManagerFactory();
        final HttpClientConnectionManager cm = cmFactory.create(HttpClientSettings.adapt(new ClientConfiguration()));
        ClientConnectionManagerFactory.wrap(cm);
    }

    @Get("/ping")
    public Partitions index() throws IOException {
        LOG.info("Local Test");
        LOG.info(httpClient);
        InputStream stream = ExampleController.class.getClassLoader().getResourceAsStream("com/amazonaws/partitions/endpoints.json");
        final Partitions partitions = mapper.readValue(stream, Partitions.class);
        LOG.info("Local Test partitions " + partitions);
        return partitions;
    }

    @Get("/http")
    public String httpClient() throws IOException {
        String url = "https://www.google.com/search?q=httpClient";
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        LOG.info("Response Code : {}" + response.getStatusLine().getStatusCode());
        return "ok";
    }
}
