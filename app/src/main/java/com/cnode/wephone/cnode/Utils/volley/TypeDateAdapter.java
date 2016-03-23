package com.cnode.wephone.cnode.Utils.volley;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 解析日期 重写Gson TypeAdapter方法
 * Created by ASUS on 2016/3/23.
 */
public class TypeDateAdapter extends TypeAdapter<Date> {

    private final DateFormat dateFormat;

    public TypeDateAdapter(){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(dateFormat.format(value));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String json = in.nextString();

        try {
            return dateFormat.parse(json);
        } catch (ParseException e) {
            throw new JsonSyntaxException(json, e);
        }
    }
}
