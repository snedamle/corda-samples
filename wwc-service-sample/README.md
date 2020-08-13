

## CordaService
Annotate any class that needs to be a long-lived service within the node, such as an oracle, with this annotation. Such a class needs to have a constructor with a single parameter of type AppServiceHub. This constructor will be invoked during node start to initialise the service. The service hub provided can be used to get information about the node that may be necessary for the service. Corda services are created as singletons within the node and are available to flows via ServiceHub.cordaService.

The service class has to implement SerializeAsToken to ensure correct usage within flows. (If possible extend SingletonSerializeAsToken instead as it removes the boilerplate.)

## Flow triggering using CordaService
The CordaService that triggers the flow is defined in ShipOrderService.java. The `CordaService` annotation is used by Corda to find any services that should be created on startup. In order for a flow to be startable by a service, the flow must be annotated with @StartableByService. An example is given in ShipOrderFlow.java.
You probably have noticed that `ShipOrderFlow` is not tagged with `@StartableByRPC` like flows normally are. That is, it will not show up in the node shell's flow list. The reason is that `ShipOrderFlow` is a completely automated process that does not need any external interactions, so it is ok to be "not-been-seen" from the RPC.

That said, CordaService broadly opens up the probabilities of writing automated flows and fast responding Cordapps! 

In this Cordapp, there are four parties: 
 - Retailer : takes the order from customer
 - Customer : places the order
 - Shipper : ships the product to customer
 
There are two states `OrderState` & `ShipmentState`, and two flows `PlaceOrderFlow` & `ShipOrderFlow`. 

This is the business logic - 

1. The customer places the order to retailer
2. The shipper ships the product to the customer.


## Running the demo 
Deploy and run the nodes by:

- Java use the `workflows-java:deployNodes` task and `./workflows-java/build/nodes/runnodes` script.

if you have any questions during setup, please go to https://docs.corda.net/getting-set-up.html for detailed setup instructions. 

Once all four nodes are started up, in Customers's node shell, run: 
```
start PlaceOrderFlowInitiator retailer : Retailer, shipper : Shipper , product_id : 2 , product_details : iphone11
```
This places an order to retailer.
This can be checked by checking retailers or shippers or customers vault.

    run vaultQuery contractStateType : net.corda.service.examples.states.OrderState

Once the order is placed, Corda service which is running uses trackBy to track the order and immediately ships the order by calling ShipOrderFlow

ShipOrderFlow ships the product to customer and creates ShipmentState
This can be checked by checking retailers or shippers or customers vault.

```
run vaultQuery contractStateType: net.corda.service.examples.states.ShipmentState
```

There are a few steps you need to take to ensure you create your service correctly. Firstly, annotate it with @CordaService and extend SingletonSerializeAsToken. Secondly, make sure that you inject them into your Flows in the correct way, which is pretty much anywhere but the constructor (or init in Kotlin). Lastly, remember to include AppServiceHub in the service’s constructor. Once you are able to use Corda services, you will be able to separate code out of your Flows. Not only making the Flows shorter, but also making them easier to understand while increasing the reusability of the code that you spent your valuable time writing.


Further Reading 

https://lankydan.dev/2018/08/19/corda-services-101

https://docs.corda.net/docs/corda-os/4.5/node-services.html
