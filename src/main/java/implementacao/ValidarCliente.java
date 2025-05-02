package implementacao;

import dominio.Cliente;
import dominio.EntidadeDominio;

public class ValidarCliente implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        StringBuilder stringBuilder = new StringBuilder();
        if(entidade==null){
            return "Entidade nula";
        }
        if(entidade instanceof Cliente){
            Cliente cliente = (Cliente)entidade; //converte entidade para o tipo cliente
            if(cliente.getCpf()==null){
                stringBuilder.append("Erro: CPF é obrigatório");
            }
            if(cliente.getEmail()==null){
                stringBuilder.append("Erro: E-mail é obrigatório");
            }
            if(cliente.getNome()==null){
                stringBuilder.append("Erro: Nome é obrigatório");
            }
            if(cliente.getDataNascimento()==null){
                stringBuilder.append("Erro: Data de Nascimento é obrigatório");
            }
            if(cliente.getGenero()==null){
                stringBuilder.append("Erro: Genero é obrigatório");
            }
            if(cliente.getSenha()==null) {
                stringBuilder.append("Erro: Senha é obrigatória");
            }
            if(cliente.getSenha() == null) {
                stringBuilder.append("Erro: Senha é obrigatória");
            } else {
                if (cliente.getSenha().length() < 8) {
                    System.out.println("menor que 8");
                    stringBuilder.append("Erro: Senha precisa ser maior que 8 caracteres");
                }
                if (!cliente.getSenha().contains(cliente.getConfirmarSenha())) {
                    stringBuilder.append("Erro: A senha deve ser igual a confirmar senha");
                }
            }
            if (cliente.getEmail() == null) {
                stringBuilder.append("Erro: E-mail é obrigatório");
            } else if (!cliente.getEmail().contains("@")) {
                stringBuilder.append("Erro: E-mail inválido");
            }

            if (cliente.getSenha() == null || cliente.getConfirmarSenha() == null) {
                stringBuilder.append("Erro: Senha e confirmação de senha são obrigatórias");
            } else if (!cliente.getSenha().equals(cliente.getConfirmarSenha())) {
                stringBuilder.append("Erro: A senha deve ser igual a confirmar senha");
            }

//            String regexLetras = "/^(?=.[A-Za-z])/";
//            String regexNumeros = "/^(?=.[0-9])/";
//            String regexCaracteres = "/^(?=.*[!@#$%^&*.;_=+-:,<>?|/])/";
//
//            Pattern patternLetras = Pattern.compile(regexLetras);
//            Pattern patternNumeros = Pattern.compile(regexNumeros);
//            Pattern patternCaracteres = Pattern.compile(regexCaracteres);
//
//            Matcher matcherLetras = patternLetras.matcher("Mariaalice");
//            Matcher matcherNumeros = patternNumeros.matcher("Mariaalice");
//            Matcher matcherCaracteres = patternCaracteres.matcher("Mariaalice");
//            System.out.println(!matcherLetras.matches());
//            if(!matcherLetras.matches()) {
//            	System.out.println("sem maius");
//	            stringBuilder.append("A senha deve conter letras maiúsculas ou minúsculas");
//            }
//            if(!matcherNumeros.matches()) {
//            	System.out.println("sem numero");
//            	stringBuilder.append("A senha deve conter numeros");
//            }
//            if(!matcherCaracteres.matches()) {
//            	System.out.println("sem especiais");
//            	stringBuilder.append("A senha deve conter caracteres especiais");
//            }
//            if(!cliente.getSenha().equals(cliente.getConfirmarSenha())) {
//            	stringBuilder.append("A senha deve ser igual a confirmar senha");
//            }
//            if(cliente.getTelefone()==null){
//                stringBuilder.append("Telefone é obrigatório");
//            }
//            if(cliente.getEnderecoCobranca()==null){
//                stringBuilder.append("Endereco de cobrança é obrigatório");
//            }
//            if(cliente.getEnderecoEntrega()==null){
//                stringBuilder.append("Endereco de entrega é obrigatório");
//            }
//            if(cliente.getEnderecoResidencial()==null){
//                stringBuilder.append("Endereco residencial é obrigatório");
//            }
//            if(cliente.getCartao()==null){
//                stringBuilder.append("Cartão é obrigatório");
//            }
        }
        return stringBuilder.toString();
    }
}