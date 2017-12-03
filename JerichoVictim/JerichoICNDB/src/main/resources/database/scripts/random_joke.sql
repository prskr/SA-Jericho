CREATE OR REPLACE FUNCTION random_joke(OUT joke_text text, OUT category_text text)
  AS
$BODY$
  DECLARE
  _offset int := floor(random() * (SELECT count(*) FROM jokes));
  BEGIN
    SELECT joke AS "joke_text", coalesce(name, '') AS "category_text"
    FROM jokes j
    LEFT JOIN categories c ON j."categoryId" = c.id
    OFFSET _offset
    LIMIT 1
    INTO joke_text, category_text;
  END;
$BODY$
  LANGUAGE plpgsql VOLATILE;