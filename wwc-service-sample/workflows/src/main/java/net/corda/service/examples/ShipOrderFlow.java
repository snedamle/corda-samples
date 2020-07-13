package net.corda.service.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.service.examples.contracts.ShipmentStateContract;
import net.corda.service.examples.states.OrderState;
import net.corda.service.examples.states.ShipmentState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ShipOrderFlow {

    @InitiatingFlow
    @StartableByService
    public static class ShipOrderFlowInitiator extends FlowLogic<SignedTransaction> {

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            List<StateAndRef<OrderState>> wBStateList = getServiceHub().getVaultService().queryBy(OrderState.class).getStates();
            OrderState vaultState = wBStateList.get(wBStateList.size() - 1).getState().getData();
            ShipmentState output = new ShipmentState(vaultState.getCustomer(), getOurIdentity(),
                    vaultState.getRetailer(), vaultState.getProduct_id(), vaultState.getProduct_details());

            TransactionBuilder txBuilder = new TransactionBuilder(notary);
            txBuilder.addCommand(new ShipmentStateContract.Commands.Ship(), Arrays.asList(vaultState.getRetailer().getOwningKey(),
                    getOurIdentity().getOwningKey(), vaultState.getCustomer().getOwningKey()));//shipper and retailer are signers
            txBuilder.addOutputState(output);
            txBuilder.verify(getServiceHub());

            FlowSession session = initiateFlow(vaultState.getRetailer());
            FlowSession session1 = initiateFlow(vaultState.getCustomer());


            SignedTransaction ptx = getServiceHub().signInitialTransaction(txBuilder);

            SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, Arrays.asList(session, session1)));
            return subFlow(new FinalityFlow(stx, Arrays.asList(session, session1)));
        }
    }

    @InitiatedBy(ShipOrderFlowInitiator.class)
    public static class ShipOrderFlowResponder extends FlowLogic<Void> {

        private final FlowSession counterpartySession;

        public ShipOrderFlowResponder(FlowSession counterPartySession) {
            this.counterpartySession = counterPartySession;
        }

        @Suspendable
        @Override
        public Void call() throws FlowException {

            // Responder flow logic goes here.
            SignedTransaction stx = subFlow(new SignTransactionFlow(counterpartySession) {
                @Override
                protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {

                }
            });

            subFlow(new ReceiveFinalityFlow(counterpartySession, stx.getId()));
            return null;
        }
    }
}
