package net.corda.service.examples.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;

// ************
// * Contract *
// ************
public class OrderStateContract implements Contract {
    // Used to identify our contract when building a transaction.
    public static final String ID = "net.corda.examples.autopayroll.contracts.MoneyStateContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        // Verification logic goes here.
        CommandWithParties<Commands> cmd = requireSingleCommand(tx.getCommands(), Commands.class);


    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class PlaceOrder implements Commands {}
    }
}
