package net.corda.database.examples.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;


public class ProductContract implements Contract {

    @Override
    public void verify(LedgerTransaction tx) {

    }


    public interface Commands extends CommandData {
        class Create implements Commands {}
    }
}