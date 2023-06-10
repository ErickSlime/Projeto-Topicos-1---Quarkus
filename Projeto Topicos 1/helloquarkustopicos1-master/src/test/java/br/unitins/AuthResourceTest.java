package br.unitins;

import br.unitins.topicos1.dto.AuthUsuarioDTO;
import br.unitins.topicos1.dto.CadastroBasicoDTO;
import br.unitins.topicos1.service.HashService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class AuthResourceTest {
    
    @Inject
    HashService hashService;

    @Test
    public void logintest(){
        AuthUsuarioDTO login = new AuthUsuarioDTO("Lucas", "1233123");

        given().contentType(ContentType.JSON)
.body(login)
.when().post("/auth/login")
.then()
.statusCode(204);

    }

    @Test
    public void cadastrotest(){
        CadastroBasicoDTO login = new CadastroBasicoDTO("Lucass","Lucas@gamisa.com", "12s33123");

        given().contentType(ContentType.JSON)
.body(login)
.when().post("/auth/cadastro")
.then()
.statusCode(201);

    }
}
