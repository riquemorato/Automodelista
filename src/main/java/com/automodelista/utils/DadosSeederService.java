//CLASSE UTILITÁRIA PARA ALIMENTAR O DB QUANDO O SITE VOLTAR DE HIBERNAÇÃO, FACILITANDO A SIMULAÇÃO COM DADOS JÁ INSERIDOS

package com.automodelista.utils;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Configuration
public class DadosSeederService {

    @Autowired DataSource dataSource;
    JdbcTemplate jdbc;

    @PostConstruct
    private void init() { jdbc = new JdbcTemplate(dataSource); }

    // Sistema de pontuação oficial da F1 (Top 10)
    private static final int[] PONTOS_RODADA_1 = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    /**
     * Posição final na rodada 1 por piloto cadastrado (mapeado de 0 a 21 conforme ordem de seed)
     * Garante posições únicas sem ultrapassar o tamanho da tabela de pontos para os top 10.
     */
    private static final int[] POSICOES_RODADA_1 = {
        3,  2,  // Ferrari (Leclerc, Hamilton)
        5,  6,  // McLaren (Norris, Piastri)
        10, 11, // Audi (Bortoleto, Hülkenberg)
        1,  12, // Red Bull (Verstappen, Hadjar)
        4,  9,  // Mercedes (Russell, Antonelli)
        8,  13, // Aston Martin (Alonso, Stroll)
        14, 15, // Alpine (Gasly, Colapinto)
        16, 7,  // Williams (Albon, Sainz)
        17, 18, // Haas (Ocon, Bearman)
        19, 20, // Racing Bulls (Lawson, Lindblad)
        21, 22  // Cadillac (Pérez, Bottas)
    };

    private static final String[] ESTRATEGIAS_RODADA_2 = {
        "AGRESSIVA", "BALANCEADA",   // Ferrari
        "AGRESSIVA", "BALANCEADA",   // McLaren
        "AGRESSIVA", "CONSERVADORA", // Audi
        "AGRESSIVA", "CONSERVADORA", // Red Bull
        "BALANCEADA", "AGRESSIVA",   // Mercedes
        "BALANCEADA", "CONSERVADORA",// Aston Martin
        "BALANCEADA", "BALANCEADA",  // Alpine
        "CONSERVADORA", "AGRESSIVA", // Williams
        "CONSERVADORA", "BALANCEADA",// Haas
        "AGRESSIVA", "CONSERVADORA", // Racing Bulls
        "BALANCEADA", "CONSERVADORA" // Cadillac
    };

    private static final String[] COMPOSTOS_RODADA_2 = {
        "MACIO", "MEDIO", // Ferrari
        "MACIO", "MEDIO", // McLaren
        "MACIO", "DURO",  // Audi
        "MACIO", "DURO",  // Red Bull
        "MEDIO", "MACIO", // Mercedes
        "MEDIO", "DURO",  // Aston Martin
        "MEDIO", "MEDIO", // Alpine
        "DURO", "MACIO",  // Williams
        "DURO", "MEDIO",  // Haas
        "MACIO", "DURO",  // Racing Bulls
        "MEDIO", "DURO"   // Cadillac
    };

    /** Roda no startup — só popula se o banco estiver vazio. */
    @Transactional
    public void popularSeNecessario() {
        Integer total = jdbc.queryForObject("SELECT COUNT(*) FROM campeonato", Integer.class);
        if (total != null && total > 0) return;

        System.out.println("\n[SEED] Banco vazio — populando dados da temporada de 2026...\n");
        criarEstruturaInicial();
        System.out.println("[SEED] Concluído — 11 equipes, 22 pilotos, 24 corridas.\n");
    } 

    /** Restaura o estado limpo de demonstração */
    public void resetarDemo() {
        jdbc.update(
            "DELETE FROM participacao WHERE corrida_id IN (SELECT id FROM corrida WHERE bloqueada = true)");

        jdbc.update(
            "UPDATE corrida SET status = CASE WHEN rodada = 1 THEN 'FINALIZADA' ELSE 'PENDENTE' END " +
            "WHERE bloqueada = true");

        jdbc.update("UPDATE piloto SET pontos_campeonato = 0");

        popularResultadosSeed();
    }

