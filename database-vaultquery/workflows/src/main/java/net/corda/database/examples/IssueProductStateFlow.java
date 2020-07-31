package net.corda.database.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.database.examples.contracts.ProductContract;
import net.corda.database.examples.states.ProductState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.util.Arrays;

/**
 * This flow is responsible to do 4 steps
 * 1. create a new output state called product state
 * 2. create transaction builder by specifying the output state and the parties who will sign the transaction.
 * 3. check if contract rules are satisfied by calling verify method
 * 4. self sign the transaction
 * 5. call finality flow to notarize and save the state in the vault.
 */
public class IssueProductStateFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {

        private final Party issuer;

        public Initiator(Party issuer) {
            this.issuer = issuer;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            ProductState productState = new ProductState(1, "test1", 101, "details", issuer, getOurIdentity());

            final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addOutputState(productState)
                    .addCommand(new ProductContract.Commands.Create(), getOurIdentity().getOwningKey());

            txBuilder.verify(getServiceHub());

            final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);

            FlowSession otherPartySession = initiateFlow(issuer);

            return subFlow(new FinalityFlow(partSignedTx, Arrays.asList(otherPartySession)));
        }
    }

    //call ReceiveFinalityFlow on the counter party side to save the state in their db
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
