package services;

import models.Estoque;
import models.Produto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {
    private static final String TEST_CSV_FILENAME = "test_registro";
    private static final String dirResources = System.getProperty("user.dir") + "\\tests\\resources\\";
    private static final String TEST_PATH = dirResources + TEST_CSV_FILENAME + ".csv";
    private Estoque estoque;
    private CSVHandler csvHandler;

    @BeforeAll
    static void initAll() throws IOException {
        FileWriter writer = new FileWriter(TEST_PATH);
        writer.write("123;Produto 1;10;5;\n");
        writer.write("456;Produto 2;20;8;\n");
        writer.close();
    }

    @BeforeEach
    public void setUp() {
        estoque = new Estoque();
        csvHandler = new CSVHandler(dirResources);
    }

    @Test
    public void leRegistroCsv_Invalido() {
        estoque = new Estoque(csvHandler.leRegistro("INVALIDO"));

        assertTrue(estoque.getProdutos().isEmpty());
    }

    @Test
    public void leRegistroCsv_Valido() {
        Produto p1 = new Produto("123", "Produto 1", 10, 5);
        Produto p2 = new Produto("456", "Produto 2", 20, 8);
        estoque = new Estoque(csvHandler.leRegistro(TEST_CSV_FILENAME));

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
        boolean result = csvHandler.gravaRegistro(estoque, nomeArquivo);

        assertTrue(result);
    }

    @Test
    public void gravaRegistroCsv_Invalido() {
        boolean result = csvHandler.gravaRegistro(estoque, TEST_CSV_FILENAME);

        assertFalse(result);
    }

    @Test
    public void montaCsv_Valido() {
        Produto produto1 = new Produto("123", "Produto 1", 10, 5);
        Produto produto2 = new Produto("456", "Produto 2", 20, 8);
        estoque.insereProduto(produto1);
        estoque.insereProduto(produto2);

        String csv = csvHandler.montaCsv(estoque.getProdutos());

        assertNotNull(csv);
        assertTrue(csv.contains("123;Produto 1;10;5"));
        assertTrue(csv.contains("456;Produto 2;20;8"));
    }

    @Test
    public void montaCsv_Invalido() {
        String csv = csvHandler.montaCsv(estoque.getProdutos());

        assertNull(csv);
    }

    @AfterAll
    static void tearDownAll(){
        File file = new File(TEST_PATH);
        file.delete();
    }
}
