question 1 :
Un job est un groupe d'étapes exécuté sur une même machine virtuelle. Une étape (step) est une action individuelle à l'intérieur d'un job : soit une commande shell (run:), soit une action prête à l'emploi.
Les étapes d'un job s'exécutent dans l'ordre et partagent le système de fichiers et les processus.Les jobs sont isolés : chacun a sa propre machine vierge et ne partage rien avec les autres. C'est pourquoi chaque job du ci.yml refait checkout et setup.sh.
question 2 :
Par défaut, les jobs sont indépendants : GitHub les lance en parallèle, chacun sur son propre runner, ce qui fait gagner du temps.
Pour imposer un ordre, on utilise le mot-clé needs
build:
  needs: [python, node, java, php]   # build attend les 4 jobs de tests
deploy:
  needs: build                        # deploy attend build
  Si un job listé dans needs échoue, le job dépendant est ignoré
  question 3 :
  Le fichier YAML est versionné dans le dépôt : toute personne ayant accès au dépôt peut le lire, et l'historique Git le conserve même après suppression. Si le dépôt est public, le mot de passe est visible par tout le monde.

 Il faut le stocker dans les secrets GitHub (Settings → Secrets and variables → Actions → New repository secret) et l'utiliser avec ${{ secrets.NOM }}. Un secret n'est jamais réaffiché après sa création et il est masqué (***) dans les logs.
question 4 :
Chaque job démarre sur un runner neuf et vierge, qui disparaît à la fin du job. La base n'existe donc que le temps du job, et db/setup.sh la reconstruit à chaque exécution.

Avantage : les tests partent d'un état connu et identique à chaque fois. Ils sont reproductibles : pas de données résiduelles d'une exécution précédente, pas de résultats aléatoires, pas de « ça marche chez moi ». Cela prouve aussi que le schéma peut être reconstruit de zéro.
question 5 :
Un test unitaire vérifie une petite unité de code (une fonction) de façon isolée, sans dépendance externe : la base de données est souvent remplacée par un faux objet (mock).
Un test d'intégration vérifie que plusieurs composants fonctionnent ensemble, par exemple le code et une vraie base de données.

