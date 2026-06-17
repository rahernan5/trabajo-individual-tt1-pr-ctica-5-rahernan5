package servicios;

import java.util.HashMap;
import modelo.DatosSolicitud;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Raul_515
 */
public class SimuladorServiceTest {
    
    private SimuladorService service;
    
    public SimuladorServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        service = new SimuladorService();
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of solicitarSimulation method, of class SimuladorService.
     */
    @Test
    public void testSolicitarSimulation() {
        java.util.Map<Integer, Integer> mapaDatos = new java.util.HashMap<>();
        mapaDatos.put(1, 5); 
        DatosSolicitud sol = new DatosSolicitud(mapaDatos);
        SimuladorService instance = new SimuladorService();
        int result = instance.solicitarSimulation(sol);
        assertTrue(result >= 0, "El token devuelto debe ser un número positivo válido");
    }

    /**
     * Test of descargarDatos method, of class SimuladorService.
     */
    @Test
    public void testDescargarDatos() {
        DatosSolicitud solicitud = new DatosSolicitud(new HashMap<>());
        int token = service.solicitarSimulation(solicitud);
        assertTrue(token >= 0, "El token devuelto debe ser un número positivo válido");
    }

    /**
     * Test of getEntities method, of class SimuladorService.
     */
    @Test
    public void testGetEntities() {
        assertFalse(service.getEntities().isEmpty(), "La lista de entidades no debe estar vacía");
        assertEquals(2, service.getEntities().size(), "Deben haberse creado 2 entidades de prueba");
    }

    /**
     * Test of isValidEntityId method, of class SimuladorService.
     */
    @Test
    public void testIsValidEntityId() {
        assertTrue(service.isValidEntityId(1), "El ID 1 debería ser válido");
        assertFalse(service.isValidEntityId(99), "El ID 99 NO debería ser válido");
    }
    
}
