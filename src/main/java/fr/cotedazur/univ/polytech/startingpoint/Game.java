package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.board.Deck;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacter;
import fr.cotedazur.univ.polytech.startingpoint.character.GameCharacterRole;
import fr.cotedazur.univ.polytech.startingpoint.character.card.*;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.player.Bot;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import fr.cotedazur.univ.polytech.startingpoint.utils.Utils;

import java.util.*;

import static fr.cotedazur.univ.polytech.startingpoint.utils.CitadelsLogger.*;

/**
 * La classe Game est la classe principale du jeu. Elle contient le deck, la couronne, les joueurs et les personnages.
 * Elle contient également les méthodes pour démarrer le jeu, mélanger les personnages et distribuer les cartes aux joueurs.
 */
public class Game {
    public static final int CITY_SIZE_TO_WIN = 8;
    private static final int START_CARDS_NUMBER = 4;
    private Deck deck = new Deck();
    private Crown crown;
    private List<Player> players;
    protected List<GameCharacter> allCharacters;
    protected List<GameCharacter> charactersInGame;
    protected List<GameCharacter> availableChars;

    protected Assassin assassin;
    protected Thief thief;
    protected King king;
    protected Bishop bishop;
    protected Merchant merchant;
    protected Warlord warlord;
    protected Magician magician;
    protected Architect architect;

    /**
     * Constructeur de la classe Game. Initialise le jeu.
     */
    public Game() {
        init();
    }

    /**
     * Getter pour l'objet Crown.
     *
     * @return l'objet Crown.
     */
    public Crown getCrown() {
        return crown;
    }

    /**
     * Getter pour la liste des objets Player.
     *
     * @return la liste des objets Player.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getter pour la liste des objets GameCharacter disponibles.
     *
     * @return la liste des objets GameCharacter disponibles.
     */
    public List<GameCharacter> getAvailableChars() {
        return availableChars;
    }

