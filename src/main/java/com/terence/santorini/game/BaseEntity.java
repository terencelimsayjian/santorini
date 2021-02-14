package com.terence.santorini.game;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@MappedSuperclass
@TypeDefs({
  @TypeDef(name = "json", typeClass = JsonStringType.class),
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class BaseEntity {}
