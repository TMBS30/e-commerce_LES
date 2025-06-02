package dominio;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class RespostasChatBot {
    private static final String[] PREFIXOS = {
            "Que legal! ",
            "Perfeito! ",
            "Ótima escolha! ",
            "Excelente pedida! ",
            "Adorei a sua busca! ",
            "Olha só que bacana! "
    };

    private static final String[] SUFIXOS = {
            " Se quiser mais opções, me avisa! 😉",
            " Espero que goste! 📚",
            " Me conta se quer que eu envie o link! 👍",
            " Qualquer dúvida, só chamar! 😊"
    };

    public static String gerarRespostaElogio() {
        String[] respostas = {
                "De nada! Fico feliz em ajudar. 😊",
                "Que bom que gostou! Posso ajudar com mais alguma coisa?",
                "Disponha! 😊",
                "Sempre às ordens!",
                "Por nada! 😊"
        };
        Random rand = new Random();
        return respostas[rand.nextInt(respostas.length)];
    }

    private static String gerarPrefixo() {
        Random rand = new Random();
        return PREFIXOS[rand.nextInt(PREFIXOS.length)];
    }

    private static String gerarSufixo() {
        Random rand = new Random();
        return SUFIXOS[rand.nextInt(SUFIXOS.length)];
    }

    public static String gerarRespostaLivro(String categoria, String titulo, double preco, int idLivro) {
        String link = "http://localhost:8080/e-commerce_LES/servlet?action=consultarProduto&id_livro=" + idLivro;
        String contexto = "Um ótimo livro de " + categoria + " seria: ";

        // Formatar preço conforme padrão brasileiro
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String precoFormatado = nf.format(preco);

        String conteudo = String.format("\"%s\" está disponível na nossa loja por %s. Confira mais detalhes: %s",
                titulo, precoFormatado, link);

        return gerarPrefixo() + contexto + conteudo + gerarSufixo();
    }
}
