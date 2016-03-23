package model;

import model.Sentiment;

public class Feature {
    private String posPattern;  //POS sequence between feature and wmin sentiment word
    private String name;        //feature name
    private Sentiment sentiment;//sentiment value
    private Sentence sentence;    //sentence in which the feature is
    private int featurePosition;       //position of the feature in the sentence
    private int sentimentPosition;       //position of the sentiment feature in the sentence
    private int biGramOffset; // = 1 if biGram, 0 else
    private String productId;

    public Feature() {

    }

    public void setPosPattern(String posPattern) {
        this.posPattern = posPattern;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public void setFeaturePosition(int featurePosition) {
        this.featurePosition = featurePosition;
    }

    public void setSentimentPosition(int sentimentPosition) {
        this.sentimentPosition = sentimentPosition;
    }

    public String getPosPattern() {
        return posPattern;
    }

    public String getName() {
        return name;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public int getFeaturePosition() {
        return featurePosition;
    }

    public int getSentimentPosition() {
        return sentimentPosition;
    }

    public int getBiGramOffset() {
        return biGramOffset;
    }

    public void setBiGramOffset(int biGramOffset) {
        this.biGramOffset = biGramOffset;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "'"+ name + "' at position " + featurePosition + " get a sentiment '" + sentiment + "' at position " + sentimentPosition + " with a POS pattern "+posPattern;
    }

    public String toStringLight(){
        return "'"+ name + " : " + sentiment;
    }

    public void reverseSentiment() {
        switch (sentiment) {
            case POSITIVE:
                sentiment = Sentiment.NEGATIVE;
                break;
            case NEGATIVE:
                sentiment = Sentiment.POSITIVE;
                break;
        }
    }
}
