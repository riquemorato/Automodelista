> Sistema de gerenciamento e simulação de uma equipe de Fórmula 1 — projeto final da disciplina de **Programação Orientada a Objetos**, curso de Análise e Desenvolvimento de Sistemas (CST) - 4o Ciclo na FATEC Baixada Santista Rubens Lara.

---

## Sobre o projeto

O **Automodelista** nasceu como o projeto final da disciplina de Programação Orientada a Objetos. O desafio proposto era construir um sistema web que realizasse operações CRUD com Spring JDBC + Thymeleaf, aplicando os conceitos ensinados durante as aulas. 

Este projeto tem como objetivo central não construir "mais um CRUD", mas sim aplicar na prática os pilares da Programação Orientada a Objetos: encapsulamento, herança, polimorfismo e abstração, dentro de um domínio que faz sentido e interessante de explorar: a gestão de uma equipe de Fórmula 1 ao longo de uma temporada.

Toda a persistência foi implementada com **JDBC puro (`JdbcTemplate`)**, sem ORM — uma exigência específica do professor para garantir que os alunos entendam o que está de fato acontecendo entre o objeto Java e a tabela do banco, sem a abstração e "simplificação" de um framework como JPA/Hibernate por trás.

## A inspiração

A inspiração veio de jogos de gestão de automobilismo como **F1 Manager 24** e **Motorsports Manager** — títulos em que o jogador não pilota um carro, mas atua como *team principal*: contrata pilotos, desenvolve o carro, define estratégias de corrida e acompanha os resultados ao longo do campeonato. O nome do projeto é uma brincadeira com um simulador de automobilismo virtual produzido no Brasil chamado **Automobilista**

Desde o ínicio, a ideia do projeto foi *"desenvolver uma versão mais simples possível da experiência oferecida nesses jogos, mas construída inteiramente com conceitos de programação vistos em sala de aula."*

No *Automodelista* você cadastra sua equipe, contrata seu piloto, define o nível de desenvolvimento do carro, inscreve-se nas corridas da temporada e clica em "Simular". O sistema então calcula um resultado dinâmico, baseado nos atributos cadastrados e em um fator de variação aleatória, exatamente como uma simulação simplificada faria.

## O conceito: uma camada de simulação primitiva

O coração do projeto é o `SimulacaoService`: uma camada de simulação deliberadamente **primitiva**, mas funcional. Ao invés de simular de física de corrida, componentes do carro, infraestrutura da equipe e muitos outros fatores, cada piloto recebe um *score de desempenho* composto por:

- atributos do **piloto** → habilidade e consistência;
- atributos do **carro** da sua equipe → motor, aerodinâmica, transmissão, suspensão;
- um **bônus de estratégia** escolhida para a corrida → conservadora, balanceada ou agressiva;
- uma **variação aleatória** → minimiza a chance de um resultado nunca ser idêntico duas vezes.

Esses scores são ordenados, posições e pontos são atribuídos seguindo o sistema oficial de pontuação da F1 (top 10 pontuam: 25-18-15-12-10-8-6-4-2-1).Há também uma chance de **DNF** (abandono), onde probabilidade varia conforme a estratégia escolhida. O resultado é salvo no banco e o campeonato é atualizado automaticamente.

É uma simulação simples de propósito, uma vez que o objetivo do projeto é demonstrar os conceitos de POO de forma clara, não competir com um motor de física real.

## Funcionalidades

- **Dashboard** — visão geral da temporada: próxima corrida, classificação, calendário, equipes e pilotos.
- **Pilotos** — cadastro, edição e listagem, com atributos de habilidade e consistência.
- **Equipes** — cadastro, edição e listagem, com orçamento e carro associado (motor, aero, transmissão, suspensão).
- **Campeonatos e Corridas** — calendário de corridas por campeonato, inscrição de pilotos em cada corrida (estratégia + composto de pneu).
- **Simulação de corrida** — botão "Simular Corrida" gera o resultado com base na fórmula de desempenho, atualiza posições, pontos e status da corrida.
- **Classificação (Standings)** — ranking geral de pilotos por pontos no campeonato.
- **Painel administrativo** — reset de demonstração, que apaga todos os dados e recria a temporada a partir do zero.

## Como funciona a simulação

### Fórmula de desempenho

```
total = basePiloto + performanceCarro + bonusEstrategia + variação(±10)

basePiloto       = habilidade × (consistência / 100)
performanceCarro = (motor × 40) + (aero × 30) + (transmissão × 20) + (suspensão × 10)
```

A `performanceCarro` tem peso muito maior que `basePiloto` na fórmula — assim como na Fórmula 1 real, o carro é o fator decisivo, mas o piloto e a estratégia ainda fazem diferença no resultado final.

### Estratégias

| Estratégia    | Bônus de desempenho                          | Degradação de pneu | Risco de DNF |
|---------------|-----------------------------------------------|--------------------|--------------|
| Conservadora  | `performance × 0.05`                           | 0.70× (mais lenta) | 0.60× (3%)   |
| Balanceada    | `(habilidade + performance) × 0.10`            | 1.00× (padrão)     | 1.00× (5%)   |
| Agressiva     | `habilidade × 0.20 + performance × 0.15`       | 1.50× (mais rápida)| 2.00× (10%)  |

A chance de abandono (DNF) é calculada como `5% × multiplicador de risco` da estratégia escolhida.

