package cards;

public class UnitCard extends Card {
    private int hp;
    private int attack;

    public UnitCard() {
        this("", 0, "", 0, 0);
    }

    public UnitCard(String name, int cost, String type, int hp, int attack) {
        super(name, cost, type);
        this.hp = hp;
        this.attack = attack;
    }

    public boolean setHp(int hp) {
        if (hp > 0 && hp < 10) {
            this.hp = hp;
            return true;
        } else {
            return false;
        }
    }

    public boolean setAttack(int attack) {
        if (attack > 0 && attack < 10) {
            this.attack = attack;
            return true;
        } else {
            return false;
        }
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }
}
