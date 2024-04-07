package com.vicary.pricety.configuration;

import com.vicary.pricety.controller.database_import.DatabaseSetterController;
import com.vicary.pricety.entity.Identifiable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.stream.Stream;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {

        if (!DatabaseSetterController.done) {
            Identifiable identifier = (Identifiable) obj;
            return identifier.getId();
        }

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream ids = session.createQuery(query).stream();

        Long max = ids.mapToLong(e -> Long.parseLong(e.toString())).max().orElse(0L);

        return max + 1;
    }
}
