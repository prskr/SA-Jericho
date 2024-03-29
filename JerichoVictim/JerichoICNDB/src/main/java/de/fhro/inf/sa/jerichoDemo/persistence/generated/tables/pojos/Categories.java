/*
 * This file is generated by jOOQ.
*/
package de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos;


import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.interfaces.ICategories;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Categories implements ICategories {

    private static final long serialVersionUID = -266798486;

    private Integer id;
    private String  name;

    public Categories() {}

    public Categories(Categories value) {
        this.id = value.id;
        this.name = value.name;
    }

    public Categories(
        Integer id,
        String  name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public Categories setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Categories setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Categories (");

        sb.append(id);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void from(ICategories from) {
        setId(from.getId());
        setName(from.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends ICategories> E into(E into) {
        into.from(this);
        return into;
    }

    public Categories(io.vertx.core.json.JsonObject json) {
        fromJson(json);
    }
}
