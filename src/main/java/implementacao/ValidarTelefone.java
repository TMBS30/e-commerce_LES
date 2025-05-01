package implementacao;

import dominio.EntidadeDominio;
import dominio.Telefone;

public class ValidarTelefone implements IStrategy{

    @Override
    public String processar(EntidadeDominio entidade) {
        StringBuilder stringBuilder = new StringBuilder();
        if(entidade==null){
            return "Entidade nula";
        }
        if(entidade instanceof Telefone){

            Telefone telefone = (Telefone)entidade; //converte entidade para o tipo telefone
            if(telefone.getNumero()==null){
                stringBuilder.append("Número de telefone é obrigatório");
            }else if(telefone.getNumero().length()<8){
                stringBuilder.append("Número de telefone inválido");
            }
            if(telefone.getDdd()==null){
                stringBuilder.append("DDD é obrigatório");
            }else if(telefone.getDdd().length()!=2){
                stringBuilder.append("DDD inválido");
            }
            if(telefone.getTipo()==null){
                stringBuilder.append("Tipo de Telefone é obrigatório");
            }

        }
        return stringBuilder.toString();
    }

}