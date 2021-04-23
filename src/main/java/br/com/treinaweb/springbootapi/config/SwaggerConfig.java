package br.com.treinaweb.springbootapi.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Arquivo para habilitar o Swagger na aplicação
 *
 * No Spring Boot o Swagger é ativado através da anotação @EnableSwagger2
 * */
@Configuration
@EnableSwagger2
class SwaggerConfig {
    /**
     * O Docket que estamos definindo no nosso bean nos permite configurar aspectos dos endpoints expostos por ele
     *
     * Nos métodos apis() e paths() definimos que todas as apis e caminhos estarão disponíveis.
     * Com isso através de reflection a biblioteca já consegue obter os endpoints definidos na aplicação.
     *
     * No método apis podemos utilizar a classe RequestHandlerSelectors para
     * filtrar quais serão considerados com base no pacote ou anotação.
     * Por exemplo, para que seja listada apenas os endpoints definidos pela nossa aplicação,
     * utilizamos o método basePackage() desta classe
     *
     * Com a classe PathSelectors, também é possível filtrar os caminhos aceitos para os endpoints
     *
     * Quando a aplicação define alguma autenticação, é necessário configurar isso, para que o SpringFox
     * também especifique isso, e mesmo endpoints protegidos sejam testáveis.
     * Esta especificação é realizada com os métodos "securitySchemes" e "securityContexts".
     * No primeiro é definido o tipo de autenticação (no momento os suportados são: ApiKey, BasicAuth e OAuth).
     * Já no segundo são especificadas particularidades desta autenticação, como os escopos e endpoints que necessitam de autenticação
     *
     * O método "apiInfo" é usado para adicionar algumas informações da api
     */
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.treinaweb.springbootapi.controller"))    
          .paths(PathSelectors.any())
          .build()
          .useDefaultResponseMessages(false)                                   
          .globalResponseMessage(RequestMethod.GET, responseMessageForGET())
          .securitySchemes(Arrays.asList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
          .securityContexts(Arrays.asList(securityContext()))
          .apiInfo(apiInfo());
    }

    // Método usado para reunir algumas informações da API
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Simple Spring Boot REST API")
                .description("\"Um exemplo de aplicação Spring Boot REST API\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Wladimilson", "https://treinaweb.com.br", "contato@treinaweb.com.br"))
                .build();
    }

    /**
     * Nas configurações padrão, o Swagger irá indicar que os endpoints retornam os códigos 200, 201, 204, 401, 403 e 404.
     * Caso a sua aplicação não retorne todos esses códigos, você pode especificar quais códigos ela
     * retorna com o método globalResponseMessage
     *
     * O globalResponseMessage recebe por parâmetro o método HTTP e uma lista de ResponseMessage que indica quais
     * códigos e mensagens o método retorna. Para que a aplicação fique modular,
     * vamos definir os ResponseMessage n método "responseMessageForGET"
     *
     * Ele será passado como parâmetro do "globalResponseMessage" do Docket
     */
    private List<ResponseMessage> responseMessageForGET()
    {
        return new ArrayList<ResponseMessage>() {
            private static final long serialVersionUID = 1L;

            {
            add(new ResponseMessageBuilder()   
                .code(500)
                .message("500 message")
                .responseModel(new ModelRef("Error"))
                .build());
            add(new ResponseMessageBuilder() 
                .code(403)
                .message("Forbidden!")
                .build());
        }};
    }

    /**
     * Definido que os endpoint de /pessoa necessitam de autenticação
     *
     * São especificadas particularidades desta autenticação, como os escopos e endpoints que necessitam de autenticação
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.ant("/pessoa/**"))
            .build();
    }
    
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
            new SecurityReference("Token Access", authorizationScopes));
    }

    //http://localhost:8080/swagger-ui.html.apis(RequestHandlerSelectors.basePackage("br.com.treinaweb"))
}