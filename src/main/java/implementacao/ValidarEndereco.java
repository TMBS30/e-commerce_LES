package implementacao;

import dominio.Endereco;
import dominio.EntidadeDominio;


public class ValidarEndereco implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        StringBuilder stringBuilder = new StringBuilder();
        if(entidade==null){
            return "Entidade nula";
        }
        if(entidade instanceof Endereco){
            Endereco endereco = (Endereco) entidade; //converte entidade para o tipo endereco
            if(endereco.getCep()==null){
                stringBuilder.append("Erro: CEP é obrigatório");
            }
            if(endereco.getLogradouro()==null){
                stringBuilder.append("Erro: Logradouro é obrigatório");
            }
            if(endereco.getBairro()==null){
                stringBuilder.append("Erro: Bairro é obrigatório");
            }
        }
        return stringBuilder.toString();
    }
}
