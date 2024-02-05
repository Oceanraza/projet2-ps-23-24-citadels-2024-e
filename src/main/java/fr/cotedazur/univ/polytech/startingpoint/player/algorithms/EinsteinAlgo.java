package fr.cotedazur.univ.polytech.startingpoint.player.algorithms;

import fr.cotedazur.univ.polytech.startingpoint.Game;
import fr.cotedazur.univ.polytech.startingpoint.GameState;
import fr.cotedazur.univ.polytech.startingpoint.Utils;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * This class represents the algorithm of the bot Einstein
 * It contains the logic of the bot's actions
 */

public class EinsteinAlgo extends BaseAlgo {
    boolean lowestDistrictFound = false;
    public EinsteinAlgo(){
        super();
    }

    public void lybraryLogic(Game game) { //draws 2 cards
        District library = new District("Bibliothèque", 6, DistrictColor.special);
        if (bot.getCity().containsDistrict("Bibliothèque")) {
            for (int i = 0; i < 2; i++) {
                drawOneCard(game);
            }
        }
    }

    public void observatoryLogic(Game game) {
        List<District> threeCards = drawThreeCards(game);
        botChoosesCard(game, threeCards);
    }

    public List<District> drawThreeCards(Game game) {
        List<District> threeCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threeCards.add(game.drawCard(this.bot));
        }
        return threeCards;
    }

    public void botChoosesCard(Game game, List<District> threeCards) {
        District chosenCard = chooseCard(threeCards);
        threeCards.remove(chosenCard);
        for (District card : threeCards) {
            game.getDeck().discard(card);
        }
        System.out.println(bot.getName() + " pioche le " + chosenCard);
        bot.getDistrictsInHand().add(chosenCard);
    }

    private void drawCardLogic(Game game) {
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt()) {
            if (bot.getCity().containsDistrict("Bibliothèque") && bot.getDistrictsInHand().size() < 2) {
                lybraryLogic(game); //draws 2 cards
            } else if (bot.getCity().containsDistrict("Observatoire")) {
                observatoryLogic(game); //draws 3 cards and keeps one
            } else {
                drawOneCard(game); //draws one card
            }
        }
    }
    public void cemeteryLogic(Game game, Player targetedPlayer, District destroyedDistrict) {
        District cemetery = new District("Cimetière", 5, DistrictColor.special);
        if (bot.getCity().containsDistrict("Cimetière") && bot.getGold() >= 1 && !bot.getCharacterName().equals("Condottiere")) {
            System.out.println(bot.getName() + " utilise le Cimetière pour reprendre le " + destroyedDistrict + " dans sa main.");
            bot.getDistrictsInHand().add(destroyedDistrict);
            bot.addGold(-1);
        }
    }


    private void addTwoGold() {
        System.out.println(bot.getName() + " prend deux pièces d'or.");
        bot.addGold(2);
    }

    public int startOfTurnChoice() { //Always draws if needed
        if (bot.getDistrictsInHand().isEmpty() || bot.districtsInHandAreBuilt()) {
            if (bot.getCity().containsDistrict("Bibliothèque") && bot.getDistrictsInHand().size() < 2) {
                lybraryLogic(game); //draws 2 cards
            } else if (bot.getCity().containsDistrict("Observatoire")) {
                observatoryLogic(game); //draws 3 cards and keeps one
            } else {
                ; //draws one card 2
            }
        } else {
            addTwoGold(); // 1
        }
    }

    public void charAlgorithmsManager(Game game){
        switch (bot.getCharacterName()){
            case("Condottiere"):
                warlordAlgorithm(game);
                break;
            case("Roi"):
                kingAlgorithm(game);
                break;
            case("Magicien"):
                magicianAlgorithm(game);
                break;
        }
    }
    public void chooseCharacterAlgorithm(Game game) { //always chooses the char that gives him the most gold, or king if can build 8th quarter next turn
        List<GameCharacter> availableChars = game.getAvailableChars();
        // If the bot can build its 8th quarter next turn, it will choose the king (if possible)
        if ((bot.getCity().getDistrictsBuilt().size() >= 7) && (bot.canBuildDistrictThisTurn())
                && (bot.isCharInList(availableChars, "Roi"))) {
            bot.chooseChar(game, "Roi");
        }
        //If the bot's hand is empty, it chooses the magician to get someone's else's hand
        else if ((bot.getDistrictsInHand().isEmpty()&&(bot.isCharInList(availableChars,"Magicien")))){
            bot.chooseChar(game,"Magicien");
        }
        // If the bot doesn't have an immediate way to win, it will just pick the character who gives out the most gold for him
        else {
            GameCharacter chosenChar = availableChars.get(0);
            for (GameCharacter cha : availableChars) {
                    if (cha.getColor() != null){
                        if (bot.getNumberOfDistrictsByColor().get(cha.getColor()) > bot.getNumberOfDistrictsByColor().get(chosenChar.getColor())) {
                            chosenChar = cha;
                    }

                }
            }
            bot.chooseChar(game, chosenChar.getName());
        }
    }

    public void warlordAlgorithm(Game game) {
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        for (Player targetedPlayer : playerList) {
            if (!targetedPlayer.getGameCharacter().getName().equals("Eveque")) { //doesn't target the bishop because he's immune to the warlord
                targetedPlayer.getLowestDistrict().ifPresent(value -> {
                    if (Utils.canDestroyDistrict(value, bot)) {
                        bot.getGameCharacter().specialEffect(bot, game, targetedPlayer, value);
                        cemeteryLogic(game, targetedPlayer, value); // Call the cemetery logic here
                        lowestDistrictHasBeenFound();
                    }
                });
                if (lowestDistrictFound) {
                    break;
                }
            }
        }
    }
    //note that this algorithm doesn't use the second part of the magician, finding it useless compared to other cards
    public void magicianAlgorithm (Game game){
        List<Player> playerList = game.getSortedPlayersByScoreForWarlord();
        playerList.remove(bot);
        Player chosenPlayer = bot;
        for (Player p : playerList){
            if (p.getDistrictsInHand().size() > chosenPlayer.getDistrictsInHand().size()){
                chosenPlayer = p; //it takes the player's hand with the most cards
            }
        }
        boolean switching = true;
        bot.getGameCharacter().specialEffect(bot,game,switching,chosenPlayer);
    }
    public void kingAlgorithm(Game game){bot.getGameCharacter().specialEffect(bot,game);}

    public void lowestDistrictHasBeenFound() {
        lowestDistrictFound = true;
    }

    public void buildOrNot(GameState gameState) { //builds if he can
        for (District district : bot.getDistrictsInHand()) {
            if (bot.buildDistrict(district, gameState)) {
                break;
            }
        }
    }

    public void huntedQuarterAlgorithm(District huntedQuarter) {
        Set<DistrictColor> colorList = new HashSet<>();
        List<District> districtsBuilt = bot.getCity().getDistrictsBuilt();
        for (District district : districtsBuilt) {
            colorList.add(district.getColor());
        }
        DistrictColor[] districtColors = DistrictColor.values();
        if (colorList.size() == districtColors.length - 1) {
            for (DistrictColor districtColor : districtColors) {
                if (!colorList.contains(districtColor)) {
                    huntedQuarter.setColor(districtColor);
                }
            }
        }
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public District chooseCard(List<District> cards) {
        District chosenCard = null;
        int minCost = Integer.MAX_VALUE;
        for (District card : cards) {
            if (card.getPrice() <= bot.getGold() && card.getPrice() < minCost) {
                chosenCard = card;
                minCost = card.getPrice();
            }
        }
        return chosenCard;
    }
}

