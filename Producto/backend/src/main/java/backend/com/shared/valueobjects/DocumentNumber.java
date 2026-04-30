package backend.com.shared.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DocumentNumber {

    private final String value;

    @JsonCreator
    public DocumentNumber(@JsonProperty("value") String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El número de documento no puede estar vacío");
        }
        this.value = value;
    }

    public DocumentNumber(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("El número de documento no puede ser nulo");
        }
        this.value = String.valueOf(value);
    }

    public DocumentNumber(DocumentNumber other) {
        this.value = other != null ? other.getValue() : null;
    }

    @Override
    public String toString() {
        return value;
    }
}
