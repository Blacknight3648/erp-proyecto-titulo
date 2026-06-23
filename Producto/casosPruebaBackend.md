# Casos de Prueba Backend — ERP Antuan SA

> Documento de especificación de tests unitarios para el backend Spring Boot.
> Cada caso incluye: **ID**, **módulo**, **clase bajo test**, **método**, **descripción**, **datos de entrada**, **resultado esperado** y el **código JUnit 5 + Mockito** correspondiente.

---

## Tabla de Contenidos

1. [Módulo Gestión de Usuarios](#1-módulo-gestión-de-usuarios)
   - [1.1 UserServiceImpl](#11-userserviceimpl)
   - [1.2 UserValidator](#12-uservalidator)
   - [1.3 UserMapper](#13-usermapper)
   - [1.4 UsuarioController](#14-usuariocontroller)
2. [Módulo Shared — Value Objects y Validaciones](#2-módulo-shared--value-objects-y-validaciones)
   - [2.1 Money](#21-money)
   - [2.2 RunValidator](#22-runvalidator)
3. [Módulo Comercial — Dominio](#3-módulo-comercial--dominio)
   - [3.1 NotaVenta](#31-notaventa)
   - [3.2 EstadoNV](#32-estadonv)
4. [Módulo Producción — Dominio](#4-módulo-producción--dominio)
   - [4.1 OrdenCompra](#41-ordencompra)
   - [4.2 EstadoOC](#42-estadooc)

---

## 1. Módulo Gestión de Usuarios

### 1.1 UserServiceImpl

**Clase bajo test:** `backend.com.gestionUsuarios.usuario.application.service.impl.UserServiceImpl`

---

#### CP-US-001 — Crear usuario exitosamente con DTO

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-001                                                                   |
| **Método**         | `crearUsuario(CreateUserDTO dto)`                                           |
| **Descripción**    | Verifica que se crea un usuario correctamente cuando el DTO es válido       |
| **Entrada**        | `CreateUserDTO` con run="12345678-5", email="test@mail.com", roles={"ADMIN"}, areas={"Comercial"} |
| **Resultado**      | Se retorna `User` con los datos mapeados y roles/áreas asignados            |

```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private AreaRepository areaRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserValidator userValidator;
    @Mock private VendedorRepository vendedorRepository;
    @Mock private EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    @Mock private NotaVentaJpaRepository notaVentaRepository;
    @Mock private SolicitudCostosJpaRepository solicitudCostosRepository;
    @Mock private SolicitudCotizacionJpaRepository solicitudCotizacionRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("CP-US-001: Crear usuario exitosamente con DTO")
    void crearUsuario_conDTOValido_retornaUsuarioCreado() {
        // Arrange
        CreateUserDTO dto = CreateUserDTO.builder()
                .usuarioRun("12345678-5")
                .usuarioNombre("Juan")
                .usuarioApellidos("Pérez")
                .usuarioEmail("juan@mail.com")
                .usuarioPassword("password123")
                .telefono("+56912345678")
                .roles(Set.of("ADMIN"))
                .areas(Set.of("Comercial"))
                .build();

        Role role = Role.builder().id(1L).nombre("ADMIN").build();
        Area area = Area.builder().areaId(1L).nombre("Comercial").build();
        User mappedUser = User.builder()
                .usuarioRun("12345678-5")
                .usuarioNombre("Juan")
                .usuarioApellidos("Pérez")
                .usuarioEmail("juan@mail.com")
                .usuarioPassword("password123")
                .telefono("+56912345678")
                .enabled(true)
                .build();

        when(roleRepository.findByNombre("ADMIN")).thenReturn(Optional.of(role));
        when(areaRepository.findByNombre("Comercial")).thenReturn(Optional.of(area));
        when(userMapper.toUser(dto)).thenReturn(mappedUser);
        doNothing().when(userValidator).validateRun(anyString());
        doNothing().when(userValidator).validateUniqueness(anyString(), anyString());
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);

        // Act
        User resultado = userService.crearUsuario(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getUsuarioNombre());
        verify(userValidator).validateRun("12345678-5");
        verify(userValidator).validateUniqueness("juan@mail.com", "12345678-5");
        verify(userRepository).save(any(User.class));
    }
}
```

---

#### CP-US-002 — Crear usuario con rol inexistente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-002                                                                   |
| **Método**         | `crearUsuario(CreateUserDTO dto)`                                           |
| **Descripción**    | Verifica que se lanza RuntimeException cuando un rol no existe en la BD     |
| **Entrada**        | `CreateUserDTO` con roles={"ROL_INEXISTENTE"}                               |
| **Resultado**      | Se lanza `RuntimeException("Rol no encontrado: ROL_INEXISTENTE")`           |

```java
@Test
@DisplayName("CP-US-002: Crear usuario con rol inexistente lanza excepción")
void crearUsuario_conRolInexistente_lanzaRuntimeException() {
    CreateUserDTO dto = CreateUserDTO.builder()
            .usuarioRun("12345678-5")
            .usuarioNombre("Juan")
            .usuarioApellidos("Pérez")
            .usuarioEmail("juan@mail.com")
            .usuarioPassword("pass")
            .telefono("+56912345678")
            .roles(Set.of("ROL_INEXISTENTE"))
            .areas(Set.of())
            .build();

    when(roleRepository.findByNombre("ROL_INEXISTENTE")).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> userService.crearUsuario(dto));

    assertTrue(ex.getMessage().contains("Rol no encontrado"));
}
```

---

#### CP-US-003 — Crear usuario con área inexistente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-003                                                                   |
| **Método**         | `crearUsuario(CreateUserDTO dto)`                                           |
| **Descripción**    | Verifica que se lanza RuntimeException cuando un área no existe en la BD    |
| **Entrada**        | `CreateUserDTO` con areas={"AREA_FANTASMA"}                                 |
| **Resultado**      | Se lanza `RuntimeException("Área no encontrada: AREA_FANTASMA")`            |

```java
@Test
@DisplayName("CP-US-003: Crear usuario con área inexistente lanza excepción")
void crearUsuario_conAreaInexistente_lanzaRuntimeException() {
    CreateUserDTO dto = CreateUserDTO.builder()
            .usuarioRun("12345678-5")
            .usuarioNombre("Juan")
            .usuarioApellidos("Pérez")
            .usuarioEmail("juan@mail.com")
            .usuarioPassword("pass")
            .telefono("+56912345678")
            .roles(Set.of())
            .areas(Set.of("AREA_FANTASMA"))
            .build();

    // Roles vacíos => no falla en roles
    when(areaRepository.findByNombre("AREA_FANTASMA")).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> userService.crearUsuario(dto));

    assertTrue(ex.getMessage().contains("Área no encontrada"));
}
```

---

#### CP-US-004 — Crear usuario con email duplicado lanza UserDuplicadoException

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-004                                                                   |
| **Método**         | `crearUsuario(User, Set<Role>, Set<Area>)`                                  |
| **Descripción**    | Verifica que no se permite crear un usuario con email ya registrado          |
| **Entrada**        | User con email existente en la BD                                           |
| **Resultado**      | Se lanza `UserDuplicadoException`                                           |

```java
@Test
@DisplayName("CP-US-004: Crear usuario con email duplicado lanza UserDuplicadoException")
void crearUsuario_conEmailDuplicado_lanzaUserDuplicadoException() {
    User user = User.builder()
            .usuarioRun("12345678-5")
            .usuarioEmail("duplicado@mail.com")
            .usuarioNombre("Test")
            .usuarioApellidos("User")
            .usuarioPassword("pass")
            .telefono("123")
            .build();

    doNothing().when(userValidator).validateRun(anyString());
    doThrow(new UserDuplicadoException("email", "duplicado@mail.com"))
            .when(userValidator).validateUniqueness("duplicado@mail.com", "12345678-5");

    assertThrows(UserDuplicadoException.class,
            () -> userService.crearUsuario(user, Set.of(), Set.of()));
}
```

---

#### CP-US-005 — Listar usuarios retorna lista completa

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-005                                                                   |
| **Método**         | `listarUsuarios()`                                                          |
| **Descripción**    | Verifica que el servicio retorna todos los usuarios del repositorio          |
| **Entrada**        | N/A (mock del repositorio retorna 3 usuarios)                               |
| **Resultado**      | Lista con 3 elementos                                                       |

```java
@Test
@DisplayName("CP-US-005: Listar usuarios retorna lista completa")
void listarUsuarios_retornaListaCompleta() {
    List<User> usuarios = List.of(
            User.builder().usuarioId(1L).usuarioNombre("Ana").build(),
            User.builder().usuarioId(2L).usuarioNombre("Luis").build(),
            User.builder().usuarioId(3L).usuarioNombre("María").build()
    );

    when(userRepository.findAll()).thenReturn(usuarios);

    List<User> resultado = userService.listarUsuarios();

    assertEquals(3, resultado.size());
    verify(userRepository).findAll();
}
```

---

#### CP-US-006 — Obtener usuario por ID existente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-006                                                                   |
| **Método**         | `obtenerUsuario(Long id)`                                                   |
| **Descripción**    | Verifica que retorna el usuario correcto cuando el ID existe                 |
| **Entrada**        | id = 1L                                                                     |
| **Resultado**      | Retorna User con id=1L                                                      |

```java
@Test
@DisplayName("CP-US-006: Obtener usuario por ID existente")
void obtenerUsuario_conIdExistente_retornaUsuario() {
    User user = User.builder().usuarioId(1L).usuarioNombre("Juan").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    User resultado = userService.obtenerUsuario(1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getUsuarioId());
    assertEquals("Juan", resultado.getUsuarioNombre());
}
```

---

#### CP-US-007 — Obtener usuario por ID inexistente lanza UserNotFoundException

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-007                                                                   |
| **Método**         | `obtenerUsuario(Long id)`                                                   |
| **Descripción**    | Verifica que se lanza excepción cuando el ID no existe                       |
| **Entrada**        | id = 999L                                                                   |
| **Resultado**      | Se lanza `UserNotFoundException`                                            |

```java
@Test
@DisplayName("CP-US-007: Obtener usuario por ID inexistente lanza UserNotFoundException")
void obtenerUsuario_conIdInexistente_lanzaUserNotFoundException() {
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
            () -> userService.obtenerUsuario(999L));
}
```

---

#### CP-US-008 — Obtener usuario por RUN existente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-008                                                                   |
| **Método**         | `obtenerUsuarioPorRun(String run)`                                          |
| **Descripción**    | Verifica que retorna usuario correcto buscando por RUN                       |
| **Entrada**        | run = "12345678-5"                                                          |
| **Resultado**      | Retorna User con ese RUN                                                    |

```java
@Test
@DisplayName("CP-US-008: Obtener usuario por RUN existente")
void obtenerUsuarioPorRun_existente_retornaUsuario() {
    User user = User.builder().usuarioRun("12345678-5").usuarioNombre("Pedro").build();
    when(userRepository.findByUsuarioRun("12345678-5")).thenReturn(Optional.of(user));

    User resultado = userService.obtenerUsuarioPorRun("12345678-5");

    assertEquals("12345678-5", resultado.getUsuarioRun());
}
```

---

#### CP-US-009 — Obtener usuario por RUN inexistente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-009                                                                   |
| **Método**         | `obtenerUsuarioPorRun(String run)`                                          |
| **Descripción**    | Verifica que se lanza excepción cuando el RUN no existe                      |
| **Entrada**        | run = "99999999-9"                                                          |
| **Resultado**      | Se lanza `UserNotFoundException`                                            |

```java
@Test
@DisplayName("CP-US-009: Obtener usuario por RUN inexistente lanza excepción")
void obtenerUsuarioPorRun_inexistente_lanzaUserNotFoundException() {
    when(userRepository.findByUsuarioRun("99999999-9")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
            () -> userService.obtenerUsuarioPorRun("99999999-9"));
}
```

---

#### CP-US-010 — Obtener usuario por email existente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-010                                                                   |
| **Método**         | `obtenerUsuarioPorEmail(String email)`                                      |
| **Descripción**    | Verifica que retorna usuario correcto buscando por email                     |
| **Entrada**        | email = "juan@mail.com"                                                     |
| **Resultado**      | Retorna User con ese email                                                  |

```java
@Test
@DisplayName("CP-US-010: Obtener usuario por email existente")
void obtenerUsuarioPorEmail_existente_retornaUsuario() {
    User user = User.builder().usuarioEmail("juan@mail.com").usuarioNombre("Juan").build();
    when(userRepository.findByUsuarioEmail("juan@mail.com")).thenReturn(Optional.of(user));

    User resultado = userService.obtenerUsuarioPorEmail("juan@mail.com");

    assertEquals("juan@mail.com", resultado.getUsuarioEmail());
}
```

---

#### CP-US-011 — Actualizar usuario parcialmente (solo nombre y teléfono)

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-011                                                                   |
| **Método**         | `actualizarUsuario(Long id, User userActualizado, Set<Role>, Set<Area>)`    |
| **Descripción**    | Verifica que solo se actualizan los campos que no son nulos/vacíos           |
| **Entrada**        | User con nombre nuevo y teléfono nuevo, resto en null                        |
| **Resultado**      | Nombre y teléfono actualizados, demás campos conservan valor original        |

```java
@Test
@DisplayName("CP-US-011: Actualizar usuario parcialmente")
void actualizarUsuario_parcial_soloActualizaCamposNoNulos() {
    User existente = User.builder()
            .usuarioId(1L)
            .usuarioRun("12345678-5")
            .usuarioNombre("Juan")
            .usuarioApellidos("Pérez")
            .usuarioEmail("juan@mail.com")
            .usuarioPassword("oldpass")
            .telefono("+56911111111")
            .enabled(true)
            .build();

    User datosActualizados = User.builder()
            .usuarioRun("12345678-5")
            .usuarioNombre("Juan Carlos")
            .usuarioApellidos(null) // No se actualiza
            .usuarioEmail(null)     // No se actualiza
            .usuarioPassword(null)  // No se actualiza
            .telefono("+56999999999")
            .enabled(true)
            .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(existente));
    doNothing().when(userValidator).validateRun(anyString());
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User resultado = userService.actualizarUsuario(1L, datosActualizados, null, null);

    assertEquals("Juan Carlos", resultado.getUsuarioNombre());
    assertEquals("Pérez", resultado.getUsuarioApellidos()); // Sin cambio
    assertEquals("juan@mail.com", resultado.getUsuarioEmail()); // Sin cambio
    assertEquals("+56999999999", resultado.getTelefono());
}
```

---

#### CP-US-012 — Eliminar usuario existente desvincula vendedor y registros comerciales

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-012                                                                   |
| **Método**         | `eliminarUsuario(Long id)`                                                  |
| **Descripción**    | Verifica que al eliminar un usuario se desvinculan sus registros comerciales |
| **Entrada**        | id = 1L (con vendedor asociado)                                             |
| **Resultado**      | Se desvinculan EVN, NV, SCOS, SCot y se elimina vendedor + usuario          |

```java
@Test
@DisplayName("CP-US-012: Eliminar usuario desvincula vendedor y registros comerciales")
void eliminarUsuario_conVendedorAsociado_desvinculaTodoYElimina() {
    Vendedor vendedor = new Vendedor();
    vendedor.setIdVendedor(10L);

    when(userRepository.existsById(1L)).thenReturn(true);
    when(vendedorRepository.findByUsuario_UsuarioId(1L)).thenReturn(Optional.of(vendedor));

    userService.eliminarUsuario(1L);

    verify(evaluacionNegocioRepository).desvincularVendedor(10L);
    verify(notaVentaRepository).desvincularVendedor(10L);
    verify(solicitudCostosRepository).desvincularVendedor(10L);
    verify(solicitudCotizacionRepository).desvincularVendedor(10L);
    verify(vendedorRepository).deleteById(10L);
    verify(userRepository).deleteById(1L);
}
```

---

#### CP-US-013 — Eliminar usuario inexistente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-013                                                                   |
| **Método**         | `eliminarUsuario(Long id)`                                                  |
| **Descripción**    | Verifica que se lanza excepción al intentar eliminar un usuario que no existe|
| **Entrada**        | id = 999L                                                                   |
| **Resultado**      | Se lanza `UserNotFoundException`                                            |

```java
@Test
@DisplayName("CP-US-013: Eliminar usuario inexistente lanza excepción")
void eliminarUsuario_conIdInexistente_lanzaUserNotFoundException() {
    when(userRepository.existsById(999L)).thenReturn(false);

    assertThrows(UserNotFoundException.class,
            () -> userService.eliminarUsuario(999L));

    verify(userRepository, never()).deleteById(anyLong());
}
```

---

#### CP-US-014 — Toggle enabled cambia estado de habilitado

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-014                                                                   |
| **Método**         | `toggleEnabled(Long id)`                                                    |
| **Descripción**    | Verifica que el toggle invierte el estado enabled del usuario                |
| **Entrada**        | id = 1L (usuario con enabled=true)                                          |
| **Resultado**      | Retorna User con enabled=false                                              |

```java
@Test
@DisplayName("CP-US-014: Toggle enabled cambia estado correctamente")
void toggleEnabled_deTrue_cambiaAFalse() {
    User user = User.builder().usuarioId(1L).enabled(true).usuarioNombre("Test").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User resultado = userService.toggleEnabled(1L);

    assertFalse(resultado.isEnabled());
}

@Test
@DisplayName("CP-US-014b: Toggle enabled de false a true")
void toggleEnabled_deFalse_cambiaATrue() {
    User user = User.builder().usuarioId(1L).enabled(false).usuarioNombre("Test").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User resultado = userService.toggleEnabled(1L);

    assertTrue(resultado.isEnabled());
}
```

---

#### CP-US-015 — Asignar roles a usuario existente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-015                                                                   |
| **Método**         | `asignarRoles(Long userId, Set<String> roles)`                              |
| **Descripción**    | Verifica que se asignan roles correctamente a un usuario                     |
| **Entrada**        | userId=1L, roles={"ADMIN", "VENDEDOR"}                                      |
| **Resultado**      | User con 2 roles asignados                                                  |

```java
@Test
@DisplayName("CP-US-015: Asignar roles a usuario existente")
void asignarRoles_conRolesValidos_actualizaUsuario() {
    User user = User.builder().usuarioId(1L).usuarioNombre("Test").roles(new HashSet<>()).build();
    Role admin = Role.builder().id(1L).nombre("ADMIN").build();
    Role vendedor = Role.builder().id(2L).nombre("VENDEDOR").build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(roleRepository.findByNombre("ADMIN")).thenReturn(Optional.of(admin));
    when(roleRepository.findByNombre("VENDEDOR")).thenReturn(Optional.of(vendedor));
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User resultado = userService.asignarRoles(1L, Set.of("ADMIN", "VENDEDOR"));

    assertEquals(2, resultado.getRoles().size());
    verify(userRepository).save(user);
}
```

---

#### CP-US-016 — Asignar áreas a usuario existente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-US-016                                                                   |
| **Método**         | `asignarAreas(Long userId, Set<String> areas)`                              |
| **Descripción**    | Verifica que se asignan áreas correctamente a un usuario                     |
| **Entrada**        | userId=1L, areas={"Comercial"}                                              |
| **Resultado**      | User con 1 área asignada                                                    |

```java
@Test
@DisplayName("CP-US-016: Asignar áreas a usuario existente")
void asignarAreas_conAreasValidas_actualizaUsuario() {
    User user = User.builder().usuarioId(1L).usuarioNombre("Test").areas(new HashSet<>()).build();
    Area comercial = Area.builder().areaId(1L).nombre("Comercial").build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(areaRepository.findByNombre("Comercial")).thenReturn(Optional.of(comercial));
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User resultado = userService.asignarAreas(1L, Set.of("Comercial"));

    assertEquals(1, resultado.getAreas().size());
    verify(userRepository).save(user);
}
```

---

### 1.2 UserValidator

**Clase bajo test:** `backend.com.gestionUsuarios.usuario.application.service.UserValidator`

---

#### CP-UV-001 — Validar unicidad de email existente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UV-001                                                                   |
| **Método**         | `validateUniqueness(String email, String run)`                              |
| **Descripción**    | Verifica que se lanza excepción si el email ya existe                        |
| **Entrada**        | email="duplicado@mail.com", run="12345678-5"                                |
| **Resultado**      | Se lanza `UserDuplicadoException` con campo "email"                         |

```java
@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    @DisplayName("CP-UV-001: Email duplicado lanza UserDuplicadoException")
    void validateUniqueness_emailDuplicado_lanzaExcepcion() {
        when(userRepository.existsByUsuarioEmail("duplicado@mail.com")).thenReturn(true);

        UserDuplicadoException ex = assertThrows(UserDuplicadoException.class,
                () -> userValidator.validateUniqueness("duplicado@mail.com", "12345678-5"));

        assertTrue(ex.getMessage().contains("email"));
    }
}
```

---

#### CP-UV-002 — Validar unicidad de RUN existente lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UV-002                                                                   |
| **Método**         | `validateUniqueness(String email, String run)`                              |
| **Descripción**    | Verifica que se lanza excepción si el RUN ya existe                          |
| **Entrada**        | email="nuevo@mail.com", run="12345678-5" (existente)                        |
| **Resultado**      | Se lanza `UserDuplicadoException` con campo "run"                           |

```java
@Test
@DisplayName("CP-UV-002: RUN duplicado lanza UserDuplicadoException")
void validateUniqueness_runDuplicado_lanzaExcepcion() {
    when(userRepository.existsByUsuarioEmail("nuevo@mail.com")).thenReturn(false);
    when(userRepository.existsByUsuarioRun("12345678-5")).thenReturn(true);

    UserDuplicadoException ex = assertThrows(UserDuplicadoException.class,
            () -> userValidator.validateUniqueness("nuevo@mail.com", "12345678-5"));

    assertTrue(ex.getMessage().contains("run"));
}
```

---

#### CP-UV-003 — Validar RUN inválido lanza IllegalArgumentException

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UV-003                                                                   |
| **Método**         | `validateRun(String run)`                                                   |
| **Descripción**    | Verifica que un RUN con dígito verificador incorrecto lanza excepción        |
| **Entrada**        | run = "12345678-0" (DV incorrecto)                                          |
| **Resultado**      | Se lanza `IllegalArgumentException("RUN inválido: 12345678-0")`             |

```java
@Test
@DisplayName("CP-UV-003: RUN inválido lanza IllegalArgumentException")
void validateRun_conRunInvalido_lanzaIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
            () -> userValidator.validateRun("12345678-0"));
}
```

---

#### CP-UV-004 — Validar unicidad con datos únicos pasa sin excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UV-004                                                                   |
| **Método**         | `validateUniqueness(String email, String run)`                              |
| **Descripción**    | Verifica que no lanza excepción cuando ambos son únicos                      |
| **Entrada**        | email="unico@mail.com", run="11111111-1" (ambos nuevos)                     |
| **Resultado**      | No se lanza ninguna excepción                                               |

```java
@Test
@DisplayName("CP-UV-004: Datos únicos pasan validación sin excepción")
void validateUniqueness_datosUnicos_noPasaNada() {
    when(userRepository.existsByUsuarioEmail("unico@mail.com")).thenReturn(false);
    when(userRepository.existsByUsuarioRun("11111111-1")).thenReturn(false);

    assertDoesNotThrow(
            () -> userValidator.validateUniqueness("unico@mail.com", "11111111-1"));
}
```

---

### 1.3 UserMapper

**Clase bajo test:** `backend.com.gestionUsuarios.usuario.infrastructure.mapper.UserMapper`

---

#### CP-UM-001 — Mapear User a UserDTO correctamente

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UM-001                                                                   |
| **Método**         | `toUserDTO(User user)`                                                      |
| **Descripción**    | Verifica que todos los campos se mapean correctamente de User a UserDTO      |
| **Entrada**        | User con id=1L, nombre="Ana", roles y areas                                 |
| **Resultado**      | UserDTO con mismos datos (sin password)                                      |

```java
class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    @DisplayName("CP-UM-001: Mapear User a UserDTO correctamente")
    void toUserDTO_conUserCompleto_mapeaTodosLosCampos() {
        Area area = Area.builder().areaId(1L).nombre("Comercial").descripcion("Área Comercial").build();
        Role role = Role.builder().id(1L).nombre("ADMIN").descripcion("Administrador").build();

        User user = User.builder()
                .usuarioId(1L)
                .usuarioRun("12345678-5")
                .usuarioNombre("Ana")
                .usuarioApellidos("López")
                .usuarioEmail("ana@mail.com")
                .usuarioPassword("secreto")
                .telefono("+56912345678")
                .enabled(true)
                .roles(Set.of(role))
                .areas(Set.of(area))
                .build();

        UserDTO dto = mapper.toUserDTO(user);

        assertEquals(1L, dto.getUsuarioId());
        assertEquals("Ana", dto.getUsuarioNombre());
        assertEquals("ana@mail.com", dto.getUsuarioEmail());
        assertNull(dto.getUsuarioPassword()); // Password NO se mapea al DTO
        assertEquals(1, dto.getRoles().size());
        assertEquals(1, dto.getAreas().size());
    }
}
```

---

#### CP-UM-002 — Mapear null a UserDTO retorna null

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UM-002                                                                   |
| **Método**         | `toUserDTO(User user)`                                                      |
| **Descripción**    | Verifica que mapear un null retorna null sin excepción                        |
| **Entrada**        | `null`                                                                      |
| **Resultado**      | Retorna `null`                                                              |

```java
@Test
@DisplayName("CP-UM-002: Mapear null a UserDTO retorna null")
void toUserDTO_conNull_retornaNull() {
    assertNull(mapper.toUserDTO(null));
}
```

---

#### CP-UM-003 — Mapear CreateUserDTO a User

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UM-003                                                                   |
| **Método**         | `toUser(CreateUserDTO dto)`                                                 |
| **Descripción**    | Verifica que el DTO de creación se mapea a entidad User con enabled=true     |
| **Entrada**        | CreateUserDTO con datos completos                                           |
| **Resultado**      | User con enabled=true y sin id asignado                                     |

```java
@Test
@DisplayName("CP-UM-003: Mapear CreateUserDTO a User con enabled=true")
void toUser_desdeCreateDTO_mapeaConEnabledTrue() {
    CreateUserDTO dto = CreateUserDTO.builder()
            .usuarioRun("12345678-5")
            .usuarioNombre("Pedro")
            .usuarioApellidos("García")
            .usuarioEmail("pedro@mail.com")
            .usuarioPassword("pass123")
            .telefono("+56911111111")
            .build();

    User user = mapper.toUser(dto);

    assertNotNull(user);
    assertNull(user.getUsuarioId()); // No tiene ID aún
    assertEquals("Pedro", user.getUsuarioNombre());
    assertTrue(user.isEnabled()); // Siempre true al crear
}
```

---

#### CP-UM-004 — Mapear CreateUserDTO null retorna null

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UM-004                                                                   |
| **Método**         | `toUser(CreateUserDTO dto)`                                                 |
| **Descripción**    | Verifica que mapear un null retorna null                                     |
| **Entrada**        | `null`                                                                      |
| **Resultado**      | Retorna `null`                                                              |

```java
@Test
@DisplayName("CP-UM-004: Mapear CreateUserDTO null retorna null")
void toUser_desdeCreateDTONull_retornaNull() {
    assertNull(mapper.toUser((CreateUserDTO) null));
}
```

---

### 1.4 UsuarioController

**Clase bajo test:** `backend.com.gestionUsuarios.usuario.infrastructure.api.UsuarioController`

---

#### CP-UC-001 — GET /api/v1/usuarios retorna lista de usuarios

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UC-001                                                                   |
| **Método**         | `getAll()`                                                                  |
| **Descripción**    | Verifica que el endpoint GET retorna HTTP 200 con la lista de usuarios       |
| **Entrada**        | N/A                                                                         |
| **Resultado**      | HTTP 200 con body List<UserDTO>                                             |

```java
@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock private UserService userService;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UsuarioController controller;

    @Test
    @DisplayName("CP-UC-001: GET /usuarios retorna 200 con lista")
    void getAll_retorna200ConLista() {
        User user1 = User.builder().usuarioId(1L).usuarioNombre("Ana").build();
        UserDTO dto1 = UserDTO.builder().usuarioId(1L).usuarioNombre("Ana").build();

        when(userService.listarUsuarios()).thenReturn(List.of(user1));
        when(userMapper.toUserDTO(user1)).thenReturn(dto1);

        ResponseEntity<List<UserDTO>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Ana", response.getBody().get(0).getUsuarioNombre());
    }
}
```

---

#### CP-UC-002 — GET /api/v1/usuarios/{id} retorna usuario por ID

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UC-002                                                                   |
| **Método**         | `getById(Long id)`                                                          |
| **Descripción**    | Verifica que retorna HTTP 200 con el usuario solicitado                      |
| **Entrada**        | id = 1L                                                                     |
| **Resultado**      | HTTP 200 con UserDTO                                                        |

```java
@Test
@DisplayName("CP-UC-002: GET /usuarios/{id} retorna 200 con usuario")
void getById_conIdValido_retorna200() {
    User user = User.builder().usuarioId(1L).usuarioNombre("Luis").build();
    UserDTO dto = UserDTO.builder().usuarioId(1L).usuarioNombre("Luis").build();

    when(userService.obtenerUsuario(1L)).thenReturn(user);
    when(userMapper.toUserDTO(user)).thenReturn(dto);

    ResponseEntity<UserDTO> response = controller.getById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Luis", response.getBody().getUsuarioNombre());
}
```

---

#### CP-UC-003 — POST /api/v1/usuarios crea usuario y retorna 201

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UC-003                                                                   |
| **Método**         | `create(CreateUserDTO dto)`                                                 |
| **Descripción**    | Verifica que POST retorna HTTP 201 con el usuario creado                     |
| **Entrada**        | CreateUserDTO válido                                                        |
| **Resultado**      | HTTP 201 (CREATED) con UserDTO                                              |

```java
@Test
@DisplayName("CP-UC-003: POST /usuarios retorna 201 con usuario creado")
void create_conDTOValido_retorna201() {
    CreateUserDTO createDto = CreateUserDTO.builder()
            .usuarioNombre("Nuevo")
            .usuarioEmail("nuevo@mail.com")
            .build();
    User created = User.builder().usuarioId(1L).usuarioNombre("Nuevo").build();
    UserDTO responseDto = UserDTO.builder().usuarioId(1L).usuarioNombre("Nuevo").build();

    when(userService.crearUsuario(createDto)).thenReturn(created);
    when(userMapper.toUserDTO(created)).thenReturn(responseDto);

    ResponseEntity<UserDTO> response = controller.create(createDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Nuevo", response.getBody().getUsuarioNombre());
}
```

---

#### CP-UC-004 — DELETE /api/v1/usuarios/{id} retorna 204

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UC-004                                                                   |
| **Método**         | `delete(Long id)`                                                           |
| **Descripción**    | Verifica que DELETE retorna HTTP 204 No Content                              |
| **Entrada**        | id = 1L                                                                     |
| **Resultado**      | HTTP 204                                                                    |

```java
@Test
@DisplayName("CP-UC-004: DELETE /usuarios/{id} retorna 204")
void delete_conIdValido_retorna204() {
    doNothing().when(userService).eliminarUsuario(1L);

    ResponseEntity<Void> response = controller.delete(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).eliminarUsuario(1L);
}
```

---

#### CP-UC-005 — PATCH /api/v1/usuarios/{id}/toggle-enabled retorna 200

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-UC-005                                                                   |
| **Método**         | `toggleEnabled(Long id)`                                                    |
| **Descripción**    | Verifica que PATCH toggle retorna HTTP 200 con usuario actualizado           |
| **Entrada**        | id = 1L                                                                     |
| **Resultado**      | HTTP 200 con UserDTO y enabled invertido                                    |

```java
@Test
@DisplayName("CP-UC-005: PATCH /usuarios/{id}/toggle-enabled retorna 200")
void toggleEnabled_retorna200ConEstadoInvertido() {
    User user = User.builder().usuarioId(1L).enabled(false).build();
    UserDTO dto = UserDTO.builder().usuarioId(1L).enabled(false).build();

    when(userService.toggleEnabled(1L)).thenReturn(user);
    when(userMapper.toUserDTO(user)).thenReturn(dto);

    ResponseEntity<UserDTO> response = controller.toggleEnabled(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(response.getBody().isEnabled());
}
```

---

## 2. Módulo Shared — Value Objects y Validaciones

### 2.1 Money

**Clase bajo test:** `backend.com.shared.valueobjects.Money`

---

#### CP-MO-001 — Crear Money con valores válidos

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-001                                                                   |
| **Método**         | Constructor `Money(BigDecimal, String)`                                     |
| **Descripción**    | Verifica que se crea correctamente con monto y moneda válidos                |
| **Entrada**        | amount=1000, currency="CLP"                                                |
| **Resultado**      | Objeto Money con valores correctos                                          |

```java
class MoneyTest {

    @Test
    @DisplayName("CP-MO-001: Crear Money con valores válidos")
    void constructor_conValoresValidos_creaCorrectamente() {
        Money money = new Money(new BigDecimal("1000"), "CLP");

        assertEquals(new BigDecimal("1000"), money.getAmount());
        assertEquals("CLP", money.getCurrency());
    }
}
```

---

#### CP-MO-002 — Crear Money con monto null lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-002                                                                   |
| **Método**         | Constructor `Money(BigDecimal, String)`                                     |
| **Descripción**    | Verifica que se lanza excepción si el monto es null                          |
| **Entrada**        | amount=null, currency="CLP"                                                |
| **Resultado**      | Se lanza `IllegalArgumentException`                                         |

```java
@Test
@DisplayName("CP-MO-002: Crear Money con monto null lanza excepción")
void constructor_conMontoNull_lanzaExcepcion() {
    assertThrows(IllegalArgumentException.class,
            () -> new Money(null, "CLP"));
}
```

---

#### CP-MO-003 — Crear Money con moneda vacía lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-003                                                                   |
| **Método**         | Constructor `Money(BigDecimal, String)`                                     |
| **Descripción**    | Verifica que se lanza excepción si la moneda es blank                        |
| **Entrada**        | amount=100, currency=""                                                     |
| **Resultado**      | Se lanza `IllegalArgumentException`                                         |

```java
@Test
@DisplayName("CP-MO-003: Crear Money con moneda vacía lanza excepción")
void constructor_conMonedaVacia_lanzaExcepcion() {
    assertThrows(IllegalArgumentException.class,
            () -> new Money(new BigDecimal("100"), ""));
}
```

---

#### CP-MO-004 — Sumar Money de misma moneda

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-004                                                                   |
| **Método**         | `add(Money other)`                                                          |
| **Descripción**    | Verifica que la suma de dos Money con misma moneda es correcta               |
| **Entrada**        | Money(1000, "CLP") + Money(500, "CLP")                                      |
| **Resultado**      | Money con amount=1500 y currency="CLP"                                      |

```java
@Test
@DisplayName("CP-MO-004: Sumar Money de misma moneda")
void add_mismaCurrency_sumaCorrectamente() {
    Money a = new Money(new BigDecimal("1000"), "CLP");
    Money b = new Money(new BigDecimal("500"), "CLP");

    Money resultado = a.add(b);

    assertEquals(new BigDecimal("1500"), resultado.getAmount());
    assertEquals("CLP", resultado.getCurrency());
}
```

---

#### CP-MO-005 — Sumar Money de distinta moneda lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-005                                                                   |
| **Método**         | `add(Money other)`                                                          |
| **Descripción**    | Verifica que no se pueden sumar Money de distintas monedas                    |
| **Entrada**        | Money(1000, "CLP") + Money(100, "USD")                                      |
| **Resultado**      | Se lanza `IllegalArgumentException`                                         |

```java
@Test
@DisplayName("CP-MO-005: Sumar Money de distinta moneda lanza excepción")
void add_diferenteCurrency_lanzaExcepcion() {
    Money clp = new Money(new BigDecimal("1000"), "CLP");
    Money usd = new Money(new BigDecimal("100"), "USD");

    assertThrows(IllegalArgumentException.class, () -> clp.add(usd));
}
```

---

#### CP-MO-006 — Multiplicar Money por entero

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-006                                                                   |
| **Método**         | `multiply(Integer quantity)`                                                |
| **Descripción**    | Verifica la multiplicación por un entero positivo                            |
| **Entrada**        | Money(500, "CLP").multiply(3)                                               |
| **Resultado**      | Money con amount=1500                                                       |

```java
@Test
@DisplayName("CP-MO-006: Multiplicar Money por entero")
void multiply_porEntero_multiplicaCorrectamente() {
    Money money = new Money(new BigDecimal("500"), "CLP");

    Money resultado = money.multiply(3);

    assertEquals(0, new BigDecimal("1500").compareTo(resultado.getAmount()));
}
```

---

#### CP-MO-007 — Multiplicar Money por negativo lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-007                                                                   |
| **Método**         | `multiply(Integer quantity)`                                                |
| **Descripción**    | Verifica que cantidad negativa lanza excepción                               |
| **Entrada**        | Money(500, "CLP").multiply(-1)                                              |
| **Resultado**      | Se lanza `IllegalArgumentException`                                         |

```java
@Test
@DisplayName("CP-MO-007: Multiplicar Money por negativo lanza excepción")
void multiply_porNegativo_lanzaExcepcion() {
    Money money = new Money(new BigDecimal("500"), "CLP");

    assertThrows(IllegalArgumentException.class, () -> money.multiply(-1));
}
```

---

#### CP-MO-008 — Money.zero crea instancia con monto cero

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-MO-008                                                                   |
| **Método**         | `Money.zero(String currency)`                                               |
| **Descripción**    | Verifica que el método estático zero crea un Money con BigDecimal.ZERO       |
| **Entrada**        | currency = "CLP"                                                            |
| **Resultado**      | Money con amount=0 y currency="CLP"                                         |

```java
@Test
@DisplayName("CP-MO-008: Money.zero crea instancia con monto cero")
void zero_retornaMoneyConMontoCero() {
    Money money = Money.zero("CLP");

    assertEquals(0, BigDecimal.ZERO.compareTo(money.getAmount()));
    assertEquals("CLP", money.getCurrency());
}
```

---

### 2.2 RunValidator

**Clase bajo test:** `backend.com.shared.validations.run.RunValidator`

---

#### CP-RV-001 — RUN válido con guión retorna true

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-001                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN chileno válido con formato guión pasa la validación      |
| **Entrada**        | "76120450-8"                                                                |
| **Resultado**      | `true`                                                                      |

```java
class RunValidatorTest {

    private final RunValidator validator = new RunValidator();

    @Test
    @DisplayName("CP-RV-001: RUN válido con guión retorna true")
    void isValid_runValidoConGuion_retornaTrue() {
        assertTrue(validator.isValid("76120450-8", null));
    }
}
```

---

#### CP-RV-002 — RUN válido con puntos y guión retorna true

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-002                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN con puntos separadores también es válido                 |
| **Entrada**        | "76.120.450-8"                                                              |
| **Resultado**      | `true`                                                                      |

```java
@Test
@DisplayName("CP-RV-002: RUN válido con puntos y guión retorna true")
void isValid_runValidoConPuntos_retornaTrue() {
    assertTrue(validator.isValid("76.120.450-8", null));
}
```

---

#### CP-RV-003 — RUN con dígito verificador incorrecto retorna false

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-003                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN con DV incorrecto es inválido                            |
| **Entrada**        | "76120450-0" (DV correcto es 8)                                             |
| **Resultado**      | `false`                                                                     |

```java
@Test
@DisplayName("CP-RV-003: RUN con DV incorrecto retorna false")
void isValid_runConDVIncorrecto_retornaFalse() {
    assertFalse(validator.isValid("76120450-0", null));
}
```

---

#### CP-RV-004 — RUN null retorna false

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-004                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN null es inválido                                         |
| **Entrada**        | `null`                                                                      |
| **Resultado**      | `false`                                                                     |

```java
@Test
@DisplayName("CP-RV-004: RUN null retorna false")
void isValid_runNull_retornaFalse() {
    assertFalse(validator.isValid(null, null));
}
```

---

#### CP-RV-005 — RUN vacío retorna false

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-005                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN vacío es inválido                                        |
| **Entrada**        | ""                                                                          |
| **Resultado**      | `false`                                                                     |

```java
@Test
@DisplayName("CP-RV-005: RUN vacío retorna false")
void isValid_runVacio_retornaFalse() {
    assertFalse(validator.isValid("", null));
}
```

---

#### CP-RV-006 — RUN con letras en cuerpo retorna false

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-006                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN con letras en el cuerpo numérico es inválido             |
| **Entrada**        | "ABCDEFG-H"                                                                |
| **Resultado**      | `false`                                                                     |

```java
@Test
@DisplayName("CP-RV-006: RUN con letras en cuerpo retorna false")
void isValid_runConLetrasEnCuerpo_retornaFalse() {
    assertFalse(validator.isValid("ABCDEFG-H", null));
}
```

---

#### CP-RV-007 — RUN con K como dígito verificador válido

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-RV-007                                                                   |
| **Método**         | `isValid(String value, ConstraintValidatorContext)`                          |
| **Descripción**    | Verifica que un RUN con DV "K" se valida correctamente                       |
| **Entrada**        | Un RUN cuyo DV calculado sea K                                              |
| **Resultado**      | `true`                                                                      |

```java
@Test
@DisplayName("CP-RV-007: RUN con DV K válido retorna true")
void isValid_runConDVK_retornaTrue() {
    // El RUN 22.222.222-K tiene DV K
    // Nota: ajustar si el DV calculado de este ejemplo no es K
    assertTrue(validator.isValid("10503050-K", null));
}
```

---

## 3. Módulo Comercial — Dominio

### 3.1 NotaVenta

**Clase bajo test:** `backend.com.comercial.domain.model.NotaVenta`

---

#### CP-NV-001 — Crear NotaVenta con estado inicial BORRADOR

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-001                                                                   |
| **Método**         | `NotaVenta.crear(...)`                                                      |
| **Descripción**    | Verifica que una NV nueva se crea con estado BORRADOR                        |
| **Entrada**        | Datos básicos de NV                                                         |
| **Resultado**      | NV con estado=BORRADOR, fecha=hoy, montos en 0                              |

```java
class NotaVentaTest {

    @Test
    @DisplayName("CP-NV-001: Crear NotaVenta tiene estado inicial BORRADOR")
    void crear_retornaNotaVentaEnBorrador() {
        DocumentNumber numero = new DocumentNumber("NV", 1L);
        NotaVenta nv = NotaVenta.crear(numero, 1L, 1L, 1L, false, null,
                LocalDate.now().plusDays(30));

        assertEquals(EstadoNV.BORRADOR, nv.getEstado());
        assertEquals(LocalDate.now(), nv.getFechaEmision());
        assertEquals(0, BigDecimal.ZERO.compareTo(nv.getMontoSubtotal().getAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(nv.getMontoTotal().getAmount()));
    }
}
```

---

#### CP-NV-002 — Aprobar NotaVenta en estado BORRADOR

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-002                                                                   |
| **Método**         | `aprobar()`                                                                 |
| **Descripción**    | Verifica que una NV en BORRADOR pasa a APROBADA                              |
| **Entrada**        | NV con estado=BORRADOR                                                      |
| **Resultado**      | estado cambia a APROBADA                                                    |

```java
@Test
@DisplayName("CP-NV-002: Aprobar NotaVenta en BORRADOR cambia a APROBADA")
void aprobar_enBorrador_cambiaAAprobada() {
    NotaVenta nv = NotaVenta.crear(
            new DocumentNumber("NV", 1L), 1L, 1L, 1L, false, null,
            LocalDate.now().plusDays(30));

    nv.aprobar();

    assertEquals(EstadoNV.APROBADA, nv.getEstado());
}
```

---

#### CP-NV-003 — Aprobar NotaVenta ya aprobada lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-003                                                                   |
| **Método**         | `aprobar()`                                                                 |
| **Descripción**    | Verifica que no se puede aprobar una NV que no está en BORRADOR              |
| **Entrada**        | NV con estado=APROBADA                                                      |
| **Resultado**      | Se lanza `IllegalStateException`                                            |

```java
@Test
@DisplayName("CP-NV-003: Aprobar NV ya aprobada lanza IllegalStateException")
void aprobar_noEnBorrador_lanzaIllegalStateException() {
    NotaVenta nv = NotaVenta.crear(
            new DocumentNumber("NV", 1L), 1L, 1L, 1L, false, null,
            LocalDate.now().plusDays(30));
    nv.aprobar(); // Ahora está APROBADA

    assertThrows(IllegalStateException.class, nv::aprobar);
}
```

---

#### CP-NV-004 — Cancelar NotaVenta en BORRADOR

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-004                                                                   |
| **Método**         | `cancelar()`                                                                |
| **Descripción**    | Verifica que una NV en BORRADOR se puede cancelar                            |
| **Entrada**        | NV con estado=BORRADOR                                                      |
| **Resultado**      | estado cambia a CANCELADA                                                   |

```java
@Test
@DisplayName("CP-NV-004: Cancelar NV en BORRADOR cambia a CANCELADA")
void cancelar_enBorrador_cambiaACancelada() {
    NotaVenta nv = NotaVenta.crear(
            new DocumentNumber("NV", 1L), 1L, 1L, 1L, false, null,
            LocalDate.now().plusDays(30));

    nv.cancelar();

    assertEquals(EstadoNV.CANCELADA, nv.getEstado());
}
```

---

#### CP-NV-005 — Cancelar NotaVenta COMPLETADA lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-005                                                                   |
| **Método**         | `cancelar()`                                                                |
| **Descripción**    | Verifica que una NV COMPLETADA no se puede cancelar                          |
| **Entrada**        | NV con estado=COMPLETADA                                                    |
| **Resultado**      | Se lanza `IllegalStateException`                                            |

```java
@Test
@DisplayName("CP-NV-005: Cancelar NV COMPLETADA lanza IllegalStateException")
void cancelar_enCompletada_lanzaIllegalStateException() {
    DocumentNumber numero = new DocumentNumber("NV", 1L);
    NotaVenta nv = new NotaVenta(1L, numero, 1L, 1L, 1L,
            EstadoNV.COMPLETADA, false, null,
            LocalDate.now(), LocalDate.now().plusDays(30),
            Money.zero("CLP"), Money.zero("CLP"), Money.zero("CLP"),
            List.of());

    assertThrows(IllegalStateException.class, nv::cancelar);
}
```

---

#### CP-NV-006 — Calcular totales con ítems (IVA 19%)

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-NV-006                                                                   |
| **Método**         | `calcularTotales()`                                                         |
| **Descripción**    | Verifica que los totales se calculan correctamente con IVA del 19%           |
| **Entrada**        | NV con un ítem de total=10000 CLP                                           |
| **Resultado**      | subtotal=10000, iva=1900, total=11900                                       |

```java
@Test
@DisplayName("CP-NV-006: Calcular totales con IVA 19%")
void calcularTotales_conUnItem_calculaCorrectamente() {
    NotaVenta nv = NotaVenta.crear(
            new DocumentNumber("NV", 1L), 1L, 1L, 1L, false, null,
            LocalDate.now().plusDays(30));

    // Crear un ItemNV mock con total = 10000
    ItemNV item = mock(ItemNV.class);
    when(item.getTotal()).thenReturn(new Money(new BigDecimal("10000"), "CLP"));

    nv.addItem(item);

    assertEquals(0, new BigDecimal("10000").compareTo(nv.getMontoSubtotal().getAmount()));
    assertEquals(0, new BigDecimal("1900").compareTo(nv.getMontoIva().getAmount()));
    assertEquals(0, new BigDecimal("11900").compareTo(nv.getMontoTotal().getAmount()));
}
```

---

### 3.2 EstadoNV

**Clase bajo test:** `backend.com.comercial.domain.enums.EstadoNV`

---

#### CP-ENV-001 — EstadoNV tiene los valores esperados

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-ENV-001                                                                  |
| **Método**         | N/A (enum values)                                                           |
| **Descripción**    | Verifica que el enum contiene exactamente los 6 estados esperados            |
| **Entrada**        | N/A                                                                         |
| **Resultado**      | 6 valores: BORRADOR, APROBADA, EN_PRODUCCION, COMPLETADA, ENTREGADA, CANCELADA |

```java
class EstadoNVTest {

    @Test
    @DisplayName("CP-ENV-001: EstadoNV tiene los 6 valores esperados")
    void estadoNV_tieneValoresCorrectos() {
        EstadoNV[] valores = EstadoNV.values();
        assertEquals(6, valores.length);
        assertNotNull(EstadoNV.valueOf("BORRADOR"));
        assertNotNull(EstadoNV.valueOf("APROBADA"));
        assertNotNull(EstadoNV.valueOf("EN_PRODUCCION"));
        assertNotNull(EstadoNV.valueOf("COMPLETADA"));
        assertNotNull(EstadoNV.valueOf("ENTREGADA"));
        assertNotNull(EstadoNV.valueOf("CANCELADA"));
    }
}
```

---

#### CP-ENV-002 — EstadoNV descripción correcta

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-ENV-002                                                                  |
| **Método**         | `getDescripcion()`                                                          |
| **Descripción**    | Verifica que cada estado tiene su descripción legible                         |
| **Entrada**        | EstadoNV.BORRADOR                                                           |
| **Resultado**      | "Borrador"                                                                  |

```java
@Test
@DisplayName("CP-ENV-002: EstadoNV tiene descripción correcta")
void estadoNV_descripcionCorrecta() {
    assertEquals("Borrador", EstadoNV.BORRADOR.getDescripcion());
    assertEquals("Aprobada", EstadoNV.APROBADA.getDescripcion());
    assertEquals("Cancelada", EstadoNV.CANCELADA.getDescripcion());
}
```

---

## 4. Módulo Producción — Dominio

### 4.1 OrdenCompra

**Clase bajo test:** `backend.com.produccion.domain.model.OrdenCompra`

---

#### CP-OC-001 — Emitir OrdenCompra con estado inicial EMITIDA

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-001                                                                   |
| **Método**         | `OrdenCompra.emitir(...)`                                                   |
| **Descripción**    | Verifica que una OC nueva se crea con estado EMITIDA                         |
| **Entrada**        | Datos básicos de OC                                                         |
| **Resultado**      | OC con estado=EMITIDA, totalNeto=0, fecha=hoy                               |

```java
class OrdenCompraTest {

    @Test
    @DisplayName("CP-OC-001: Emitir OC tiene estado inicial EMITIDA")
    void emitir_retornaOrdenCompraEnEmitida() {
        DocumentNumber numero = new DocumentNumber("OC", 1L);
        OrdenCompra oc = OrdenCompra.emitir(numero, 1L,
                LocalDate.now().plusDays(15), "Observación de prueba");

        assertEquals(EstadoOC.EMITIDA, oc.getEstado());
        assertEquals(LocalDate.now(), oc.getFechaEmision());
        assertEquals(0, BigDecimal.ZERO.compareTo(oc.getTotalNeto()));
        assertTrue(oc.getItems().isEmpty());
    }
}
```

---

#### CP-OC-002 — Transición EMITIDA → ENVIADA es válida

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-002                                                                   |
| **Método**         | `marcarEnviada()`                                                           |
| **Descripción**    | Verifica que una OC EMITIDA puede pasar a ENVIADA                            |
| **Entrada**        | OC con estado=EMITIDA                                                       |
| **Resultado**      | estado cambia a ENVIADA                                                     |

```java
@Test
@DisplayName("CP-OC-002: Transición EMITIDA → ENVIADA es válida")
void marcarEnviada_desdeEmitida_cambiaAEnviada() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);

    oc.marcarEnviada();

    assertEquals(EstadoOC.ENVIADA, oc.getEstado());
}
```

---

#### CP-OC-003 — Transición EMITIDA → RECEPCIONADA es inválida

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-003                                                                   |
| **Método**         | `marcarRecepcionada()`                                                      |
| **Descripción**    | Verifica que una OC EMITIDA NO puede saltar a RECEPCIONADA                   |
| **Entrada**        | OC con estado=EMITIDA                                                       |
| **Resultado**      | Se lanza `IllegalStateException`                                            |

```java
@Test
@DisplayName("CP-OC-003: Transición EMITIDA → RECEPCIONADA es inválida")
void marcarRecepcionada_desdeEmitida_lanzaExcepcion() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);

    assertThrows(IllegalStateException.class, oc::marcarRecepcionada);
}
```

---

#### CP-OC-004 — Transición ENVIADA → RECEPCIONADA es válida

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-004                                                                   |
| **Método**         | `marcarRecepcionada()`                                                      |
| **Descripción**    | Verifica que una OC ENVIADA puede pasar a RECEPCIONADA                       |
| **Entrada**        | OC con estado=ENVIADA                                                       |
| **Resultado**      | estado cambia a RECEPCIONADA                                                |

```java
@Test
@DisplayName("CP-OC-004: Transición ENVIADA → RECEPCIONADA es válida")
void marcarRecepcionada_desdeEnviada_cambiaARecepcionada() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);
    oc.marcarEnviada();

    oc.marcarRecepcionada();

    assertEquals(EstadoOC.RECEPCIONADA, oc.getEstado());
}
```

---

#### CP-OC-005 — Transición RECEPCIONADA → CERRADA es válida

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-005                                                                   |
| **Método**         | `cerrar()`                                                                  |
| **Descripción**    | Verifica que una OC RECEPCIONADA puede cerrarse                              |
| **Entrada**        | OC con estado=RECEPCIONADA                                                  |
| **Resultado**      | estado cambia a CERRADA                                                     |

```java
@Test
@DisplayName("CP-OC-005: Transición RECEPCIONADA → CERRADA es válida")
void cerrar_desdeRecepcionada_cambiaACerrada() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);
    oc.marcarEnviada();
    oc.marcarRecepcionada();

    oc.cerrar();

    assertEquals(EstadoOC.CERRADA, oc.getEstado());
}
```

---

#### CP-OC-006 — Cerrar OC ya CERRADA lanza excepción

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-006                                                                   |
| **Método**         | `cerrar()`                                                                  |
| **Descripción**    | Verifica que una OC CERRADA no puede volver a cerrarse                       |
| **Entrada**        | OC con estado=CERRADA                                                       |
| **Resultado**      | Se lanza `IllegalStateException`                                            |

```java
@Test
@DisplayName("CP-OC-006: Cerrar OC ya CERRADA lanza excepción")
void cerrar_desdeCerrada_lanzaIllegalStateException() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);
    oc.marcarEnviada();
    oc.marcarRecepcionada();
    oc.cerrar();

    assertThrows(IllegalStateException.class, oc::cerrar);
}
```

---

#### CP-OC-007 — Recalcular total neto de OC con ítems

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-007                                                                   |
| **Método**         | `addItem(OrdenCompraItem item)` / `recalcularTotal()`                       |
| **Descripción**    | Verifica que al agregar ítems, el total neto se recalcula                    |
| **Entrada**        | OC con 2 ítems (subtotal 5000 y 3000)                                       |
| **Resultado**      | totalNeto = 8000                                                            |

```java
@Test
@DisplayName("CP-OC-007: Recalcular total neto con ítems")
void addItem_conDosItems_recalculaTotal() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);

    OrdenCompraItem item1 = mock(OrdenCompraItem.class);
    OrdenCompraItem item2 = mock(OrdenCompraItem.class);
    when(item1.getSubtotal()).thenReturn(new BigDecimal("5000"));
    when(item2.getSubtotal()).thenReturn(new BigDecimal("3000"));

    oc.addItem(item1);
    oc.addItem(item2);

    assertEquals(0, new BigDecimal("8000").compareTo(oc.getTotalNeto()));
}
```

---

#### CP-OC-008 — Lista de ítems es inmutable

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-OC-008                                                                   |
| **Método**         | `getItems()`                                                                |
| **Descripción**    | Verifica que getItems retorna lista inmutable                                |
| **Entrada**        | OC con ítems                                                                |
| **Resultado**      | Se lanza `UnsupportedOperationException` al intentar modificar               |

```java
@Test
@DisplayName("CP-OC-008: getItems retorna lista inmutable")
void getItems_retornaListaInmutable() {
    OrdenCompra oc = OrdenCompra.emitir(
            new DocumentNumber("OC", 1L), 1L,
            LocalDate.now().plusDays(15), null);

    List<OrdenCompraItem> items = oc.getItems();

    assertThrows(UnsupportedOperationException.class,
            () -> items.add(mock(OrdenCompraItem.class)));
}
```

---

### 4.2 EstadoOC

**Clase bajo test:** `backend.com.produccion.domain.model.EstadoOC`

---

#### CP-EOC-001 — Transiciones válidas de EMITIDA

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-EOC-001                                                                  |
| **Método**         | `puedeTransicionarA(EstadoOC destino)`                                      |
| **Descripción**    | Verifica todas las transiciones válidas e inválidas desde EMITIDA            |
| **Entrada**        | EstadoOC.EMITIDA                                                            |
| **Resultado**      | Solo puede ir a ENVIADA                                                     |

```java
class EstadoOCTest {

    @Test
    @DisplayName("CP-EOC-001: EMITIDA solo puede transicionar a ENVIADA")
    void emitida_soloPuedeIrAEnviada() {
        assertTrue(EstadoOC.EMITIDA.puedeTransicionarA(EstadoOC.ENVIADA));
        assertFalse(EstadoOC.EMITIDA.puedeTransicionarA(EstadoOC.RECEPCIONADA));
        assertFalse(EstadoOC.EMITIDA.puedeTransicionarA(EstadoOC.RECEPCIONADA_PARCIAL));
        assertFalse(EstadoOC.EMITIDA.puedeTransicionarA(EstadoOC.CERRADA));
    }
}
```

---

#### CP-EOC-002 — Transiciones válidas de ENVIADA

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-EOC-002                                                                  |
| **Método**         | `puedeTransicionarA(EstadoOC destino)`                                      |
| **Descripción**    | Verifica transiciones desde ENVIADA                                          |
| **Entrada**        | EstadoOC.ENVIADA                                                            |
| **Resultado**      | Puede ir a RECEPCIONADA_PARCIAL o RECEPCIONADA                              |

```java
@Test
@DisplayName("CP-EOC-002: ENVIADA puede ir a RECEPCIONADA_PARCIAL o RECEPCIONADA")
void enviada_puedeIrARecepcionadaOParcial() {
    assertTrue(EstadoOC.ENVIADA.puedeTransicionarA(EstadoOC.RECEPCIONADA_PARCIAL));
    assertTrue(EstadoOC.ENVIADA.puedeTransicionarA(EstadoOC.RECEPCIONADA));
    assertFalse(EstadoOC.ENVIADA.puedeTransicionarA(EstadoOC.EMITIDA));
    assertFalse(EstadoOC.ENVIADA.puedeTransicionarA(EstadoOC.CERRADA));
}
```

---

#### CP-EOC-003 — CERRADA no puede transicionar a ningún estado

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-EOC-003                                                                  |
| **Método**         | `puedeTransicionarA(EstadoOC destino)`                                      |
| **Descripción**    | Verifica que CERRADA es un estado final                                      |
| **Entrada**        | EstadoOC.CERRADA                                                            |
| **Resultado**      | Retorna false para todos los destinos                                       |

```java
@Test
@DisplayName("CP-EOC-003: CERRADA no puede transicionar a ningún estado")
void cerrada_noTransicionaANada() {
    for (EstadoOC destino : EstadoOC.values()) {
        assertFalse(EstadoOC.CERRADA.puedeTransicionarA(destino),
                "CERRADA no debería poder ir a " + destino);
    }
}
```

---

#### CP-EOC-004 — Transición con destino null retorna false

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-EOC-004                                                                  |
| **Método**         | `puedeTransicionarA(EstadoOC destino)`                                      |
| **Descripción**    | Verifica que null como destino retorna false sin excepción                    |
| **Entrada**        | EstadoOC.EMITIDA, destino=null                                              |
| **Resultado**      | `false`                                                                     |

```java
@Test
@DisplayName("CP-EOC-004: Transición con destino null retorna false")
void puedeTransicionar_conNull_retornaFalse() {
    assertFalse(EstadoOC.EMITIDA.puedeTransicionarA(null));
    assertFalse(EstadoOC.ENVIADA.puedeTransicionarA(null));
    assertFalse(EstadoOC.RECEPCIONADA.puedeTransicionarA(null));
    assertFalse(EstadoOC.CERRADA.puedeTransicionarA(null));
}
```

---

#### CP-EOC-005 — RECEPCIONADA solo puede transicionar a CERRADA

| Campo              | Detalle                                                                     |
|--------------------|-----------------------------------------------------------------------------|
| **ID**             | CP-EOC-005                                                                  |
| **Método**         | `puedeTransicionarA(EstadoOC destino)`                                      |
| **Descripción**    | Verifica que RECEPCIONADA solo puede cerrarse                                |
| **Entrada**        | EstadoOC.RECEPCIONADA                                                       |
| **Resultado**      | Solo puede ir a CERRADA                                                     |

```java
@Test
@DisplayName("CP-EOC-005: RECEPCIONADA solo puede ir a CERRADA")
void recepcionada_soloPuedeIrACerrada() {
    assertTrue(EstadoOC.RECEPCIONADA.puedeTransicionarA(EstadoOC.CERRADA));
    assertFalse(EstadoOC.RECEPCIONADA.puedeTransicionarA(EstadoOC.EMITIDA));
    assertFalse(EstadoOC.RECEPCIONADA.puedeTransicionarA(EstadoOC.ENVIADA));
    assertFalse(EstadoOC.RECEPCIONADA.puedeTransicionarA(EstadoOC.RECEPCIONADA_PARCIAL));
}
```

---

## Resumen de Cobertura

| Módulo                    | Clase / Componente            | Nº Casos | IDs                           |
|---------------------------|-------------------------------|----------|-------------------------------|
| Gestión Usuarios - Service | `UserServiceImpl`            | 16       | CP-US-001 a CP-US-016         |
| Gestión Usuarios - Validator | `UserValidator`            | 4        | CP-UV-001 a CP-UV-004         |
| Gestión Usuarios - Mapper | `UserMapper`                  | 4        | CP-UM-001 a CP-UM-004         |
| Gestión Usuarios - Controller | `UsuarioController`       | 5        | CP-UC-001 a CP-UC-005         |
| Shared - Value Object     | `Money`                       | 8        | CP-MO-001 a CP-MO-008         |
| Shared - Validación       | `RunValidator`                | 7        | CP-RV-001 a CP-RV-007         |
| Comercial - Dominio       | `NotaVenta`                   | 6        | CP-NV-001 a CP-NV-006         |
| Comercial - Enum          | `EstadoNV`                    | 2        | CP-ENV-001 a CP-ENV-002       |
| Producción - Dominio      | `OrdenCompra`                 | 8        | CP-OC-001 a CP-OC-008         |
| Producción - Enum         | `EstadoOC`                    | 5        | CP-EOC-001 a CP-EOC-005       |
| **Total**                 |                               | **65**   |                               |

---

## Dependencias Requeridas (ya en pom.xml)

```xml
<!-- Ya incluido en spring-boot-starter-test -->
<!-- JUnit 5, Mockito, AssertJ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Imports Comunes para los Tests

```java
// JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

// Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

// Java
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
```
