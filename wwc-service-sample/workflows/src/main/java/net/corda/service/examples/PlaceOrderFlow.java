package net.corda.service.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.service.examples.contracts.OrderStateContract;
import net.corda.service.examples.states.OrderState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PlaceOrderFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class PlaceOrderFlowInitiator extends FlowLogic<SignedTransaction> {

        private Party retailer;
        private Party shipper;
        private Integer product_id;
        private String product_details;

        public PlaceOrderFlowInitiator(Party retailer, Party shipper, Integer product_id, String product_details) {
            this.retailer = retailer;
            this.shipper = shipper;
            this.product_id = product_id;
            this.product_details = product_details;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            OrderState output = new OrderState(getOurIdentity(), retailer, shipper, product_id, product_details);

            TransactionBuilder txBuilder = new TransactionBuilder(notary);
            txBuilder.addCommand(new OrderStateContract.Commands.PlaceOrder(), Arrays.asList(getOurIdentity().getOwningKey(),
                    retailer.getOwningKey(), shipper.getOwningKey()));
            txBuilder.addOutputState(output);

            txBuilder.verify(getServiceHub());

            FlowSession session = initiateFlow(retailer);
            FlowSession session1 = initiateFlow(shipper);
            SignedTransaction ptx = getServiceHub().signInitialTransaction(txBuilder);

            SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, Arrays.asList(session, session1)));
            return subFlow(new FinalityFlow(stx, Arrays.asList(session, session1)));
        }
    }

    @InitiatedBy(PlaceOrderFlowInitiator.class)
    public static class PlaceOrderFlowResponder extends FlowLogic<Void> {

        private final FlowSession counterpartySession;

        public PlaceOrderFlowResponder(FlowSession counterPartySession) {
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
