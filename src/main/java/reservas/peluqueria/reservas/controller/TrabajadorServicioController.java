package reservas.peluqueria.reservas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reservas.peluqueria.reservas.dto.DatosTrabajador;
import reservas.peluqueria.reservas.entity.Servicio;
import reservas.peluqueria.reservas.entity.TrabajadorServicio;
import reservas.peluqueria.reservas.entity.Usuario;
import reservas.peluqueria.reservas.repository.ServicioRepository;
import reservas.peluqueria.reservas.repository.TrabajadoresServiciosRepository;
import reservas.peluqueria.reservas.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trabajadores-servicios")
public class TrabajadorServicioController {

    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final TrabajadoresServiciosRepository tsRepository;

    public TrabajadorServicioController(UsuarioRepository usuarioRepository,
                                        ServicioRepository servicioRepository,
                                        TrabajadoresServiciosRepository tsRepository) {
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
        this.tsRepository = tsRepository;
    }

    @PostMapping("/asociar")
    public ResponseEntity<?> asociarTrabajadorServicio(
            @RequestParam Integer idTrabajador,
            @RequestParam Integer idServicio) {
        try {
            Usuario trabajador = usuarioRepository.findById(idTrabajador)
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
            Servicio servicio = servicioRepository.findById(idServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            TrabajadorServicio ts = new TrabajadorServicio();
            ts.setTrabajador(trabajador);
            ts.setServicio(servicio);
            tsRepository.save(ts);

            return ResponseEntity.ok("Asociación creada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al asociar: " + e.getMessage());
        }
    }

    // ✅ Nuevo: listar todas las asociaciones (para el dashboard)
    @GetMapping("/listar-todo")
    public List<Map<String, Object>> listarTodo() {
        List<TrabajadorServicio> asociaciones = tsRepository.findAll();

        return asociaciones.stream().map(ts -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idTrabajadorServicio", ts.getIdTrabajadorServicio());
            mapa.put("trabajador", ts.getTrabajador());
            mapa.put("servicio", ts.getServicio());
            return mapa;
        }).toList();
    }

    // Mantienes tus métodos antiguos:
    @GetMapping("/listar/{idTrabajador}")
    public List<Servicio> listarServiciosTrabajador(@PathVariable Integer idTrabajador) {
        return tsRepository.findServiciosByTrabajadorId(idTrabajador);
    }

    @GetMapping("/por-servicio/{idServicio}")
    public List<DatosTrabajador> listarTrabajadoresPorServicio(@PathVariable Integer idServicio) {
        return tsRepository.findTrabajadoresByServicioId(idServicio)
                .stream()
                .map(u -> new DatosTrabajador(u.getIdUsuario(), u.getNombre(), u.getApellido()))
                .toList();
    }
}
