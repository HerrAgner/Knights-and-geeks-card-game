package cards;

public class SpellCard extends Card {
    private String type;
    private boolean many;
    private int value;

    public SpellCard(){
        this("", false, 0, "", 0);
    }

    public SpellCard(String type, boolean many,int value, String name, int cost){
        super(name, cost);
        this.type = type;
        this.many = many;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMany() {
        return many;
    }

    public void setMany(boolean many) {
        this.many = many;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
