package com.example.hiarflow;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    static SessionFactory sessionFactory;
    public  static SessionFactory getSessionFactory() {

        try{

            sessionFactory = new Configuration().configure().buildSessionFactory();

        }catch(HibernateException e) { e.printStackTrace();}

        return sessionFactory;


    }
}
