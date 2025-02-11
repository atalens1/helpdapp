package com.iticbcn.usuaris.Controllers;

import java.io.BufferedReader;
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
            u = InputView.DemanarDadesUsuari(bf, dni);
            try {
                udao.PersistirUsuari(u);
                System.out.println("Usuari creat amb èxit");
            } catch (Exception e) {
                System.err.println("Error al crear l'usuari: " + e.getMessage());
            }
        } else {
            System.out.println("Usuari existent trobat: " + u.getNomUsuari());
            System.out.println("Tornant a menú principal");
        }
    }

    public static List<Usuari> LlistarUsuaris(SessionFactory sf) throws Exception {

        UsuariDAO udao = new UsuariDAO(sf);
        return udao.LlistarUsuaris();
    }
    
}
