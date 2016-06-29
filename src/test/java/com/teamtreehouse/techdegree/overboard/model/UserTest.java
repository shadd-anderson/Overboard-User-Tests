package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {
    private Board board;
    private User testatron;
    private User mcTest;
    private Question question;
    private Answer answer;

    @Before
    public void setUp() {
        board = new Board("Unit testing! :D");
        testatron = board.createUser("Testatron 3000");
        mcTest = board.createUser("McTest with cheese");
        question = testatron.askQuestion("How do you test stuff?");
        answer = mcTest.answerQuestion(question, "By writing tests!");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void upvotedQuestionGivesFiveReputationPoints() throws Exception {
        mcTest.upVote(question);

        assertEquals(5,testatron.getReputation());
    }

    @Test
    public void upvotedAnswerGivesTenReputationPoints() throws Exception {
        testatron.upVote(answer);

        assertEquals(10,mcTest.getReputation());
    }

    @Test
    public void acceptedAnswerGivesFifteenReputationPoints() throws Exception {
        testatron.acceptAnswer(answer);

        assertEquals(15,mcTest.getReputation());
    }

    @Test(expected = VotingException.class)
    public void votingDownOnOwnQuestionThrowsException() throws Exception {
        testatron.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void votingUpOnOwnQuestionThrowsException() throws Exception {
        testatron.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void votingUpOnOwnAnswerThrowsException() throws Exception {
        mcTest.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void votingDownOnOwnAnswerThrowsException() throws Exception {
        mcTest.downVote(answer);
    }

    @Test
    public void onlyQuestionerCanAcceptAnswers() throws Exception {
        User questioner = answer.getQuestion().getAuthor();
        String message = String.format("Only %s can accept this answer as it is their question",
                questioner.getName());

        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(message);

        mcTest.acceptAnswer(answer);
    }

    @Test
    public void downvotedAnswerSubtractsOneFromReputation() throws Exception {
        User debbieDowner = board.createUser("Debbie Downer");

        testatron.upVote(answer);
        debbieDowner.downVote(answer);

        assertEquals(9,mcTest.getReputation());
    }
}