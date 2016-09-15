macro_rules! counter {
   () => { 0 };
   ($e:expr) => { 1 };
   ($e:expr $($es:expr)+) => { 1 + counter!($($es)*) };
}

fn main() {
    println!("{}", counter!('a', 'b', 6, "Foo", Vec::new()));
}
