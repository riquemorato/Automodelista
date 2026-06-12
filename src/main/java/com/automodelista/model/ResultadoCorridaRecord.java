
package com.automodelista.model;

/**
 *
 * @author Henrique
 */

//CLASSE CONVERTIDA PARA RECORD POR SER UM RESULTADO IMUTÁVEL - com auxilio de IA
public record ResultadoCorridaRecord(
        Piloto  piloto,
        int     score,
        boolean abandonou,
        int     posicao,
        int     pontosObtidos
) {
    
    // Getters no estilo JavaBean para compatibilidade com Thymeleaf
    public Piloto getPiloto(){
        return piloto();
    }
    
    public int getScore(){
        return score();
    }

    public boolean isAbandonou(){
        return abandonou();
    }

    public int getPosicao(){
        return posicao();
    }

    public int getPontosObtidos(){
        return pontosObtidos();
    }

    public boolean pontuou(){
        return pontosObtidos() > 0;
    }

    public boolean foiPodio(){
        return !abandonou() && posicao() <= 3;
    }
}