package demo.pafday28demo.model;

import org.bson.types.ObjectId;

public record Pokemon(
    ObjectId _id,
    String img,
    String name,
    String type,
    String stats
) {
    
}
