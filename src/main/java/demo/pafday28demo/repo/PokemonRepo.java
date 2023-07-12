package demo.pafday28demo.repo;

import java.util.ArrayList;
import java.util.List;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PokemonRepo {

@Autowired
private MongoTemplate template;

private final String P_NAME = "name";
private final String P_POKEMON = "pokemon";
private final String P_TYPE = "type";

public List<Document> getPokemonByName(String name) {
    //make query case insensitive
    Criteria criteria = Criteria.where(P_NAME).regex(name,"i");
    // Criteria criteria = Criteria.where(P_NAME).is(name);
    Query query = Query.query(criteria);

    return template.find(query, Document.class, P_POKEMON);
}

//     db.pokemon.aggregate([
//     {$unwind: "$type"},
//     {$group: {_id: "$type"}},
//     {$sort: {_id:1}}
// ])

public List<Document> getPokemonTypes() {
    UnwindOperation unw = Aggregation.unwind("type");
    GroupOperation grp = Aggregation.group("type");
    SortOperation srt = Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id"));

    Aggregation pipeline = Aggregation.newAggregation(unw, grp, srt);
    return template.aggregate(pipeline, P_POKEMON, Document.class).getMappedResults();
}

public List<String> getType() {
    List<String> distinctTypes = template.getCollection(P_POKEMON).distinct("type", String.class).into(new ArrayList<>());
    return distinctTypes;
}

public List<Document> getPokemonByType(String type) {

    Criteria criteria = Criteria.where(P_TYPE).is(type);
    Query query = Query.query(criteria);

    return template.find(query, Document.class, P_POKEMON);

}

}
