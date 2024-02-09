package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole.*;
import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * Classe de gestion des actions dans le jeu.
 */
public class ActionManager {
    private ActionManager() {
        throw new IllegalStateException("Action Manager est une classe utilitaire");
    }

    /**
     * Imprime l'or du joueur et ajoute de l'or au joueur.
     *
     * @param player    le joueur concerné.
     * @param addedGold l'or à ajouter.
     * @return l'or ajouté.
     */
    public static int printGold(Player player, int addedGold) {
        player.addGold(addedGold);
        if (addedGold != 0) {
            String collectGoldMessage = (player.getGameCharacter().getRole().toStringLeOrLUpperCase()) + " a donne " + addedGold + " or a " + player.getName();
            LOGGER.info(collectGoldMessage);
        }
        return addedGold;
    }

    /**
     * Collecte de l'or pour le joueur en fonction de la couleur du quartier.
     * @param player le joueur concerné.
     * @param districtColor la couleur du quartier.
     * @return l'or ajouté.
     */
    private static int collectGoldUtil(Player player, DistrictColor districtColor) {
        int addedGold = player.getNumberOfDistrictsByColor().get(districtColor);
        return printGold(player, addedGold);
    }

    /**
     * Commence le tour de jeu pour le joueur.
     * @param game l'état actuel du jeu.
     * @param player le joueur qui joue.
     */
    public static void startOfTurn(Game game, Player player) {
        Bot bot = (Bot) player;
        int choice = bot.getBotAlgo().startOfTurnChoice();
        if (choice == 1) { // Prend 2 pièces d'or
            String take2GoldMessage = bot.getName() + " prend deux pieces d'or.";
            LOGGER.info(take2GoldMessage);
            bot.addGold(2);
        } else { // Pioche une carte
            executeSpecificCardDraw(game, bot);
        }
    }

    /**
     * Exécute une pioche de carte spécifique en fonction du personnage du joueur et des quartiers de sa ville.
     * @param game l'état actuel du jeu.
     * @param player le joueur qui pioche.
     */
    public static void executeSpecificCardDraw(Game game, Player player) {
        Bot bot = (Bot) player;
        if (bot.getGameCharacter().getRole().equals(ARCHITECT)) {
            applyArchitectOrLibraryEffect(game, bot); //pioche 2 cartes
        }
        if (bot.getCity().containsDistrict("Bibliotheque")) {
            applyArchitectOrLibraryEffect(game, bot); //pioche 2 cartes
        } else if (bot.getCity().containsDistrict("Observatoire")) {
            applyObservatoryEffect(game, bot); //pioche 3 cartes et en garde une
        } else {
            drawCard(bot, game); //pioche une carte
        }
    }

    /**
     * Pioche une carte pour le joueur.
     * @param player le joueur qui pioche.
     * @param game l'état actuel du jeu.
     * @return le quartier pioché.
     */
    public static District drawCard(Player player, Game game) {
        District drawnDistrict = game.getDeck().drawCard();
        String drawCardMessage = player.getName() + " pioche la carte " + drawnDistrict + ".";
        LOGGER.info(drawCardMessage);
        player.getDistrictsInHand().add(drawnDistrict);
        return drawnDistrict;
    }

    /**
     * Applique l'effet de l'Architecte ou de la Bibliothèque (pioche 2 cartes).
     * @param game l'état actuel du jeu.
     * @param player le joueur qui pioche.
     */
    public static void applyArchitectOrLibraryEffect(Game game, Player player) {
        Bot bot = (Bot) player;
        for (int i = 0; i < 2; i++) {
            drawCard(bot, game);
        }
    }

