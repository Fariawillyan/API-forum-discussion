package com.about.forum.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloControler {

    @RequestMapping("/")//url que o spring chama o metodo
    @ResponseBody // devolve string direto p navegador

    public String hello(){
        return "hello world";

    }

}
