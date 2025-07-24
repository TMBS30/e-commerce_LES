package controle;

import dao.CarrinhoDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class CleanupListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Aplicação iniciada. Agendando tarefa de limpeza de carrinhos inativos.");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();

         }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Aplicação sendo desligada. Cancelando tarefa de limpeza de carrinhos inativos.");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}
