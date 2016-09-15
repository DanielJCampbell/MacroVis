macro_rules! add_val {
    ($val: expr) => {
        x = x + $val;
        y = y + $val;
        z = z + $val;
    };
}

fn main() {
    let mut x = 1;
    let mut y = 3;
    let mut z = 5;
    add_val!(1);
    println!("X: {} Y: {} Z: {}", x,y,z);
}