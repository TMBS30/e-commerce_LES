package controle;

import dominio.Cartao;
import dominio.Endereco;
import dominio.EntidadeDominio;

import java.sql.SQLException;
import java.util.List;

public interface IFachada {
    String salvarEndereco(Endereco endereco) throws Exception;

    public String salvar(EntidadeDominio entidade) throws Exception;
    // public String salvarDep(EntidadeDominio entidade, String id);
    public String alterar(EntidadeDominio entidade);
    public String excluir(EntidadeDominio entidade);
    public List<EntidadeDominio> consultar(EntidadeDominio entidade);
    public EntidadeDominio selecionar(EntidadeDominio entidade);
    String adicionarEnderecoParaClienteExistente(dominio.Endereco endereco) throws Exception;

    String salvarCartao(Cartao cartaoSalvar, Integer clienteId) throws Exception;

    String alterarCartao(Cartao cartao) throws Exception;
}
