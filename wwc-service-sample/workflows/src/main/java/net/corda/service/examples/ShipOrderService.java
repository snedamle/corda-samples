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
        serviceHub.getVaultService().trackBy(OrderState.class).getUpdates().subscribe(
                update -> {
                    update.getProduced().forEach(
                            message -> {
                                TransactionState<OrderState> state = message.getState();
                                if (ourIdentity.equals(
                                        serviceHub.getNetworkMapCache().getPeerByLegalName(new CordaX500Name("Shipper", "San Diego", "US"))
                                )) {
                                    executor.execute(() -> {
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
