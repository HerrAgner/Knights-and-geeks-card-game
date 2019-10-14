package cards;

public class SpellCard extends Card {
    private String type;
    private boolean many;
    private int hp;
    private int dmg;

    public SpellCard(String type, boolean many, int dmg, String name, int cost){
        super(name, cost);
        this.type = type;
        this.many = many;
        this.dmg = dmg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMany(boolean many) {
        this.many = many;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public String getType() {
        return type;
    }

    public boolean isMany() {
        return many;
    }
    public int getDmg() {
        return dmg;
    }
}
