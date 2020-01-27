package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.entities.AbstractEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public interface TransactionHandler {
    default void performTransaction(@NotNull Transactions transactionType, @NotNull AbstractEntity entity) {
        Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
        Transaction txl = session.beginTransaction();
        if (transactionType.equals(TransactionHandler.Transactions.DELETE))
            session.delete(entity);
        else if (transactionType.equals(TransactionHandler.Transactions.UPDATE))
            session.update(entity);
        else if (transactionType.equals(TransactionHandler.Transactions.SAVE))
            session.save(entity);
        txl.commit();
    }

    enum Transactions {
        SAVE,
        UPDATE,
        DELETE
    }
}
