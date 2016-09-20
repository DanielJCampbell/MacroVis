# MacroVis

Java tool that works in tandem with Rust in this commit: https://github.com/DanielJCampbell/rust/tree/Stepwise

Using the macro expansion JSON data generated by Rust, visualises that data, allowing users to view the expanded macro traces of any simple (single-file) crates. Also comes with basic file loading/saving/editing functionality, for ease of use.

This tool has been constructed by Daniel Campbell to meet the requirements of the COMP 489 Honours paper at Victoria University of Wellington.

# Running Notes

To run, make sure the supplied script folder (containing rustcompile.sh) is in the same directory as the supplied (or built) jar file. Make sure you have downloaded and installed the Stepwise version of rust at the above link. If this version of rust is on a path that is not part of the environment, modify rustcompile.sh to add it (it contains example code). Then simply run the jar file, and open a rust file of your choice.

Questions for the user study can be found in the StudyQuestions folder - the Novice subfolder is targeted at those without prior Rust experience, the Expert folder is targeted at those with some familiarity with Macros in Rust.
