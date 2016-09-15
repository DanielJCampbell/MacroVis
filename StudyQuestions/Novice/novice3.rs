macro_rules! muncher {
   (? $($t:tt)*) => {{
       print!("foo");
       muncher!($($t)*);
   }};

   (! $($t:tt)*) => {{
       print!("bar");
       muncher!($($t)*);
   }};

   () => {{
       print!("baz!");
       muncher!(!n);
   }};

   (!n) => {{
       println!("");
   }};
}

fn main() {
    muncher!(!?!);
}
