package services;

import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CSVHandler {
    private static final String NOME_REGISTRO = "Registro-Produtos-Estoque";
    private static final String SEPARADOR_CSV = ";";
    private final String dirResources;

    public CSVHandler() {
        this.dirResources = System.getProperty("user.dir") + "\\src\\resources\\";
    }

    public CSVHandler(String dirResources) {
        this.dirResources = dirResources;
    }

    /**
     * Lê registro de produtos de arquivo CSV.
     * @param nomeArquivo (String) Nome do arquivo a ser lido.
     * @return (HashMap<String, Produto>) Produtos registrados.
     */
    public Map<String, Produto> leRegistro(String nomeArquivo) {
        String path = dirResources + nomeArquivo + ".csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String linha;
            String[] dados;
            Produto produto;
            Map<String, Produto> estoque = new HashMap<>();

            while ((linha = reader.readLine()) != null) {
                dados = linha.split(SEPARADOR_CSV);
                produto = new Produto(dados[0], dados[1], Integer.parseInt(dados[2]), Integer.parseInt(dados[3]));
                estoque.put(produto.getCodigo(), produto);
            }

            reader.close();
            return estoque.isEmpty() ? null : estoque;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Lê registro de produtos de arquivo CSV com nome padrão definido.
     * @return (Map<String, Produto>) Produtos registrados.
     */
    public Map<String, Produto> leRegistro() {
        return leRegistro(NOME_REGISTRO);
    }

    /**
     * Grava itens do estoque em um arquivo CSV externo.
     * @param estoque (Estoque)
     * @param nomeArquivo (String) Nome do arquivo a ser registrado.
     * @return (boolean) Sucesso da operação.
     */
    public boolean gravaRegistro(Estoque estoque, String nomeArquivo) {
        String csv = montaCsv(estoque.getProdutos());
        if (csv == null)
            return false;

        File file = new File(dirResources);
        if (!file.exists())
            if (!file.mkdir())
                return false;

        String path = dirResources + nomeArquivo + ".csv";
        try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            writer.write(csv);
            writer.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Grava itens do estoque em um arquivo CSV externo com nome padrão definido.
     * @param estoque (Estoque)
     * @return (boolean) Sucesso da operação.
     */
    public boolean gravaRegistro(Estoque estoque) {
        return gravaRegistro(estoque, NOME_REGISTRO);
    }

    /**
     * Monta arquivo CSV com registro de produtos do estoque.
     * @param produtos (Map<String, Produto>)
     * @return (String) Dados do arquivo.
     */
    public String montaCsv(Map<String, Produto> produtos) {
        if (produtos.isEmpty())
            return null;

        StringBuilder csv = new StringBuilder();
        for (String key : produtos.keySet())
            csv.append(produtos.get(key).toCsv()).append("\n");

        return csv.toString();
    }
}
