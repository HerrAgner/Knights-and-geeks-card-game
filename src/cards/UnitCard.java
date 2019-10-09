package cards;

public class UnitCard extends Card {
   private int hp;
   private int attack;

    public UnitCard(int hp, int attack) {
        super("test", 1,"Type");
        this.hp = hp;
        this.attack = attack;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }
}
