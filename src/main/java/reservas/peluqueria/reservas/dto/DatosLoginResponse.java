package reservas.peluqueria.reservas.dto;

public record DatosLoginResponse(
        String token,
        UsuarioResponse usuario
) {
    public record UsuarioResponse(
            Integer idUsuario,
            String nombre,
            String correoElectronico,
            RolResponse rol
    ) {}

    public record RolResponse(
            Long idRol,
            String nombre
    ) {}
}