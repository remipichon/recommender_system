package model;

import model.Sentiment;

public class Feature {
    private String posPattern;  //POS sequence between feature and wmin sentiment word
    private String name;        //feature name
    private Sentiment sentiment;//sentiment value
    private String sentence;    //sentence in which the feature is
    private int featurePosition;       //position of the feature in the sentence
    private int sentimentPosition;       //position of the sentiment feature in the sentence

    public Feature(String posPattern, String name, Sentiment sentiment, String sentence, int featurePosition, int sentimentPosition) {
        this.posPattern = posPattern;
        this.name = name;
        this.sentiment = sentiment;
        this.sentence = sentence;
        this.featurePosition = featurePosition;
        this.sentimentPosition = sentimentPosition;
    }

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

    public void setSentence(String sentence) {
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

    public String getSentence() {
        return sentence;
    }

    public int getFeaturePosition() {
        return featurePosition;
    }

    public int getSentimentPosition() {
        return sentimentPosition;
    }


    @Override
    public String toString() {
        return "'"+ name + "' at position " + featurePosition + " get a sentiment '" + sentiment + "' at position " + sentimentPosition + " with a POS pattern "+posPattern;
    }
}
