package net.corda.database.examples.states;

import net.corda.core.contracts.ContractState;
import net.corda.database.examples.contracts.ProductContract;
import net.corda.database.examples.schema.ProductSchemaV1;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.List;

//1 state can map to multiple schemas. state will always be stored on ledger. If you want to map your state to custom schemas/ custom tables, and you want to
//query these custom schemas(which are stored off ledger) , then make sure to implement state with QueryableState
@BelongsToContract(ProductContract.class)
public class ProductState implements ContractState, QueryableState {
    private final Integer sku;
    private final String name;
    private final Integer detail_id;
    private final String detail;
    private final Party issuer;
    private final Party owner;

    public ProductState(Integer sku, String name, Integer detail_id, String detail, Party issuer, Party owner) {
        this.sku = sku;
        this.name = name;
        this.detail_id = detail_id;
        this.detail = detail;
        this.issuer = issuer;
        this.owner = owner;
    }

    public Integer getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public Party getIssuer() {
        return issuer;
    }

    public Party getOwner() {
        return owner;
    }

    public Integer getDetail_id() {
        return detail_id;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "sku : " + sku+ "name : " + name+ "issuer : " + issuer+ "owner : " + owner;
    }

    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner, issuer);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof ProductSchemaV1) {
            return new ProductSchemaV1.PersistentProduct(
                    getSku(),
                    getName(),
                    new ProductSchemaV1.PersistentProductDetail(getDetail(), getDetail_id()));
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return Arrays.asList(new ProductSchemaV1());
    }
}