#INTRODUCTION
Hey, thanks for taking an interest in this study, before you begin please read the information sheet [here] - also note that by taking part in this study you consent to your survey answers being used in my Honours report (you are completely anonymous, I am not tracking anything). This project is being supervised by Nick Cameron (nrc), we basically want to see if the new macro data I've added to Rust, and the tool I've built to use it, is better than Rust's default functionality)

# HOW TO RUN:

# STARTUP:

Setup consists of a single step - reset this directory to put all the example files back in their base state. Open the terminal, navigate to MacroVis folder, and run the command `git reset --hard ~HEAD`

After that, to open the tool, it's just `java -jar MacroVis.jar`

# TOOL NOTES

The tool is split into two main tabs, the Source Editor and Expansion Viewer. The source editor consists of a basic text editor, a compile&run button (which also saves), and a text pane showing the rustc output for the most recent run. (There's also a menu with the standard open/save options, those work as you'd expect).

The Expansion Viewer is the more interesting tab - if the compile didn't panic, it will show an uneditable pane of the source text again, plus an empty side pane and some buttons. You can step forward and back with the buttons to step through macro invocations, seeing what each macro invocation expanded to (recursion = multiple steps). You can also select an individual invocation by clicking on it to bring it up on the right, where it can be stepped through without affecting the main view. Finally, if you check the textbox then stepping through the macro on the right pane will step through it on the left pane, allowing you to step individual macros in the larger screen.

# WHAT TO DO:
Basically, play around with it! The study questions folder has 4 novice (easy) and 3 expert (much harder) questions, where a question is a macro definition/call that contains one or more bugs. The novice/expert pdfs explain what each macro is meant to do, and what the output of each call is meant to be.

However, you're not being tested - just play around and use the tool, see if you can solve some of the questions (Novice 3 & 4 are good candidates), see if the tool helps. When you're done, just navigate here [form goes here] and fill out the survey. This study is just seeing whether you feel the tool is an improvement over rust's default macro expansion data (rustc -Zallow-unstable-options pretty=expanded). If you want to check what that's like, you can run ControlVis.jar, which is the same tool but running that command instead (so greatly pared down functionality - you can step between unexpanded and all macros fully expanded, nothing else).

# FINAL NOTES (WHAT TO DO IF IT BREAKS):
No guarantees the tool will work perfectly. The GUI itself should be fine or near fine, but the underlying Rust implementation has some issues still - I lack the time to fix them. If you get a major crash, or you've borked it up beyond repair, copy over from the StudyQuestionsSource and start over. If those files are done, you can just reset the git repo: git reset --hard ~HEAD. That should put everything back the way it is (and copy StudyQuestions
