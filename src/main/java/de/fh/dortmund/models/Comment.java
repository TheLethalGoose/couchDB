package de.fh.dortmund.models;

import com.google.gson.JsonObject;
import de.fh.dortmund.models.enums.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment extends Post {
    public Comment(String userId, String content, String createdAt, String modifiedAt, String parentPostId) {
        super(userId, content, PostType.COMMENT, createdAt, modifiedAt, parentPostId);
    }
    public Comment(JsonObject jsonObject) {
        super(jsonObject.get("idUser").getAsString(), jsonObject.get("content").getAsString(), PostType.COMMENT, jsonObject.get("idParent").getAsString());
    }

}
