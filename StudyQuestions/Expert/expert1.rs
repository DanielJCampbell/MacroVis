use self::Church::{Zero, Plus, Minus};

enum Church {
    Zero,
    Plus(Box<Church>),
    Minus(Box<Church>)
}

fn inner(v: Church) -> Church {
    match v {
        Zero => Zero,
        Plus(val) => *val,
        Minus(val) => *val
    }
}

fn to_int(v: Church) -> i32 {
    match v {
        Zero => 0,
        Plus(val) => 1+to_int(*val),
        Minus(val) => -to_int(*val),
    }
}

macro_rules! church {

    (@int $res:ident) => { $res };

    (@int $res:ident; - $($rem:tt)+) => {{
        let res = match $res {
            Zero => Minus(Box::new(Plus(Box::new(Zero)))),
            Plus(val) => *val,
            Minus(val) => Minus(Box::new(Plus(val)))
        };
        church!(@int res $($rem)*)
    }};
    
    (@int $res:ident; - $($rem:tt)+) => {{
        let res = match $res {
            Minus(val) => match inner(*val) {
                Zero => Plus(Box::new(Zero)),
                val => Minus(Box::new(val)),
            },
            val => Plus(Box::new(val))
        };
        church!(@int res $($rem)*)
    }};
    
    ($($t:tt)*) => {{
        let res = Zero;
        church!(@int res $($t)*)
    }}
}

fn main() {
    println!("{}", to_int(church!(+--+-++)));
}
