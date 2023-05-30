package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstoqueTest {
    private Estoque estoque;
    private Produto p;

    @BeforeEach
    public void inicializa() {
        estoque = new Estoque();
        p = new Produto("0001", "Cuia de chimarrao", 120, 30);
    }

    @Test
    public void insereProdutoValido() {
        assertTrue(estoque.insereProduto(p));
    }

    @Test
    public void insereProdutoJaExistente() {
        estoque.insereProduto(p);
        assertFalse(estoque.insereProduto(p));
    }

    @Test
    public void insereProdutoNull() {
        assertFalse(estoque.insereProduto(null));
    }

    @Test
    public void insereProdutoValido20() {
        for (int i = 1; i <= 20; i++) {
            p = new Produto(String.valueOf(i), "Produto #" + i, 200, 80);
            assertTrue(estoque.insereProduto(p));
        }

        assertEquals(20, estoque.getProdutos().size());
    }

    @Test
    public void insereProdutoValido50() {
        for (int i = 1; i <= 50; i++) {
            p = new Produto(String.valueOf(i), "Produto #" + i, 200, 80);
            assertTrue(estoque.insereProduto(p));
        }

        assertEquals(50, estoque.getProdutos().size());
    }

    @Test
    public void insereProdutoValido1000() {
        for (int i = 1; i <= 1000; i++) {
            p = new Produto(String.valueOf(i), "Produto #" + i, 200, 80);
            assertTrue(estoque.insereProduto(p));
        }

        assertEquals(1000, estoque.getProdutos().size());
    }

    @Test
    public void consultaProdutoValido() {
        estoque.insereProduto(p);

        Produto p1 = estoque.consultaProduto(p.getCodigo());
        assertEquals(p, p1);
    }

    @Test
    public void consultaProdutoInexistente() {
        assertNull(estoque.consultaProduto("0002"));
    }

    @Test
    public void adicionaQuantidadeValida() {
        estoque.insereProduto(p);

        assertTrue(estoque.adicionaQuantidade(p.getCodigo(), 50));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(170, quantidade);
    }

    @Test
    public void adicionaQuantidadeZerada() {
        estoque.insereProduto(p);

        assertFalse(estoque.adicionaQuantidade(p.getCodigo(), 0));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(120, quantidade);
    }

    @Test
    public void adicionaQuantidadeNegativa() {
        estoque.insereProduto(p);

        assertFalse(estoque.adicionaQuantidade(p.getCodigo(), -10));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(120, quantidade);
    }

    @Test
    public void removeQuantidadeValida() {
        estoque.insereProduto(p);

        assertTrue(estoque.removeQuantidade(p.getCodigo(), 50));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(70, quantidade);
    }

    @Test
    public void removeQuantidadeMaiorQueEstoque() {
        estoque.insereProduto(p);

        assertFalse(estoque.removeQuantidade(p.getCodigo(), 800));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(120, quantidade);
    }

    @Test
    public void removeQuantidadeZerada() {
        estoque.insereProduto(p);

        assertFalse(estoque.removeQuantidade(p.getCodigo(), 0));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(120, quantidade);
    }

    @Test
    public void removeQuantidadeNegativa() {
        estoque.insereProduto(p);

        assertFalse(estoque.removeQuantidade(p.getCodigo(), -12));

        int quantidade = estoque.consultaProduto(p.getCodigo()).getQuantidade();
        assertEquals(120, quantidade);
    }

    @Test
    public void listaProdutosValidos() {
        Produto p2 = new Produto("0002", "Bomba de chimarrao", 80, 12);
        Produto p3 = new Produto("0003", "Pacote de erva mate (1kg)", 30, 60);

        estoque.insereProduto(p);
        estoque.insereProduto(p2);
        estoque.insereProduto(p3);

        String listagemEsperada = "0001    Cuia de chimarrao                         120         30       \n" +
                                  "0002    Bomba de chimarrao                        80          12       \n" +
                                  "0003    Pacote de erva mate (1kg)                 30          60       \n";

        assertEquals(listagemEsperada, estoque.listaProdutos());
    }

    @Test
    public void listaProdutosEstoqueVazio() {
        assertNull(estoque.listaProdutos());
    }

    @Test
    public void listaProdutosAbaixoEstoqueValidos() {
        Produto p2 = new Produto("0002", "Bomba de chimarrao", 5, 12);
        Produto p3 = new Produto("0003", "Pacote de erva mate (1kg)", 60, 60);
        Produto p4 = new Produto("0004", "Mateira", 2, 10);

        estoque.insereProduto(p);
        estoque.insereProduto(p2);
        estoque.insereProduto(p3);
        estoque.insereProduto(p4);

        String listagemEsperada = "0002    Bomba de chimarrao                        5           12       \n" +
                                  "0004    Mateira                                   2           10       \n";

        assertEquals(listagemEsperada, estoque.listaProdutosAbaixoEstoque());
    }

    @Test
    public void listaProdutosAbaixoEstoqueListagemVazia() {
            Produto p2 = new Produto("0002", "Bomba de chimarrao", 80, 12);
            Produto p3 = new Produto("0003", "Pacote de erva mate (1kg)", 60, 60);

            estoque.insereProduto(p);
            estoque.insereProduto(p2);
            estoque.insereProduto(p3);

            assertNull(estoque.listaProdutosAbaixoEstoque());
    }

    @Test
    public void listaProdutosAbaixoEstoqueVazio() {
        assertNull(estoque.listaProdutosAbaixoEstoque());
    }
}
