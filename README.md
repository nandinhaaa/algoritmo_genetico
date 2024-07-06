
### Mudanças na classe `AGJava2024`:
1. **Ajustes no Método Principal:**
   - Definição de parâmetros como tamanho da população, limite de peso e dimensões máximas.
   - Configuração da probabilidade de mutação, número de cruzamentos e gerações.

### Mudanças na classe `AlgoritmoGenetico`:
1. **Construtor:**
   - Adicionado parâmetros para capacidade de peso (`capacidadePeso`), dimensões máximas (`larguraMaxima`, `alturaMaxima`, `profundidadeMaxima`) e parâmetros do algoritmo genético.

2. **Método `carregaArquivo`:**
   - Modificado para ler dados dos produtos de um arquivo CSV (`carga_aviao.csv`).
   - Inicialização do `tamCromossomo` baseado no número de produtos lidos.

3. **Método `fitness`:**
   - Aprimoramento da avaliação de aptidão para calcular peso total (`pesoTotal`), volume total (`volumeTotal`) e valor total (`valorTotal`) com base nos itens selecionados.
   - Implementação de verificações para dimensões individuais dos itens excedendo os valores máximos permitidos (`larguraMaxima`, `alturaMaxima`, `profundidadeMaxima`).

4. **Método `inicializaPopulacao`:**
   - Implementação da inicialização da população inicial (`populacao`) usando valores binários aleatórios (0s e 1s) para cada cromossomo.

5. **Método `mostraPopulacao`:**
   - Adicionado para exibir a população atual.

### Mudanças na classe `Produto`:
1. **Classe de Produto:**
   - Criada para representar cada item de carga com atributos como descrição, peso, valor, largura, altura e profundidade.
   - Implementação do método `toString` para exibir informações detalhadas de cada produto.

Essas alterações foram realizadas para adaptar o algoritmo genético ao problema específico de planejamento de carga em aviões, levando em consideração restrições de peso e dimensões. <br> 
 <br>  2. **Resultado :**
![image](https://github.com/nandinhaaa/algoritmo_genetico/assets/91507393/1bdb1381-4f96-4d85-885d-f706fe8badf9)