    /**
     * Applique l'effet de l'Observatoire (pioche 3 cartes et en garde une).
     * @param game l'état actuel du jeu.
     * @param player le joueur qui pioche.
     */
    public static void applyObservatoryEffect(Game game, Player player) {
        Bot bot = (Bot) player;
        List<District> threeCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threeCards.add(drawCard(bot, game));
        }
        bot.getBotAlgo().botChoosesCard(game, threeCards);
    }

    /**
     * Collecte de l'or pour le joueur en fonction de son personnage.
     * @param player le joueur concerné.
     * @return l'or ajouté.
     */
    public static int collectGold(Player player) {
        GameCharacterRole role = player.getGameCharacter().getRole();
        if (role == WARLORD) return collectGoldUtil(player, DistrictColor.MILITARY);
        else if (role == KING) return collectGoldUtil(player, DistrictColor.NOBLE);
        else if (role == BISHOP) return collectGoldUtil(player, DistrictColor.RELIGIOUS);
        else if (role == MERCHANT) return collectGoldUtil(player, DistrictColor.TRADE);
        return 0;
    }

    /**
     * Applique l'effet spécial du personnage du joueur.
     * @param player le joueur concerné.
     * @param game l'état actuel du jeu.
     */
    public static void applyCharacterSpecialEffect(Player player, Game game) {
        player.getGameCharacter().specialEffect(player, game);
    }

    /**
     * Applique l'effet des cartes spéciales du joueur.
     * @param game l'état actuel du jeu.
     * @param player le joueur concerné.
     */
    public static void applySpecialCardsEffect(Game game, Player player) {
        boolean hasManufacture = player.getCity().containsDistrict("Manufacture");
        boolean hasLaboratory = player.getCity().containsDistrict("Laboratoire");
        Bot bot = (Bot) player;
        if (hasManufacture && bot.getGold() >= 3) {
            boolean canUseManufacture = bot.getBotAlgo().manufactureChoice();
            if (canUseManufacture) {
                applyManufactureEffect(game, player, bot);
            }
        }
        if (hasLaboratory) {
            Optional<District> districtToDiscard = bot.getBotAlgo().laboratoryChoice(); // Optional.empty() si le bot ne veut pas utiliser le laboratoire
            districtToDiscard.ifPresent(district -> applyLaboratoryEffect(game, player, district, bot));
        }
    }

    /**
     * Applique l'effet du Laboratoire (défausse une carte pour 1 pièce d'or).
     * @param game l'état actuel du jeu.
     * @param player le joueur qui utilise le Laboratoire.
     * @param districtToDiscard le quartier à défausser.
     * @param bot le bot qui utilise le Laboratoire.
     */
    private static void applyLaboratoryEffect(Game game, Player player, District districtToDiscard, Bot bot) {
        String laboratoryMessage = COLOR_PURPLE + player.getName() + " utilise le Laboratoire pour defausser sa carte " + districtToDiscard.getName() + " contre 1 piece d'or." + COLOR_RESET;
        LOGGER.info(laboratoryMessage);
        bot.removeFromHandAndPutInDeck(game.getDeck(), districtToDiscard);
        bot.addGold(1);
    }

    /**
     * Applique l'effet du Cimetière (reprend un quartier détruit pour 1 pièce d'or).
     * @param deck le deck du jeu.
     * @param graveyardOwner le propriétaire du Cimetière.
     * @param destroyedDistrict le quartier détruit.
     */
    public static void applyGraveyardEffect(Deck deck, Player graveyardOwner, District destroyedDistrict) {
        if (graveyardOwner.getGold() >= 1 && !graveyardOwner.getCharacterName().equals("Condottiere") && ((Bot) graveyardOwner).getBotAlgo().graveyardChoice()) {
            String graveyardMessage = COLOR_PURPLE + graveyardOwner.getName() + " utilise le Cimetiere pour reprendre le " + destroyedDistrict + " dans sa main." + COLOR_RESET;
            LOGGER.info(graveyardMessage);
            graveyardOwner.getDistrictsInHand().add(destroyedDistrict);
            graveyardOwner.removeGold(1);
        } else {
            deck.putCardAtBottom(destroyedDistrict);
        }
    }

    /**
     * Applique l'effet de la Manufacture (pioche 3 cartes pour 3 pièces d'or).
     * @param game l'état actuel du jeu.
     * @param player le joueur qui utilise la Manufacture.
     * @param bot le bot qui utilise la Manufacture.
     */
    private static void applyManufactureEffect(Game game, Player player, Bot bot) {
        String manufactureMessage = COLOR_PURPLE + player.getName() + " utilise la Manufacture pour piocher 3 cartes et paye 3 pieces." + COLOR_RESET;
        LOGGER.info(manufactureMessage);
        bot.removeGold(3);
        for (int i = 0; i < 3; i++) {
            drawCard(bot, game);
        }
    }
}