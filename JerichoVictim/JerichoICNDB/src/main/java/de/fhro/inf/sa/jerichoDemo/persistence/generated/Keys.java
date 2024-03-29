/*
 * This file is generated by jOOQ.
*/
package de.fhro.inf.sa.jerichoDemo.persistence.generated;


import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.Categories;
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.Jokes;
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.CategoriesRecord;
import de.fhro.inf.sa.jerichoDemo.persistence.generated.tables.records.JokesRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<CategoriesRecord, Integer> IDENTITY_CATEGORIES = Identities0.IDENTITY_CATEGORIES;
    public static final Identity<JokesRecord, Integer> IDENTITY_JOKES = Identities0.IDENTITY_JOKES;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CategoriesRecord> PK_CATEGORIES = UniqueKeys0.PK_CATEGORIES;
    public static final UniqueKey<JokesRecord> PK_JOKES = UniqueKeys0.PK_JOKES;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<JokesRecord, CategoriesRecord> JOKES__FK_CATEGORY_ID = ForeignKeys0.JOKES__FK_CATEGORY_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<CategoriesRecord, Integer> IDENTITY_CATEGORIES = createIdentity(Categories.CATEGORIES, Categories.CATEGORIES.ID);
        public static Identity<JokesRecord, Integer> IDENTITY_JOKES = createIdentity(Jokes.JOKES, Jokes.JOKES.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<CategoriesRecord> PK_CATEGORIES = createUniqueKey(Categories.CATEGORIES, "pk_categories", Categories.CATEGORIES.ID);
        public static final UniqueKey<JokesRecord> PK_JOKES = createUniqueKey(Jokes.JOKES, "pk_jokes", Jokes.JOKES.ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<JokesRecord, CategoriesRecord> JOKES__FK_CATEGORY_ID = createForeignKey(de.fhro.inf.sa.jerichoDemo.persistence.generated.Keys.PK_CATEGORIES, Jokes.JOKES, "jokes__fk_category_id", Jokes.JOKES.CATEGORYID);
    }
}
