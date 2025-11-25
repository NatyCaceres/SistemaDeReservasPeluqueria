package reservas.peluqueria.reservas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reservas.peluqueria.reservas.dto.HorarioDisponibleDTO;
import reservas.peluqueria.reservas.entity.*;
import reservas.peluqueria.reservas.repository.*;
import reservas.peluqueria.reservas.service.ReservaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recepcionista")
public class RecepcionistaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private  ReservaService reservaService;


    @Autowired
    private TrabajadoresServiciosRepository trabajadorServicioRepository;

    @Autowired
    private HorariosDisponiblesRepository horariosDisponiblesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    // --------------------------------------------------------------------------
    // 1. CLIENTES
    // --------------------------------------------------------------------------

    @GetMapping("/clientes")
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByIdRol(4);
    }

    // CREAR CLIENTE
    @PostMapping("/clientes")
    public ResponseEntity<?> crearCliente(@RequestBody Usuario cliente) {

        if (usuarioRepository.findByCorreoElectronico(cliente.getCorreoElectronico()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está en uso");
        }

        Rol rolCliente = rolRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        cliente.setRol(rolCliente);
        cliente.setContrasenaHash(passwordEncoder.encode(cliente.getContrasenaHash()));

        usuarioRepository.save(cliente);

        return ResponseEntity.ok("Cliente creado correctamente");
    }

    // EDITAR CLIENTE
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> editarCliente(@PathVariable Integer id, @RequestBody Usuario datos) {

        Usuario cliente = usuarioRepository.findById(id).orElse(null);

        if (cliente == null) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }

        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setCorreoElectronico(datos.getCorreoElectronico());

        if (datos.getContrasenaHash() != null && !datos.getContrasenaHash().isEmpty()) {
            cliente.setContrasenaHash(passwordEncoder.encode(datos.getContrasenaHash()));
        }

        usuarioRepository.save(cliente);
        return ResponseEntity.ok("Cliente actualizado");
    }

    // ELIMINAR CLIENTE
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Cliente eliminado");
    }


    // --------------------------------------------------------------------------
    // 2. RESERVAS DEL DÍA
    // --------------------------------------------------------------------------
    @GetMapping("/reservas/dia")
    public List<Reserva> reservasDelDia(@RequestParam String fecha) {
        LocalDate parsedFecha = LocalDate.parse(fecha);
        return reservaRepository.findByFecha(parsedFecha);
    }

    // --------------------------------------------------------------------------
    // 3. CREAR RESERVA
    // --------------------------------------------------------------------------
    @PostMapping("/reservas")
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {

        reserva.setTipoReserva("recepcionista");
        reserva.setEstadoReserva("confirmada");

        reservaRepository.save(reserva);

        return ResponseEntity.ok("Reserva creada correctamente");
    }

    // --------------------------------------------------------------------------
    // 4. LISTAR TRABAJADORES
    // --------------------------------------------------------------------------
    @GetMapping("/trabajadores")
    public List<Usuario> listarTrabajadores() {
        return usuarioRepository.findByIdRol(3);
    }

    // --------------------------------------------------------------------------
    // 5. SERVICIOS POR TRABAJADOR (ÚNICO CORRECTO)
    // --------------------------------------------------------------------------
    @GetMapping("/trabajadores/{id}/servicios")
    public List<Servicio> serviciosPorTrabajador(@PathVariable Integer id) {
        return trabajadorServicioRepository.findServiciosByIdTrabajador(id);
    }

    // --------------------------------------------------------------------------
    // 6. HORARIOS DISPONIBLES (ÚNICO CORRECTO)
    // --------------------------------------------------------------------------
    @GetMapping("/horarios-disponibles")
    public List<HorarioDisponibleDTO> obtenerHorariosDisponibles(
            @RequestParam Integer trabajadorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return reservaService.obtenerHorariosDisponibles(trabajadorId, fecha);
    }


}
