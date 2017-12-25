package com.numiitech.memestugas.classes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.numiitech.memestugas.R;
import com.numiitech.memestugas.Sobre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nuno- on 25/12/2017.
 */

public class App {
    // Request Codes
    public static final int SHARE_MEME = 101;

    // Attributes

    // Methods
    public static List<Meme> memesList() {
        return new ArrayList<>(Arrays.asList(
                new Meme("Adoro pila", R.raw.adoro_pila),
                new Meme("Aprendizagem", R.raw.aprendizagem),
                new Meme("Bocado Rijo", R.raw.bocado_rijo),
                new Meme("É um cabrão?", R.raw.cabrao),
                new Meme("Uma cagada!", R.raw.cagada),
                new Meme("Cheira mal", R.raw.cheira_mal),
                new Meme("Cocó Enrolado", R.raw.coco_enrolado),
                new Meme("E o dinheiro está no ca#!%?€!", R.raw.dinheiro_caralho),
                new Meme("Droga!", R.raw.droga),
                new Meme("Já estou a \"engasgar-se\"", R.raw.engasgar_se),
                new Meme("Epah, cala-te!", R.raw.epah_calate),
                new Meme("Já tou a falar espanhol...", R.raw.espanhol),
                new Meme("Espetáculo!", R.raw.espetaculo),
                new Meme("Fazer uma ganza", R.raw.fazer_ganza),
                new Meme("Na feira da Ladra", R.raw.feira_ladra),
                new Meme("Ganza na areia", R.raw.ganza_areia),
                new Meme("Que informação dramática...", R.raw.info_dramatica),
                new Meme("Leave me Alone!", R.raw.leave_alone),
                new Meme("Maria Leal", R.raw.maria_leal),
                new Meme("Ando na mecânica e no roubo", R.raw.mecanica_roubo),
                new Meme("Nem quero comentar", R.raw.nem_comentar),
                new Meme("Ordenhar uma vaca", R.raw.ordenhar_vaca),
                new Meme("Passaste!", R.raw.passaste),
                new Meme("Perdemos? Que se f#&?!", R.raw.perdemos),
                new Meme("Fo$#-€% a pesca!", R.raw.pesca),
                new Meme("Tem planos para jantar?", R.raw.planos_jantar),
                new Meme("Drogados a caçar Pokémons", R.raw.pokemons),
                new Meme("Porra pá!", R.raw.porra_pa),
                new Meme("E vai mas é para o ca#!%?€!", R.raw.pro_caralho),
                new Meme("Tu és solteiro ca#!%?€!", R.raw.solteiro),
                new Meme("Acabou o sossego!", R.raw.sossego),
                new Meme("Vaca não sou, p&%# se calhar", R.raw.vaca_puta),
                new Meme("Grandessíssimas vadias!", R.raw.vadias),
                new Meme("Vou-te comer (e comi!)", R.raw.vou_comer),
                new Meme("Windoohhh!", R.raw.windoh),
                new Meme("Windoohhh! (fã)", R.raw.windoh_fa)
        ));
    }

    // Getters and Setters

}
