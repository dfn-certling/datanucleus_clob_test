package de.dfncert.datanucleus_clob_test;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Test
{
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
    long id;

    @Column(jdbcType="CLOB")
    String string;

    public Test(String string)
    {
        this.string = string;
    }

    public String toString() {
        return getClass().getName() + "(" + id + " - " + string + ")";
    }
}
