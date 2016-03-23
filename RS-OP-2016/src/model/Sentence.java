package model;

public class Sentence {
    private String content;
    private String[] tokens; //  the sentence tokens (words)
    private String pos[]; //  the POS tag for each sentence token
    private String chunks[]; //  the chunk tags for the sentence

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public String[] getPos() {
        return pos;
    }

    public void setPos(String[] pos) {
        this.pos = pos;
    }

    public String[] getChunks() {
        return chunks;
    }

    public void setChunks(String[] chunks) {
        this.chunks = chunks;
    }
}
