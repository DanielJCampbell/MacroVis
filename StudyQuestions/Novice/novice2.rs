macro_rules! add_all {
   (@inner $($x:expr, $($y:expr),+);* ) => {
       $(
           let mut x = $x;
           $(
               x = x + $y;
           )*
       )*
   };
   
   ($($x:ident, $($y:expr),+ );* ) => {
       add_all!(@inner $($x, $($y),*);*);
   };
}

fn main() {
    let mut x = 1;
    let mut y = 100;
    addall!(x, 2, 4; y, 6; x, 1);
    println!("X: {}, Y: {}", x, y);
}
