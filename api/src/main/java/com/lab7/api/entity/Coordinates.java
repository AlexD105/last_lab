package com.lab7.api.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {

    @NotNull(message = "X can't be null")
    private Double x; // Поле не может быть null
    private int y;

    public Coordinates(Double x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y && x.equals(that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "; " + y + "]";
    }
}