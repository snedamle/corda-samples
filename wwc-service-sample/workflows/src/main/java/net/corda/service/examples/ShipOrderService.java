package net.corda.service.examples;

import net.corda.core.contracts.TransactionState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.node.AppServiceHub;
import net.corda.core.node.services.CordaService;
import net.corda.core.serialization.SingletonSerializeAsToken;
import net.corda.service.examples.states.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.corda.core.identity.Party;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//This long lived service has a trackBy method within directShipment method. This trackBy is triggered when an order is placed by the
//customer by calling the PlaceOrderFlow. Once it is triggered it calls the ShipOrderFlow to ship the order. This shows how you can get your work done
//inside a service.
@CordaService
public class ShipOrderService extends SingletonSerializeAsToken {
    private final static Logger log = LoggerFactory.getLogger(ShipOrderService.class);
    private final static Executor executor = Executors.newFixedThreadPool(8);
    private final AppServiceHub serviceHub;

    public ShipOrderService(AppServiceHub serviceHub) {
        this.serviceHub = serviceHub;
        directShipment();
    }

    private void directShipment() {
        Party ourIdentity = ourIdentity();//shipper
        //use trackBy to get updates when an order state is created in the vault.
        serviceHub.getVaultService().trackBy(OrderState.class).getUpdates().subscribe(
                update -> {
                    update.getProduced().forEach(
                            message -> {
                                TransactionState<OrderState> state = message.getState();
                                //only the shipper will ship the order once the order is created.
                                if (ourIdentity.equals(
                                        serviceHub.getNetworkMapCache().getPeerByLegalName(new CordaX500Name("Shipper", "San Diego", "US"))
                                )) {
                                    executor.execute(() -> {
                                        //this will ship the order
                                        serviceHub.startFlow(new ShipOrderFlow.ShipOrderFlowInitiator());
                                    });
                                }
                            }
                    );
                }
        );

    }

    private Party ourIdentity() {
        return serviceHub.getMyInfo().getLegalIdentities().get(0);
    }
}
