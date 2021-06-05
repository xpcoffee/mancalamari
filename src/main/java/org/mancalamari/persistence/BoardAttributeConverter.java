package org.mancalamari.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.mancalamari.game.Board;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Log4j2
@Converter
public class BoardAttributeConverter implements AttributeConverter<Board, String> {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Board board) {
        String value = null;
        try {
            value = objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            log.error("Error converting board into JSON", e);
        }

        return value;
    }

    @Override
    public Board convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Board.class);
        } catch (IOException e) {
            log.error("Error reading JSON into board", e);
        }
        return null;
    }
}
