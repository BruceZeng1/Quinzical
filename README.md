# project

To run Quinzical, execute run.sh from the terminal

Quinzical.jar should be in the same directory as the Questions folder, and the InternationalQuestions folder

If you encounter any problems or errors please delete the tmp folder.
Errors can occur if multiple instances of the game are opened at the same time, please refrain from doing this.

Please make sure you have akl_nz_jdt_diphone in usr/share/festival/voices/english/

To add questions, either use the untility in the app, or add questions directly to the the text files.
For the International questions, this must be done in the game files.
When adding a question, add it in the form "QUESTION|PREFIX|ANSWER", the "|" is necessary otherwise you will encounter errors.

If you are adding a new category, then you must add at least 5 questions.
the text file should be in the form:

CATEGORY NAME
QUESTION|PREFIX|ANSWER
QUESTION|PREFIX|ANSWER
QUESTION|PREFIX|ANSWER
QUESTION|PREFIX|ANSWER
QUESTION|PREFIX|ANSWER

NOTE: the category name will come from the file name

If you wish to delete categories you may do so, but there must be at leat 5 categories in the Questions folder otherwise you will encounter errors.

please do not add subfolders to the question folders as this will cause errors
