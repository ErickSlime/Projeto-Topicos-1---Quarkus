
package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.DadosPessoaisDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.SenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.model.Sexo;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.TokenJwtService;
import br.unitins.topicos1.service.UsuarioService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import jakarta.inject.Inject;


@QuarkusTest
public class UsuarioLogadoResourceTest {

@Inject
UsuarioService usuarioService;

@Inject
HashService hashService;

@Inject
TokenJwtService jwt;

@Test
public void testGetAll() {
    Usuario usuario = usuarioService.findByIdUsu(1l);
    String token = jwt.generateJwt(usuario);


given().header("Authorization", "Bearer " + token)
.when().get("/usuariologado/dadospessoais/")
.then()
.statusCode(200);
}

@Test
public void testUpdate() {

    Usuario usuario = usuarioService.findByIdUsu(2l);
    String token = jwt.generateJwt(usuario);

    DadosPessoaisDTO dados2 = new DadosPessoaisDTO("Joao@unitins.br", 2);

given().header("Authorization", "Bearer " + token)
.contentType(ContentType.JSON)
.body(dados2)
.when().patch("/usuariologado/atualizardadospessoais/")
.then()
.statusCode(204);
// Verificando se os dados foram atualizados no banco de dados
UsuarioResponseDTO usuResponse = usuarioService.findById(2l);
assertThat(usuResponse.pessoa().getEmail(), is("Joao@unitins.br"));
assertThat(usuResponse.pessoa().getSexo(), is(Sexo.FEMININO));

}

@Test
public void testUpdateSenha() {

    Usuario usuario = usuarioService.findByIdUsu(2l);
    String token = jwt.generateJwt(usuario);

    SenhaDTO senha = new SenhaDTO("123", "235");

given().header("Authorization", "Bearer " + token)
.contentType(ContentType.JSON)
.body(senha)
.when().patch("/usuariologado/senhausuario/")
.then()
.statusCode(204);
// Verificando se os dados foram atualizados no banco de dados
UsuarioResponseDTO usuResponse = usuarioService.findById(2l);
assertThat(usuResponse.senha(), is(hashService.getHashSenha("123")));

}

@Test
public void testUpdateTelefone() {

    Usuario usuario = usuarioService.findByIdUsu(2l);
    String token = jwt.generateJwt(usuario);

    TelefoneDTO telefne = new TelefoneDTO("54", "1231231331");

given().header("Authorization", "Bearer " + token)
.contentType(ContentType.JSON)
.body(telefne)
.when().patch("/usuariologado/telefoneusuario/")
.then()
.statusCode(204);
// Verificando se os dados foram atualizados no banco de dados
UsuarioResponseDTO usuResponse = usuarioService.findById(2l);
assertThat(usuResponse.telefone().getCodigoArea(), is("63"));
assertThat(usuResponse.telefone().getNumero(), is("986532145"));

}


// @Test
// @TestSecurity(user = "testUser", roles = {"Admin","User"})
// public void testFindById() {
// Long id = 2l;
// given()
// .when().get("/usuarios/" + id)
// .then()
// .statusCode(200);
// }

@Test
    public void updateEnderecoTest() {

        Usuario usuario = usuarioService.getByLogin("Joao");

        String token = jwt.generateJwt(usuario);

        EnderecoDTO enderecoDTO = new EnderecoDTO("Avenida Goiás", "setor Jardim Brasília", "8712", "apto 3", "77798-347", 1l);

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
                .body(enderecoDTO)
            .when()
                .patch("/usuariologado/enderecousuario")
            .then()
                .statusCode(204);
    }

// @Test
// @TestSecurity(user = "testUser", roles = {"Admin","User"})
// public void testCount() {
// given()
// .when().get("/usuarios/count")
// .then()
// .statusCode(200);
// }

// @Test
// @TestSecurity(user = "testUser", roles = {"Admin","User"})
// public void testSearch() {

// String loginUsu = "Erick";

// given()
// .when().get("/usuarios/search/" + loginUsu)
// .then()
// .statusCode(200);
// }

// @Test
// @TestSecurity(user = "testUser", roles = {"Admin","User"})
// public void testInsertListaDesejo() {


// ListaDesejDTO desj = new ListaDesejDTO(1l,2l);

// given()
// .contentType(ContentType.JSON)
// .body(desj)
// .when().post("/usuarios/listadesejo")
// .then()
// .statusCode(201);

// ListaDesejoResponseDTO desjResponse =
// usuarioService.getListaDesejo(desj.idUsuario());

// assertThat(desjResponse.usuario().get("nome"), is("Erick"));
// assertThat(desjResponse.produtos().get(1).get("nome"), is("Skol"));

// }

// @Test
// @TestSecurity(user = "testUser", roles = {"Admin","User"})
// public void testGetListaDesejo() {
// Long id = 1l;
// given()
// .when().get("/usuarios/listadesejo/" + id)
// .then()
// .statusCode(200);
// }

}
 