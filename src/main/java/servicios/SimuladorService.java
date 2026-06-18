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

import com.tt1.trabajo.utilidades.ApiClient;
import com.tt1.trabajo.utilidades.SolicitudApi;
import com.tt1.trabajo.utilidades.Configuration;
import com.tt1.trabajo.utilidades.ApiException;
import com.tt1.trabajo.utilidades.ResultadosApi;

import modelo.Punto;

/**
 *
 * @author Raul_515
 */
@Service
public class SimuladorService implements InterfazContactoSim {

    private Map<Integer, DatosSolicitud> solicitudesTemporales = new HashMap<>();
    private Random random = new Random();

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        
        DatosSimulation ds = new DatosSimulation();
        Map<Integer, List<Punto>> mapaDePuntos = new HashMap<>();
        int maxTiempo = 0;

        try {
            // Configuramos el cliente base generado por OpenAPI
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            String urlBackend = System.getenv("BACKEND_URL");
            if (urlBackend == null || urlBackend.isEmpty()) {
                urlBackend = "http://localhost:8080"; 
            }
            defaultClient.setBasePath(urlBackend);

            // Instanciamos la API
            ResultadosApi resultadosApi = new ResultadosApi(defaultClient);

            Object responseObj = resultadosApi.resultadosPost("UsuarioPrueba", ticket);

            System.out.println("========== RESPUESTA RAW DEL SERVIDOR ==========");
            System.out.println(responseObj);
            System.out.println("================================================");

            Object dataObj = responseObj;
            
            // Si la respuesta viene envuelta en un Objeto (Map), extraemos el campo "data"
            if (responseObj instanceof Map) {
                Map<?, ?> responseMap = (Map<?, ?>) responseObj;
                if (responseMap.containsKey("data")) {
                    dataObj = responseMap.get("data");
                }
            }

            List<String> lineasExtraidas = new ArrayList<>();

            // Escenario A: Es una Lista de elementos (Array JSON)
            if (dataObj instanceof List) {
                for (Object item : (List<?>) dataObj) {
                    lineasExtraidas.add(item.toString());
                }
            } 
            // Escenario B: Es un único Texto gigante con saltos de línea (String)
            else if (dataObj instanceof String) {
                String[] lineas = ((String) dataObj).split("\\r?\\n");
                for (String linea : lineas) {
                    if (!linea.trim().isEmpty()) {
                        lineasExtraidas.add(linea);
                    }
                }
            }

            if (!lineasExtraidas.isEmpty()) {
                int ancho = Integer.parseInt(lineasExtraidas.get(0).trim());
                ds.setAnchoTablero(ancho);

                for (int i = 1; i < lineasExtraidas.size(); i++) {
                    String[] partes = lineasExtraidas.get(i).split(",");
                    
                    if (partes.length >= 4) {
                        int t = Integer.parseInt(partes[0].trim());
                        int y = Integer.parseInt(partes[1].trim());
                        int x = Integer.parseInt(partes[2].trim());
                        String color = partes[3].trim();

                        if (t > maxTiempo) maxTiempo = t;

                        if (!mapaDePuntos.containsKey(t)) {
                            mapaDePuntos.put(t, new ArrayList<>());
                        }

                        Punto p = new Punto();
                        p.setX(x);
                        p.setY(y);
                        p.setColor(color);
                        mapaDePuntos.get(t).add(p);
                    }
                }
            } else {
                System.out.println("El endpoint de resultados no devolvió datos válidos de cuadrícula.");
            }
        } catch (ApiException e) {
            System.err.println("Error del servidor API (Descargar): " + e.getCode() + " - " + e.getResponseBody());
        } catch (Exception e) {
            System.err.println("Error procesando los datos: " + e.getMessage());
        }

        for (int i = 0; i <= maxTiempo; i++) {
            if (!mapaDePuntos.containsKey(i)) {
                mapaDePuntos.put(i, new ArrayList<>());
            }
        }

        ds.setPuntos(mapaDePuntos);
        ds.setMaxSegundos(maxTiempo + 1);
        
        return ds;
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            String urlBackend = System.getenv("BACKEND_URL");
            if (urlBackend == null || urlBackend.isEmpty()) {
                urlBackend = "http://localhost:8080"; 
            }
            defaultClient.setBasePath(urlBackend);
            SolicitudApi api = new SolicitudApi(defaultClient);

            // Preparamos el cuerpo de la petición con las entidades elegidas en el formulario
            com.tt1.trabajo.utilidades.Solicitud peticion = new com.tt1.trabajo.utilidades.Solicitud();
            List<String> nombres = new ArrayList<>();
            List<Integer> cantidades = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : sol.getNums().entrySet()) {
                int id = entry.getKey();
                int cantidad = entry.getValue();
                
                if (id == 1) nombres.add("Oveja");
                else if (id == 2) nombres.add("Lobo");
                else nombres.add("Entidad" + id);
                
                cantidades.add(cantidad);
            }

            peticion.setNombreEntidades(nombres);
            peticion.setCantidadesIniciales(cantidades);

            // Registramos la simulación real en la Máquina Virtual (Endpoint POST)
            api.solicitudSolicitarPost("UsuarioPrueba", peticion);

            //  El servidor ya la ha creado. Ahora le pedimos nuestra lista de tokens y cogemos el último.
            List<?> tokens = api.solicitudGetSolicitudesUsuarioGet("UsuarioPrueba");
            if (tokens != null && !tokens.isEmpty()) {
                int tokenReal = Integer.parseInt(tokens.get(tokens.size() - 1).toString());
                solicitudesTemporales.put(tokenReal, sol); // Lo guardamos para el test unitario
                return tokenReal;
            }

        } catch (ApiException e) {
            System.err.println("Error de la API (Solicitar): " + e.getCode() + " - " + e.getResponseBody());
        } catch (Exception e) {
            System.err.println("Error local al solicitar: " + e.getMessage());
        }
        
        // Fallback por si falla la conexión
        int tokenFalso = Math.abs(random.nextInt(10000));
        solicitudesTemporales.put(tokenFalso, sol);
        return tokenFalso;
    }

    @Override
    public List<Entidad> getEntities() {
        
        List<Entidad> lista = new ArrayList<>();
        
        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Oveja");
        
        Entidad e2 = new Entidad();
        e2.setId(2);
        e2.setName("Lobo");
        
        lista.add(e1);
        lista.add(e2);
        
        return lista;
    }

    @Override
    public boolean isValidEntityId(int id) {

        for (Entidad e : getEntities()) {
            if (e.getId() == id) {
                return true;
            }
        }
        return false;
    }
}