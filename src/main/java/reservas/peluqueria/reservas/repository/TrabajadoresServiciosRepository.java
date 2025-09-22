package reservas.peluqueria.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reservas.peluqueria.reservas.entity.Servicio;
import reservas.peluqueria.reservas.entity.TrabajadorServicio;

import java.util.List;

public interface TrabajadoresServiciosRepository extends JpaRepository<TrabajadorServicio, Integer> {
    @Query("SELECT ts.servicio FROM TrabajadorServicio ts WHERE ts.trabajador.idUsuario = :idTrabajador")
    List<Servicio> findServiciosByTrabajadorId(@Param("idTrabajador") Integer idTrabajador);
}
