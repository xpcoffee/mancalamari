package org.mancalamari.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.mancalamari.game.*;

import java.io.IOException;
import java.util.ArrayList;

public class BoardDeserializer extends StdDeserializer<Board> {
    ObjectMapper objectMapper = new ObjectMapper();
    public BoardDeserializer() {
        super(Board.class);
    }

    protected BoardDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Board deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.readValueAsTree();
        JsonNode pitsNode = root.get("pits");
        ArrayList<Pit> pits = new ArrayList<>();

        pitsNode.forEach(pitNode -> {
            String type = pitNode.get("pitType").asText();

            if(PitType.Mancala.toString().equals(type)) {
                Pit pit = objectMapper.convertValue(pitNode, Mancala.class);
                pits.add(pit);
            } else {
                Pit pit = objectMapper.convertValue(pitNode, NormalPit.class);
                pits.add(pit);
            }
        });

        return new Board(pits);
    }
}
