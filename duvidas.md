### Gerando Token com JWT

- Em uma API Rest, não é uma boa prática utilizar autenticação com o uso de session;
- Uma das maneiras de fazer autenticação stateless é utilizando tokens JWT (Json Web Token);
- Para utilizar JWT na API, devemos adicionar a dependência da biblioteca jjwt no arquivo pom.xml do projeto;
- Para configurar a autenticação stateless no Spring Security, devemos utilizar o método sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
- Para disparar manualmente o processo de autenticação no Spring Security, devemos utilizar a classe AuthenticationManager;
- Para poder injetar o AuthenticationManager no controller, devemos criar um método anotado com @Bean, na classe SecurityConfigurations, que retorna uma chamada ao método super.authenticationManager();
- Para criar o token JWT, devemos utilizar a classe Jwts;
- O token tem um período de expiração, que pode ser definida no arquivo application.properties;
- Para injetar uma propriedade do arquivo application.properties, devemos utilizar a anotação @Value.

### Documentacao Swagger

A partir da versão 2.6 do Spring Boot houve uma mudança que impacta na utilização do Springfox Swagger,
causando uma exception ao rodar o projeto. É necessário adicionar a seguinte propriedade no arquivo application.
properties para que o projeto funcione sem problemas:

spring.mvc.pathmatch.matching-strategy=ant_path_matcher

Obs: Essa solução é incompatível com o módulo do Actuator, sendo que ao adicionar o actuator no projeto, 
o mesmo erro voltará a ocorrer. Para utilizar o Actuator em conjunto com o Springfox Swagger será necessário
então realizar o downgrade do Spring Boot para alguma versão anterior à versão 2.6.