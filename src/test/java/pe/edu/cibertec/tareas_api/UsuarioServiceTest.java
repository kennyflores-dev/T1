package pe.edu.cibertec.tareas_api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;
import pe.edu.cibertec.tareas_api.service.UsuarioService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository _repo;


    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(3L);
        usuario.setNombre("Angel Flores");
        usuario.setEmail("angel.Flo@example.com");
        usuario.setRol("ADMIN");
        usuario.setActivo(true);
    }

    @Test
    @DisplayName("Listado de Todos los Usuarios")
    void testlistado() {
        when(_repo.findAll()).thenReturn(Arrays.asList(usuario));
        List<Usuario> resultado = usuarioService.listarTodos();
        assertNotNull(resultado ,"no debe de ser nulo");
        assertEquals(1, resultado.size(),"tamaño de lista debe de ser 1 ");
        verify(_repo, times(1)).findAll();
    }

    // b) buscarPorId_Exitoso()
    @Test
    @DisplayName("encontrar por id")
    void testbuscarid() {

        when(_repo.findById(3L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorId(3L);

        assertTrue(resultado.isPresent());
        assertEquals("Angel Flores", resultado.get().getNombre(),"es Angel Flores");
        verify(_repo, times(1)).findById(3L);
    }


    @Test
    @DisplayName("crear usuarios")
    void testCrear() {

        when(_repo.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(_repo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crear(usuario);

        assertNotNull(resultado,"No debe ser nulo");
        assertEquals("Angel Flores", resultado.getNombre(),"Debe ser Angel Flores");
        verify(_repo, times(1)).existsByEmail(usuario.getEmail());
        verify(_repo, times(1)).save(usuario);
    }

}



