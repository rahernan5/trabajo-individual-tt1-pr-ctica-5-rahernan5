package servicios;

import modelo.Destinatario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Raul_515
 */
public class EmailServiceTest {
    
    public EmailServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of enviarEmail method, of class EmailService.
     */
    @Test
    public void testEnviarEmail() {
        Logger testLogger = LoggerFactory.getLogger(EmailServiceTest.class);
        EmailService service = new EmailService(testLogger);
        
        Destinatario dest = new Destinatario();
        boolean resultado = service.enviarEmail(dest, "Contenido de prueba");
        
        assertTrue(resultado, "El método debe devolver true al loggear el mensaje");
    }
    
}
