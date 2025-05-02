package dominio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    // Método para converter data em formato específico
    public static String converterData(Date data) {
        try {
            // Define o formato de saída desejado
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Formata a data convertida
            return outputFormat.format(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retorna null em caso de erro
        }
    }

    public static void main(String[] args) {
        // Teste de conversão de data
        String dataConvertida = converterData(new Date());  // Usando a data atual
        if (dataConvertida != null) {
            System.out.println(dataConvertida);
        } else {
            System.out.println("Erro ao converter a data.");
        }
    }
}

