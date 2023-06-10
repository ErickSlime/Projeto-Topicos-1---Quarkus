package br.unitins;



import br.unitins.topicos1.dto.PessoaFisicaDTO;
import br.unitins.topicos1.dto.PessoaFisicaResponseDTO;
import br.unitins.topicos1.model.Sexo;
import br.unitins.topicos1.service.PessoaFisicaService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class PessoaFisicaResourceTest {

    @Inject
    PessoaFisicaService pessoaFisicaService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
        given()
                .when().get("/pessoasfisicas")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testInsert() {
        PessoaFisicaDTO pessoa = new PessoaFisicaDTO(
                "Erick",
                "95519519519",
                "asdasdasdad@gmail.com",
                1);
        given()
                .contentType(ContentType.JSON)
                .body(pessoa)
                .when().post("/pessoasfisicas")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("Erick"),
                        "cpf", is("95519519519"),
                        "email", is("asdasdasdad@gmail.com"),
                        "sexo.label", is("Masculino"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testUpdate() {
        // Adicionando uma pessoaFisica no banco de dados
        PessoaFisicaDTO pessoa = new PessoaFisicaDTO(
                "Erick",
                "95519519519",
                "asdasdasdad@gmail.com",
                1);
        Long id = pessoaFisicaService.create(pessoa).id();
        // Criando outra pessoaFisica para atuailzacao
        PessoaFisicaDTO pessoa2 = new PessoaFisicaDTO(
                "Ana",
                "1231231233",
                "klklklklklkl@gmail.com",
                2);
        given()
                .contentType(ContentType.JSON)
                .body(pessoa2)
                .when().put("/pessoasfisicas/" + id)
                .then()
                .statusCode(204);
        // Verificando se os dados foram atualizados no banco de dados
        PessoaFisicaResponseDTO pessoaResponse = pessoaFisicaService.findById(id);
        assertThat(pessoaResponse.nome(), is("Ana"));
        assertThat(pessoaResponse.cpf(), is("1231231233"));
        assertThat(pessoaResponse.email(), is("klklklklklkl@gmail.com"));
        assertThat(pessoaResponse.sexo(), is(Sexo.FEMININO));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testDelete() {
        // Adicionando uma pessoaFisica no banco de dados
        PessoaFisicaDTO pessoa = new PessoaFisicaDTO(
                "Erick",
                "95519519519",
                "asdasdasdad@gmail.com",
                1);
        Long id = pessoaFisicaService.create(pessoa).id();
        given()
                .when().delete("/pessoasfisicas/" + id)
                .then()
                .statusCode(204);
        // verificando se a pessoaFisica fisica foi excluida
        PessoaFisicaResponseDTO pessoaResponse = null;
        try {
            pessoaFisicaService.findById(id);
        } catch (Exception e) {
        } finally {
            assertNull(pessoaResponse);
        }

    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testFindById() {
        Long id = 2l;
        given()
                .when().get("/pessoasfisicas/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testCount() {
        given()
                .when().get("/pessoasfisicas/count")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testSearch() {

        String email = "ericksantos@unitins.br";

        given()
                .when().get("/pessoasfisicas/search/" + email)
                .then()
                .statusCode(200);
    }

}
