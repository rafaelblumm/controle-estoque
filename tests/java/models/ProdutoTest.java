package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdutoTest {
    private Produto p;

    @BeforeEach
    public void setUp() {
        p = new Produto("0001", "Teclado mecanico", 13, 30);
    }

    @Test
    public void formataDadosParaListagem() {
        String esperado = "0001    Teclado mecanico                          13          30       ";
        assertEquals(esperado, p.toListagem());
    }

    @Test
    public void formataDadosParaCsv() {
        String esperado = "0001;Teclado mecanico;13;30";
        assertEquals(esperado, p.toCsv());
    }
}
