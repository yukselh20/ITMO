package com.spaceport.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a spaceship in the docking manager.
 * Implements Comparable to sort by tonnage (descending).
 * Implements Serializable for network transfer.
 */
public class Spaceship implements Comparable<Spaceship>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final double tonnage;
    private final String type;
    private final int ownerId;
    private final double x;
    private final double y;

    public Spaceship(String id, String name, double tonnage, String type, int ownerId, double x, double y) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        try {
            int idNum = Integer.parseInt(id);
            if (idNum <= 0) {
                throw new IllegalArgumentException("ID must be a positive integer.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a numeric value.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (tonnage <= 0) {
            throw new IllegalArgumentException("Tonnage must be positive.");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or empty.");
        }

        this.id = id;
        this.name = name;
        this.tonnage = tonnage;
        this.type = type;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTonnage() {
        return tonnage;
    }

    public String getType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int compareTo(Spaceship other) {
        return Double.compare(other.tonnage, this.tonnage);
    }

    @Override
    public String toString() {
        return String.format("Ship[ID=%s, Name='%s', Tonnage=%.1f, Type='%s', OwnerID=%d, X=%.1f, Y=%.1f]", id, name,
                tonnage, type,
                ownerId, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Spaceship spaceship = (Spaceship) o;
        return Objects.equals(id, spaceship.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
