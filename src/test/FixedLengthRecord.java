package test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FixedLengthRecord {
    // Constants for field sizes
    private static final int INT_SIZE = Integer.BYTES; // 4 bytes
    private static final int DOUBLE_SIZE = Double.BYTES; // 8 bytes
    private static final int BOOLEAN_SIZE = 1; // 1 byte
    private static final int CHAR_ARRAY_SIZE = 20 * Character.BYTES; // 20 chars * 2 bytes = 40 bytes
    private static final int STRING_MAX_LENGTH = 30; // 30 characters
    private static final int STRING_SIZE = STRING_MAX_LENGTH * Character.BYTES; // 30 chars * 2 bytes = 60 bytes

    // Total record size
    private static final int RECORD_SIZE = INT_SIZE + DOUBLE_SIZE + BOOLEAN_SIZE + CHAR_ARRAY_SIZE + STRING_SIZE;

    // Fields
    private int intField;
    private double doubleField;
    private boolean booleanField;
    private char[] charArrayField; // Fixed size of 20
    private String stringField; // Max length of 30

    public FixedLengthRecord(int intField, double doubleField, boolean booleanField, char[] charArrayField, String stringField) {
        if (charArrayField.length != 20) {
            throw new IllegalArgumentException("charArrayField must be exactly 20 characters long");
        }
        if (stringField.length() > STRING_MAX_LENGTH) {
            throw new IllegalArgumentException("stringField length must be at most " + STRING_MAX_LENGTH + " characters");
        }
        this.intField = intField;
        this.doubleField = doubleField;
        this.booleanField = booleanField;
        this.charArrayField = charArrayField;
        this.stringField = stringField;
    }

    // Convert the record to a byte array
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
        buffer.putInt(intField);
        buffer.putDouble(doubleField);
        buffer.put((byte) (booleanField ? 1 : 0));
        for (char c : charArrayField) {
            buffer.putChar(c);
        }
        byte[] stringBytes = stringField.getBytes(StandardCharsets.UTF_16);
        buffer.put(stringBytes);
        if (stringBytes.length < STRING_SIZE) {
            buffer.put(new byte[STRING_SIZE - stringBytes.length]); // Padding if string is shorter
        }
        return buffer.array();
    }

    // Create a record from a byte array
    public static FixedLengthRecord fromByteArray(byte[] bytes) {
        if (bytes.length != RECORD_SIZE) {
            throw new IllegalArgumentException("Byte array length must be exactly " + RECORD_SIZE + " bytes");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int intField = buffer.getInt();
        double doubleField = buffer.getDouble();
        boolean booleanField = buffer.get() == 1;
        char[] charArrayField = new char[20];
        for (int i = 0; i < 20; i++) {
            charArrayField[i] = buffer.getChar();
        }
        byte[] stringBytes = new byte[STRING_SIZE];
        buffer.get(stringBytes);
        String stringField = new String(stringBytes, StandardCharsets.UTF_16).trim();
        return new FixedLengthRecord(intField, doubleField, booleanField, charArrayField, stringField);
    }

    @Override
    public String toString() {
        return "FixedLengthRecord{" +
                "intField=" + intField +
                ", doubleField=" + doubleField +
                ", booleanField=" + booleanField +
                ", charArrayField=" + Arrays.toString(charArrayField) +
                ", stringField='" + stringField + '\'' +
                '}';
    }

    public static void main(String[] args) {
        // Create a new record
        char[] charArray = "abcdefghijklmnopqrst".toCharArray();
        FixedLengthRecord record = new FixedLengthRecord(12345, 123.45, true, charArray, "Hello World");

        // Convert to byte array
        byte[] byteArray = record.toByteArray();
        System.out.println("Byte array: " + Arrays.toString(byteArray));

        // Convert back to record
        FixedLengthRecord newRecord = FixedLengthRecord.fromByteArray(byteArray);
        System.out.println("New record: " + newRecord);
    }
}
