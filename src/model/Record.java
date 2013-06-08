package model;
 
public class Record {
    private String question;
    private String answer;
     

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String tytul) {
        this.question = tytul;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
     
    public Record() {}
    public Record(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
     
    @Override
    public String toString() {
        return question+" - "+answer;
    }
}
