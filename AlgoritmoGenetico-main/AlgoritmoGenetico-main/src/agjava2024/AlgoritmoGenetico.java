package agjava2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGenetico {
    private ArrayList<Produto> produtos;
    private int tamPopulacao;
    private double capacidadePeso;
    private double larguraMaxima;
    private double alturaMaxima;
    private double profundidadeMaxima;
    private int probabilidadeMutacao;
    private int qtdeCruzamentos;
    private int numGeracoes;
    private int tamCromossomo = 0;

    private ArrayList<ArrayList<Integer>> populacao;

    public AlgoritmoGenetico(int populacao, double capacidadePeso, double larguraMaxima, double alturaMaxima, double profundidadeMaxima, int probabilidadeMutacao, int qtdeCruzamentos, int numGeracoes) {
        this.produtos = new ArrayList<>();
        this.populacao = new ArrayList<>();
        this.tamPopulacao = populacao;
        this.capacidadePeso = capacidadePeso;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
        this.probabilidadeMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numGeracoes = numGeracoes;
    }

    public void carregaArquivo(String fileName) {
        String csvFile = fileName;
        String line = "";
        String[] produto = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                produto = line.split(",");
                if (produto.length == 5) {
                    Produto novoProduto = new Produto();
                    novoProduto.setDescricao(produto[0]);
                    novoProduto.setPeso(Double.parseDouble(produto[1]));
                    novoProduto.setValor(Double.parseDouble(produto[2]));
                    novoProduto.setLargura(Double.parseDouble(produto[3]));
                    novoProduto.setAltura(Double.parseDouble(produto[4]));
                    produtos.add(novoProduto);
                    System.out.println(novoProduto);
                    this.tamCromossomo++;
                } else {
                    System.out.println("Linha ignorada (número incorreto de colunas): " + line);
                }
            }
            System.out.println("Tamanho do cromossomo: " + this.tamCromossomo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calcularPesoTotal(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0;
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                pesoTotal += produtos.get(i).getPeso();
            }
        }
        return pesoTotal;
    }

    private double calcularVolumeTotal(ArrayList<Integer> cromossomo) {
        double volumeTotal = 0;
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                Produto p = produtos.get(i);
                volumeTotal += p.getLargura() * p.getAltura() * p.getProfundidade();
            }
        }
        return volumeTotal;
    }

    private boolean verificaRestricoes(ArrayList<Integer> cromossomo) {
        double pesoTotal = 0;
        double volumeTotal = 0;
        for (int i = 0; i < this.tamCromossomo; i++) {
            if (cromossomo.get(i) == 1) {
                Produto p = produtos.get(i);
                pesoTotal += p.getPeso();
                volumeTotal += p.getLargura() * p.getAltura() * p.getProfundidade();
                if (p.getLargura() > larguraMaxima || p.getAltura() > alturaMaxima || p.getProfundidade() > profundidadeMaxima) {
                    return false; // Penaliza se qualquer dimensão exceder o máximo permitido
                }
            }
        }
        return pesoTotal <= this.capacidadePeso && volumeTotal <= (larguraMaxima * alturaMaxima * profundidadeMaxima);
    }

    private double calcularBeneficio(ArrayList<Integer> cromossomo) {
        return calcularVolumeTotal(cromossomo); // O benefício é dado pelo volume total
    }

    private double fitness(ArrayList<Integer> cromossomo) {
        if (!verificaRestricoes(cromossomo)) {
            return 0; // Penaliza soluções que não atendem às restrições
        }
        return calcularBeneficio(cromossomo);
    }

    private void inicializaPopulacao() {
        Random rand = new Random();
        for (int i = 0; i < tamPopulacao; i++) {
            ArrayList<Integer> cromossomo = new ArrayList<>();
            for (int j = 0; j < tamCromossomo; j++) {
                cromossomo.add(rand.nextInt(2)); // Adiciona 0 ou 1 aleatoriamente
            }
            populacao.add(cromossomo);
        }
    }

public void mostraPopulacao() {
    for (int i = 0; i < populacao.size(); i++) {
        ArrayList<Integer> cromossomo = populacao.get(i);
        double fitness = fitness(cromossomo);
        double pesoTotal = calcularPesoTotal(cromossomo);
        double volumeTotal = calcularVolumeTotal(cromossomo);

        boolean respeitaRestricoes = verificaRestricoes(cromossomo);
        String statusRestricoes = respeitaRestricoes ? "Respeita todas as restrições" : "Não respeita todas as restrições";

        System.out.println("Cromossomo " + i + ": " + cromossomo +
                "\nFitness: " + fitness +
                "\nPeso total: " + pesoTotal +
                "\nVolume total: " + volumeTotal +
                "\nStatus das restrições: " + statusRestricoes +
                "\n");
    }
}


    public void executar() {
        inicializaPopulacao();

        for (int geracao = 0; geracao < numGeracoes; geracao++) {
            System.out.println("Geração: " + geracao);
            mostraPopulacao();

            ArrayList<ArrayList<Integer>> novaPopulacao = new ArrayList<>();

            // Seleção e Crossover
            while (novaPopulacao.size() < tamPopulacao) {
                ArrayList<Integer> pai1 = selecionaPaiPorTorneio();
                ArrayList<Integer> pai2 = selecionaPaiPorTorneio();

                ArrayList<Integer> filho1 = new ArrayList<>(crossover(pai1, pai2));
                ArrayList<Integer> filho2 = new ArrayList<>(crossover(pai2, pai1));

                aplicaMutacao(filho1);
                aplicaMutacao(filho2);

                novaPopulacao.add(filho1);
                novaPopulacao.add(filho2);
            }

            // Substitui a população antiga pela nova
            populacao = new ArrayList<>(novaPopulacao);
        }
    }

    private ArrayList<Integer> selecionaPaiPorTorneio() {
        Random rand = new Random();
        ArrayList<Integer> melhorIndividuo = null;
        double melhorFitness = -1;

        // Seleciona dois indivíduos aleatórios e escolhe o melhor deles
        for (int i = 0; i < 2; i++) {
            ArrayList<Integer> individuo = populacao.get(rand.nextInt(tamPopulacao));
            double fitnessAtual = fitness(individuo);

            if (fitnessAtual > melhorFitness) {
                melhorIndividuo = individuo;
                melhorFitness = fitnessAtual;
            }
        }

        return melhorIndividuo;
    }

    private ArrayList<Integer> crossover(ArrayList<Integer> pai1, ArrayList<Integer> pai2) {
        Random rand = new Random();
        int pontoCorte = rand.nextInt(tamCromossomo);

        ArrayList<Integer> filho = new ArrayList<>();

        // Crossover de um ponto
        for (int i = 0; i < tamCromossomo; i++) {
            if (i < pontoCorte) {
                filho.add(pai1.get(i));
            } else {
                filho.add(pai2.get(i));
            }
        }

        return filho;
    }

    private void aplicaMutacao(ArrayList<Integer> cromossomo) {
        Random rand = new Random();

        for (int i = 0; i < tamCromossomo; i++) {
            if (rand.nextInt(100) < probabilidadeMutacao) { // Probabilidade de mutação
                cromossomo.set(i, 1 - cromossomo.get(i)); // Alterna entre 0 e 1
            }
        }
    }
}
