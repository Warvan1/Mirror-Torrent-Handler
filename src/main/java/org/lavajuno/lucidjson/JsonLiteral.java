package org.lavajuno.lucidjson;

import org.lavajuno.lucidjson.util.Index;

import java.text.ParseException;

/**
 * Represents a JSON literal value (true/false/null).
 * Provides functionality for getting and setting the value.
 */
@SuppressWarnings("unused")
public class JsonLiteral extends JsonEntity {
    Boolean value;

    /**
     * Constructs a JsonLiteral with a value of null.
     */
    public JsonLiteral() { this.value = null; }

    /**
     * Constructs a JsonLiteral with the given value.
     * @param value Value of this JsonLiteral (true/false/null)
     */
    public JsonLiteral(Boolean value) { this.value = value; }

    /**
     * Constructs a JsonLiteral by parsing the input.
     * @param i Index of next character to parse
     * @param text JSON to parse
     */
    protected JsonLiteral(String text, Index i) throws ParseException {
        if(text.startsWith("true", i.pos)) {
            i.pos += 4;
            value = true;
        } else if(text.startsWith("false", i.pos)) {
            i.pos += 5;
            value = false;
        } else if(text.startsWith("null", i.pos)) {
            i.pos += 4;
            value = null;
        } else {
            throwParseError(text, i.pos, "Parsing literal, unknown value");
        }
    }

    /**
     * Sets the value of this JsonLiteral.
     * @param value Value of this JsonLiteral (true/false/null)
     */
    public void setValue(Boolean value) { this.value = value; }

    /**
     * Gets the value of this JsonLiteral.
     * @return Value of this JsonLiteral (true/false/null)
     */
    public Boolean getValue() { return value; }

    @Override
    public String toString() {
        if(value == null) { return "null"; }
        return value ? "true" : "false";
    }

    @Override
    protected String toString(int indent) { return this.toString(); }
}
