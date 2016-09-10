package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Sense {

    public final Set<String> pos = new HashSet<>();
    public final Set<String> field = new HashSet<>();
    public final Set<String> misc = new HashSet<>();
    public String info = "";
    public final Set<String> dial = new HashSet<>();
    public final Set<String> gloss = new HashSet<>();

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
        if (!pos.isEmpty()) {
            builder.append("pos", pos);
        }

        if (!field.isEmpty()) {
            builder.append("field", field);
        }

        if (!misc.isEmpty()) {
            builder.append("misc", misc);
        }

        if (!dial.isEmpty()) {
            builder.append("dial", dial);
        }

        builder.append("gloss", gloss);

        return builder.toString();
    }


    public static Sense fromString(String value) {
        Sense sense = new Sense();

        String[] values = value.split("/");

        Collections.addAll(sense.pos, values[0].split(";"));
        Collections.addAll(sense.field, values[1].split(";"));
        Collections.addAll(sense.misc, values[2].split(";"));
        sense.info = values[3];
        Collections.addAll(sense.dial, values[4].split(";"));
        Collections.addAll(sense.gloss, values[5].split(";"));

        return sense;
    }
}
