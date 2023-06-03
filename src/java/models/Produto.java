package models;

import java.util.Formatter;

public class Produto {
    private String codigo;
    private String nome;
    private int quantidade;
    private int quantidadeMinima;

    public Produto(String codigo, String nome, int quantidade, int quantidadeMinima) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    /**
     * Formata informações do produto para exibição em listagem.
     * @return (String) Linha de exibição formatada com dados do produto.
     */
    public String toListagem() {
        Formatter formatter = new Formatter();
        formatter.format("%-4s    %-40s  %-9d   %-9d", codigo, nome, quantidade, quantidadeMinima);

        String listagem = formatter.toString();
        formatter.close();

        return listagem;
    }

    /**
     * Formata dados do produto no formato CSV conforme o caractere definido por Estoque.
     * @return (String) Dados do produto em CSV.
     */
    public String toCsv() {
        String separador = ";";
        return codigo + separador +
                nome + separador +
                quantidade + separador +
                quantidadeMinima + separador;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                "\nNome: " + nome +
                "\nQuantidade: " + quantidade +
                "\nQuantidade mínima: " + quantidadeMinima;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(getClass()) &&
                toString().equals(obj.toString());
    }
}
