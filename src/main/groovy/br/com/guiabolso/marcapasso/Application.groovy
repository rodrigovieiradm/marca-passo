package br.com.guiabolso.marcapasso

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
@EnableCaching
class Application extends SpringBootServletInitializer {

    Application() {
        super()
        setRegisterErrorPageFilter(false)
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class)
    }

    static void main(String[] args) {
        SpringApplication.run(Application, args)
    }

}