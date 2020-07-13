package net.corda.service.examples.states;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.service.examples.contracts.OrderStateContract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BelongsToContract(OrderStateContract.class)
public class OrderState implements ContractState {

    private Party customer;
    private Party retailer;
    private Party shipper;
    private Integer product_id;
    private String product_details;

    public OrderState(Party customer, Party retailer, Party shipper, Integer product_id, String product_details) {
        this.customer = customer;
        this.retailer = retailer;
        this.shipper = shipper;
        this.product_id = product_id;
        this.product_details = product_details;
    }

    public Party getCustomer() {
        return customer;
    }

    public Party getRetailer() {
        return retailer;
    }

    public Party getShipper() {
        return shipper;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public String getProduct_details() {
        return product_details;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(customer, retailer, shipper);
    }
}
