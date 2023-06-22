package de.fh.dortmund.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import de.fh.dortmund.models.enums.PostType;
import lombok.Data;

import java.util.List;


@Data
public class Question extends Post {

    @SerializedName("Title")
    private String title;

    @SerializedName("Views")
    private int views;

    private List<Tag> tags;

    public Question(String userId, String title, String content) {
        super(userId, content, PostType.QUESTION, null);
        this.title = title;
    }
    public Question(String userId, String title, String content, String createdAt, String modifiedAt, int views, List<Tag> tags) {
        super(userId, content, PostType.QUESTION, null, createdAt, modifiedAt);
        this.title = title;
        this.views = views;
        this.tags = tags;
    }
    public Question(JsonObject jsonObject) {
        super(jsonObject.get("idUser").getAsString(), jsonObject.get("content").getAsString(), PostType.QUESTION, null);
        this.title = jsonObject.get("title").getAsString();
    }
    @Override
    public String toString() {
        return "Question{" +
                "id='" + getId().substring(0, 7) + "[...]" + '\'' +
                ", title='" + getTitle() + '\'' +
                ", content='" + getContent().substring(0, 5) + "[...]" + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", modifiedAt='" + getModifiedAt() + '\'' +
                ", views='" + getViews() + '\'' +
                '}';
    }
}
