package com.demo.common.utilities;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.Instant;
import java.time.LocalDate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateJsonDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        SimpleDateFormat format = new SimpleDateFormat(Constant.API_FORMAT_DATE);
        String date = jsonParser.getText();
        try {

            return format.parse(date);
        } catch (ParseException e) {

            return java.sql.Date.valueOf(LocalDate.of(1111, 11, 11));
        }

    }
}
