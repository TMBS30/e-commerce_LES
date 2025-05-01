package implementacao;

import dominio.Cliente;
import dominio.EntidadeDominio;

public class ValidarLogin implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        StringBuilder stringBuilder = new StringBuilder();

        if (entidade == null) {
            return "Entidade nula";
        }

        if (entidade instanceof Cliente) {
            Cliente cliente = (Cliente) entidade;

            // Validar email
            if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
                stringBuilder.append("Erro: E-mail é obrigatório.\n");
            } else if (!cliente.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                stringBuilder.append("Erro: E-mail inválido.\n");
            }

            // Validar senha
            if (cliente.getSenha() == null || cliente.getSenha().isEmpty()) {
                stringBuilder.append("Erro: Senha é obrigatória.\n");
            }
        }

        return stringBuilder.toString();
    }
}