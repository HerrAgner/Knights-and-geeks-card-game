package cards;

public class UnitCard extends Card {
    private int health;
    private int currentHealth;
    private int attack;
    private int attackDefault;
    private boolean fatigue;

    public UnitCard() {
        this("", 0, 0 , 0);
    }

    public UnitCard(String name, int cost, int hp, int attack) {
        super(name, cost);
        this.health = hp;
        this.attack = attack;
        this.fatigue = false;
        this.currentHealth = this.health;;
        this.attackDefault = this.attack;
    }

    public void changeCurrentHealth(int hpChange) {
        if(currentHealth+hpChange <= 0) setCurrentHealth(0);
        else if(currentHealth+hpChange > health) {
            setCurrentHealth(health);
        } else setCurrentHealth(currentHealth+hpChange);
        System.out.println("newCURR " + currentHealth + "\n");
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

    private void setCurrentHealth(int hp){
        currentHealth = hp;
    }

    public void changeCurrentHealth(int hpChange) {
        if(currentHealth+hpChange <= 0) setCurrentHealth(0);
        else if(currentHealth+hpChange > health) {
            setCurrentHealth(health);
        } else setCurrentHealth(currentHealth+hpChange);
    }

    private void setMaxHealth(int maxHealth){
        this.health = maxHealth;
    }

    public void changeAttack(int attackChange) {
        if (getAttack() + attackChange <= 0) {
            setAttack(1);
        } else {
            attack = attack + attackChange;
        }
    }

    public int getAttack() {
        return attack;
    }

    private void setAttack(int attack){
        this.attack = attack;
    }

    public int getAttackDefault() {
        return attack;
    }

    public boolean getFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }
}