    private void criarEstruturaInicial() {
        int campeonatoId = jdbc.queryForObject(
            "INSERT INTO campeonato(nome, temporada) VALUES(?,?) RETURNING id",
            Integer.class, "Fórmula 1 World Championship", 2026);

        // --- 11 EQUIPES ---
        int ferrariId     = seedEquipe("Scuderia Ferrari", 200_000_000, 8, 7, 6, 7);
        int mclarenId     = seedEquipe("McLaren", 200_000_000, 5, 5, 5, 5);
        int audiId        = seedEquipe("Audi", 150_000_000, 3, 4, 3, 3);
        int redbullId     = seedEquipe("Oracle Red Bull Racing", 200_000_000, 8, 7, 6, 7);
        int mercedesId    = seedEquipe("Mercedes-AMG Petronas", 200_000_000, 5, 5, 5, 5);
        int astonId       = seedEquipe("Aston Martin Aramco", 150_000_000, 3, 4, 3, 3);
        int alpineId      = seedEquipe("BWT Alpine F1 Team", 150_000_000, 5, 5, 5, 5);
        int williamsId    = seedEquipe("Atlassian Williams", 100_000_000, 3, 4, 3, 3);
        int haasId        = seedEquipe("TGR Haas F1 Team", 100_000_000, 3, 4, 3, 3);
        int racingbullsId = seedEquipe("Visa Cash App Racing Bulls", 120_000_000, 3, 4, 3, 3);
        int cadillacId    = seedEquipe("Cadillac Formula 1 Team", 150_000_000, 5, 5, 5, 5);

        // --- 22 PILOTOS ---
        seedPiloto("Charles Leclerc", "Mônaco", 27, 16, 88, 85, ferrariId);
        seedPiloto("Lewis Hamilton", "Reino Unido", 41, 44, 84, 80, ferrariId);

        seedPiloto("Lando Norris", "Reino Unido", 26, 4, 78, 75, mclarenId);
        seedPiloto("Oscar Piastri", "Austrália", 24, 81, 75, 78, mclarenId);

        seedPiloto("Gabriel Bortoleto", "Brasil", 21, 17, 70, 72, audiId);
        seedPiloto("Nico Hülkenberg", "Alemanha", 38, 27, 68, 70, audiId);

        seedPiloto("Max Verstappen", "Países Baixos", 28, 1, 88, 85, redbullId);
        seedPiloto("Isack Hadjar", "França", 21, 6, 84, 80, redbullId);

        seedPiloto("George Russell", "Reino Unido", 28, 63, 78, 75, mercedesId);
        seedPiloto("Kimi Antonelli", "Itália", 19, 12, 75, 78, mercedesId);

        seedPiloto("Fernando Alonso", "Espanha", 44, 14, 70, 72, astonId);
        seedPiloto("Lance Stroll", "Canadá", 27, 18, 68, 70, astonId);

        seedPiloto("Pierre Gasly", "França", 30, 10, 88, 85, alpineId);
        seedPiloto("Franco Colapinto", "Argentina", 22, 43, 84, 80, alpineId);

        seedPiloto("Alexander Albon", "Tailândia", 29, 23, 78, 75, williamsId);
        seedPiloto("Carlos Sainz Jr.", "Espanha", 31, 55, 75, 78, williamsId);

        seedPiloto("Esteban Ocon", "França", 29, 31, 70, 72, haasId);
        seedPiloto("Oliver Bearman", "Reino Unido", 20, 87, 68, 70, haasId);

        seedPiloto("Liam Lawson", "Nova Zelândia", 24, 30, 88, 85, racingbullsId);
        seedPiloto("Arvid Lindblad", "Reino Unido", 18, 9, 84, 80, racingbullsId);

        seedPiloto("Sergio Pérez", "México", 36, 11, 78, 75, cadillacId);
        seedPiloto("Valtteri Bottas", "Finlândia", 36, 77, 75, 78, cadillacId);

        // --- 24 CORRIDAS ---
        seedCorrida("GP de Interlagos", "Autódromo José Carlos Pace", 1, campeonatoId);
        seedCorrida("GP de Mônaco", "Circuit de Monaco", 2, campeonatoId);
        seedCorrida("GP de Silverstone", "Silverstone Circuit", 3, campeonatoId);
        seedCorrida("GP de Spa", "Circuit de Spa-Francorchamps", 4, campeonatoId);
        seedCorrida("GP de Suzuka", "Suzuka International Racing Course", 5, campeonatoId);
        seedCorrida("GP do Bahrein", "Bahrain International Circuit", 6, campeonatoId);
        seedCorrida("GP da Arábia Saudita", "Jeddah Corniche Circuit", 7, campeonatoId);
        seedCorrida("GP da Austrália", "Albert Park Circuit", 8, campeonatoId);
        seedCorrida("GP da China", "Shanghai International Circuit", 9, campeonatoId);
        seedCorrida("GP de Miami", "Miami International Autodrome", 10, campeonatoId);
        seedCorrida("GP de Emília-Romanha", "Autodromo Enzo e Dino Ferrari", 11, campeonatoId);
        seedCorrida("GP do Canadá", "Circuit Gilles-Villeneuve", 12, campeonatoId);
        seedCorrida("GP da Áustria", "Red Bull Ring", 13, campeonatoId);
        seedCorrida("GP da Hungria", "Hungaroring", 14, campeonatoId);
        seedCorrida("GP da Holanda", "Circuit Zandvoort", 15, campeonatoId);
        seedCorrida("GP da Itália", "Autodromo Nazionale Monza", 16, campeonatoId);
        seedCorrida("GP do Azerbaijão", "Baku City Circuit", 17, campeonatoId);
        seedCorrida("GP de Singapura", "Marina Bay Street Circuit", 18, campeonatoId);
        seedCorrida("GP dos Estados Unidos", "Circuit of the Americas", 19, campeonatoId);
        seedCorrida("GP da Cidade do México", "Autódromo Hermanos Rodríguez", 20, campeonatoId);
        seedCorrida("GP de Las Vegas", "Las Vegas Strip Circuit", 21, campeonatoId);
        seedCorrida("GP do Catar", "Lusail International Circuit", 22, campeonatoId);
        seedCorrida("GP de Abu Dhabi", "Yas Marina Circuit", 23, campeonatoId);
        seedCorrida("GP de Espanha", "Circuit de Barcelona-Catalunya", 24, campeonatoId);

        resetarDemo();
    }

