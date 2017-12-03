CREATE OR REPLACE FUNCTION random_joke(OUT joke_id INT, OUT joke_text TEXT, OUT category_text TEXT)
AS
$BODY$
DECLARE
  _offset INT := floor(random() * (SELECT count(*)
                                   FROM jokes));
BEGIN
  SELECT
    j.id               AS "joke_id",
    joke               AS "joke_text",
    coalesce(name, '') AS "category_text"
  FROM jokes j
    LEFT JOIN categories c ON j."categoryId" = c.id
  OFFSET _offset
  LIMIT 1
  INTO joke_id, joke_text, category_text;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;