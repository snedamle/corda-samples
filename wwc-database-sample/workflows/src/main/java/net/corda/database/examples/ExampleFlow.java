package net.corda.database.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.database.examples.contracts.ProductContract;
import net.corda.database.examples.states.ProductState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.util.Arrays;

public class ExampleFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {

        private final Party otherParty;

        public Initiator(Party otherParty) {
            this.otherParty = otherParty;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {


            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            ProductState productState = new ProductState(1, "test1", "prop1", otherParty, getOurIdentity(), new UniqueIdentifier());
            final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addOutputState(productState)
                    .addCommand(new ProductContract.Commands.Create(), getOurIdentity().getOwningKey());

            txBuilder.verify(getServiceHub());

            final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);

            FlowSession otherPartySession = initiateFlow(otherParty);

            return subFlow(new FinalityFlow(partSignedTx, Arrays.asList(otherPartySession)));
        }
    }

    @InitiatedBy(Initiator.class)
    public static class Acceptor extends FlowLogic<SignedTransaction> {

        private FlowSession counterpartySession;

        public Acceptor(FlowSession counterpartySession) {
            this.counterpartySession = counterpartySession;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            return subFlow(new ReceiveFinalityFlow(counterpartySession));
        }
    }
}
