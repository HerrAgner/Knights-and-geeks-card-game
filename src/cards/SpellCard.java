package cards;

public class SpellCard extends Card {
    private String type;
    private boolean many;
    private int dmg;
    private int heal;

    public SpellCard(){
        this("", false, 0, 0, "", 0);
    }

    public SpellCard(String type, boolean many, int dmg, int heal, String name, int cost){
        super(name, cost);
        this.type = type;
        this.many = many;
        if(dmg > 0){
            this.dmg = dmg;
            this.heal = 0;
        } else if (heal > 0) {
            this.dmg = 0;
            this.heal = heal;
        }
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

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }
}
