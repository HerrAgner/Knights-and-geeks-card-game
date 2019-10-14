package cards;

public class EffectCard extends Card {
    private String type;
    private int health;
    private int attack;

    public EffectCard() {
        this("none", 0, 0);
    }

    public EffectCard(String type, int health, int attack) {
        this.type = type;
        this.health = health;
        this.attack = attack;
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }
}
