# database-entitymanager-jdbcsession cordapp 

This CorDapp demonstrates [EntityManager and JDBCSession](https://docs.corda.net/docs/corda-os/4.5/api-persistence.html#jpa-support) works in Corda.
ServiceHub exposes the Java Persistence API to flows via the withEntityManager method. This method can be used to persist and query entities which 
inherit from MappedSchema. 
This is particularly useful if off-ledger data must be maintained in conjunction with on-ledger state data.

Apps may also interact directly with the underlying Node’s database by using a standard JDBC connection (session) as described by the Java SQL Connection API


### Flows

There are two flow in this cordapp:

1. [QueryDatabaseJdbcFlow]: This show hows to create and retrieve Product object using Jdbc.

2. [QueryDatabaseEntityManagerFlow]: This shows how to create and retrieve Product object using EntityManager.

## Usage

### Pre-requisites:
See https://docs.corda.net/getting-set-up.html.

### Running the CorDapp

Open a terminal and go to the project root directory and type: (to deploy the nodes using bootstrapper)
```
./gradlew clean deployNodes
```
Then type: (to run the nodes)
```
./build/nodes/runnodes
```

### Connecting to the Database

The JDBC url to connect to the database would be printed in the console in node
startup. Use the url to connect to the database using a suitable client. The
default username is 'sa' and password is '' (blank).
You could download H2 Console to connect to h2 database here:
http://www.h2database.com/html/download.html

<p align="center">
  <img src="./clients/src/main/resources/static/JDBC-url.png" alt="Database URL" width="400">
</p>

Refer here for more details regarding connecting to the node database.
https://docs.corda.net/head/node-database-access-h2.html
