package de.fh.dortmund.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.UUID;

@Data
public class Tag {
    @SerializedName("_id")
    private String id = UUID.randomUUID().toString();

    @SerializedName("Name")
    private String name;

    @SerializedName("Info")
    private String info;

    public Tag(String tagName, String info) {
        this.name = tagName;
        this.info = info;
    }

    public Tag(JsonObject jsonObject) {
        this.id = jsonObject.get("_id").getAsString();
        this.name = jsonObject.get("name").getAsString();
        this.info = jsonObject.get("info").getAsString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;

        return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
