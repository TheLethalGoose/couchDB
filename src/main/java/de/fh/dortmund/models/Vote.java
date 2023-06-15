package de.fh.dortmund.models;

import de.fh.dortmund.models.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
public class Vote {
    private String userId;

    private String postId;

    private VoteType voteType;


    public boolean isUpvote() {
        return this.voteType == VoteType.UPVOTE;
    }
    public boolean isDownVote() {
        return this.voteType == VoteType.DOWNVOTE;
    }

    public static VoteType randomVoteType(){
        VoteType[] voteOptions = {VoteType.DOWNVOTE, VoteType.UPVOTE};
        Random random = new Random();
        int randomVoteOption = random.nextInt(voteOptions.length);
        return voteOptions[randomVoteOption];
    }
}