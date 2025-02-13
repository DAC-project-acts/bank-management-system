package com.bank.security.configurations;

import java.net.URL;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "NexBank Digital System Services",
							description = "This is api for banking systems",
							termsOfService = "Terms and Condition applied ...",
							contact = @Contact(name = "Develop By Team Incredible",
											email = "cdacchetan@gmail.com"),
							license = @License(name = "chetan licence "),
							version = "Api/V1"),
							servers = {@Server(description = "testEnv")},security = @SecurityRequirement(name = "bankSecurity"))

@SecurityScheme (name = "bankSecurity",in = SecuritySchemeIn.HEADER,type = SecuritySchemeType.HTTP,
bearerFormat  = "JWT",
scheme  = "Bearer ")
public class SwaggerConfiguration{

}