    /**
     * Renvoie l'index du joueur courant dans l'ordre de jeu
     *
     * @param currentPlayer le joueur courant
     * @return l'index du joueur courant dans l'ordre de jeu
     */
    public int getCurrentPlayerIndexInRunningOrder(Player currentPlayer) {
        List<Player> runningOrder = getRunningOrder();
        for (int i = 0; i < runningOrder.size(); i++) {
            if (runningOrder.get(i).equals(currentPlayer)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Vérifie si le rôle est disponible
     *
     * @param role le rôle à vérifier
     * @return vrai si le rôle est disponible, faux sinon
     */
    public boolean containsAvailableRole(GameCharacterRole role) {
        return availableChars.stream().anyMatch(gameCharacter -> gameCharacter.getRole().equals(role));
    }

    /**
     * Vérifie si les rôles sont disponibles
     *
     * @param roles les rôles à vérifier
     * @return vrai si les rôles sont disponibles, faux sinon
     */
    public boolean containsAvailableRoles(GameCharacterRole... roles) { // Vérifie si les personnages disponibles contiennent au moins un des rôles
        List<GameCharacterRole> rolesList = Arrays.asList(roles);
        return availableChars.stream().anyMatch(gameCharacter -> rolesList.contains(gameCharacter.getRole()));
    }

    /**
     * Getter pour la liste des objets GameCharacter en jeu.
     *
     * @return la liste des objets GameCharacter en jeu.
     */
    public List<GameCharacter> getCharactersInGame() {
        return charactersInGame;
    }

    /**
     * Setter pour ajouter des joueurs à la liste des joueurs
     *
     * @param bots les joueurs à ajouter
     */
    public void setPlayers(Player... bots) {
        players.addAll(Arrays.asList(bots));
    }

    /**
     * Définit l'ordre de jeu en fonction de l'ordre de jeu des personnages
     *
     * @return la liste des joueurs dans l'ordre de jeu
     */
    public List<Player> getRunningOrder() {
        return this.getPlayers().stream().sorted(Comparator.comparingInt(player -> player.getGameCharacter().getRunningOrder())).toList();
    }

    /**
     * Supprime un personnage disponible
     *
     * @param cha le personnage à supprimer
     */
    public void removeAvailableChar(GameCharacter cha) {
        availableChars.remove(cha);
    }

    /**
     * Met face cachée 1 personnage du jeu
     */
    private void putCharacterFacedown() {
        int indexCharacter;
        GameCharacter cha;

        indexCharacter = Utils.generateRandomNumber(availableChars.size());
        cha = availableChars.get(indexCharacter);

        String wontBePlayedMessage = cha.getRole().toStringLeOrLUpperCase() + " est ecarte face cachee";
        LOGGER.info(wontBePlayedMessage);

        availableChars.remove(indexCharacter);
    }

    /**
     * Met face ouverte 2 personnages du jeu (sauf le roi)
     */
    private void putCharactersFaceup() {
        for (int i = 0; i < 2; i++) {
            int indexCharacter;
            GameCharacter cha;

            indexCharacter = Utils.generateRandomNumber(charactersInGame.size());
            cha = charactersInGame.get(indexCharacter);

            // Le roi doit être disponible pour les joueurs
            while (cha.getRole().equals(GameCharacterRole.KING)) {
                indexCharacter = Utils.generateRandomNumber(charactersInGame.size());
                cha = charactersInGame.get(indexCharacter);
            }
            charactersInGame.remove(cha);
            String wontBePlayedMessage = cha.getRole().toStringLeOrLUpperCase() + " est ecarte face ouverte";
            LOGGER.info(wontBePlayedMessage);
        }
    }

    /**
     * Init démarre le jeu en créant le deck, la couronne, les joueurs et les personnages
     */
    public void init() {
        deck.resetDeck();
        allCharacters = new ArrayList<>();
        availableChars = new ArrayList<>();

        // Crée une couronne
        crown = new Crown();

        // Crée la liste des joueurs
        players = new ArrayList<>();

        // Crée les personnages

        assassin = new Assassin();
        thief = new Thief();
        king = new King();
        bishop = new Bishop();
        merchant = new Merchant();
        warlord = new Warlord();
        magician = new Magician();
        architect = new Architect();

        // Crée la liste des personnages
        allCharacters.add(assassin);
        allCharacters.add(thief);
        allCharacters.add(king);
        allCharacters.add(bishop);
        allCharacters.add(merchant);
        allCharacters.add(warlord);
        allCharacters.add(magician);
        allCharacters.add(architect);

        // Donne les cartes aux joueurs
        startCardGame();
    }

    /**
     * Démarre le jeu de cartes
     */
    public void startCardGame() {
        // Mélange le deck
        deck.shuffle();

        // Donne 4 cartes à chaque joueur
        giveStartingCards();
    }

    /**
     * Mélange les personnages
     */
    public void shuffleCharacters() {
        // Réinitialise les listes précédentes
        availableChars.clear();
        charactersInGame = new ArrayList<>(allCharacters);

        // Supprime 2 personnages de la liste des personnages en jeu
        putCharactersFaceup();

        availableChars = new ArrayList<>(charactersInGame);

        // Supprime 1 personnage de la liste des personnages disponible
        putCharacterFacedown();
    }

    /**
     * Donne les cartes de départ
     */
    private void giveStartingCards() {
        for (Player player : players) {
            for (int i = 0; i < START_CARDS_NUMBER; i++) {
                player.addDistrictInHand(deck.drawCard());
            }
        }
    }

    /**
     * Imprime les personnages disponibles
     */
    public void printAvailableCharacters() {
        LOGGER.info(COLOR_GREEN + "Les personnages disponibles sont : " + COLOR_RESET);
        String availableCharsMessage;
        for (GameCharacter temp : availableChars) {
            availableCharsMessage = COLOR_GREEN + temp.getRole() + COLOR_RESET;
            LOGGER.info(availableCharsMessage);
        }
    }

    /**
     * Remplit la sélection de personnages
     *
     * @param startingPos la position de départ
     */
    public void charSelectionFiller(int startingPos) {
        int i = startingPos + 1;
        while (i != (startingPos)) {
            if (i == players.size()) {
                i = 0;
                if (startingPos == 0) {
                    return;
                }
            }
            Bot p = (Bot) players.get(i);
            if (p.getGameCharacter() == null) {
                String playerInfo = p.toString();
                LOGGER.info(playerInfo);
                p.getBotAlgo().chooseCharacterAlgorithm(this);
            }
            i++;

        }
    }

    /**
     * Obtient le propriétaire de la couronne
     *
     * @return le propriétaire de la couronne
     */
    public Bot getCrownOwner() {
        return (Bot) this.getCrown().getOwner();
    }

    /**
     * Imprime le propriétaire de la couronne
     *
     * @return le propriétaire de la couronne
     */
    public Bot printCrownOwner() {
        Bot crownOwner = getCrownOwner();
        String crownOwnerMessage = "La couronne appartient a " + (crownOwner != null ? crownOwner.getName() : "personne");
        LOGGER.info(crownOwnerMessage);
        return crownOwner;
    }

    /**
     * Sélectionne un personnage
     *
     * @param crownOwner le propriétaire de la couronne
     * @param cOpos      la position du propriétaire de la couronne
     */
    public void characterSelection(Bot crownOwner, int cOpos) { //cO = crownOwner
        if (crownOwner != null) {
            String crownOwnerInfos = crownOwner.toString();
            LOGGER.info(crownOwnerInfos);
            crownOwner.getBotAlgo().chooseCharacterAlgorithm(this);
        } else {
            cOpos = 0; //Il n'y a pas de propriétaire de la couronne, donc le premier joueur commence
            Bot p1 = (Bot) players.get(0);
            LOGGER.info(p1.toString());
            p1.getBotAlgo().chooseCharacterAlgorithm(this);
            //Le premier joueur est traité ici pour garder la logique de charSelectionFiller
        }
        charSelectionFiller(cOpos);
    }

    /**
     * Gère le cas où un joueur est tué
     *
     * @param characterKilled le personnage tué
     * @param playerKilled    le joueur tué
     */
    public void playerKilled(GameCharacter characterKilled, Player playerKilled) {
        String isKilledMessage = "\n" + characterKilled.getRole().toStringLeOrLUpperCase() + " a ete tue par " + characterKilled.getAttacker().getName();
        String cannotPlayMessage = playerKilled.getName() + " ne pourra pas jouer ce tour !";
        LOGGER.info(isKilledMessage);
        LOGGER.info(cannotPlayMessage);
        // Si le roi est tué, il récupère la couronne à la fin de ce tour
        if (characterKilled.getRole() == GameCharacterRole.KING) {
            this.getCrown().setOwner(playerKilled);
            LOGGER.info("Il recuperera la couronne a la fin de ce tour");
        }
    }

    /**
     * Supprime les personnages des joueurs
     */
    public void resetChars() {
        for (Player p : players) {
            p.setGameCharacter(null);
        }
    }

    /**
     * Supprime les attaques sur les personnages
     */
    public void resetCharsState() {
        for (GameCharacter cha : allCharacters) {
            cha.setIsAlive(true);
            cha.setAttacker(null);
        }
    }

    /**
     * Obtient la liste des joueurs triés par score
     *
     * @return la liste des joueurs triés par score
     */
    public List<Player> getSortedPlayersByScore() {
        List<Player> sortedPlayersByScore = new ArrayList<>();
        for (Player player : getPlayers()) {
            player.calculateAndSetScore();
            sortedPlayersByScore.add(player);
        }
        Comparator<Player> playerComparator = Comparator.comparingInt(Player::getScore).reversed();
        sortedPlayersByScore.sort(playerComparator);
        return sortedPlayersByScore;
    }

    /**
     * Obtient la liste des joueurs triés par score pour le Warlord
     *
     * @return la liste des joueurs triés par score pour le Warlord
     */
    public List<Player> getSortedPlayersByScoreForWarlord() {
        List<Player> sortedPlayersByScore = getSortedPlayersByScore();
        for (Player player : sortedPlayersByScore) {
            // Warlord ne peut pas détruire les districts du bishop
            if (player.getGameCharacter().getRole().equals(GameCharacterRole.BISHOP)) {
                sortedPlayersByScore.remove(player);
                return sortedPlayersByScore;
            }
        }
        return sortedPlayersByScore;
    }

    /**
     * Obtient la liste des personnages qui peuvent être tués
     *
     * @return la liste des personnages qui peuvent être tués
     */
    public List<GameCharacter> getKillableCharacters() {
        List<GameCharacter> killableCharacters = new ArrayList<>(getCharactersInGame());
        for (GameCharacter cha : killableCharacters) {
            // Assassin ne peut pas se tuer lui-même
            if (cha.getRole().equals(GameCharacterRole.ASSASSIN)) {
                killableCharacters.remove(cha);
                break;
            }
        }
        return killableCharacters;
    }

    /**
     * Obtient la liste des personnages qui peuvent être volés
     *
     * @return la liste des personnages qui peuvent être volés
     */
    public List<GameCharacter> getCharactersThatCanBeStolen() {
        // Thief ne peut pas voler l'Assassin
        List<GameCharacter> charactersThatCanBeStolen = new ArrayList<>(getKillableCharacters());
        List<GameCharacter> temp = new ArrayList<>(getKillableCharacters());

        for (GameCharacter cha : temp) {
            // Thief ne peut pas se voler lui-même
            if (cha.getRole().equals(GameCharacterRole.THIEF)) {
                charactersThatCanBeStolen.remove(cha);
            }
            // Thief ne peut pas voler un personnage mort
            if (!cha.getIsAlive()) {
                String deadCharacterMessage = COLOR_RED + cha.getRole().toStringLeOrLUpperCase() + " est mort. Il ne peut pas etre vole" + COLOR_RESET;
                LOGGER.info(deadCharacterMessage);
                charactersThatCanBeStolen.remove(cha);
            }
        }

        return charactersThatCanBeStolen;
    }


    /**
     * Getter pour le deck
     *
     * @return le deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * toString pour le deck
     *
     * @return
     */
    @Override
    public String toString() {
        return deck.toString();
    }

    /**
     * Réinitialise le jeu en appelant la méthode init().
     */
    public void resetGame() {
        init();
    }

    /**
     * Retourne la taille de la ville nécessaire pour gagner.
     *
     * @return la taille de la ville nécessaire pour gagner.
     */
    public int getCitySizeToWin() {
        return CITY_SIZE_TO_WIN;
    }

    /**
     * Retourne le joueur ayant le plus de quartiers.
     *
     * @return le joueur ayant le plus de quartiers.
     */
    public Player getPlayerWithMostDistricts() {
        return players.stream().max((p1, p2) -> Integer.compare(p1.getCity().size(), p2.getCity().size())).orElse(null);
    }

    /**
     * Retourne le joueur le plus riche.
     *
     * @return le joueur le plus riche.
     */
    public Player getRichestPlayer() {
        return players.stream().max((p1, p2) -> Integer.compare(p1.getGold(), p2.getGold())).orElse(null);
    }

    /**
     * Calcule et retourne la taille moyenne des villes des joueurs.
     *
     * @return la taille moyenne des villes des joueurs.
     */
    public double averageCitySize() {
        return getPlayers().stream().mapToInt(player -> player.getCity().size()).average().getAsDouble();
    }

    /**
     * Retourne le premier joueur ayant exactement 6 quartiers.
     *
     * @return le premier joueur ayant exactement 6 quartiers.
     */
    public Player getPlayerWith6Districts() {
        return players.stream().filter(player -> player.getCity().size() == 6).findFirst().orElse(null);
    }

    /**
     * Retourne le joueur ayant le plus de cartes en main.
     *
     * @return le joueur ayant le plus de cartes en main.
     */
    public Player getPlayerWithMostCardInHand() {
        return players.stream().max((p1, p2) -> Integer.compare(p1.getDistrictsInHand().size(), p2.getDistrictsInHand().size())).orElse(null);
    }

    /**
     * Retourne le joueur ayant le quartier le moins cher dans sa ville.
     *
     * @return le joueur ayant le quartier le moins cher dans sa ville.
     */
    public Optional<Player> getPlayerWithLowestDistrictPrice() {
        Optional<Player> playerWithLowestDistrictPrice = Optional.empty();
        int lowestPrice = Integer.MAX_VALUE;

        for (Player player : players) {
            for (District district : player.getCity().getDistrictsBuilt()) {
                if (district.getPrice() < lowestPrice) {
                    lowestPrice = district.getPrice();
                    playerWithLowestDistrictPrice = Optional.of(player);
                }
            }
        }

        return playerWithLowestDistrictPrice;
    }

    /**
     * Vérifie si un joueur possède un quartier spécial.
     *
     * @param players      la liste des joueurs à vérifier.
     * @param districtName le nom du quartier spécial.
     * @return le joueur qui possède le quartier spécial, ou vide si aucun joueur ne le possède.
     */
    public static Optional<Player> playerHasSpecialDistrict(List<Player> players, String districtName) {
        for (Player player : players) {
            if (player.getCity().containsDistrict(districtName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
