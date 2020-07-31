# database-vaultquery cordapp 

This CorDapp demonstrates [QueryableState](https://docs.corda.net/docs/corda-os/api-persistence.html) works in Corda. Corda allows developers
to have the ability to expose some or all parts of their states to a custom database
table using an ORM tools. To support this feature the state must implement
`QueryableState`.

In this CorDapp we would use an `Product` state and persist its properties in a
custom table in the database. We have used
this `ProductDetail` to demonstrate _One-to-One_ relationship.

### Flows

There are two flow in this cordapp:

1. [IssueProductStateFlow]: It issues a product state.

2. [QueryDatabaseVaultQueryFlow]: This is used to retrieve the created product state from the vault.

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
