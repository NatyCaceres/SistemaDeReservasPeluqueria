package reservas.peluqueria.reservas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reservas.peluqueria.reservas.dto.DatosRegistroUsuario;
import reservas.peluqueria.reservas.entity.Rol;
import reservas.peluqueria.reservas.entity.Usuario;
import reservas.peluqueria.reservas.repository.RolRepository;
import reservas.peluqueria.reservas.repository.UsuarioRepository;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class TrabajadorController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public TrabajadorController(UsuarioRepository usuarioRepository,
                                RolRepository rolRepository,
                                PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/rol/trabajador")
    public ResponseEntity<List<Usuario>> listarTrabajadores() {
        List<Usuario> trabajadores = usuarioRepository.findByIdRol(3); // Rol TRABAJADOR = 3
        return ResponseEntity.ok(trabajadores);
    }

    @PostMapping("/registrar-trabajador")
    public ResponseEntity<?> registrarTrabajador(@RequestBody DatosRegistroUsuario datos) {

        if (usuarioRepository.findByCorreoElectronico(datos.correoElectronico()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        Rol rolTrabajador = rolRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Rol TRABAJADOR no encontrado"));

        Usuario trabajador = new Usuario();
        trabajador.setNombre(datos.nombre());
        trabajador.setApellido(datos.apellido());
        trabajador.setCorreoElectronico(datos.correoElectronico());
        trabajador.setContrasenaHash(passwordEncoder.encode(datos.contrasena()));
        trabajador.setRol(rolTrabajador);

        usuarioRepository.save(trabajador);

        return ResponseEntity.ok("Trabajador registrado correctamente");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTrabajador(@PathVariable Integer id) {

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador no encontrado");
        }

        usuarioRepository.deleteById(id);

        return ResponseEntity.ok("Trabajador eliminado correctamente");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editarTrabajador(@PathVariable Integer id, @RequestBody Usuario datos) {
        Usuario trabajador = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        trabajador.setNombre(datos.getNombre());
        trabajador.setApellido(datos.getApellido());
        trabajador.setCorreoElectronico(datos.getCorreoElectronico());

        // Si viene una contraseña nueva → encriptar
        if (datos.getContrasenaHash() != null && !datos.getContrasenaHash().isBlank()) {
            trabajador.setContrasenaHash(passwordEncoder.encode(datos.getContrasenaHash()));
        }

        usuarioRepository.save(trabajador);

        return ResponseEntity.ok("Trabajador actualizado correctamente");
    }
    @GetMapping("/{id}")
    public Usuario obtenerTrabajador(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
    }

}
