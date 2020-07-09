package net.corda.database.examples.states;

import net.corda.database.examples.contracts.ProductContract;
import net.corda.database.examples.schema.ProductSchemaV1;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.List;

//1 state can map to multiple schemas. state will always be stored on ledger. If you want to map your state to custom schemas/ custom tables, and you want to
//query them , then make sure to implement state with QueryableState.
@BelongsToContract(ProductContract.class)
public class ProductState implements LinearState, QueryableState {
    private final Integer sku;
    private final String name;
    private final String property;
    private final Party issuer;
    private final Party owner;
    private final UniqueIdentifier id;
    //add product detail here

    public ProductState(Integer sku, String name, String property, Party issuer, Party owner, UniqueIdentifier id) {
        this.sku = sku;
        this.name = name;
        this.property = property;
        this.issuer = issuer;
        this.owner = owner;
        this.id = id;
    }

    public Integer getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getProperty() {
        return property;
    }

    public Party getIssuer() {
        return issuer;
    }

    public Party getOwner() {
        return owner;
    }

    public UniqueIdentifier getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Id : " + id + "sku : " + sku+ "name : " + name+ "issuer : " + issuer+ "property : " + property+ "owner : " + owner;
    }

    @Override public UniqueIdentifier getLinearId() { return id; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof ProductSchemaV1) {
            return new ProductSchemaV1.PersistentProduct(
                    getId().getId(),
                    getSku(),
                    getName(),
                    getIssuer().getName().toString(), getProperty(),
                    getOwner().getName().toString(),
                    new ProductSchemaV1.PersistentProductDetail("", new Integer(1)));
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return Arrays.asList(new ProductSchemaV1());
    }
}