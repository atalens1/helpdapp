package com.iticbcn.usuaris.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.iticbcn.usuaris.Model.*;

import jakarta.validation.ConstraintViolationException;

public class UsuariDAO {

    private SessionFactory sessionFactory;

    public UsuariDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void PersistirUsuari(Usuari us) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.persist(us);
                ses.getTransaction().commit();
            } catch (JDBCException jdbcex) {
                handleException(ses, jdbcex, "Error de JDBC");                
            } catch (ConstraintViolationException cnsex) {
                handleException(ses, cnsex, "Error de restricció en claus");          
            } catch (HibernateException hbex) {
                handleException(ses, hbex, "Error d'Hibernate a la transacció");     
            } catch (Exception ex) {
                handleException(ses, ex, "Altres excepcions");    
            }
        } catch (SessionException sesexcp) {
            System.err.println("Error de Sessió: "+sesexcp.getMessage());
            throw sesexcp;
        } catch (HibernateException hbex) {
            System.err.println("Error d'Hibernate: "+hbex.getMessage());
            throw hbex;
        }
    }

    public Usuari ObtenirUsuari(int idUsuari) throws Exception {
        Usuari us = null;

        try (Session ses = sessionFactory.openSession()) {
            try {
                us = ses.find(Usuari.class, idUsuari);
            } catch (JDBCException jdbcex) {
                handleException(ses, jdbcex, "Error de JDBC");                       
            } catch (HibernateException hbex) {
                handleException(ses, hbex, "Error d'Hibernate a la consulta");     
            } catch (Exception ex) {
                handleException(ses, ex, "Altres excepcions");    
            }
        } catch (SessionException sesexcp) {
            System.err.println("Error de Sessió: "+sesexcp.getMessage());
            throw sesexcp;
        } catch (HibernateException hbex) {
            System.err.println("Error d'Hibernate: "+hbex.getMessage());
            throw hbex;
        }

        return us;
    }

    public void ModificarUsuari(Usuari us) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.merge(us);
                ses.getTransaction().commit();
            } catch (JDBCException jdbcex) {
                handleException(ses, jdbcex, "Error de JDBC");                
            } catch (ConstraintViolationException cnsex) {
                handleException(ses, cnsex, "Error de restricció en claus");          
            } catch (HibernateException hbex) {
                handleException(ses, hbex, "Error d'Hibernate a la transacció");     
            } catch (Exception ex) {
                handleException(ses, ex, "Altres excepcions");    
            }
        } catch (SessionException sesexcp) {
            System.err.println("Error de Sessió: "+sesexcp.getMessage());
            throw sesexcp;
        } catch (HibernateException hbex) {
            System.err.println("Error d'Hibernate: "+hbex.getMessage());
            throw hbex;
        }
    }

    public void EsborrarUsuari(Usuari us) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.remove(us);
                ses.getTransaction().commit();
            } catch (JDBCException jdbcex) {
                handleException(ses, jdbcex, "Error de JDBC");                
            } catch (ConstraintViolationException cnsex) {
                handleException(ses, cnsex, "Error de restricció en claus");          
            } catch (HibernateException hbex) {
                handleException(ses, hbex, "Error d'Hibernate a la transacció");     
            } catch (Exception ex) {
                handleException(ses, ex, "Altres excepcions");    
            }
        } catch (SessionException sesexcp) {
            System.err.println("Error de Sessió: "+sesexcp.getMessage());
            throw sesexcp;
        } catch (HibernateException hbex) {
            System.err.println("Error d'Hibernate: "+hbex.getMessage());
            throw hbex;
        }
    }

    public List<Usuari> LlistarUsuaris() throws Exception {
        List<Usuari> usuaris = new ArrayList<>();

        try (Session ses = sessionFactory.openSession()) {
            try {
                Query<Usuari> q = ses.createQuery("from Usuari",Usuari.class);
                usuaris = q.list();
            } catch (JDBCException jdbcex) {
                handleException(ses, jdbcex, "Error de JDBC");                       
            } catch (HibernateException hbex) {
                handleException(ses, hbex, "Error d'Hibernate a la consulta");     
            } catch (Exception ex) {
                handleException(ses, ex, "Altres excepcions");    
            }
        } catch (SessionException sesexcp) {
            System.err.println("Error de Sessió: "+sesexcp.getMessage());
            throw sesexcp;
        } catch (HibernateException hbex) {
            System.err.println("Error d'Hibernate: "+hbex.getMessage());
            throw hbex;
        }

        return usuaris;
    }

    private void handleException(Session ses, Exception ex, String errorMsg) throws Exception{
        if (ses.getTransaction() != null && ses.getTransaction().isActive()) {
            ses.getTransaction().rollback();
        }
        System.err.println(errorMsg + ": " + ex.getMessage());
        throw ex;
    }

    
}

