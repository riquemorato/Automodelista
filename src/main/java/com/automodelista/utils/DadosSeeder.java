/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Henrique
 */
@Configuration
public class DadosSeeder {

    @Bean
    CommandLineRunner seedDados(DadosSeederService dadosSeederService) {
        return args -> dadosSeederService.popularSeNecessario();
    }
}