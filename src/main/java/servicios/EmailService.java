package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Raul_515
 */
@Service
public class EmailService implements InterfazEnviarEmails{

    private final Logger logger;

    public EmailService(Logger simulationLogger) {
        this.logger = simulationLogger;
    }
    
    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        logger.info("Simulación de envío de correo. Mensaje: {}", email);
        return true;
    }
    
}
