/*
 * This file is generated by jOOQ.
*/
package de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.daos;


import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.Categories;
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.CategoriesRecord;

import io.github.jklingsporn.vertx.jooq.async.future.VertxDAO;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


import java.util.concurrent.CompletableFuture;
import io.github.jklingsporn.vertx.jooq.async.future.AsyncJooqSQLClient;
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
public class CategoriesDao extends DAOImpl<CategoriesRecord, de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories, Integer> implements VertxDAO<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.CategoriesRecord,de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories,java.lang.Integer> {

    /**
     * Create a new CategoriesDao without any configuration
     */
    public CategoriesDao() {
        super(Categories.CATEGORIES, de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories.class);
    }

    /**
     * Create a new CategoriesDao with an attached configuration
     */
    public CategoriesDao(Configuration configuration) {
        super(Categories.CATEGORIES, de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories> fetchById(Integer... values) {
        return fetch(Categories.CATEGORIES.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories fetchOneById(Integer value) {
        return fetchOne(Categories.CATEGORIES.ID, value);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories> fetchByName(String... values) {
        return fetch(Categories.CATEGORIES.NAME, values);
    }

    /**
     * Fetch records that have <code>id IN (values)</code> asynchronously
     */
    public CompletableFuture<List<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories>> fetchByIdAsync(List<Integer> values) {
        return fetchAsync(Categories.CATEGORIES.ID,values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code> asynchronously
     */
    public CompletableFuture<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories> fetchOneByIdAsync(Integer value) {
        return fetchOneAsync(Categories.CATEGORIES.ID,value);
    }

    /**
     * Fetch records that have <code>name IN (values)</code> asynchronously
     */
    public CompletableFuture<List<de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories>> fetchByNameAsync(List<String> values) {
        return fetchAsync(Categories.CATEGORIES.NAME,values);
    }

    private AsyncJooqSQLClient client;

    @Override
    public void setClient(AsyncJooqSQLClient client) {
        this.client = client;
    }

    @Override
    public AsyncJooqSQLClient client() {
        return this.client;
    }

    @Override
    public java.util.function.Function<io.vertx.core.json.JsonObject, de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories> jsonMapper() {
        return de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.pojos.Categories::new;
    }

}
