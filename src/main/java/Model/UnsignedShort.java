package Model;

import java.util.Objects;

public class UnsignedShort {
    private char data;

    UnsignedShort(short s){
        data = (char)s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnsignedShort that = (UnsignedShort) o;
        return data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return ""+(int)data;
    }
}
