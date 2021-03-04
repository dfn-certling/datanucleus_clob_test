# DataNucleus CLOB test

This project tests changes for columns with jdbcType CLOB in PostgreSQL
databases between DataNucleus versions. The DataNucleus version is given by the
property datanucleusVersion in pom.xml.

By default it connects to a local PostgreSQL database `test` using username and
password `test`.

The main function accepts two different commands to store and retrieve test
objects from the database.

`put [string]` - stores an object in the database, if `string` is not specified
it uses `foo`.

`get [id]` - retrieves the object with the given `id` from the database or all
objects if `id` is missing.

## CLOB change between 5.2.2 and 5.2.3

Compiling the project with DataNucleus version 5.2.2 and putting an object into
the database yields

```
> mvn exec:java -Dexec.args="put 5.2.2"
Putting object into database
de.dfncert.datanucleus_clob_test.Test(1 - 5.2.2)
> mvn exec:java -Dexec.args="get"
Getting all object from database
de.dfncert.datanucleus_clob_test.Test(1 - 5.2.2)
```

Now changing to 5.2.3 results in

```
> mvn exec:java -Dexec.args="get"
Getting all object from database
de.dfncert.datanucleus_clob_test.Test(1 - 16611)
> mvn exec:java -Dexec.args="put 5.2.3"
Putting object into database
de.dfncert.datanucleus_clob_test.Test(11 - 5.2.3)
> mvn exec:java -Dexec.args="get"
Getting all object from database
de.dfncert.datanucleus_clob_test.Test(11 - 5.2.3)
de.dfncert.datanucleus_clob_test.Test(1 - 16611)
```

The previously written string cannot be read anymore.

The log file shows a different column mapping being used for the string column.

5.2.2:
```
[DataNucleus.Datastore.Schema] - Attempt to find JDBC driver 'typeInfo' for jdbc-type=CLOB but sql-type=VARCHAR is not found. Using default sql-type for this jdbc-type.
[DataNucleus.Datastore.Schema] - Field [de.dfncert.datanucleus_clob_test.Test.string] -> Column(s) ["TEST"."STRING"] using mapping of type "org.datanucleus.store.rdbms.mapping.java.StringMapping" (org.datanucleus.store.rdbms.mapping.column.ClobColumnMapping)
```

5.2.3:
```
[DataNucleus.Datastore.Schema] - Attempt to find JDBC driver 'typeInfo' for jdbc-type=LONGVARCHAR but sql-type=VARCHAR is not found. Using default sql-type for this jdbc-type.
[DataNucleus.Datastore.Schema] - Field [de.dfncert.datanucleus_clob_test.Test.string] -> Column(s) ["TEST"."STRING"] using mapping of type "org.datanucleus.store.rdbms.mapping.java.StringMapping" (org.datanucleus.store.rdbms.mapping.column.LongVarcharColumnMapping)
```
