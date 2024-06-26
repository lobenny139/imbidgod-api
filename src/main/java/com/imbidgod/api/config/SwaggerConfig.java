package com.imbidgod.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@ConditionalOnExpression(value = "${useSwagger:false}")
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    /* controller  的 package 名 */
    static final String basePackage = "com.imbidgod.api.controller";

    /* 文件title */
    static final String docTitle = "Imbidgod APIs 說明";

    /* 作者信箱 */
    final static String mail = "benny139@gmail.com";

    /* 版号 */
    final static String version = "1.0.0";

    /* 開發團隊名稱 */
    final static String devTeamName = "開發團隊名稱";

    @Bean
    public Docket createRestApi() {
        //=====添加head参数start============================
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        tokenPar.name("oToken").description("Access Token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
        // =========添加head参数end===================

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())

                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))

                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())//paths(PathSelectors.regex("/.*"))
                .build();
//                .globalOperationParameters(pars);//************把消息头添加

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(docTitle)
                .version(version)
                .contact(new Contact("Dev-Team", "", mail))
                .build();
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }


}
