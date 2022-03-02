package com.about.forum.config.Security;


import com.about.forum.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * # @EnableWebSecurity ---> Os endpoints se tornam restritos, por padrão.
 * # 1 AuthenticationManagerBuilder ----> configuracoes de autenticacao
 * (AutenticacaoService, UsuarioRepository)
 * BCryptPasswordEncoder para gerar algoritmo de criptragafia
 *
 *
 * # 2 HttpSecurity ----> configuracao de autorizacao
 * # csrf().disable() ---> disabilitar para evitar tipos de ataque hacker
 * # SessionCreationPolicy.STATELESS ----> representa um servico e nao guarda informacoes.
 *
 * # 3 WebSecurity ----> configuracao de recursos estaticos(js, css, imagens e etc...)
 * ()
 */


@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService AutenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }



    @Override //1
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(AutenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

    }
    @Override //2
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()
                .antMatchers(HttpMethod.GET,"/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST,"/auth").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, UsuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    @Override //3
    public void configure(WebSecurity web) throws Exception{

    }

/*
para gerar a senha criptografada e jogar no data.sql para testar
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }*/





}
