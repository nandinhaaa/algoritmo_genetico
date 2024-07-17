# AGJava2024 - Algoritmo Genético para Planejamento de Carga em Aviões

Este projeto implementa um Algoritmo Genético (AG) para resolver o problema de otimização do planejamento de carga em aviões. O objetivo é maximizar o benefício total da carga respeitando as restrições de peso e dimensões máximas permitidas na aeronave.

## Alterações Realizadas

### Classe Produto

A classe `Produto` foi criada para representar cada item de carga com os seguintes atributos:

- `descricao`: Descrição do produto.
- `peso`: Peso do produto em kg.
- `valor`: Valor do produto.
- `largura`: Largura do produto em cm.
- `altura`: Altura do produto em cm.
- `profundidade`: Profundidade do produto em cm (valor padrão definido como 50).

Implementação do método `toString()` para exibir informações detalhadas de cada produto.

### Classe AlgoritmoGenetico

#### Construtor

O construtor foi ajustado para incluir parâmetros essenciais para o algoritmo:

- `populacao`: Tamanho da população inicial.
- `capacidadePeso`: Limite máximo de peso que a aeronave suporta em kg.
- `larguraMaxima`: Largura máxima permitida para um item de carga em cm.
- `alturaMaxima`: Altura máxima permitida para um item de carga em cm.
- `profundidadeMaxima`: Profundidade máxima permitida para um item de carga em cm.
- `probabilidadeMutacao`: Probabilidade de mutação durante o processo de evolução.
- `qtdeCruzamentos`: Número de cruzamentos realizados durante o crossover.
- `numGeracoes`: Número máximo de gerações para executar o algoritmo.

#### Métodos

- **`carregaArquivo(String fileName)`**: Modificado para ler dados dos produtos de um arquivo CSV (`carga_aviao.csv`). Cada linha do arquivo CSV contém informações sobre um produto, incluindo descrição, peso, valor, largura e altura.
  
- **`fitness(ArrayList<Integer> cromossomo)`**: Aprimorado para calcular o fitness de um cromossomo considerando o benefício total (volume total) e verificando se os itens selecionados respeitam as restrições de peso e dimensões máximas.

- **`inicializaPopulacao()`**: Implementado para inicializar a população inicial com valores binários aleatórios (0s e 1s) para cada cromossomo, onde cada gene representa a seleção de um produto para o carregamento.

- **`mostraPopulacao()`**: Adicionado para exibir a população atual em cada geração, mostrando informações como o cromossomo, fitness, peso total, volume total e o status de respeito às restrições.

- **`executar()`**: Método principal que executa o algoritmo genético ao longo de várias gerações, realizando seleção por torneio, crossover de um ponto, mutação e substituição da população para buscar a melhor solução.

### Execução Principal (`AGJava2024.main`)

O método principal `main` foi ajustado para configurar os parâmetros do algoritmo genético, carregar os produtos do arquivo CSV e iniciar a execução do algoritmo para otimizar o planejamento de carga em aviões.

### Resultado
![image](https://github.com/user-attachments/assets/796f7926-9de1-4702-823e-0c7b70c09d70)

---

Essas mudanças foram feitas para adaptar o algoritmo genético ao problema específico de planejamento de carga em aviões, garantindo que as restrições de peso e dimensões sejam estritamente respeitadas durante a otimização.
