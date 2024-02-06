# Contribuer à Citadelles

Bienvenue dans Citadelles ! Pour garantir une expérience fluide et collaborative, veuillez prendre un moment pour consulter les lignes directrices de contribution ci-dessous.

---

### Table des matières
* [Code de Conduite](#code-de-conduite)
* [Comment Contribuer](#comment-contribuer)
  * [Questions](#questions)
  * [Créer une Issue](#créer-une-issue)
  * [Cycle de vie des Issues](#cycle-de-vie-des-issues)
    * [Commits](#commits)
    * [Soumettre une Pull Request](#soumettre-une-pull-request)
    * [Participer aux Reviews](#participer-aux-revues)
* [Code](#code)
* [Stratégie de Branches](#stratégie-de-branches)

---

### Code de Conduite

Citadels opère sous la direction de l'Équipe E. En tant que contributeur, vous êtes tenu de respecter notre code de conduite afin de maintenir un environnement professionnel.

---

### Comment Contribuer

#### Questions

Si vous avez des questions concernant le projet ou son développement, n'hésitez pas à les poser via nos canaux de communication.

#### Créer une Issue

Les contributions commencent par la création d'issues bien définies. Chaque issue doit inclure une description détaillée et des labels appropriés pour faciliter la catégorisation efficace. De plus, chaque issue doit être liée à un milestone, ce qui aide à organiser les tâches dans un délai défini. Nos tableaux de projet fournissent un aperçu de toutes les issues associées à un milestone.

#### Cycle de vie des Issues

À sa création, une issue est marquée comme `To Do`, en attente d'attribution à un membre de l'équipe. Une fois attribuée, elle passe à `In Progress`. À son achèvement, elle passe à l'étape de pull request, nécessitant la revue d'au moins un membre de l'équipe. L'issue est marquée comme `Ready for review`. Suite à des discussions approfondies, des commentaires et à l'approbation, la pull request peut être fusionnée et l'issue est considérée comme `Done`. Bien que fermées, les issues peuvent être réouvertes si des ajustements supplémentaires sont nécessaires.

##### Commits

Chaque commit doit respecter un début conventionnel (conventional commit).

##### Soumettre une Pull Request

Si vos modifications correspondent à une issue existante, assurez-vous de référencer le numéro d'issue GitHub pertinent dans la description de la pull request.

##### Participer aux Reviews

Les revues jouent un rôle crucial dans la garantie de l'intégrité et de la qualité de la base de code, minimisant ainsi les problèmes potentiels.

---

### Code

Notre projet maintient une norme de codage rigoureuse en conformité avec les pratiques industrielles établies et respecte les directives fournies par SonarQube. Des revues de code sont menées pour identifier et corriger tout code suspect, garantissant ainsi la qualité globale et la maintenabilité de la base de code.

---

### Stratégie de Branches

Notre processus de développement suit la stratégie de branchement Git Flow qui comprend :

- **Branch Master :** Contient les versions stables en production.
- **Branch Develop :** Originaire de master, cette branche sert de branche de développement principale. Des branches de fonctionnalités sont créées à partir de develop pour chaque issue.
- **Branches de Fonctionnalités :** Chaque issue est attribuée à une branche de fonctionnalité dédiée à des fins de développement. Une fois terminée, une pull request est initiée pour fusionner la fonctionnalité dans develop.

Tout push ou pull request affectant master et develop déclenche des processus automatisés de test et de build. L'exécution du build implique `mvn exec:java`, tandis que les tests sont effectués à l'aide de `mvn clean package`.

Cette approche stratégique garantit un développement systématique, des tests rigoureux et une intégration transparente des fonctionnalités dans le projet.