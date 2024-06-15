package com.minhden.minigraph.kernel.storage;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * This class describe the schema for Record
 */
public class RecordDescription implements Serializable {

    public static class Field implements Serializable {
        public final Type type;
        public final String name;

        public Field(Type t, String n) {
            this.type = t;
            this.name = n;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "type=" + type +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    Vector<Field> fields;

    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    @Serial
    private static final long serialVersionUID = 1L;

    public RecordDescription(Type[] types, String[] names) {
        fields = new Vector<>(types.length);

        for (int i = 0; i < types.length; i++) {
            fields.add(new Field(types[i], names[i]));
        }
    }

    public RecordDescription(Type[] types) {
        fields = new Vector<>(types.length);

        for (Type type : types) {
            fields.add(new Field(type, null));
        }
    }

    public int numFields() {
        return fields.size();
    }

    public String getFieldName(int i) throws NoSuchElementException {
        if (i < 0 || i > numFields()) {
            throw new NoSuchElementException();
        }
        return fields.get(i).name;
    }

    public Type getFieldType(int i) throws NoSuchElementException {
        if (i < 0 || i > numFields()) {
            throw new NoSuchElementException();
        }

        return fields.get(i).type;
    }

    public int getFieldIndexByName(String name) throws NoSuchElementException {
        if (name == null) {
            throw new NoSuchElementException();
        }

        for (int i = 0; i < numFields(); i++) {
            if (name.equals(getFieldName(i))) {
                return i;
            }
        }

        throw new NoSuchElementException();
    }

    /**
     *
     * @return the size (in bytes) of Records corresponding to the RecordDescription.
     */
    public int getByteSize() {
        int totalSize = 0;
        int numFields = numFields();
        Type tempType;

        for (int i = 0; i < numFields; i++) {
            tempType = getFieldType(i);
            totalSize += tempType.getLen();
        }
    }
}
