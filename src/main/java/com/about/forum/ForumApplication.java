package com.about.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * # @SpringBootApplication ----> é uma anotação de conveniência que contém as seguintes anotações do Spring.
 * # @EnableSpringDataWebSupport ----> habilita suporte p spring pegar requisao, campos, ordenacao e repassar!
 * # @EnableCaching ---->  habilita o uso de cache na aplicacao
 *
 *
 */


@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }



}
