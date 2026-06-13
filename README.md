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
