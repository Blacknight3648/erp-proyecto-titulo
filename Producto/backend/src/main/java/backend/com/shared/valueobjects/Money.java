package backend.com.shared.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {

    private final BigDecimal amount;
    private final String currency;

    @JsonCreator
    public Money(@JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser null");
        }

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }

        this.amount = amount;
        this.currency = currency;
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money multiply(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("La cantidad debe ser no negativa");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("El factor no puede ser null");
        }
        return new Money(this.amount.multiply(factor), this.currency);
    }

    public Money add(Money other) {
        if (other == null) {
            return this;
        }
        if (!this.currency.equals(other.getCurrency())) {
            throw new IllegalArgumentException("No se pueden sumar montos de distintas monedas: "
                    + this.currency + " y " + other.getCurrency());
        }
        return new Money(this.amount.add(other.getAmount()), this.currency);
    }
}
