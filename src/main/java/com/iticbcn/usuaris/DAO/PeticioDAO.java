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

public class PeticioDAO {

    private SessionFactory sessionFactory;

    public PeticioDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void PersistirPeticio(Peticio p) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.persist(p);
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

    public Peticio ObtenirPeticio(int idPeticio) throws Exception {
        Peticio pet = null;

        try (Session ses = sessionFactory.openSession()) {
            try {
                pet = ses.find(Peticio.class, idPeticio);
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

        return pet;
    }

    public void ModificarPeticio(Peticio p) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.merge(p);
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

    public void EsborrarPeticio(Peticio p) throws Exception {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            try {
                ses.remove(p);
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

    public List<Peticio> LlistarPeticions() throws Exception {
        List<Peticio> peticions = new ArrayList<>();

        try (Session ses = sessionFactory.openSession()) {
            try {
                Query<Peticio> q = ses.createQuery("from Peticio",Peticio.class);
                peticions = q.list();
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

        return peticions;
    }

    private void handleException(Session ses, Exception ex, String errorMsg) throws Exception{
        if (ses.getTransaction() != null && ses.getTransaction().isActive()) {
            ses.getTransaction().rollback();
        }
        System.err.println(errorMsg + ": " + ex.getMessage());
        throw ex;
    }

    
}
