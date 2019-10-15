package cards;

public class UnitCard extends Card {
    private int health;
    private int currentHealth;
    private int attack;
    private boolean fatigue;

    public UnitCard() {
        this("", 0, 0, 0);
    }

    public UnitCard(String name, int cost, int hp, int attack) {
        super(name, cost);
        this.health = hp;
        this.attack = attack;
        this.fatigue = false;
        currentHealth = this.health;
    }

    public void changeMaxHealth(int hpChange) {
        if (getMaxHealth() + hpChange <= 0) {
            setMaxHealth(1);
            if(getCurrentHealth() + hpChange <= 0) {
                setCurrentHealth(1);
            } else currentHealth = currentHealth + hpChange;
        } else {
            health = health + hpChange;
            currentHealth = currentHealth + hpChange;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        if(this.currentHealth > health){
            this.currentHealth = health;
        }
    }

    public boolean changeAttack(int attackChange) {
        if (getAttack() + attackChange <= 0) {
            setAttack(1);
        } else {
            attack = attack + attackChange;
        }
        return true;
    }

    private void setAttack(int attack){
        this.attack = attack;
    }
    public int getMaxHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    private void setMaxHealth(int maxHealth){
        this.health = maxHealth;
    }
    public boolean getFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }
}
