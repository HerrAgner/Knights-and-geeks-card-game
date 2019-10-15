package cards;

public class UnitCard extends Card {
    private int health;
    private int currentHealth;
    private int attack;
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
        System.out.println("change " + hpChange);
        System.out.println("current " + currentHealth);
        int newCurr = currentHealth+hpChange;
        if(newCurr > health) {
            currentHealth = health;
        } else currentHealth = newCurr;
        System.out.println(currentHealth);
//        this.currentHealth = currentHealth + hpChange > health ? health : currentHealth+hpChange;
//        if(this.currentHealth > health){
//            this.currentHealth = health;
//        }
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