### Critério de classificação

1. Pilotos que **não abandonaram** são ordenados do maior para o menor `total`.
2. Pilotos que **abandonaram (DNF)** vão para o final da lista, independentemente do score.
3. Pontuação segue o sistema oficial da F1 para o top 10.

## Conceitos de POO aplicados

| Conceito                  | Onde aparece                                                                 |
|---------------------------|-------------------------------------------------------------------------------|
| **Abstração**              | `EstrategiaAbstract` define o contrato comum para qualquer estratégia de corrida. |
| **Herança / Polimorfismo** | `EstrategiaConservadora`, `EstrategiaBalanceada` e `EstrategiaAgressiva` implementam `calcularBonus()`, `getFatorDegradacao()` e `getMultiplicadorRisco()` de formas diferentes, mas são tratadas pelo mesmo tipo (`EstrategiaAbstract`) pelo restante do sistema. |
| **Factory Method**         | `EstrategiaAbstract.criar(tipo)` decide, em tempo de execução, qual subclasse instanciar a partir de uma string vinda do formulário. |
| **Encapsulamento**          | Todas as entidades (`Piloto`, `Equipe`, `Carro`, `Corrida`, `ParticipacaoCorrida`) expõem seus dados via getters/setters, mantendo os atributos privados. |
| **Records / Imutabilidade** | `FatorSimulacao`, `ResultadoCorridaRecord`, `PosicaoCampeonatoRecord` e o record interno `DadosDesempenho` representam resultados de cálculo que, uma vez gerados, não devem ser alterados — uma corrida simulada não pode "mudar de sorte" depois do fato. |
| **Separação em camadas**    | Arquitetura `Controller → Service → DAO`, isolando regras de negócio (Service) do acesso a dados (DAO, via `JdbcTemplate`) e da apresentação (Controller + Thymeleaf). |

## Stack tecnológica

**Backend**
- Java 21
- Spring Boot 4
- Spring JDBC (`JdbcTemplate` puro, sem JPA/Hibernate)
- PostgreSQL

**Frontend**
- Thymeleaf (fragments para layout reutilizável — topbar, sidebar)
- HTML5, CSS3 e JavaScript (vanilla, sem frameworks)
- Bootstrap 5.3 como base, com tema dark mode customizado (`theme.css`)

**Build**
- Maven (via Maven Wrapper — `./mvnw`)

## Estrutura do projeto

```
src/main/java/com/automodelista/
├── controller/      # Endpoints MVC (Dashboard, Pilotos, Equipes, Corridas, Campeonatos, Admin, Standings)
├── service/         # Regras de negócio (SimulacaoService, PilotoService, EquipeService, CorridaService...)
├── dao/              # Acesso a dados via JdbcTemplate
├── model/            # Entidades (Piloto, Equipe, Carro, Corrida, Campeonato, ParticipacaoCorrida)
│   ├── abstracts/    # EstrategiaAbstract
│   ├── estrategia/   # EstrategiaConservadora, EstrategiaBalanceada, EstrategiaAgressiva
│   └── enums/        # StatusCorrida, CompostoPneu
└── utils/            # DadosSeederService (popula/reseta dados de demonstração)

src/main/resources/
├── templates/        # Páginas Thymeleaf
│   ├── fragments/    # layout.html (topbar, sidebar, head, scripts)
│   ├── piloto/, equipe/, corrida/, campeonato/, standings/, admin/
└── static/
    ├── css/theme.css # Tema dark mode customizado
    └── js/app.js     # Sidebar mobile, destaque de menu ativo, delay de simulação
```

## Como executar

### Pré-requisitos

- JDK 21
- PostgreSQL (local ou remoto)
- Maven (ou use o wrapper incluído, `./mvnw`)

### Configuração

1. Crie um banco PostgreSQL para o projeto.
2. Configure as credenciais de conexão em `src/main/resources/application.properties` (URL, usuário e senha do banco).

### Executando

```bash
./mvnw clean
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Dados de demonstração

Na primeira execução com o banco vazio, o sistema popula automaticamente a temporada com **11 equipes** e **22 pilotos** baseados no grid da Fórmula 1 2026, além de um calendário com **24 corridas**. Esses dados de referência aparecem marcados com o selo **🔒 Referência** nas listagens e não podem ser editados — servem apenas como base para você cadastrar sua própria equipe e piloto e competir contra eles.

O **Painel Admin** (`/admin`) permite resetar a demonstração a qualquer momento, apagando todos os dados (incluindo cadastros do usuário) e recriando a temporada do zero — útil para reiniciar uma demonstração ou apresentação.

## Possíveis evoluções futuras

- Suporte a múltiplos campeonatos rodando em paralelo, com pilotos/equipes vinculados a cada um.
- Histórico detalhado por corrida, incluindo estratégia e composto de pneu utilizados no resultado.
- Sistema de desenvolvimento de carro entre corridas (investir orçamento em melhorias incrementais).
- Deploy público (Render) para demonstração sem necessidade de ambiente local.

###

Uso de IA no projeto:
Inteligência artificial foi utilizada para:
- Validação e simplificação da complexidade inicial da regra de negócio;
- Validação da lógica da camada de simulação;
- Correção de um bug fatal no processo de reset dos dados de demonstração;
- Implementação da parte estética do frontend;
- Geração do Readme do projeto;
