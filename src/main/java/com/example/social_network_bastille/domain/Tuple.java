package com.example.social_network_bastille.domain;

import java.util.Objects;

public class Tuple<E, F> {
    protected E id1;
    protected F id2;

    public Tuple(E id1, F id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public E getId1() {
        return id1;
    }

    public F getId2() {
        return id2;
    }

    @Override
    public String toString() {
        return "ID Friend1: " + id1 + " ID Friend2: " + id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple<?, ?> tuple)) return false;
        return Objects.equals(id1, tuple.id1) && Objects.equals(id2, tuple.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
