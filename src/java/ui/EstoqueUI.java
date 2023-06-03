package ui;

import models.*;
import services.CSVHandler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class EstoqueUI {
    private static Estoque estoque;
    private static CSVHandler csvHandler;

    public static void iniciar() {
        estoque = new Estoque();
        csvHandler = new CSVHandler(estoque);

        // Carregar os dados do estoque a partir do arquivo CSV
        if (csvHandler.leRegistroCsv()) {
            System.out.println("Dados do estoque foram carregados do arquivo CSV.");
        } else {
            System.out.println("Não foi possível carregar os dados do estoque do arquivo CSV.");
        }

        exibirMenu();
    }

    private static void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            try {
                System.out.println("\n==== Menu ====");
                System.out.println("1. Inserir produto");
                System.out.println("2. Adicionar quantidade de produtos no estoque");
                System.out.println("3. Retirar quantidade de produtos no estoque");
                System.out.println("4. Listar todos os produtos");
                System.out.println("5. Listar produtos abaixo do estoque");
                System.out.println("0. Sair");

                System.out.print("Digite a opção desejada: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        inserirProduto(scanner);
                        break;
                    case 2:
                        adicionarQuantidade(scanner);
                        break;
                    case 3:
                        retirarQuantidade(scanner);
                        break;
                    case 4:
                        listarProdutos();
                        break;
                    case 5:
                        listarProdutosAbaixoEstoque();
                        break;
                    case 0:
                        sair();
                        break;
                    default:
                        System.out.println("Opção inválida. Digite um número válido.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Digite um valor válido!");
                scanner.next();
                opcao = -1;
            }
        } while (opcao != 0);
    }


    private static void inserirProduto(Scanner scanner) {
        System.out.print("Digite o código do produto: ");
        String codigo = scanner.nextLine();

        if (estoque.consultaProduto(codigo) != null) {
            System.out.println("O produto já existe no estoque.");
            return;
        }

        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();

        int quantidade = obterQuantidade(scanner, "Digite a quantidade de produtos: ");

        int quantidadeMinima = obterQuantidade(scanner, "Digite a quantidade mínima do estoque: ");

        Produto produto = new Produto(codigo, nome, quantidade, quantidadeMinima);
        if (estoque.insereProduto(produto)) {
            System.out.println("Produto inserido no estoque com sucesso.");
        } else {
            System.out.println("Falha ao inserir produto no estoque.");
        }
    }

    private static void adicionarQuantidade(Scanner scanner) {
        System.out.print("Digite o código do produto: ");
        String codigo = scanner.nextLine();

        Produto produto = estoque.consultaProduto(codigo);

        if (produto == null) {
            System.out.println("O produto não existe no estoque.");
            return;
        }

        int quantidade = obterQuantidade(scanner, "Digite a quantidade a ser adicionada: ");

        if (estoque.adicionaQuantidade(codigo, quantidade)) {
            System.out.println("Quantidade adicionada ao estoque com sucesso.");
        } else {
            System.out.println("Falha ao adicionar quantidade ao estoque.");
        }
    }

    private static void retirarQuantidade(Scanner scanner) {
        System.out.print("Digite o código do produto: ");
        String codigo = scanner.nextLine();

        Produto produto = estoque.consultaProduto(codigo);

        if (produto == null) {
            System.out.println("O produto não existe no estoque.");
            return;
        }

        int quantidade = obterQuantidade(scanner, "Digite a quantidade a ser retirada: ");

        if (estoque.removeQuantidade(codigo, quantidade)) {
            System.out.println("Quantidade retirada do estoque com sucesso.");
        } else {
            System.out.println("Falha ao retirar quantidade do estoque.");
        }
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

    private static int obterQuantidade(Scanner scanner, String mensagem) {
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
        if (csvHandler.gravaRegistroCsv()) {
            System.out.println("Dados do estoque foram salvos no arquivo CSV.");
        } else {
            System.out.println("Não foi possível salvar os dados do estoque no arquivo CSV.");
        }

        System.out.println("Encerrando o programa. Até logo!");
    }
}
