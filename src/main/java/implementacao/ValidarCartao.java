package implementacao;

import dominio.Cartao;
import dominio.EntidadeDominio;

public class ValidarCartao implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        StringBuilder stringBuilder = new StringBuilder();

        if(entidade==null){
            return "Entidade nula";
        }

        if(entidade instanceof Cartao){
            Cartao cartao = (Cartao)entidade; //converte entidade para o tipo cartao

            if(cartao.getNumero()==null || cartao.getNumero().equals("")){
                stringBuilder.append("Erro: Número do cartão é obrigatório");
            }
            if(cartao.getNomeTitular()==null){
                stringBuilder.append("Erro: Nome do Titular é obrigatório");
            }
            if(cartao.getCodSeguranca()==null || cartao.getCodSeguranca().equals("")){
                stringBuilder.append("Erro: Codigo de segurança é obrigatório");
            }else if(cartao.getCodSeguranca().length()!=3){
                stringBuilder.append("Erro: Código de segurança inválido");
            }

        }
        return stringBuilder.toString();
    }
}