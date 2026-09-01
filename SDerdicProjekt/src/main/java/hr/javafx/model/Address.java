package hr.javafx.model;

public record Address(
        String streetName,
        String streetNumber,
        String postalCode,
        String cityName
) { }

