package models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Estoque {
    private final Map<String,Produto> produtos;

    public Estoque() {
        this.produtos = new HashMap<>();
    }

    public Map<String, Produto> getProdutos() {
        return produtos;
    }

    /**
     * Insere novo produto no estoque.
     * @param produto (Produto) Novo produto a ser adicionado no estoque.
     * @return (boolean) Sucesso da operação.
     */
    public boolean insereProduto(Produto produto) {
        if (produto == null || produtos.containsKey(produto.getCodigo()))
            return false;

        produtos.put(produto.getCodigo(), produto);
        return true;
    }

    /**
     * Consulta produto no estoque.
     * @param codigo (String) Código do produto.
     * @return (Produto) Produto consultado.
     */
    public Produto consultaProduto(String codigo) {
        return produtos.get(codigo);
    }

    /**
     * Adiciona produtos no estoque.
     * @param codigo (String) Código do produto.
     * @param qnt (int) Quantidade a adicionar do produto.
     * @return (boolean) Sucesso da operação.
     */
    public boolean adicionaQuantidade(String codigo, int qnt) {
        if (qnt <= 0)
            return false;

        Produto produto = produtos.get(codigo);
        produto.setQuantidade(produto.getQuantidade() + qnt);
        return true;
    }

    /**
     * Remove produtos no estoque.
     * @param codigo (String) Código do produto.
     * @param qnt (int) Quantidade a ser removida do produto.
     * @return (boolean) Sucesso da operação.
     */
    public boolean removeQuantidade(String codigo, int qnt) {
        Produto produto = produtos.get(codigo);
        int novaQuantidade = produto.getQuantidade() - qnt;

        if (novaQuantidade < 0 || qnt <= 0)
            return false;

        produto.setQuantidade(novaQuantidade);
        return true;
    }

    /**
     * Lista dados de todos os produtos do estoque.
     * @return (String) Dados dos produtos.
     */
    public String listaProdutos() {
        if (produtos.isEmpty())
            return null;

        String [] codigos = produtos.keySet().toArray(new String[0]);
        Arrays.sort(codigos);

        StringBuilder listagem = new StringBuilder();
        for (String key : codigos)
            listagem.append(produtos.get(key).toListagem()).append("\n");

        return listagem.toString();
    }

    /**
     * Lista dados de todos os produtos do estoque cuja quantidade é menor que a quantidade mínima.
     * @return (String) Dados dos produtos.
     */
    public String listaProdutosAbaixoEstoque() {
        if (produtos.isEmpty())
            return null;

        String [] codigos = produtos.keySet().toArray(new String[0]);
        Arrays.sort(codigos);

        Produto produto;
        StringBuilder listagem = new StringBuilder();
        for (String key : codigos) {
            produto = produtos.get(key);
            if (produto.getQuantidade() < produto.getQuantidadeMinima())
                listagem.append(produto.toListagem()).append("\n");
        }

        return listagem.isEmpty() ? null : listagem.toString();
    }
}