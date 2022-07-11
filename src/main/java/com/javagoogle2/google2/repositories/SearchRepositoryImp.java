package com.javagoogle2.google2.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.javagoogle2.google2.entities.WebPage;

@Repository
public class SearchRepositoryImp implements SearchRepository{

    //esto es para poder interactuar con las bdd
    @PersistenceContext
    EntityManager entityManager;



    @Transactional //envolver varios llamados en una sola consulta
    @Override
    public List<WebPage> search(String textSearch) {
        String query = "FROM WebPage WHERE description like : textSearch"; //esto no es sql, es sql de HIBERNATE. por eso no hay select ni nada

        return entityManager.createQuery(query)
            .setParameter("textSearch", "%"+textSearch+ "%") //que compare y traiga cualquier texto que tenga ese textsearch y que termine con cualquier texto
            .getResultList(); //me salta el mensaje porque dice que la query puede estar vacía, pero no es necesario controlar eso ahora
    }


    @Transactional
    @Override
    public void save(WebPage webPage) {
        entityManager.merge(webPage);

    }


    @Override
    public WebPage getByUrl(String url) {
        String query = "FROM WebPage WHERE url = :url";
        List<WebPage> list = entityManager.createQuery(query)
            .setParameter("url", url)
            .getResultList();

        return list.size() == 0 ? null : list.get(0);
    }


    @Override
    public boolean exist(String url) {
        return getByUrl(url) != null;
    }


    @Override
    public List<WebPage> getLinksToindex() {
        //String query = "FROM WebPage WHERE title is null OR description is null"; //esto no es sql, es sql de HIBERNATE. por eso no hay select ni nada
        String query = "FROM WebPage"; //ACA ESTOY TRAYENDO TODAS LAS BASURA. TENGO QUE PONER LO DE ARRIBA SI SOLO QUIERO MOSTRAR LO IMPORTANTE

        return entityManager.createQuery(query)
            .setMaxResults(100)
            .getResultList(); //me salta el mensaje porque dice que la query puede estar vacía, pero no es necesario controlar eso ahora
    }
    
}
