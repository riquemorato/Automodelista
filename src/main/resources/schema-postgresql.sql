-- ================================================================
-- AUTOMODELISTA — Schema PostgreSQL
-- ================================================================

CREATE TABLE IF NOT EXISTS equipe (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    orcamento NUMERIC(15,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS carro (
    id                SERIAL PRIMARY KEY,
    nome              VARCHAR(100),
    equipe_id         INT REFERENCES equipe(id),
    nivel_motor       INT NOT NULL DEFAULT 1,
    nivel_aero        INT NOT NULL DEFAULT 1,
    nivel_transmissao INT NOT NULL DEFAULT 1,
    nivel_suspensao   INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS piloto (
    id                SERIAL PRIMARY KEY,
    nome              VARCHAR(100) NOT NULL,
    nacionalidade     VARCHAR(60),
    idade             INT,
    numero_carro      INT,
    habilidade        INT NOT NULL DEFAULT 50,
    consistencia      INT NOT NULL DEFAULT 50,
    pontos_campeonato INT          DEFAULT 0,
    equipe_id         INT REFERENCES equipe(id)
);

CREATE TABLE IF NOT EXISTS campeonato (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    temporada INT NOT NULL
);

CREATE TABLE IF NOT EXISTS corrida (
    id            SERIAL PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    circuito      VARCHAR(100),
    rodada        INT,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    campeonato_id INT REFERENCES campeonato(id)
);

CREATE TABLE IF NOT EXISTS participacao (
    id              SERIAL PRIMARY KEY,
    piloto_id       INT REFERENCES piloto(id),
    corrida_id      INT REFERENCES corrida(id),
    posicao_final   INT,
    pontos_obtidos  INT     DEFAULT 0,
    tipo_estrategia VARCHAR(20),
    compound_pneu   VARCHAR(20),
    abandonou       BOOLEAN DEFAULT FALSE,
    UNIQUE (piloto_id, corrida_id)
);

-- ================================================================
-- Flags de bloqueio — dados de referência (seed) não editáveis
-- ================================================================

ALTER TABLE equipe  ADD COLUMN IF NOT EXISTS bloqueada BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE piloto  ADD COLUMN IF NOT EXISTS bloqueado BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE corrida ADD COLUMN IF NOT EXISTS bloqueada BOOLEAN NOT NULL DEFAULT FALSE;