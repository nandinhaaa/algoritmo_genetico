package agjava2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {

    private final List<Carga> cargas = new ArrayList<>();
    private final List<List<Integer>> populacao = new ArrayList<>();
    private final List<Integer> roletaVirtual = new ArrayList<>();
    private final int tamanhoCromossomo;
    private final int limitePeso;
    private final int probabilidadeMutacao;
    private final int qtdeCruzamentos;
    private final int numeroGeracoes;
    private final double larguraMaxima;
    private final double alturaMaxima;
    private final double profundidadeMaxima;

    public AlgoritmoGenetico(int populacao, double limitePeso, int probabilidadeMutacao, int qtdeCruzamentos,
                             int numeroGeracoes, double larguraMaxima, double alturaMaxima, double profundidadeMaxima) {
        this.tamanhoCromossomo = populacao;
        this.limitePeso = (int) limitePeso;
        this.probabilidadeMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
        this.larguraMaxima = larguraMaxima;
        this.alturaMaxima = alturaMaxima;
        this.profundidadeMaxima = profundidadeMaxima;
    }

    public void carregaArquivo(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            String[] dadosCarga;
            while ((linha = br.readLine()) != null) {
                dadosCarga = linha.split(",");
                adicionarCarga(criarCarga(dadosCarga));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo '" + nomeArquivo + "'", e);
        }
    }

    private Carga criarCarga(String[] dadosCarga) {
        if (dadosCarga == null || dadosCarga.length < 5) {
            throw new IllegalArgumentException("Dados insuficientes para criar carga!");
        }
        Carga novaCarga = new Carga();
        novaCarga.setDescricao(dadosCarga[0]);
        novaCarga.setPeso(Double.parseDouble(dadosCarga[1]));
        novaCarga.setLargura(Double.parseDouble(dadosCarga[2]));
        novaCarga.setAltura(Double.parseDouble(dadosCarga[3]));
        novaCarga.setProfundidade(Double.parseDouble(dadosCarga[4]));
        return novaCarga;
    }

    private void adicionarCarga(Carga novaCarga) {
        if (novaCarga == null) {
            throw new NullPointerException("Não há carga para adicionar!");
        }
        cargas.add(novaCarga);
    }

    public void executar() {
        criarPopulacao();
        for (int i = 0; i < numeroGeracoes; i++) {
            System.out.println("Geração: " + i);
            mostrarPopulacao();
            operadoresGeneticos();
            renovarPopulacao();
            System.out.println("");
        }
        mostrarCargaAviao(populacao.get(obterMelhor()));
        mostrarPioresAvaliados();
    }

    private void criarPopulacao() {
        for (int i = 0; i < tamanhoCromossomo; i++) {
            populacao.add(criarCromossomo());
        }
    }

    private List<Integer> criarCromossomo() {
        List<Integer> novoCromossomo = new ArrayList<>();
        for (int i = 0; i < cargas.size(); i++) {
            if (Math.random() < 0.6) {
                novoCromossomo.add(0);
            } else {
                novoCromossomo.add(1);
            }
        }
        return novoCromossomo;
    }

    private void mostrarPopulacao() {
        for (int i = 0; i < tamanhoCromossomo; i++) {
            System.out.println("Cromossomo [" + i + "]: " + populacao.get(i));
            System.out.println("> Avaliação: " + avaliarCromossomo(populacao.get(i)));
        }
    }

    private double avaliarCromossomo(List<Integer> cromossomo) {
        double massaTotal = 0;
        double volumeTotal = 0;
        boolean dimensaoInvalida = false;

        for (int i = 0; i < cargas.size(); i++) {
            if (cromossomo.get(i) == 1) {
                Carga cargaAnalisada = cargas.get(i);
                massaTotal += cargaAnalisada.getPeso(); // Calcula o peso total dos itens selecionados

                // Calcula o volume total considerando largura, altura e profundidade
                volumeTotal += cargaAnalisada.getLargura() * cargaAnalisada.getAltura() * cargaAnalisada.getProfundidade();

                // Verifica se algum item excede as dimensões máximas permitidas individualmente
                if (cargaAnalisada.getLargura() > larguraMaxima
                        || cargaAnalisada.getAltura() > alturaMaxima
                        || cargaAnalisada.getProfundidade() > profundidadeMaxima) {
                    dimensaoInvalida = true;
                }
            }
        }

        // Verifica se o conjunto de itens selecionados excede o peso máximo permitido ou as dimensões máximas
        if (massaTotal > limitePeso || dimensaoInvalida) {
            return 0D; // Penaliza soluções que não atendem às restrições
        }

        // O benefício da função de avaliação é dado pelo volume total de carga que se consegue carregar
        return volumeTotal; // Quanto maior o volume total, maior o benefício
    }

    private void operadoresGeneticos() {
        List<Integer> primeiroFilho;
        List<Integer> segundoFilho;
        List<List<Integer>> filhos;
        gerarRoleta();
        for (int i = 0; i < qtdeCruzamentos; i++) {
            filhos = cruzamento();
            primeiroFilho = filhos.get(0);
            segundoFilho = filhos.get(1);
            mutacao(primeiroFilho);
            mutacao(segundoFilho);
            populacao.add(primeiroFilho);
            populacao.add(segundoFilho);
        }
    }

    private void gerarRoleta() {
        List<Double> fitnessIndividuos = new ArrayList<>();
        double totalFitness = 0;
        for (int i = 0; i < tamanhoCromossomo; i++) {
            fitnessIndividuos.add(avaliarCromossomo(populacao.get(i)));
            totalFitness += fitnessIndividuos.get(i);
        }
        System.out.println("Soma total fitness: " + totalFitness);
        for (int i = 0; i < tamanhoCromossomo; i++) {
            double qtdPosicoes = (fitnessIndividuos.get(i) / totalFitness) * 1000;
            for (int j = 0; j <= qtdPosicoes; j++) {
                roletaVirtual.add(i);
            }
        }
    }

    private List<List<Integer>> cruzamento() {
        List<Integer> filho1 = new ArrayList<>();
        List<Integer> filho2 = new ArrayList<>();
        List<List<Integer>> filhos = new ArrayList<>();
        List<Integer> pai1, pai2;
        int indice_pai1, indice_pai2;
        indice_pai1 = roleta();
        indice_pai2 = roleta();
        pai1 = populacao.get(indice_pai1);
        pai2 = populacao.get(indice_pai2);
        Random r = new Random();
        int pos = r.nextInt(cargas.size());
        for (int i = 0; i <= pos; i++) {
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for (int i = pos + 1; i < cargas.size(); i++) {
            filho1.add(pai2.get(i));
            filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    private int roleta() {
        Random r = new Random();
        int selecionado = r.nextInt(roletaVirtual.size());
        return roletaVirtual.get(selecionado);
    }

    private void mutacao(List<Integer> filho) {
        Random r = new Random();
        int v = r.nextInt(100);
        if (v < probabilidadeMutacao) {
            int ponto = r.nextInt(cargas.size());
            filho.set(ponto, filho.get(ponto) == 1 ? 0 : 1); // Inverte o valor do gene
            System.out.println("Ocorreu mutação!");
        }
    }

    private void renovarPopulacao() {
        for (int i = 0; i < qtdeCruzamentos; i++) {
            populacao.remove(obterPior());
        }
    }

    private int obterPior() {
        int indicePior = 0;
        double pior;
        double nota;
        pior = avaliarCromossomo(populacao.get(0));
        for (int i = 1; i < tamanhoCromossomo; i++) {
            nota = avaliarCromossomo(populacao.get(i));
            if (nota < pior) {
                pior = nota;
                indicePior = i;
            }
        }
        return indicePior;
    }

    private void mostrarCargaAviao(List<Integer> resultado) {
        System.out.println(" Avaliação do melhor: " + avaliarCromossomo(resultado));
        System.out.println("As cargas que foram levadas no avião:");
        for (int i = 0; i < resultado.size(); i++) {
            if (resultado.get(i) == 1) {
                System.out.println(cargas.get(i));
            }
        }
    }

    private void mostrarPioresAvaliados() {
        System.out.println("\nCargas que não entram no avião devido às restrições:");
        for (int i = 0; i < tamanhoCromossomo; i++) {
            double avaliacao = avaliarCromossomo(populacao.get(i));
            if (avaliacao == 0) {
                System.out.println("Cromossomo [" + i + "]: " + populacao.get(i));
                System.out.println("> Avaliação: " + avaliacao);
            }
        }
    }

    private int obterMelhor() {
        int indiceMelhor = 0;
        double melhor;
        double nota;
        melhor = avaliarCromossomo(populacao.get(0));
        for (int i = 1; i < tamanhoCromossomo; i++) {
            nota = avaliarCromossomo(populacao.get(i));
            if (nota > melhor) {
                melhor = nota;
                indiceMelhor = i;
            }
        }
        return indiceMelhor;
    }
}
