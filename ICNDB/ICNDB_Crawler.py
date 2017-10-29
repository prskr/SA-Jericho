""" this module is for crawling all entries from the ICNDB """

import http.client
import json

def crawl_all_jokes():
    connection = http.client.HTTPSConnection("api.icndb.com")
    headers = {"Content-Type": "application/json"}

    connection.request("GET", "/jokes/count", None, headers=headers)
    count_req_body = json.loads(connection.getresponse().read().decode())

    jokes = []
    jokes_count = int(count_req_body['value'])
    joke_id = 0

    print(str.format("There are {} Chuck Norris jokes available", jokes_count))
    while len(jokes) < jokes_count:
        connection.request("GET", str.format("/jokes/{}", joke_id), None, headers=headers)
        joke_id += 1
        joke_body = json.loads(connection.getresponse().read().decode())
        if joke_body['type'] == "success":
            # remove id value
            joke_body['value'].pop('id')
            jokes.append(joke_body['value'])
        if joke_id >= 1000:
            break
    return jokes


def main():
    jokes = crawl_all_jokes()
    with open('jokes.json', 'w') as outfile:
        json.dump(jokes, outfile)
    print(str.format("Crawled {} jokes", len(jokes)))

if __name__ == "__main__": main()
