package fr.cotedazur.univ.polytech.startingpoint.player;

import fr.cotedazur.univ.polytech.startingpoint.ActionManager;
import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.algorithms.BaseAlgo;

import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

public class Bot extends Player {

    private BaseAlgo botAlgo;

    public BaseAlgo getBotAlgo() {
        return botAlgo;
    }

    public Bot(String name, BaseAlgo algo) {
        super(name);
        this.botAlgo = algo;
        botAlgo.setBot(this);
    }
    public Bot(String name){ //for tests
        super(name);
    }

    public boolean canBuildDistrictThisTurn() { // Checks If a district in Hand can be built with +2 gold
        for (District dist : getDistrictsInHand()) {
            if (dist.getPrice() <= this.getGold() + 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isCharInList(List<GameCharacter> cha, GameCharacterRole askedChar) {
        for (GameCharacter temp : cha) {
            if (temp.getRole().equals(askedChar)) {
                return true;
            }
        }
        return false;
    }

    public Optional<GameCharacter> getCharInList(List<GameCharacter> cha, GameCharacterRole askedChar) {
        for (GameCharacter gameCharacter : cha) {
            if (gameCharacter.getRole().equals(askedChar)) {
                return Optional.of(gameCharacter);
            }
        }
        return Optional.empty();
    }

    public void chooseChar(Game game, GameCharacterRole askedChar) {
        Optional<GameCharacter> chosenCharacter = getCharInList(game.getAvailableChars(), askedChar);
        if(chosenCharacter.isEmpty()) {
            return;
        }

        game.printAvailableCharacters();
        setGameCharacter(chosenCharacter.get());
        game.removeAvailableChar(chosenCharacter.get());
        String chosenCharMessage = COLOR_BLUE + this.getName() + " a choisi " + chosenCharacter.get().getRole().toStringLeOrLLowerCase() + COLOR_RESET;
        LOGGER.info(chosenCharMessage);
    }
    @Override
    public void play(Game game, GameState gameState) {
        boolean collectGoldBeforeBuild = botAlgo.collectGoldBeforeBuildChoice();
        ActionManager.startOfTurn(game, this);

        if(collectGoldBeforeBuild) {
            ActionManager.collectGold(this);
        }

        ActionManager.applySpecialCardsEffect(game, this);
        botAlgo.charAlgorithmsManager(game);
        botAlgo.buildOrNot(gameState);

        if(!collectGoldBeforeBuild) {
            ActionManager.collectGold(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
