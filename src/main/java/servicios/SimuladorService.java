package servicios;

import interfaces.InterfazContactoSim;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.springframework.stereotype.Service;

/**
 *
 * @author Raul_515
 */
@Service
public class SimuladorService implements InterfazContactoSim{

    private Map<Integer, DatosSolicitud> solicitudes = new HashMap<>();
    private List<Entidad> listaEntidades = new ArrayList<>();
    private Random random = new Random();
    
    public SimuladorService() {
        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Ordenador CPU");
        e1.setDescripcion("Unidad de procesamiento");

        Entidad e2 = new Entidad();
        e2.setId(2);
        e2.setName("Servidor GPU");
        e2.setDescripcion("Unidad para IA");

        listaEntidades.add(e1);
        listaEntidades.add(e2);
    }
    
    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        int token = random.nextInt(999999);
        solicitudes.put(token, sol);
        return token;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        DatosSimulation ds = new DatosSimulation();
        ds.setAnchoTablero(10);
        ds.setMaxSegundos(10);
        ds.setPuntos(new HashMap<>());
        return ds; 
    }

    @Override
    public List<Entidad> getEntities() {
        return listaEntidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return listaEntidades.stream().anyMatch(e -> e.getId() == id);
    }
    
}
