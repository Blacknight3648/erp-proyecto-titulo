package backend.com.produccion.domain.enums;

import backend.com.shared.exception.ForbiddenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("AccionCosteo · política de roles")
class AccionCosteoTest {

    @Test
    @DisplayName("roles autorizados pueden aprobar/rechazar (case-insensitive)")
    void rolesAutorizados() {
        assertThat(AccionCosteo.APROBAR.permite("JEFE_PRODUCCION")).isTrue();
        assertThat(AccionCosteo.APROBAR.permite("jefe_comercial")).isTrue();
        assertThat(AccionCosteo.RECHAZAR.permite(" JEFE_ADMIN ")).isTrue();
        assertThatCode(() -> AccionCosteo.APROBAR.validarRol("JEFE_PRODUCCION")).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("roles no autorizados o nulos son rechazados (403)")
    void rolesNoAutorizados() {
        assertThat(AccionCosteo.APROBAR.permite("VENDEDOR")).isFalse();
        assertThat(AccionCosteo.RECHAZAR.permite("OPERARIO_PRODUCCION")).isFalse();
        assertThat(AccionCosteo.APROBAR.permite(null)).isFalse();
        assertThatThrownBy(() -> AccionCosteo.RECHAZAR.validarRol("VENDEDOR"))
                .isInstanceOf(ForbiddenException.class);
    }
}
