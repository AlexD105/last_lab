package com.lab7.api.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class DragonCave implements Serializable {


    private long depth;

    @NotNull(message = "Treasures can't be null")
    @Min(value = 1, message = "Treasures can't be less than 1")
    private Long numberOfTreasures; //Поле не может быть null, Значение поля должно быть больше 0

    public DragonCave(long depth, Long numberOfTreasures) {
        this.depth = depth;
        this.numberOfTreasures = numberOfTreasures;
    }

    public DragonCave() {
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public Long getNumberOfTreasures() {
        return numberOfTreasures;
    }

    public void setNumberOfTreasures(Long numberOfTreasures) {
        this.numberOfTreasures = numberOfTreasures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DragonCave)) return false;
        DragonCave that = (DragonCave) o;
        return depth == that.depth && numberOfTreasures.equals(that.numberOfTreasures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depth, numberOfTreasures);
    }

    @Override
    public String toString() {
        return "[" + depth + "; " + numberOfTreasures + "]";
    }
}