macro_rules! bct {
    (($($commands:tt)*) -> ()) => { println!(""); };

    (() $($t:tt)*) => { println!(""); };

    ((0 $($commands:tt)*) -> ($dead:tt $($data:tt)*)) => {{
        println!("[{} , 0] -> [{}]",
                 stringify!($($commands),*), 
                 stringify!($($data),*));
        bct!(($($commands)*0) -> ($($data)*));
    }};

    ((1) => ($($data:tt)*)) => {{
        println!("[1] -> [{}]", stringify!($($data),*1));
        bct!((1) -> ($($data)*1));
    }};

    ((1 $n:tt$($rest:tt)*) -> ($($data:tt)*)) => {{
        println!("[{} , 1 {}] -> [{}]",
                 stringify!($($rest),*),
                 stringify!($n),
                 stringify!($($data),* , $n));
        bct!(($($rest)*1$n) -> ($($data)*$n));
    }}
}

fn main() {
    bct!((00100) -> (101));
}
