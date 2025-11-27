package pe.edu.cibertec.tareas_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.TareaRepository;
import pe.edu.cibertec.tareas_api.service.TareaService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TareaServiceTest {
    @Mock
    private TareaRepository _repotarea;
    @Mock
    private ProyectoRepository _proyec;
    @InjectMocks
    private TareaService service;
    private Tarea tarea;
    private Proyecto ye;


    @BeforeEach
    void setUp() {
        ye = new Proyecto();
        ye.setId(3L);
        ye.setNombre("Implementacion de E-Commerce");
        ye.setDescripcion("Implementacion de E-Commerce al proyecto");
        ye.setFechaInicio(LocalDate.of(2025, 10, 11));
        ye.setFechaFin(LocalDate.of(2026, 11, 21));
        ye.setActivo(true);

        tarea = new Tarea();
        tarea.setId(2L);
        tarea.setDescripcion("Crear una clase donde Se defina el modelo de Productos");
        tarea.setEstado("COMPLETADA");
        tarea.setPrioridad("ALTA");
        tarea.setProyecto(ye);
    }

    @Test
    @DisplayName("listado de tareas")
    void testListarTodos() {
        when(_repotarea.findAll()).thenReturn(Arrays.asList(tarea));
        List<Tarea> resultado = service.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(_repotarea, times(1)).findAll();
    }

    @Test
    @DisplayName("Creacion de Tareas")
    void testCrearTarea() {
        when(_proyec.findById(ye.getId())).thenReturn(Optional.of(ye));
        when(_repotarea.save(any(Tarea.class))).thenReturn(tarea);
        Tarea res = service.crear(tarea);
        assertNotNull(res);
        assertEquals("Crear una clase donde Se defina el modelo de Productos", res.getDescripcion());
        verify(_proyec, times(1)).findById(3L);
        verify(_repotarea, times(1)).save(tarea);
    }


    @Test
    @DisplayName("Estado cambio")
    void testCrearEstadoInvalido() {
        tarea.setEstado("ESTADO_INVALIDO");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.crear(tarea);
        });
        assertEquals("Estado inválido. Debe ser: PENDIENTE, EN_PROGRESO o COMPLETADA",
                ex.getMessage());

                verify(_repotarea, never()).save(any(Tarea.class));
         }

    }


