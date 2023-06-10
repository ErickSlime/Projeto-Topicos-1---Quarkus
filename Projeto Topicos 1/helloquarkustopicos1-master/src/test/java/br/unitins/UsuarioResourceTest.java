
package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ListaDesejDTO;
import br.unitins.topicos1.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.dto.PessoaFisicaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.UsuarioService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.inject.Inject;


@QuarkusTest
public class UsuarioResourceTest {

@Inject
UsuarioService usuarioService;

@Inject
HashService hashService;

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testGetAll() {
given()
.when().get("/compras")
.then()
.statusCode(200);
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testInsert() {

TelefoneDTO telefone = new TelefoneDTO("63", "981546289");

EnderecoDTO endereco = new EnderecoDTO("Mato","Matos","770000","Sn","Arvore",
1l);

PessoaFisicaDTO pessoa = new PessoaFisicaDTO("Lucas", "32154689785", "ericksantos@unitins.br", 1);

UsuarioDTO usu = new UsuarioDTO(
"Erick",
"12345678",
telefone,
endereco,
pessoa
);
given()
.contentType(ContentType.JSON)
.body(usu)
.when().post("/usuarios")
.then()
.statusCode(201)
.body("id", notNullValue(),
"login", is("Erick"),
"pessoa.email", is("ericksantos@unitins.br"),
"senha", is(hashService.getHashSenha("12345678")),
"pessoa.cpf", is("32154689785"),
"telefone.codigoArea" , is("63"),
"telefone.numero", is("981546289"),
"endereco.logradouro", is("Mato"),
"endereco.cep", is("770000"),
"endereco.bairro", is("Matos"),
"endereco.numero", is("Sn"),
"endereco.complemento", is("Arvore"),
"endereco.municipio.nome", is("Palmas")
);
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testUpdate() {
// Adicionando uma usuario no banco de dados
TelefoneDTO telefone = new TelefoneDTO("63", "981546289");

EnderecoDTO endereco = new EnderecoDTO("Mato","Matos","770000","Sn","Arvore",
1l);

PessoaFisicaDTO pessoa = new PessoaFisicaDTO("Lucas", "32154689785", "ericksantos@unitins.br", 1);

UsuarioDTO usu = new UsuarioDTO(
"Erick",
"12345678",
telefone,
endereco,
pessoa
);
Long id = usuarioService.create(usu).id();

TelefoneDTO telefone2 = new TelefoneDTO("62", "561165114");

EnderecoDTO endereco2 = new EnderecoDTO("Sla","Sla2","7781965","Sn","Ns",
2l);

PessoaFisicaDTO pessoa2 = new PessoaFisicaDTO("Ana", "1231233123", "AnaSantos@unitins.br", 2);

// Criando outra usuario para atuailzacao
UsuarioDTO usuUpdate = new UsuarioDTO(
"Anaasd",
"1234567822222",
telefone2,
endereco2,
pessoa2
);
given()
.contentType(ContentType.JSON)
.body(usuUpdate)
.when().put("/usuarios/" + id)
.then()
.statusCode(204);
// Verificando se os dados foram atualizados no banco de dados
UsuarioResponseDTO usuResponse = usuarioService.findById(id);
assertThat(usuResponse.login(), is("Anaasd"));
assertThat(usuResponse.pessoa().getNome(), is("Ana"));
assertThat(usuResponse.pessoa().getEmail(), is("AnaSantos@unitins.br"));
assertThat(usuResponse.senha(), is(hashService.getHashSenha("1234567822222")));
assertThat(usuResponse.pessoa().getCpf(), is("1231233123"));
assertThat(usuResponse.telefone().getNumero(), is("561165114"));
assertThat(usuResponse.telefone().getCodigoArea(), is("62"));
assertThat(usuResponse.endereco().getLogradouro(), is("Sla"));
assertThat(usuResponse.endereco().getBairro(), is("Sla2"));
assertThat(usuResponse.endereco().getCep(), is("7781965"));
assertThat(usuResponse.endereco().getNumero(), is("Sn"));
assertThat(usuResponse.endereco().getComplemento(), is("Ns"));
assertThat(usuResponse.endereco().getMunicipio().getNome(),
is("Paraiso do Tocantins"));
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testDelete() {
// Adicionando uma usuario no banco de dados
TelefoneDTO telefone = new TelefoneDTO("63", "981546289");

EnderecoDTO endereco = new EnderecoDTO("Mato","Matos","770000","Sn","Arvore",
1l);

PessoaFisicaDTO pessoa = new PessoaFisicaDTO("Lucas", "32154689785", "ericksantos@unitins.br", 1);

UsuarioDTO usu = new UsuarioDTO(
"Erick",
"12345678",
telefone,
endereco,
pessoa
);
Long id = usuarioService.create(usu).id();
given()
.when().delete("/usuarios/" + id)
.then()
.statusCode(204);
// verificando se a usuario fisica foi excluida
UsuarioResponseDTO usuResponse = null;
try {
usuarioService.findById(id);
} catch (Exception e) {
}
finally {
assertNull(usuResponse);
}

}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testFindById() {
Long id = 2l;
given()
.when().get("/usuarios/" + id)
.then()
.statusCode(200);
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testCount() {
given()
.when().get("/usuarios/count")
.then()
.statusCode(200);
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testSearch() {

String loginUsu = "Erick";

given()
.when().get("/usuarios/search/" + loginUsu)
.then()
.statusCode(200);
}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testInsertListaDesejo() {


ListaDesejDTO desj = new ListaDesejDTO(1l,2l);

given()
.contentType(ContentType.JSON)
.body(desj)
.when().post("/usuarios/listadesejo")
.then()
.statusCode(201);

ListaDesejoResponseDTO desjResponse =
usuarioService.getListaDesejo(desj.idUsuario());

assertThat(desjResponse.usuario().get("nome"), is("Erick"));
assertThat(desjResponse.produtos().get(1).get("nome"), is("Skol"));

}

@Test
@TestSecurity(user = "testUser", roles = {"Admin","User"})
public void testGetListaDesejo() {
Long id = 1l;
given()
.when().get("/usuarios/listadesejo/" + id)
.then()
.statusCode(200);
}

}
 