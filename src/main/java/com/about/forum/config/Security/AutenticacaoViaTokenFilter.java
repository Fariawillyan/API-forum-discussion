package com.about.forum.config.Security;

import com.about.forum.Model.Usuario;
import com.about.forum.Repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *     Para enviar o token JWT na requisição, é necessário adicionar o cabeçalho Authorization, passando como valor Bearer token;
 *     Para criar um filtro no Spring, devemos criar uma classe que herda da classe OncePerRequestFilter;
 *     Para recuperar o token JWT da requisição no filter, devemos chamar o método request.getHeader("Authorization");
 *     Para habilitar o filtro no Spring Security, devemos chamar o método and().addFilterBefore(new AutenticacaoViaTokenFilter(), UsernamePasswordAuthenticationFilter.class);
 *     Para indicar ao Spring Security que o cliente está autenticado, devemos utilizar a classe SecurityContextHolder, chamando o método SecurityContextHolder.getContext().setAuthentication(authentication).
 *     OncePerRequestFilter chama uma vez a cada requisicao
*/



public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UsuarioRepository repository;


    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = requestToken(request);
        boolean valido = tokenService.isTokenValido(token);
        if(valido){
            autenticarCliente(token);
        }


        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario = repository.findById(idUsuario).get();
     UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private String requestToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isBlank() || token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
