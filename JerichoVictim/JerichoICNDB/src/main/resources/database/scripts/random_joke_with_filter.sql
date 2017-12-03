CREATE OR REPLACE FUNCTION random_joke_with_filter(categories_filter CHARACTER VARYING [], OUT joke_text TEXT,
                                                                                           OUT category_text TEXT)
AS
$BODY$
DECLARE
  _offset INT := floor(random() * (
    SELECT count(*)
    FROM jokes j
      JOIN categories cat ON j."categoryId" = cat.id
    WHERE cat.name in (SELECT unnest(categories_filter))
  ));
BEGIN
  SELECT
    joke               AS "joke_text",
    coalesce(name, '') AS "category_text"
  FROM jokes j
    LEFT JOIN categories c ON j."categoryId" = c.id
  WHERE c.name IN (SELECT unnest(categories_filter))
  OFFSET _offset
  LIMIT 1
  INTO joke_text, category_text;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;