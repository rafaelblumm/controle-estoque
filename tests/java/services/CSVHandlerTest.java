package services;

import models.Estoque;
import models.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {
    private static final String TEST_CSV_FILENAME = "test_registro.csv";

    private Estoque estoque;
    private CSVHandler csvHandler;

    @BeforeEach
    public void setUp() {
        estoque = new Estoque();
        csvHandler = new CSVHandler(estoque);
    }

    @Test
    public void leRegistroCsv_Invalido() throws IOException {
        boolean result = csvHandler.leRegistroCsv("INVALIDO.CSV");

        assertFalse(result);
        assertTrue(estoque.getProdutos().isEmpty());
    }

    @Test
    public void leRegistroCsv_Valido() throws IOException {
        createTestCsvFile(TEST_CSV_FILENAME);

        boolean result = csvHandler.leRegistroCsv(TEST_CSV_FILENAME);

        assertTrue(result);
        assertFalse(estoque.getProdutos().isEmpty());

        deleteTestCsvFile(TEST_CSV_FILENAME);
    }

    @Test
    public void gravaRegistroCsv_Valido() throws IOException {
        Produto produto1 = new Produto("123", "Produto 1", 10, 5);
        Produto produto2 = new Produto("456", "Produto 2", 20, 8);
        estoque.insereProduto(produto1);
        estoque.insereProduto(produto2);

        createTestCsvFile(TEST_CSV_FILENAME);

        boolean result = csvHandler.gravaRegistroCsv(TEST_CSV_FILENAME);

        assertTrue(result);
        deleteTestCsvFile(TEST_CSV_FILENAME);

    }

    @Test
    public void gravaRegistroCsv_Invalido() {
        boolean result = csvHandler.gravaRegistroCsv(TEST_CSV_FILENAME);

        assertFalse(result);
    }

    @Test
    public void montaCsv_Valido() {
        Produto produto1 = new Produto("123", "Produto 1", 10, 5);
        Produto produto2 = new Produto("456", "Produto 2", 20, 8);
        estoque.insereProduto(produto1);
        estoque.insereProduto(produto2);

        String csv = csvHandler.montaCsv();

        assertNotNull(csv);
        assertTrue(csv.contains("123;Produto 1;10;5;"));
        assertTrue(csv.contains("456;Produto 2;20;8;"));
    }

    @Test
    public void montaCsv_Invalido() {
        String csv = csvHandler.montaCsv();

        assertNull(csv);
    }

    private void createTestCsvFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("123;Produto 1;10;5;\n");
        writer.write("456;Produto 2;20;8;\n");
        writer.close();
    }

    private void deleteTestCsvFile(String filename) {
        File file = new File(filename);
        file.delete();
    }
}
