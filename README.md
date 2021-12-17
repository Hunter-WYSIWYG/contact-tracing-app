clone repo onto your computer:
git clone https://gitlab.hs-anhalt.de/stmagage/projekt_mobile-anwendungsentwicklung.git

when adding features (maybe over several days):
1. create feature branch from master (in Gitlab: Repository -> Branches -> New Branch)

2. git pull (in console)

3. git checkout [name of feature branch] (in console)

4. do your changes

5. git add . (in console)

6. git commit -m "[some commit message]" (in console)

7. git push (in console)

-> Repeat steps 2-7 when you need to work on this feature again

when feature is finished:
1. git pull

2. git checkout [name of feature branch] (in console)

3. git merge master

4. git add. (in console)

5. git commit -m "[some commit message]" (in console)

6. git push (in console)

7. create merge request (in Gitlab: Merge Request -> New Merge Request)

8a. Merge if there are no conflicts

8b. Contact Marcus if there are Conflicts ;)
