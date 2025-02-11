package com.iticbcn.usuaris.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.DAO.UsuariDAO;
import com.iticbcn.usuaris.Model.Usuari;
import com.iticbcn.usuaris.Views.InputView;

public class UsuariController {

    public static void AfegirUsuari(BufferedReader bf,SessionFactory sf) throws Exception {

        Usuari u = new Usuari();
        UsuariDAO udao = new UsuariDAO(sf);

        System.out.println("INSERIR NOU USUARI");
        String dni = InputView.DemanarDNIUsuari(bf);

    // Comprovar si ja existeix
        u = udao.getUsuariByDNI(dni);

        if (u == null) {
            u = NouUsuari(bf, dni);
            udao.PersistirUsuari(u);
        } else {
            System.out.println("Usuari existent trobat: " + u.getNomUsuari());
            System.out.println("Tornant a menú principal");
        }

    }

    public static Usuari NouUsuari(BufferedReader bf, String dni) throws Exception {

        Usuari usuari = null;

        System.out.println("Usuari no existent, creant un de nou...");
        System.out.print("Introdueix el nom: ");
        String nom = LecturaEntrada(bf);
        System.out.print("Introdueix el rol: ");
        String rol = LecturaEntrada(bf);
        usuari = new Usuari(nom, dni, rol);
                
        return usuari;
    }

    public static List<Usuari> LlistarUsuaris(SessionFactory sf) throws Exception {

        UsuariDAO udao = new UsuariDAO(sf);
        return udao.LlistarUsuaris();
    }

    public static String LecturaEntrada(BufferedReader bf) {
        String str1 = null;
        try {
            str1 = bf.readLine();
        } catch (IOException e) {
            System.err.println("Error d'entrada: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error general: " + ex.getMessage());
        }

        return str1;
    }
    
}
