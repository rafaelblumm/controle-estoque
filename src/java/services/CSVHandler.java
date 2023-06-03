package services;

import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVHandler {

    private static final String NOME_REGISTRO = "Registro-Produtos-Estoque";
    private static final String SEPARADOR_CSV = ";";
    private Estoque estoque;

    public CSVHandler(Estoque estoque) {
        this.estoque = estoque;
    }

    /**
     * Lê registro de produtos de arquivo CSV.
     * @return (boolean) Sucesso da operação.
     */
    public boolean leRegistroCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_REGISTRO + ".csv", StandardCharsets.UTF_8))) {
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
     * Grava itens do estoque em um arquivo CSV externo.
     * @return (boolean) Sucesso da operação.
     */
    public boolean gravaRegistroCsv() {
        String csv = montaCsv();
        if (csv == null)
            return false;

        try (FileWriter writer = new FileWriter(NOME_REGISTRO + ".csv", StandardCharsets.UTF_8)) {
            writer.write(csv);
            writer.close();

            return true;
        } catch (IOException e) {
            return false;
        }
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

    /**
     * Testa se arquivo do registro de produtos do estoque existe.
     * @return (boolean)
     */
    private boolean testaSeArquivoExiste(){
        File csv = new File(NOME_REGISTRO + ".csv");
        return (csv.exists() && !csv.isDirectory());
    }
}
