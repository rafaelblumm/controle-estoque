package services;

import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVHandler {
    private static final String NOME_REGISTRO = "Registro-Produtos-Estoque";
    private static final String SEPARADOR_CSV = ";";
    private final String dirResources;
    private Estoque estoque;

    public CSVHandler(Estoque estoque) {
        this.estoque = estoque;
        this.dirResources = System.getProperty("user.dir") + "\\src\\resources\\";
    }

    public CSVHandler(String dirResources, Estoque estoque) {
        this.dirResources = dirResources;
        this.estoque = estoque;
    }

    /**
     * Lê registro de produtos de arquivo CSV.
     * @param nomeArquivo (String) Nome do arquivo a ser lido.
     * @return (boolean) Sucesso da operação.
     */
    public boolean leRegistro(String nomeArquivo) {
        String path = dirResources + nomeArquivo + ".csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String linha;
            String[] dados;
            Produto produto;

            while ((linha = reader.readLine()) != null) {
                dados = linha.split(SEPARADOR_CSV);
                produto = new Produto(dados[0], dados[1], Integer.parseInt(dados[2]), Integer.parseInt(dados[3]));
                estoque.getProdutos().put(produto.getCodigo(), produto);
            }

            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lê registro de produtos de arquivo CSV com nome padrão definido.
     * @return (boolean) Sucesso da operação.
     */
    public boolean leRegistro() {
        return leRegistro(NOME_REGISTRO);
    }

    /**
     * Grava itens do estoque em um arquivo CSV externo.
     * @param nomeArquivo (String) Nome do arquivo a ser registrado.
     * @return (boolean) Sucesso da operação.
     */
    public boolean gravaRegistro(String nomeArquivo) {
        String csv = montaCsv();
        if (csv == null)
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
     * @return (boolean) Sucesso da operação.
     */
    public boolean gravaRegistro() {
        return gravaRegistro(NOME_REGISTRO);
    }

    /**
     * Monta arquivo CSV com registro de produtos do estoque.
     * @return (String) Dados do arquivo.
     */
    public String montaCsv() {
        if (estoque.getProdutos().isEmpty())
            return null;

        StringBuilder csv = new StringBuilder();
        for (String key : estoque.getProdutos().keySet())
            csv.append(estoque.getProdutos().get(key).toCsv()).append("\n");

        return csv.toString();
    }
}
