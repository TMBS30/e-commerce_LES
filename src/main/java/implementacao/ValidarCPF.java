package implementacao;
import dominio.Cliente;
import dominio.EntidadeDominio;

public class ValidarCPF implements IStrategy {
    @Override
    public String processar(EntidadeDominio entidade) {
        if (entidade instanceof Cliente) {

            Cliente c = (Cliente) entidade;

            if (c.getId()==0) {
                if (c.getCpf().equals("00000000000") || c.getCpf().equals("11111111111") ||
                        c.getCpf().equals("22222222222") || c.getCpf().equals("33333333333") ||
                        c.getCpf().equals("44444444444") || c.getCpf().equals("55555555555") ||
                        c.getCpf().equals("66666666666") || c.getCpf().equals("77777777777") ||
                        c.getCpf().equals("88888888888") || c.getCpf().equals("99999999999") ||
                        (c.getCpf().length() != 11))
                    return "CPF Inválido!";

                char dig10, dig11;
                int total, i, resultado, num, peso;

                try {
                    total = 0;
                    peso = 10;
                    for (i=0; i<9; i++) {
                        num = (int)(c.getCpf().charAt(i) - 48);
                        total = total + (num * peso);
                        peso = peso - 1;
                    }

                    resultado = 11 - (total % 11);
                    if ((resultado == 10) || (resultado == 11))
                        dig10 = '0';
                    else
                        dig10 = (char)(resultado + 48);
                    total = 0;
                    peso = 11;
                    for(i=0; i<10; i++) {
                        num = (int)(c.getCpf().charAt(i) - 48);
                        total = total + (num * peso);
                        peso = peso - 1;
                    }

                    resultado = 11 - (total % 11);
                    if ((resultado == 10) || (resultado == 11))
                        dig11 = '0';
                    else
                        dig11 = (char)(resultado + 48);

                    if ((dig10 == c.getCpf().charAt(9)) && (dig11 == c.getCpf().charAt(10)))
                        return null;
                    else
                        return "Erro: CPF Inválido!";
                } catch (Exception erro) {
                    return "Algo inesperado aconteceu!";
                }
            }
            return null;
        }
        return null;
    }
}