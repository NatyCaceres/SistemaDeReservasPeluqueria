package reservas.peluqueria.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDisponibleDTO {
    private String horaInicio;
    private String horaFin;
}
