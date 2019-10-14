package cards;

public class EffectCard extends Card {
    private String type;
    private int health;
    private int attack;

    public EffectCard(String name, int cost, String type, int health, int attack) {
        super(name, cost);
        this.type = type;
//        this.health = health;
//        this.attack = attack;

        if(health > 0){
            this.attack = 0;
            this.health = health;
        } else if (attack > 0){
            this.health = 0;
            this.attack = attack;
        }
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
