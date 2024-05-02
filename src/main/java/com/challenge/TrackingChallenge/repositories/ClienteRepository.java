package com.challenge.TrackingChallenge.repositories;

import com.challenge.TrackingChallenge.domain.Cliente.Cliente;
import com.challenge.TrackingChallenge.exception.EntityNotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Cliente salvar(Cliente cliente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(cliente);
            transaction.commit();
            return cliente;

        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Erro ao salvar no banco de dados! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }

    public Cliente listarPorId(long id) {
        Session session = sessionFactory.openSession();

        try {
            String sql = "FROM ClienteFisica WHERE id = :id";
            Query query = session.createQuery(sql);
            query.setParameter("id", id);
            return (Cliente) query.uniqueResult();
        } catch (HibernateException he) {
            throw new HibernateException("Erro ao consultar no banco de dados! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }

    public Cliente listarPorCpf(String cpf) {
        Session session = sessionFactory.openSession();

        try {
            String sql = "FROM ClienteFisica WHERE cpf = :cpf";
            Query query = session.createQuery(sql);
            query.setParameter("cpf", cpf);
            return (Cliente) query.uniqueResult();
        } catch (HibernateException he) {
            throw new HibernateException("Erro ao consultar no banco de dados! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }

    public Cliente listarPorCnpj(String cnpj) {
        Session session = sessionFactory.openSession();

        try {
            String sql = "FROM ClienteJuridica WHERE cnpj = :cnpj";
            Query query = session.createQuery(sql);
            query.setParameter("cnpj", cnpj);
            return (Cliente) query.uniqueResult();
        } catch (HibernateException he) {
            throw new HibernateException("Erro ao buscar no banco de dados! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }

    public Cliente atualizar(Cliente cliente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Cliente clienteAtualizado = session.merge(cliente);
            transaction.commit();
            return clienteAtualizado;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Erro ao atualizar no banco de dados! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }

    public void deletar(long id){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
                transaction.commit();
            } else {
                throw new EntityNotFoundException("Usuário não encontrado no banco de dados");
            }
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Erro ao deletar! Causa: " + he.getMessage());
        } finally {
            session.close();
        }
    }
}