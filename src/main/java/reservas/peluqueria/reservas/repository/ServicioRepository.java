package reservas.peluqueria.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reservas.peluqueria.reservas.entity.Servicio;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio,Integer> {
    List<Servicio> findByActivoTrue();
}
