package reservas.peluqueria.reservas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reservas.peluqueria.reservas.dto.DatosReserva;
import reservas.peluqueria.reservas.entity.Reserva;
import reservas.peluqueria.reservas.service.ReservaService;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(@RequestBody DatosReserva datos,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        // idCliente obtenido del JWT
        Integer idCliente = Integer.parseInt(userDetails.getUsername());
        Reserva reserva = reservaService.crearReserva(datos, idCliente);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> listarReservasCliente(@AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = Integer.parseInt(userDetails.getUsername());
        return ResponseEntity.ok(reservaService.listarReservasCliente(idCliente));
    }

    @GetMapping("/trabajador/{idTrabajador}")
    public ResponseEntity<List<Reserva>> listarReservasTrabajador(@PathVariable Integer idTrabajador) {
        return ResponseEntity.ok(reservaService.listarReservasTrabajador(idTrabajador));
    }
}
