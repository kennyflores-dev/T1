package pe.edu.cibertec.tareas_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;
import pe.edu.cibertec.tareas_api.service.ProyectoService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository _reporsitory;

    @Mock
    private UsuarioRepository _repo;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto _proyecto;
    private Usuario _usuario;

    @BeforeEach
    void setUp() {
        _usuario = new Usuario();
        _usuario.setId(3L);
        _usuario.setNombre("Angel Flores");
        _usuario.setActivo(true);

        _proyecto = new Proyecto();
        _proyecto.setId(3L);
        _proyecto.setNombre("Implementacion de E-Commerce");
        _proyecto.setDescripcion("Implementacion de E-Commerce al proyecto");
        _proyecto.setFechaInicio(LocalDate.of(2025, 10, 11));
        _proyecto.setFechaFin(LocalDate.of(2026, 11, 21));
        _proyecto.setUsuario(_usuario);
        _proyecto.setActivo(true);

    }

    @Test
    @DisplayName("Listado de Todos los Proyectos")
    void testListarTodos() {
        when(_reporsitory.findAll()).thenReturn(Arrays.asList(_proyecto));
        List<Proyecto> resultado = proyectoService.listarTodos();
        assertNotNull(resultado, "no debe de ser nulo");
        assertEquals(1, resultado.size(), "tamaño de lista debe de ser 1 ");
        verify(_reporsitory, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 2: Buscar proyecto por ID exitosamente")
    void testBuscarPorIdExitoso() {

        when(_reporsitory.findById(3L)).thenReturn(Optional.of(_proyecto));

        Optional<Proyecto> resultado = proyectoService.buscarPorId(3L);

        assertTrue(resultado.isPresent());
        assertEquals("Implementacion de E-Commerce", resultado.get().getNombre(), "es Implementacion de E-Commerce");
        verify(_reporsitory, times(1)).findById(3L);
    }


    @Test
    @DisplayName("Test 3: Crear proyecto exitosamente")
    void testCrearProyectoExitoso() {
        when(_repo.findById(_usuario.getId())).thenReturn(Optional.of(_usuario));
        when(_reporsitory.save(any(Proyecto.class))).thenReturn(_proyecto);
        Proyecto result = proyectoService.crear(_proyecto);
        assertNotNull(result, "No debe ser nulo");
        assertEquals("Implementacion de E-Commerce", result.getNombre(), "Debe ser Implementacion de E-Commerce");
        verify(_repo, times(1)).findById(_usuario.getId());
        verify(_reporsitory, times(1)).save(_proyecto);
    }

    @Test
    @DisplayName("Excepcion de fechas")
    void creacionFehaInvalida() {
        Proyecto res = new Proyecto();
        res.setFechaInicio(LocalDate.of(2024, 1, 11));
        res.setFechaFin(LocalDate.of(2024, 1, 8));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.crear(res);
        });

        assertEquals("La fecha de fin debe ser mayor a la fecha de inicio",
                exception.getMessage());
    }
    }