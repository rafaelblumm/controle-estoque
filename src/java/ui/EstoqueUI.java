package ui;

import models.*;
import services.CSVHandler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class EstoqueUI {
    private static final int OP_INSERIR = 1;
    private static final int OP_ADICIONAR_QNT = 2;
    private static final int OP_RETIRAR_QNT = 3;
    private static final int OP_LISTAR = 4;
    private static final int OP_LISTAR_ABAIXO_ESTOQUE = 5;
    private static final int OP_SAIR = 0;
    private static final int OP_INVALIDA = -1;

    private static Estoque estoque;
    private static CSVHandler csvHandler;
    private static Scanner scanner;
    private static boolean estoqueFoiModificado;

    public static void iniciar() {
        estoque = new Estoque();
        csvHandler = new CSVHandler(estoque);
        scanner = new Scanner(System.in);
        estoqueFoiModificado = false;

        // Carregar os dados do estoque a partir do arquivo CSV
        if (csvHandler.leRegistro())
            System.out.println("Dados do estoque foram carregados do arquivo CSV.");
        else
            System.out.println("Não foi possível carregar os dados do estoque do arquivo CSV.");

        int opcao;
        do {
            exibeMenu();
            opcao = aceitaOperacao();
            executaOperacao(opcao);
        } while (opcao != OP_SAIR);
    }

    private static void exibeMenu() {
        System.out.println("\n==== Menu ====");
        System.out.println("1. Inserir produto");
        System.out.println("2. Adicionar quantidade de produtos no estoque");
        System.out.println("3. Retirar quantidade de produtos no estoque");
        System.out.println("4. Listar todos os produtos");
        System.out.println("5. Listar produtos abaixo do estoque");
        System.out.println("0. Sair");
    }

    private static int aceitaOperacao() {
        int opcao;
        do {
            try {
                System.out.print("Digite a opção desejada: ");
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Digite um valor válido!");
                scanner.next();
                opcao = OP_INVALIDA;
            }
        } while (opcao == OP_INVALIDA);

        return opcao;
    }

    private static void executaOperacao(int opcao) {
        switch (opcao) {
            case OP_INSERIR -> inserirProduto();
            case OP_ADICIONAR_QNT -> adicionarQuantidade();
            case OP_RETIRAR_QNT -> retirarQuantidade();
            case OP_LISTAR -> listarProdutos();
            case OP_LISTAR_ABAIXO_ESTOQUE -> listarProdutosAbaixoEstoque();
            case OP_SAIR -> sair();
            default -> System.out.println("Opção inválida. Digite um número válido.");
        }
    }

    private static void inserirProduto() {
        String codigo = obterCodigo();

        if (estoque.consultaProduto(codigo) != null) {
            System.out.println("O produto já existe no estoque.");
            return;
        }

        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        int quantidade = obterQuantidade("Digite a quantidade de produtos: ");
        int quantidadeMinima = obterQuantidade("Digite a quantidade mínima do estoque: ");

        Produto produto = new Produto(codigo, nome, quantidade, quantidadeMinima);
        if (estoque.insereProduto(produto)) {
            System.out.println("Produto inserido no estoque com sucesso.");
            estoqueFoiModificado = true;
        } else
            System.out.println("Falha ao inserir produto no estoque.");
    }

    private static void adicionarQuantidade() {
        String codigo = obterCodigo();
        Produto produto = estoque.consultaProduto(codigo);

        if (produto == null) {
            System.out.println("O produto não existe no estoque.");
            return;
        }

        int quantidade = obterQuantidade("Digite a quantidade a ser adicionada: ");

        if (estoque.adicionaQuantidade(codigo, quantidade)) {
            System.out.println("Quantidade adicionada ao estoque com sucesso.");
            estoqueFoiModificado = true;
        } else
            System.out.println("Falha ao adicionar quantidade ao estoque.");
    }

    private static void retirarQuantidade() {
        String codigo = obterCodigo();
        Produto produto = estoque.consultaProduto(codigo);

        if (produto == null) {
            System.out.println("O produto não existe no estoque.");
            return;
        }

        int quantidade = obterQuantidade("Digite a quantidade a ser retirada: ");

        if (estoque.removeQuantidade(codigo, quantidade)) {
            System.out.println("Quantidade retirada do estoque com sucesso.");
            estoqueFoiModificado = true;
        } else
            System.out.println("Falha ao retirar quantidade do estoque.");
    }

    private static void listarProdutos() {
        String produtos = estoque.listaProdutos();

        if (produtos == null) {
            System.out.println("O estoque está vazio.");
        } else {
            System.out.println("==== Produtos no Estoque ====");
            System.out.println("Código    Nome                                 Quantidade   Quantidade Mínima");
            System.out.println(produtos);
        }
    }

    private static void listarProdutosAbaixoEstoque() {
        String produtosAbaixoEstoque = estoque.listaProdutosAbaixoEstoque();

        if (produtosAbaixoEstoque == null) {
            System.out.println("Não há produtos abaixo do estoque mínimo.");
        } else {
            System.out.println("==== Produtos Abaixo do Estoque ====");
            System.out.println("Código    Nome                                 Quantidade   Quantidade Mínima");
            System.out.println(produtosAbaixoEstoque);
        }
    }

    private static String obterCodigo() {
        String codigo;

        while (true) {
            System.out.println("Digite o código do produto: ");
            codigo = scanner.nextLine().trim();

            if (codigo.matches("[0-9]+"))
                break;

            System.out.println("Código inválido. Somente números são permitidos.");
        }

        return codigo;
    }

    private static int obterQuantidade(String mensagem) {
        int quantidade;

        while (true) {
            System.out.print(mensagem);
            try {
                quantidade = scanner.nextInt();
                scanner.nextLine();

                if (quantidade < 0) {
                    System.out.println("A quantidade não pode ser negativa.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Quantidade inválida. Digite um número válido.");
                scanner.nextLine();
            }
        }

        return quantidade;
    }

    private static void sair() {
        // Gravar os dados do estoque no arquivo CSV
        if (estoqueFoiModificado)
            System.out.println(csvHandler.gravaRegistro() ? "Dados do estoque foram salvos no arquivo CSV." :
                    "Não foi possível salvar os dados do estoque no arquivo CSV.");

        System.out.println("Encerrando o programa. Até logo!");
    }
}
