describe('template spec', () => {
  it('passes', () => {
    cy.visit("http://localhost:8080/e-commerce_LES/", { failOnStatusCode: false });

    cy.get('input[name="email_login"]').type('joao.silva@example.com');
    cy.get('input[name="senha_login"]').type('senha12a');
    cy.contains('button', 'Entrar').click();
    cy.contains('a', 'Cadastrar').click();


    cy.get('input[name="nome_cadastro"]').type('Joao Santos');
    cy.get('input[name="cpf_cadastro"]').type('57722005004');
    cy.get('input[name="email_cadastro"]').type('joao.santos@example.com');
    cy.get('input[name="senha_cadastro"]').type('Senha123#');
    cy.get('input[name="confirmarSenha_cadastro"]').type('Senha123#');
    cy.get('input[name="genero_cadastro"][value="MASCULINO"]').check();
    cy.get('#dataNascimento').invoke('val', '2000-03-10').trigger('change');
    cy.get('#tipoTel').should('be.visible');
    cy.get('#tipoTel').select('CELULAR');
    cy.get('#tipoTel').should('have.value', 'CELULAR');
    cy.get('input[name="ddd_cadastro"]').type('11');
    cy.get('input[name="numero_cadastro"]').type('975228997');
    cy.contains('button', 'Proximo').click();


    cy.get('#nomePaisRes').should('be.visible');
    cy.get('#nomePaisRes').select('Brasil');
    cy.get('#nomePaisRes').should('have.value', 'Brasil');

    cy.get('#nomeEstadoRes').should('be.visible');
    cy.get('#nomeEstadoRes').select('Sao Paulo');
    cy.get('#nomeEstadoRes').should('have.value', 'Sao Paulo');

    cy.get('#nomeCidadeRes').should('be.visible');
    cy.get('#nomeCidadeRes').select('Mogi das Cruzes');
    cy.get('#nomeCidadeRes').should('have.value', 'Mogi das Cruzes');

    cy.get('#tipoLogradouroRes').should('be.visible');
    cy.get('#tipoLogradouroRes').select('Rua');
    cy.get('#tipoLogradouroRes').should('have.value', 'Rua');

    cy.get('#tipoResidenciaRes').should('be.visible');
    cy.get('#tipoResidenciaRes').select('Casa');
    cy.get('#tipoResidenciaRes').should('have.value', 'Casa');

    cy.get('input[name="cepRes_cadastro"]').type('08751351');
    cy.get('input[name="logradouroRes_cadastro"]').type('Nagajima Honda');
    cy.get('input[name="numeroRes_cadastro"]').type('181');
    cy.get('input[name="bairroRes_cadastro"]').type('Santa Luna');
    cy.get('input[name="observacaoRes_cadastro"]').type('Próximo a Fazenda Silveira');

    cy.get('#nomePaisCob').should('be.visible');
    cy.get('#nomePaisCob').select('Argentina');
    cy.get('#nomePaisCob').should('have.value', 'Argentina');
    cy.get('#nomeEstadoCob').should('be.visible');
    cy.get('#nomeEstadoCob').select('Buenos Aires');
    cy.get('#nomeEstadoCob').should('have.value', 'Buenos Aires');
    cy.get('#nomeCidadeCob').should('be.visible');
    cy.get('#nomeCidadeCob').select('Palermo');
    cy.get('#nomeCidadeCob').should('have.value', 'Palermo');
    cy.get('#tipoLogradouroCob').should('be.visible');
    cy.get('#tipoLogradouroCob').select('Avenida');
    cy.get('#tipoLogradouroCob').should('have.value', 'Avenida');
    cy.get('#tipoResidenciaCob').should('be.visible');
    cy.get('#tipoResidenciaCob').select('Apartamento');
    cy.get('#tipoResidenciaCob').should('have.value', 'Apartamento');
    cy.get('input[name="cepCob_cadastro"]').type('08852445');
    cy.get('input[name="logradouroCob_cadastro"]').type('Riquelme Palacio');
    cy.get('input[name="numeroCob_cadastro"]').type('51');
    cy.get('input[name="bairroCob_cadastro"]').type('La Cidad');
    cy.get('input[name="observacaoCob_cadastro"]').type('Próximo a Escola Diego Maradona');


    cy.get('#nomePaisCob').should('be.visible');
    cy.get('#nomePaisEnt').select('Alemanha');
    cy.get('#nomePaisEnt').should('have.value', 'Alemanha');
    cy.get('#nomeEstadoEnt').should('be.visible');
    cy.get('#nomeEstadoEnt').select('Munique');
    cy.get('#nomeEstadoEnt').should('have.value', 'Munique');
    cy.get('#nomeCidadeEnt').should('be.visible');
    cy.get('#nomeCidadeEnt').select('Grasbrunn');
    cy.get('#nomeCidadeEnt').should('have.value', 'Grasbrunn');
    cy.get('#tipoLogradouroEnt').should('be.visible');
    cy.get('#tipoLogradouroEnt').select('Alameda');
    cy.get('#tipoLogradouroEnt').should('have.value', 'Alameda');
    cy.get('#tipoResidenciaEnt').should('be.visible');
    cy.get('#tipoResidenciaEnt').select('Condominio');
    cy.get('#tipoResidenciaEnt').should('have.value', 'Condominio');
    cy.get('input[name="cepEnt_cadastro"]').type('04223141');
    cy.get('input[name="logradouroEnt_cadastro"]').type('Podolski Neill');
    cy.get('input[name="numeroEnt_cadastro"]').type('1635');
    cy.get('input[name="bairroEnt_cadastro"]').type('Dortmundo');
    cy.get('input[name="observacaoEnt_cadastro"]').type('Próximo ao Estadio Olimpico de Munique');
    cy.contains('button', 'Proximo').click();


    /*cy.get('input[name="email_login"]').type('joao.silva@example.com');
    cy.get('input[name="senha_login"]').type('senha123');
    cy.contains('button', 'Entrar').click();
    cy.contains('a', 'Consultar Informacoes Pessoais').click();
    cy.contains('a', 'Ver Cartoes').click();
    cy.contains('button', 'Editar Cartao').click();
    cy.contains('button', 'Cancelar').click();
    cy.contains('button', 'Voltar').click();
    cy.contains('a', 'Ver Endereco').click();
    cy.contains('button', 'Voltar').click();*/

    //cy.visit("http://localhost:8080/e-commerce_LES/servlet", { failOnStatusCode: false });

    //cy.get('[href="servlet?action=cadastro"]').click();

    //cy.contains('button', 'Entrar').click();

    //cy.contains('a', 'Cadastrar-se').click();

  })

})

/*describe('template spec', () => {
  it('passes', () => {
    cy.visit('http://localhost:8080/e-commerce_LES/');

    // Faz a requisição antes de clicar
    cy.request({
      method: 'GET',
      url: 'http://localhost:8080/e-commerce_LES/servlet?action=cadastro',
      failOnStatusCode: false  // Não faz o teste falhar em caso de erro 500
    }).then((response) => {
      // Aqui você pode verificar o status ou fazer outras verificações
      console.log(response.status); // Pode imprimir o status da resposta para depuração
    });

    // Agora clica no link
    cy.contains('a', 'Cadastrar-se').click();
  });
});*/