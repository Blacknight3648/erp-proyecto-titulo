package backend.com.shared.valueobjects;

import lombok.Value;

import java.util.Objects;

@Value
public class TenantId {
    Long value;

    public TenantId(Long value) {
        this.value = Objects.requireNonNull(value, "Tenant ID cannot be null");
    }
}
