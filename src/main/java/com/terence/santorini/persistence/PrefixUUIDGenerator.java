package com.terence.santorini.persistence;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class PrefixUUIDGenerator implements IdentifierGenerator, Configurable {
  private String prefix;

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    return prefix + "-" + UUID.randomUUID();
  }

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
      throws MappingException {
    prefix = params.getProperty("prefix");
  }
}
