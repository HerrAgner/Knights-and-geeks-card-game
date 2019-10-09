package cards;

public class UnitCard extends Card {
   private int hp;
   private int attack;

    public UnitCard(String name, int cost, String type, int hp, int attack) {
        super(name, cost, type);
        this.hp = hp;
        this.attack = attack;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }
}
