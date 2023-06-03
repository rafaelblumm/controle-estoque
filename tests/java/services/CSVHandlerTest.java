package services;

import models.Estoque;
import models.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {
    private static final String TEST_CSV_FILENAME = "test_registro";
    private static final String dirResources = System.getProperty("user.dir") + "\\tests\\resources\\";

    private Estoque estoque;
    private CSVHandler csvHandler;

    @BeforeEach
    public void setUp() {
        estoque = new Estoque();
        csvHandler = new CSVHandler(dirResources, estoque);
    }

    @Test
    public void leRegistroCsv_Invalido() {
        boolean result = csvHandler.leRegistro("INVALIDO");

        assertFalse(result);
        assertTrue(estoque.getProdutos().isEmpty());
    }

    @Test
    public void leRegistroCsv_Valido() {
        Produto p1 = new Produto("123", "Produto 1", 10, 5);
        Produto p2 = new Produto("456", "Produto 2", 20, 8);
        boolean result = csvHandler.leRegistro(TEST_CSV_FILENAME);

        assertTrue(result);
        assertFalse(estoque.getProdutos().isEmpty());
        assertEquals(p1, estoque.getProdutos().get("123"));
        assertEquals(p2, estoque.getProdutos().get("456"));
    }

    @Test
    public void gravaRegistroCsv_Valido() {
        Produto produto1 = new Produto("123", "Produto 1", 10, 5);
        Produto produto2 = new Produto("456", "Produto 2", 20, 8);
        estoque.insereProduto(produto1);
        estoque.insereProduto(produto2);

        String nomeArquivo = TEST_CSV_FILENAME + "_temp";
        boolean result = csvHandler.gravaRegistro(nomeArquivo);

        assertTrue(result);
        deleteTestCsvFile(nomeArquivo);
    }

    @Test
    public void gravaRegistroCsv_Invalido() {
        boolean result = csvHandler.gravaRegistro(TEST_CSV_FILENAME);

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
        assertTrue(csv.contains("123;Produto 1;10;5"));
        assertTrue(csv.contains("456;Produto 2;20;8"));
    }

    @Test
    public void montaCsv_Invalido() {
        String csv = csvHandler.montaCsv();

        assertNull(csv);
    }

    private void deleteTestCsvFile(String filename) {
        String path = dirResources + filename + ".csv";
        File file = new File(path);
        file.delete();
    }
}
