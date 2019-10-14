package cards;

public class EffectCard extends Card {
    private String type;
    private int effectValue;

    public EffectCard() {
        this("", 0, "", 0, 0);
    }

    public EffectCard(String name, int cost, String type, int health, int attack) {
        super(name, cost);
        this.type = type;
//        this.health = health;
//        this.attack = attack;

    }

    public String getType() {
        return type;
    }

    public int getEffectValue() {
        return effectValue;
    }
}
