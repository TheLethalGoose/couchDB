package de.fh.dortmund.models;

import com.google.gson.annotations.SerializedName;
import de.fh.dortmund.models.enums.VoteType;
import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
public class Vote {

    @SerializedName("_id")
    private String id = UUID.randomUUID().toString();

    @SerializedName("_rev")
    private String revision;

    private String userId;

    private String postId;

    private VoteType voteType;

    public boolean isUpvote() {
        return this.voteType == VoteType.UPVOTE;
    }
    public boolean isDownVote() {
        return this.voteType == VoteType.DOWNVOTE;
    }

    public Vote(String userId, String postId, VoteType voteType) {
        this.userId = userId;
        this.postId = postId;
        this.voteType = voteType;
    }

    public static VoteType randomVoteType(){
        VoteType[] voteOptions = {VoteType.DOWNVOTE, VoteType.UPVOTE};
        Random random = new Random();
        int randomVoteOption = random.nextInt(voteOptions.length);
        return voteOptions[randomVoteOption];
    }
}