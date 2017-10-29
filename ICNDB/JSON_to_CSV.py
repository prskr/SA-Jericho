import json
import csv

def main():
    jokes = None
    with open('jokes.json', 'r') as jokes_json:
        jokes = json.load(jokes_json)

    categories = dict(zip(set([c for j in jokes for c in j['categories']]), range(5, 20, 5)))
    jokes_trans = [{ 'joke': str(j['joke']).replace("&quot;", "\'"), 'categoryId': str('NULL') if not j['categories'] else categories[j['categories'][0]]} for j in jokes]

    with open('jokes.csv', 'w', newline='', encoding='utf-8') as csv_file:
        csv_writer = csv.DictWriter(csv_file, ['joke', 'categoryId'], delimiter='|')
        csv_writer.writeheader()
        for joke_obj in jokes_trans:
            csv_writer.writerow(joke_obj)

    with open('categories.csv', 'w', newline='', encoding='utf-8') as csv_file:
        csv_writer = csv.DictWriter(csv_file, ['name', 'id'], delimiter='|')
        csv_writer.writeheader()
        for cat_entry in [{'name': key, 'id': value} for(key, value) in categories.items()]:
            csv_writer.writerow(cat_entry)


if __name__ == "__main__":
    main()
