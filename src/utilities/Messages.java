package utilities;

import static utilities.CLIColors.*;

public class Messages {
    public static final Object[] MENU =             {"\nMake a move!",
            "1. Print cards from hand and table",
                    "2. Print hp and mana",
                    "3. Play card",
                    "4. Attack with card",
                    "5. End turn",
                    "6. End game"};
    public static final Object[] END_TURN = {
            "Ending turn.\n",
            String.format("%-200s", BLACK_BACKGROUND_BRIGHT
                    + "\n" + YELLOW_BACKGROUND
                    + "\n" + BLACK_BACKGROUND_BRIGHT),
            RESET};
    public static final String ENTER_NAME = "Enter name for player";
    public static final String INV_NAME_1 = "Invalid name. Max length is 10 characters, please enter a new one.";
    public static final String INV_NAME_2 = "Invalid name. Max length is 10 characters and has to be different from Player One. " +
            "\nPlease enter a new name!";
    public static final String ENTER_SIZE = "Enter your desired card pile size";
    public static final String INV_SIZE = "Invalid size, chose number between 45-100";
    public static final String DEF_CARD_TBL = "\nDefending cards on table:";
    public static final String OWN_CARD_TBL = "\nYour cards on table: ";
    public static final String OWN_CARD_HND = "\nCards on hand: ";
    public static final String BUFF_HP = "You buffed the card %s with %s hp";
    public static final String BUFF_ATK = "You buffed the card %s with %s attack";
    public static final String DEBUFF_HP = "You debuffed the card %s with %s hp";
    public static final String DEBUFF_ATK = "You debuffed the card %s with %s attack";
    public static final String ENT_PLAY_CARD = "Which card do you want to play?";
    public static final String ENT_ATK_CARD = "Choose card: ";
    public static final String ENT_HEAL_CARD = "Which card do you want to heal? (0 to heal you)";
    public static final String ENT_DEF_CARD = "Which card do you want to attack? (0 to attack player)";
    public static final String ENT_DEBUFF_CARD = "Which card do you want to debuff?";
    public static final String ENT_BUFF_CARD = "Which card do you want to buff?";
    public static final String UNIT_USED = "Played card ";
    public static final String FULL_CARDS = "To many cards on the table. Max 7.";
    public static final String NO_CARDS = "No cards on table";
    public static final String NO_MANA = "Not enough mana.";
    public static final String INV_FATIGUE = "\nCard is fatigue, wait one turn to attack!\n";
    public static final String INV_NO_CARDS = "No cards on table. Choose another option";
    public static final String TURN = "'s turn";
    public static final String BOTH = "Both " ;
    public static final String AND = " and ";
    public static final String DIE_FIGHT = " died fighting. ";
    public static final String KILLED =  " killed ";
    public static final String LETHAL_ATK =  " with a lethal attack.";
    public static final String NEW_HP = " new health: ";
    public static final String DIE_ATK = " died while attacking ";
    public static final String LIVES = " lives with ";
    public static final String ATTACKED = " attacked ";
    public static final String BLOW = " with a deadly blow!";
    public static final String TOOK = " took ";
    public static final String DMG = " damage ";
    public static final String DARK_MAGIC = " with dark magic!";
    public static final String INFLICT = " inflicted ";
    public static final String HEAL = " healed ";
    public static final String WITH = " with ";
    public static final String TO = " to ";
    public static final String ALL_DMG = " dmg to all enemy cards.";
    public static final String ALL_HEAL = " hp to all your wounded cards.";
    public static final String LIGHT_MAGIC = " hp with light magic!";
    public static final String NO_HEAL = "You healed for nothing, you fool!";




    public static final String LUR1 = "cheeta";
    public static final String LUR2 = "zebra";

}
