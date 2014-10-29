package ch.furrylittlefriends.hamsterhelper.util;

import com.activeandroid.serializer.TypeSerializer;

import org.joda.time.DateTime;

/**
 * Created with love by fork on 04.10.14.
 */
public class DateTimeDbSerializer extends TypeSerializer {
    @Override
    public Class<?> getDeserializedType() {
        return DateTime.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return Long.class;
    }

    @Override
    public Object serialize(Object o) {
        if (o == null) {
            return null;
        }
        DateTime dateTime = (DateTime) o;
        return ((DateTime) o).getMillis();
    }

    @Override
    public Object deserialize(Object o) {
        if (o == null) {
            return null;
        }
        Long millis = (Long) o;
        return new DateTime(millis);
    }
}