    private void popularResultadosSeed() {
        List<Integer> pilotoIds = jdbc.queryForList(
            "SELECT id FROM piloto WHERE bloqueado = true ORDER BY id", Integer.class);
        if (pilotoIds.size() < 22) return; 

        Integer corridaRodada1 = jdbc.queryForObject(
            "SELECT id FROM corrida WHERE bloqueada = true AND rodada = 1", Integer.class);
        Integer corridaRodada2 = jdbc.queryForObject(
            "SELECT id FROM corrida WHERE bloqueada = true AND rodada = 2", Integer.class);

        // Rodada 1 — Resultados Completos (Com proteção para quem não pontuou)
        for (int i = 0; i < 22; i++) {
            int pilotoId = pilotoIds.get(i);
            int posicao  = POSICOES_RODADA_1[i];
            int pontos   = (posicao <= 10) ? PONTOS_RODADA_1[posicao - 1] : 0;

            jdbc.update(
                "INSERT INTO participacao(piloto_id, corrida_id, posicao_final, pontos_obtidos, tipo_estrategia, compound_pneu, abandonou) " +
                "VALUES(?,?,?,?,?,?,false)",
                pilotoId, corridaRodada1, posicao, pontos, "BALANCEADA", "MEDIO");

            if (pontos > 0) {
                jdbc.update("UPDATE piloto SET pontos_campeonato = pontos_campeonato + ? WHERE id=?", pontos, pilotoId);
            }
        }

        // Rodada 2 — Inscrições de todo o grid
        for (int i = 0; i < 22; i++) {
            jdbc.update(
                "INSERT INTO participacao(piloto_id, corrida_id, tipo_estrategia, compound_pneu) VALUES(?,?,?,?)",
                pilotoIds.get(i), corridaRodada2, ESTRATEGIAS_RODADA_2[i], COMPOSTOS_RODADA_2[i]);
        }
    }

    private int seedEquipe(String nome, double orcamento, int motor, int aero, int transm, int susp) {
        int equipeId = jdbc.queryForObject(
            "INSERT INTO equipe(nome, orcamento, bloqueada) VALUES(?,?,true) RETURNING id",
            Integer.class, nome, orcamento);

        jdbc.update(
            "INSERT INTO carro(nome, equipe_id, nivel_motor, nivel_aero, nivel_transmissao, nivel_suspensao) VALUES(?,?,?,?,?,?)",
            nome + " — Carro", equipeId, motor, aero, transm, susp);

        return equipeId;
    }

    private int seedPiloto(String nome, String nacionalidade, int idade, int numeroCarro,
                           int habilidade, int consistencia, int equipeId) {
        return jdbc.queryForObject(
            "INSERT INTO piloto(nome, nacionalidade, idade, numero_carro, habilidade, consistencia, pontos_campeonato, equipe_id, bloqueado) " +
            "VALUES(?,?,?,?,?,?,0,?,true) RETURNING id",
            Integer.class, nome, nacionalidade, idade, numeroCarro, habilidade, consistencia, equipeId);
    }

    private int seedCorrida(String nome, String circuito, int rodada, int campeonatoId) {
        return jdbc.queryForObject(
            "INSERT INTO corrida(nome, circuito, rodada, status, campeonato_id, bloqueada) VALUES(?,?,?,'PENDENTE',?,true) RETURNING id",
            Integer.class, nome, circuito, rodada, campeonatoId);
    }
}