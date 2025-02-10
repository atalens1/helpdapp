package com.iticbcn.usuaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.Views.InputView;


public class Main {
    public static void main(String[] args) {

        SessionFactory sf = HibernateUtil.getSessionFactory();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("=============================");
            System.out.println("Gestio de Peticions i Usuaris");
            System.out.println("=============================");
            InputView.MostrarMenu(bf,sf);

        } catch (IOException ioe) {
            System.err.println("Error d'entrada");
        }
    }


}   