package demo.pafday28demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.pafday28demo.model.Pokemon;
import demo.pafday28demo.model.PokemonType;
import demo.pafday28demo.repo.PokemonRepo;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepo pRepo;

    public List<Document> findPokemonByName(String name) {
        List<Document> result = pRepo.getPokemonByName(name);
        if (result.isEmpty()) {
            throw new NoSuchElementException("No such pokemon");
        }

        return result;

    }

    public Pokemon docToPokemon(Document d) {
        List<String> typeList = d.getList("type", String.class);
        Document stats = d.get("stats", Document.class);
        String statsString = stats.toJson();

        return new Pokemon(
                d.getObjectId("_id"),
                d.getString("img"),
                d.getString("name"),
                typeList.toString(),
                statsString
                );
    }
    
    public PokemonType docToType(Document doc) {
        return new PokemonType(
            doc.getString("_id")
        );
    }

    public List<Document> findPokemonType() {
        return pRepo.getPokemonTypes();
    }

    public List<String> getType() {
        return pRepo.getType();
    }

    public List<Document> findPokemonByType(String type) {
        return pRepo.getPokemonByType(type);
    }

}

    

    /* based on this data
    "_id" : ObjectId("64ae004efb073d3c1504b4bd"),
    "id" : "002",
    "name" : "Ivysaur",
    "img" : "http://img.pokemondb.net/artwork/ivysaur.jpg",
    "type" : [
        "Grass",
        "Poison"
    ],
    "stats" : {
        "hp" : "60",
        "attack" : "62",
        "defense" : "63",
        "spattack" : "80",
        "spdefense" : "80",
        "speed" : "60"
    },
    */