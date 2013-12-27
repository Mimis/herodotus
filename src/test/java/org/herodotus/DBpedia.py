import json
from urlfetch import get
u = "http://dbpedia.org/data/Aquarium_of_Rhodes.json"
data = get(url=u)
json_data = json.loads(data.content)


print [abstract['value'] for abstract in json_data["http://dbpedia.org/resource/Aquarium_of_Rhodes"]["http://dbpedia.org/ontology/abstract"] if abstract['lang'] == 'en'][0]
print [thumbnail['value'] for abstract in json_data["http://dbpedia.org/resource/Aquarium_of_Rhodes"]["http://dbpedia.org/ontology/thumbnail"][0]