package graal.app

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("graal.app")
                .mainClass(Application.javaClass)
                .start()
    }
}