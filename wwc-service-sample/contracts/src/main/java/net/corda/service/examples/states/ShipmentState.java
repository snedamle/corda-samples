package net.corda.service.examples.states;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.service.examples.contracts.ShipmentStateContract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BelongsToContract(ShipmentStateContract.class)
public class ShipmentState implements ContractState {

    private Party customer;
    private Party shipper;
    private Party retailer;
    private Integer product_id;
    private String product_details;

    public ShipmentState(Party customer, Party shipper, Party retailer , Integer product_id, String product_details) {
        this.customer = customer;
        this.shipper = shipper;
        this.retailer = retailer;
        this.product_id = product_id;
        this.product_details = product_details;
    }

    public Party getCustomer() {
        return customer;
    }

    public Party getShipper() {
        return shipper;
    }

    public Party getRetailer() {
        return retailer;
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
        return Arrays.asList(customer, shipper, retailer);
    }
}
