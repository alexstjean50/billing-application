package ca.ulaval.glo4002.billing.persistence.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateQueryHelper<T>
{
    private final EntityManagerFactory entityManagerFactory;
    private final Class<T> persistentClass;

    public HibernateQueryHelper(Class<T> persistentClass, EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.persistentClass = persistentClass;
    }

    public List<T> findAll()
    {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.persistentClass);
        Root<T> rootEntry = criteriaQuery.from(this.persistentClass);
        CriteriaQuery<T> selectAllQuery = criteriaQuery.select(rootEntry);
        TypedQuery<T> query = entityManager.createQuery(selectAllQuery);

        return query.getResultList();
    }

    public void save(T entity)
    {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction()
                .begin();
        entityManager.merge(entity);
        entityManager.getTransaction()
                .commit();
    }
}
