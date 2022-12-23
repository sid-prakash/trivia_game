package coms309.Trivia;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trivia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String question;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String answer;
    private String questionTheme;

    // 1 = multiple choice, 2 = true or false, 3 = fill in the blank
    private int questionType;

    public Trivia(String question, String a1, String a2, String a3, String a4, String answer, String questionTheme, int questionType) {
        this.question = question;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.answer = answer;
        this.questionTheme = questionTheme;
        this.questionType = questionType;
    }

    public Trivia() {

    }

    public Trivia updateTrivia(Trivia request) {
        String ans1 = request.getA1();
        String ans2 = request.getA2();
        String ans3 = request.getA3();
        String ans4 = request.getA4();
        String answer = request.getAnswer();
        String theme = request.getQuestionTheme();
        int type = request.getQuestionType();

        if (ans1 != null) {
            this.a1 = ans1;
        }
        if (ans2 != null) {
            this.a2 = ans2;
        }
        if (ans3 != null) {
            this.a3 = ans3;
        }
        if (ans4 != null) {
            this.a4 = ans4;
        }
        if (theme != null) {
            this.questionTheme = theme;
        }
        if (answer != null) {
            this.answer = answer;
        }
        if (type != 0) {
            this.questionType = type;
        }

        return this;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getA1() {
        return a1;
    }

    public String getA2() {
        return a2;
    }

    public String getA3() {
        return a3;
    }

    public String getA4() {
        return a4;
    }

    public List<String> getAnswerChoices() {
        List<String> answerChoices = new ArrayList<>();
        answerChoices.add(a1);
        answerChoices.add(a2);
        answerChoices.add(a3);
        answerChoices.add(a4);
        return answerChoices;
    }

    public String getQuestionTheme() {
        return questionTheme;
    }

    public void setQuestionTheme(String questionTheme) {
        this.questionTheme = questionTheme;
    }

    public int getQuestionType() { return questionType; }

    public void setQuestionType(int questionType) { this.questionType = questionType; }
}
