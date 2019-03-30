/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cc.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;

import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.SubTipo;

/**
 *
 * @author Usuario
 */
public abstract class GenericDAO<T extends Serializable> {

    @PersistenceContext(unitName = "ccomissao")
    private final EntityManager entityManager;
    private final Class<T> persistentClass;

    public GenericDAO() {
        this.entityManager = EntityManagerUtil.getEntityManager();
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    protected void save(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            getEntityManager().persist(entity);
            tx.commit();
            //close();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        }
    }

    protected void remove(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            getEntityManager().remove(entity);
            tx.commit();
            //close();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        }
    }
    
    public void remover(Categoria categoria){
    	EntityTransaction tx = getEntityManager().getTransaction();
    	tx.begin();
    	getEntityManager().remove(getEntityManager().getReference(Categoria.class, categoria.getId()));
    	tx.commit();
    }
    
    public void remover(SubTipo subTipo){
    	EntityTransaction tx = getEntityManager().getTransaction();
    	tx.begin();
    	getEntityManager().remove(getEntityManager().getReference(SubTipo.class, subTipo.getId()));
    	tx.commit();
    }

    protected T saveOrUpdate(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            entity = getEntityManager().merge(entity);
            tx.commit();
            //close();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        }
        return entity;
    }

    public List<T> findAll() {
        Session session = (Session) getEntityManager().getDelegate();
        return session.createCriteria(persistentClass).list();
    }

    public <T> List<T> getList(String query, Object... values) {
        javax.persistence.Query query1 = createQuery(query, values);
        return query1.getResultList();
    }

    public <T> T getPojo(Class<T> classToCast, Serializable primaryKey) {
        return entityManager.find(classToCast, primaryKey);
    }

    @SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getPojo(String query, Object... values) {
        javax.persistence.Query query1 = createQuery(query, values);
        return (T) query1.getSingleResult();
    }
    


    private javax.persistence.Query createQuery(String query, Object[] values) {
        javax.persistence.Query qr = entityManager.createQuery(query);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object object = values[i];
                qr.setParameter(i + 1, object);
            }
        }
        return qr;
    }

    private void close() {
        if (getEntityManager().isOpen()) {
            getEntityManager().close();
        }
        //shutdown();
    }

    private void shutdown() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery("SHUTDOWN").executeUpdate();
        em.close();
    }
}
