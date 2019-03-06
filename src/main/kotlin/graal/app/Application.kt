package graal.app

import io.micronaut.runtime.Micronaut
import org.apache.commons.logging.LogFactory

object Application {

    private val LOG = LogFactory.getLog(ExampleController::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        LOG.info("Registering truststore in Application")
        System.setProperty("javax.net.ssl.trustStore","/home/application/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        Micronaut.build()
                .packages("graal.app")
                .mainClass(Application.javaClass)
                .start()
    }
}
