package com.kalettre.springboot.app.commons.usuarios;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//el packete debe de ser diferente al del microservicio que lo va a consumir
@SpringBootApplication
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class})
public class SpringbootServicioUsuariosCommonsApplication {



}
