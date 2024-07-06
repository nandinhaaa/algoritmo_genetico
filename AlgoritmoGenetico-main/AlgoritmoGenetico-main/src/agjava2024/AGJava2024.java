package agjava2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AGJava2024 {

    public static void main(String[] args) {
        int populacao = 20;
        double limitePeso = 8000; // kg
        double larguraMaxima = 300; // cm
        double alturaMaxima = 250; // cm
        double profundidadeMaxima = 400; // cm
        int probabilidadeMutacao = 5; // 5%
        int qtdeCruzamentos = 5;
        int numeroGeracoes = 10;

        AlgoritmoGenetico meuAg = new AlgoritmoGenetico(populacao, limitePeso, larguraMaxima, alturaMaxima, profundidadeMaxima, probabilidadeMutacao, qtdeCruzamentos, numeroGeracoes);
        meuAg.carregaArquivo("carga_aviao.csv");
        meuAg.executar();
    }
}
