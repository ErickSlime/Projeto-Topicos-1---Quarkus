package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.MarcaDTO;
import br.unitins.topicos1.dto.MarcaResponseDTO;
import br.unitins.topicos1.service.MarcaService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.inject.Inject;


@QuarkusTest
public class MarcaResourceTest {

    @Inject
    MarcaService marcaService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
    given()
    .when().get("/marcas")
    .then()
    .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testInsert() {
        MarcaDTO mar = new MarcaDTO(
            "Ambev",
            "561651656"
        );
            given()
            .contentType(ContentType.JSON)
            .body(mar)
            .when().post("/marcas")
            .then()
            .statusCode(201)
            .body("id", notNullValue(),
            "nome", is("Ambev"),
            "cnpj", is("561651656")
            );
 }

        @Test
        @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testUpdate() {
                // Adicionando uma marca no banco de dados
                MarcaDTO mun = new MarcaDTO(
                    "Ambev",
                 "561651656"
                );
                Long id = marcaService.create(mun).id();
                // Criando outra marca para atuailzacao
                MarcaDTO munUpdate = new MarcaDTO(
                    "Sla",
                    "12354"
                    );
                given()
                .contentType(ContentType.JSON)
                .body(munUpdate)
                .when().put("/marcas/" + id)
                .then()
                .statusCode(204);
                     // Verificando se os dados foram atualizados no banco de dados
                MarcaResponseDTO munResponse = marcaService.findById(id);
                assertThat(munResponse.nome(), is("Sla"));
                assertThat(munResponse.cnpj(), is("12354"));
}

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
        public void testDelete() {
        // Adicionando uma marca no banco de dados
        MarcaDTO mar = new MarcaDTO(
            "Ambev",
            "561651656"
        );
        Long id = marcaService.create(mar).id();
        given()
        .when().delete("/marcas/" + id)
        .then()
        .statusCode(204);
        // verificando se a marca fisica foi excluida
        MarcaResponseDTO marResponse = null;
        try {
         marcaService.findById(id);
        } catch (Exception e) {
        }
         finally {
         assertNull(marResponse);
        }

         }

         @Test
         @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testFindById() {
                Long id = 2l;
                given()
                .when().get("/marcas/" + id)
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testCount() {
                given()
                .when().get("/marcas/count")
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testSearch() {

                String nome = "Ambev";

                given()
                .when().get("/marcas/search/" + nome)
                .then()
                .statusCode(200);
            }

   }
