describe('template spec', () => {
 beforeEach(() => {
      cy.viewport(1440, 900)
    })
  it('passes', () => {
    cy.visit("http://localhost:8080/e-commerce_LES/", { failOnStatusCode: false });

    cy.get('input[name="email_login"]').type('joao.silva@example.com');
    cy.get('input[name="senha_login"]').type('senha12a');
    cy.contains('button', 'login').click();

    /*cy.get('input[name="email_login"]').type('sabrina.vitoria@example.com');
        cy.get('input[name="senha_login"]').type('Sabri567#');
        cy.contains('button', 'login').click();*/


    //*********************CADASTRO DE CLIENTE*********************************
    cy.contains('a', 'Cadastrar').click();
        cy.get('input[name="nome_cadastro"]').type('Vera Lucia');
        cy.get('input[name="cpf_cadastro"]').type('47089894827');
        cy.get('input[name="email_cadastro"]').type('vera.lucia@gmail.com.br');
        cy.get('input[name="senha_cadastro"]').type('Lulu258#');
        cy.get('input[name="confirmarSenha_cadastro"]').type('Lulu258#');
        cy.get('input[name="genero_cadastro"][value="FEMININO"]').check();
        cy.get('#dataNascimento').invoke('val', '1989-01-12').trigger('change');
        cy.get('#tipoTel')
          .should('be.visible')
          .select('CELULAR')
          .should('have.value', 'CELULAR');
        cy.get('input[name="ddd_cadastro"]').type('11');
        cy.get('input[name="numero_cadastro"]').type('932269578');
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
        cy.get('input[name="numeroRes_cadastro"]').type('14');
        cy.get('input[name="bairroRes_cadastro"]').type('Santa Luna');
        cy.get('input[name="observacaoRes_cadastro"]').type('Proximo a Fazenda Silveira');

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
        cy.get('input[name="numeroCob_cadastro"]').type('689');
        cy.get('input[name="bairroCob_cadastro"]').type('La Cidad');
        cy.get('input[name="observacaoCob_cadastro"]').type('Proximo a Escola Diego Maradona');


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
        cy.get('input[name="numeroEnt_cadastro"]').type('19525');
        cy.get('input[name="bairroEnt_cadastro"]').type('Dortmund');
        cy.get('input[name="observacaoEnt_cadastro"]').type('Proximo ao Estadio Olimpico de Munique');
        cy.contains('button', 'Proximo').click();
        //**********************FIM DO CADASTRO DE CLIENTE*******************************************

        cy.get('input[name="email_login"]').type('vera.lucia@gmail.com.br');
        //cy.get('input[name="email_cadastro"]').type('sabrina.vitoria@example.com');
        cy.get('input[name="senha_login"]').type('Lulu258#');
        //cy.get('input[name="senha_cadastro"]').type('Sabri567#');
        cy.contains('button', 'login').click();

            cy.get('a.link-user').click();
            cy.contains('a', 'Ver Cartoes').click();
            cy.contains('button', 'Adicionar Cartao +').click();
            cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Lucia');
            cy.get('input[name="numeroCartao_novoCartao"]').type('1106878295768025');
            cy.get('#bandeira_novoCartao')
              .should('be.visible')
              .select('MASTERCARD')
              .should('have.value', 'MASTERCARD');
          cy.get('input[name="codSeguranca_novoCartao"]').type('264');
          cy.get('input[name="dataVencimento_novoCartao"]').type('0331');
          cy.get('input[name="preferencialCartao_novoCartao"]').check();
          cy.contains('button', 'Salvar').click();


          cy.contains('button', 'Adicionar Cartao +').click();
              cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Lucia Silva');
              cy.get('input[name="numeroCartao_novoCartao"]').type('5897230047493944');
              cy.get('#bandeira_novoCartao')
                .should('be.visible')
                .select('VISA')
                .should('have.value', 'VISA');
            cy.get('input[name="codSeguranca_novoCartao"]').type('503');
            cy.get('input[name="dataVencimento_novoCartao"]').type('1228');
            cy.contains('button', 'Salvar').click();

            cy.contains('button', 'Excluir Cartao').first().click();
                cy.on('window:confirm', (mensagem) => {
                expect(mensagem).to.equal('Tem certeza que deseja excluir este cartao?');
                return true;
                });

           cy.contains('button', 'Editar Cartao').click();
            cy.get('input[name="dataVencimento_editarCartao"]').clear();
            cy.get('input[name="dataVencimento_editarCartao"]').type('0232');
            cy.contains('button', 'Salvar').click();



          cy.contains('button', 'Adicionar Cartao +').click();
            cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Silva');
            cy.get('input[name="numeroCartao_novoCartao"]').type('2259130577939980');
            cy.get('#bandeira_novoCartao')
              .should('be.visible')
              .select('VISA')
              .should('have.value', 'VISA');
              cy.get('input[name="codSeguranca_novoCartao"]').type('447');
              cy.get('input[name="dataVencimento_novoCartao"]').type('0131');
              cy.contains('button', 'Salvar').click();
              cy.contains('button', 'Voltar').click();
              cy.contains('a', 'PageStation').click();

        cy.get('a.chatbot').click();
              cy.get('input[name="mensagemUsuario"]').type('Quero comer lasanha, onde posso comer?');
              cy.contains('button', 'Enviar').click();
              cy.wait(9000);
              cy.get('input[name="mensagemUsuario"]').type('Gosto de game of thrones, quero um livro parecido com isso');
              cy.contains('button', 'Enviar').click();
              cy.wait(6000);
              cy.visit('http://localhost:8080/e-commerce_LES/servlet?action=consultarProduto&id_livro=20');

              cy.contains('button', 'ADICIONAR AO CARRINHO').click();

        cy.wait(8000);

        cy.contains('a', 'PageStation').click();
        cy.contains('a.livro-item', 'A Guerra dos Tronos').click();
        cy.contains('button', '+').click();
        cy.contains('button', 'COMPRAR').click();
        cy.get('a.link-carrinho').click();
        cy.contains('a', 'Ver meus cupons').click();
        cy.get('input[name="habilitarPromocionais"]').check();
        cy.get('input[name="idCupomPromocional"]').first().check();
        cy.contains('button', 'Salvar').click();
        cy.get('select[name="tipoFrete"]').select('Econômico');
        cy.contains('button', 'Finalizar Compra').click();
        cy.contains('a', 'Adicionar/Trocar Endereco de Entrega').click();
        cy.contains('button', 'Adicionar Endereco de Entrega').click();

        cy.get('#nomePaisNovoEnd').should('be.visible');
        cy.get('#nomePaisNovoEnd').select('Brasil');
        cy.get('#nomePaisNovoEnd').should('have.value', 'Brasil');

        cy.get('#nomeEstadoNovoEnd').should('be.visible');
        cy.get('#nomeEstadoNovoEnd').select('Sao Paulo');
        cy.get('#nomeEstadoNovoEnd').should('have.value', 'Sao Paulo');

        cy.get('#nomeCidadeNovoEnd').should('be.visible');
        cy.get('#nomeCidadeNovoEnd').select('Mogi das Cruzes');
        cy.get('#nomeCidadeNovoEnd').should('have.value', 'Mogi das Cruzes');

        cy.get('#tipoLogradouroNovoEnd').should('be.visible'); // Corrigido: #tipoLogradouroRes -> #tipoLogradouroNovoEnd
        cy.get('#tipoLogradouroNovoEnd').select('Rua');
        cy.get('#tipoLogradouroNovoEnd').should('have.value', 'Rua');

        cy.get('#tipoResidenciaNovoEnd').should('be.visible'); // Corrigido: #tipoResidenciaRes -> #tipoResidenciaNovoEnd
        cy.get('#tipoResidenciaNovoEnd').select('Casa');
        cy.get('#tipoResidenciaNovoEnd').should('have.value', 'Casa');

        // Esses inputs estavam corretos em relação ao name
        cy.get('input[name="cep_NovoEnd"]').type('08966820'); // ID é 'cepNovoEnd'
        cy.get('input[name="logradouro_NovoEnd"]').type('Orquideas Amarelas'); // ID é 'logradouroNovoEnd'
        cy.get('input[name="numero_NovoEnd"]').type('564'); // ID é 'numeroNovoEnd'
        cy.get('input[name="bairro_NovoEnd"]').type('Jd. Hera'); // ID é 'bairroNovoEnd'
        cy.get('input[name="observacao_NovoEnd"]').type('Proximo ao pesqueiro'); // ID é 'observacaoNovoEnd'
        cy.contains('button', 'Adicionar Endereco').click();


        cy.get('input[name="idEnderecoEntregaSelecionado"]').first().check();
        cy.contains('button', 'Salvar Endereco de Entrega').click();
        cy.contains('a', 'Adicionar/Trocar Endereco de Cobranca').click();
        cy.get('input[name="idEnderecoCobrancaSelecionado"]').first().check();
        cy.contains('button', 'Salvar Endereco de Cobranca').click();
        cy.contains('a', 'Adicionar/Trocar Cartao').click();
        cy.get('input[name="idsCartoes"]').first().check();
        cy.get('input[name="idsCartoes"]').eq(1).check();
        cy.contains('button', 'Salvar Cartoes Selecionados').click();
        cy.contains('button', 'Confirmar Compra').click();
        cy.contains('button', 'Pedidos').click();
        cy.contains('a', 'Pedidos').click();
        cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();


        cy.get('input[name="email_login"]').type('adm@pagestation.com');
        cy.get('input[name="senha_login"]').type('@PageADM2025');
        cy.contains('button', 'login').click();
        cy.contains('a', 'Pedidos').click();
        cy.contains('Status da Compra')
          .parent()
          .find('select')
          .select('ENTREGUE');
        cy.contains('button', 'Salvar Status').click();
        cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();


        cy.get('input[name="email_login"]').type('vera.lucia@gmail.com.br');
        cy.get('input[name="senha_login"]').type('Lulu258#');
        cy.contains('button', 'Login').click();
        cy.contains('a', 'Pedidos').click();
        cy.contains('button', 'Trocar').click();
        cy.contains('button', 'Pedidos').click();
        cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();


        cy.get('input[name="email_login"]').type('adm@pagestation.com');
        cy.get('input[name="senha_login"]').type('@PageADM2025');
        cy.contains('button', 'login').click();
        cy.contains('a', 'Pedidos').click();
        cy.contains('button', 'Autorizar Troca').click();
        cy.get('a[href="servlet?action=voltarHomePageADM"]', { timeout: 10000 })
          .should('be.visible')
          .click();

        cy.contains('a', 'Sair').click();


        cy.get('input[name="email_login"]').type('vera.lucia@gmail.com.br');
        cy.get('input[name="senha_login"]').type('Lulu258#');
        cy.contains('button', 'Login').click();
        cy.contains('a', 'Pedidos').click();
        cy.contains('a', 'Cupons').click();
        cy.contains('a', 'PageStation').click();
        cy.get('a.chatbot').click();
          cy.get('input[name="mensagemUsuario"]').type('Quero ler um livro sobre romance e drama, que fale de superação');
          cy.contains('button', 'Enviar').click();
          cy.wait(9000);
          cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();

        cy.get('input[name="email_login"]').type('adm@pagestation.com');
        cy.get('input[name="senha_login"]').type('@PageADM2025');
        cy.contains('button', 'login').click();
        cy.contains('a', 'Estoque').click();
        cy.get('.livro-container').eq(2).within(() => { // Seleciona o terceiro container de livro
        cy.get('input[name="qtde_entrada"]').type('25');
        cy.get('input[name="data_entrada"]').type('2025-04-14');
        cy.get('input[name="valor_custo"]').clear().type('29,61');
        cy.get('select[name="id_fornec"]').select('BOOKWIRE BRASIL');
        cy.contains('button', 'REGISTRAR').click();
        });
        cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();

        cy.get('input[name="email_login"]').type('vera.lucia@gmail.com.br');
        cy.get('input[name="senha_login"]').type('Lulu258#');
        cy.contains('button', 'Login').click();
        cy.get('a.chatbot').click();
          cy.get('input[name="mensagemUsuario"]').type('Quero ler um livro sobre romance e drama, que fale de superação');
          cy.contains('button', 'Enviar').click();
          cy.wait(9000);
          cy.contains('a', 'PageStation').click();
        cy.contains('a', 'Sair').click();

        cy.get('input[name="email_login"]').type('adm@pagestation.com');
                cy.get('input[name="senha_login"]').type('@PageADM2025');
                cy.contains('button', 'login').click();
        cy.contains('a', 'Dashboard').click();
        cy.get('#startDate')
        .should('be.visible')
        .type('2025-05-05');
        cy.get('#endDate')
        .should('be.visible')
        .type('2025-07-03');
        cy.get('#filterButton')
        .should('be.visible')
        .click();
        cy.contains('a', 'PageStation').click();

/*==============================FIM DE_TODO FLUXO============================================*/


//============================= SESSÃO PÓS CADASTRO===============================
    /*cy.get('input[name="email_login"]').type('sabrina.vitoria@example.com');
    cy.get('input[name="senha_login"]').type('Sabri567#');
    cy.contains('button', 'login').click();*/

    /*cy.get('a.link-user').click();
    cy.contains('a', 'Ver Cartoes').click();*/
    /*cy.contains('button', 'add-novo-cartao').click();
    cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Lucia');
    cy.get('input[name="numeroCartao_novoCartao"]').type('1106878295768025');
    cy.get('#bandeira_novoCartao')
      .should('be.visible')
      .select('MASTERCARD')
      .should('have.value', 'MASTERCARD');
  cy.get('input[name="codSeguranca_novoCartao"]').type('264');
  cy.get('input[name="dataVencimento_novoCartao"]').type('0331');
  cy.get('input[name="preferencialCartao_novoCartao"]').check();
  cy.contains('button', 'Salvar').click();*/


  /*cy.contains('button', 'add-novo-cartao').click();
      cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Lucia Silva');
      cy.get('input[name="numeroCartao_novoCartao"]').type('5897230047493944');
      cy.get('#bandeira_novoCartao')
        .should('be.visible')
        .select('VISA')
        .should('have.value', 'VISA');
    cy.get('input[name="codSeguranca_novoCartao"]').type('503');
    cy.get('input[name="dataVencimento_novoCartao"]').type('1228');
    cy.get('input[name="preferencialCartao_novoCartao"]').check();
    cy.contains('button', 'Salvar').click();*/

    /*cy.contains('button', 'Excluir Cartao').first().click();
        cy.on('window:confirm', (mensagem) => {
        expect(mensagem).to.equal('Tem certeza que deseja excluir este cartao?');
        return true;
        });*/

   /* cy.contains('button', 'Editar Cartao').click();
    cy.get('input[name="dataVencimento_editarCartao"]').clear();
    cy.get('input[name="dataVencimento_editarCartao"]').type('0232');
    //cy.contains('button', 'Salvar').click();*/



  /*cy.contains('button', 'add-novo-cartao').click();
    cy.get('input[name="nomeTitular_novoCartao"]').type('Vera Silva');
    cy.get('input[name="numeroCartao_novoCartao"]').type('2259130577939980');
    cy.get('#bandeira_novoCartao')
      .should('be.visible')
      .select('VISA')
      .should('have.value', 'VISA');
      cy.get('input[name="codSeguranca_novoCartao"]').type('447');
      cy.get('input[name="dataVencimento_novoCartao"]').type('0131');
      cy.contains('button', 'Salvar').click();
      cy.contains('button', 'Voltar').click();
      cy.contains('a', 'PageStation').click();
      cy.get('a.chatbot').click();
      cy.get('input[name="mensagemUsuario"]').type('Quero comer lasanha, onde posso comer?');
      cy.contains('button', 'Enviar').click();
      cy.get('input[name="mensagemUsuario"]').type('Gosto de game of thrones, quero um livro parecido com isso');
      cy.contains('button', 'Enviar').click();
      cy.get('a[href="/servlet?action=consultarProduto&id_livro=20"]').click();

  */

/*
=======================================OUTRO CÓDIGO QUE COMEÇA NA ADIÇÃO AO CARRINHO=============
    cy.contains('a.livro-item', 'A Guerra dos Tronos').click();

    // Clica no botão "Adicionar ao Carrinho"
    cy.contains('button', 'ADICIONAR AO CARRINHO').click();

    cy.wait(6000);

    cy.contains('a', 'PageStation').click();
    cy.contains('a.livro-item', 'A Guerra dos Tronos').click();
    cy.contains('button', '+').click();
    cy.contains('button', 'COMPRAR').click();
    cy.get('a.link-carrinho').click();
    cy.contains('a', 'Ver meus cupons').click();
    /*cy.get('input[name="habilitarTroca"]').check();
    cy.get('input[name="idCupomTroca"]').first().check();
    cy.contains('button', 'Salvar').click();
    cy.contains('button', 'Voltar').click();
    cy.get('select[name="tipoFrete"]').select('Econômico');
    cy.contains('button', 'Finalizar Compra').click();
    cy.contains('a', 'Adicionar/Trocar Endereco de Entrega').click();
    cy.get('input[name="idEnderecoEntregaSelecionado"]').first().check();
    cy.contains('button', 'Salvar Endereco de Entrega').click();
    cy.contains('a', 'Adicionar/Trocar Endereco de Cobranca').click();
    cy.get('input[name="idEnderecoCobrancaSelecionado"]').first().check();
    cy.contains('button', 'Salvar Endereco de Cobranca').click();
    cy.contains('a', 'Adicionar/Trocar Cartao').click();
    cy.get('input[name="idsCartoes"]').first().check();
    cy.contains('button', 'Salvar Cartoes Selecionados').click();
    cy.contains('button', 'Confirmar Compra').click();
    cy.contains('button', 'Pedidos').click();
    cy.contains('a', 'Pedidos').click();
    cy.contains('a', 'PageStation').click();
    cy.contains('a', 'Sair').click();


    cy.get('input[name="email_login"]').type('adm@pagestation.com');
    cy.get('input[name="senha_login"]').type('@PageADM2025');
    cy.contains('button', 'login').click();
    cy.contains('a', 'Pedidos').click();
    cy.contains('Status da Compra')
      .parent()
      .find('select')
      .select('ENTREGUE');
    cy.contains('button', 'Salvar Status').click();
    cy.contains('a', 'PageStation').click();
    cy.contains('a', 'Sair').click();


    cy.get('input[name="email_login"]').type('sabrina.vitoria@example.com');
    cy.get('input[name="senha_login"]').type('Sabri567#');
    cy.contains('button', 'Login').click();
    cy.contains('a', 'Pedidos').click();
    cy.contains('button', 'Trocar').click();
    cy.contains('button', 'Pedidos').click();
    cy.contains('a', 'PageStation').click();
    cy.contains('a', 'Sair').click();


    cy.get('input[name="email_login"]').type('adm@pagestation.com');
    cy.get('input[name="senha_login"]').type('@PageADM2025');
    cy.contains('button', 'login').click();
    cy.contains('a', 'Pedidos').click();
    cy.contains('button', 'Autorizar Troca').click();
    cy.get('a[href="servlet?action=voltarHomePageADM"]', { timeout: 10000 })
      .should('be.visible')
      .click();

    cy.contains('a', 'Sair').click();


    cy.get('input[name="email_login"]').type('sabrina.vitoria@example.com');
    cy.get('input[name="senha_login"]').type('Sabri567#');
    cy.contains('button', 'Login').click();
    cy.contains('a', 'Pedidos').click();
    cy.contains('a', 'Cupons').click();
    cy.contains('a', 'PageStation').click();
    cy.contains('a', 'Sair').click();

    cy.get('input[name="email_login"]').type('adm@pagestation.com');
    cy.get('input[name="senha_login"]').type('@PageADM2025');
    cy.contains('button', 'login').click();
    cy.contains('a', 'Estoque').click();
    cy.get('input[name="qtde_entrada"]').first().type('25');
    cy.get('input[name="data_entrada"]').first().type('2025-04-14');
    cy.get('input[name="valor_custo"]').first().clear();
    cy.get('input[name="valor_custo"]').first().type('29,61');
    cy.get('select[name="id_fornec"]')
     .first()
     .should('be.visible')
     .select('BOOKWIRE BRASIL')
     .should('contain.text', 'BOOKWIRE BRASIL');

    cy.contains('button', 'REGISTRAR').first().click();*/

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