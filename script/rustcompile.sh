# If your system does not have the stepwise version of rust in the environment,
# Set the path appropriately below
PATH=$HOME/rust/bin:$PATH
rustc -Zmacro-analysis -Zcontinue-parse-after-error $FILENAME
