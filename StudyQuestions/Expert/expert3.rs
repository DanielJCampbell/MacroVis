use self::Integer::{Int};

#[derive(Debug)]
enum Integer {
    Int(i32),
}

impl Integer {

    fn add (&self, other: &Integer) -> Integer {
        let s = match self {
            &Int(val) => val,
        };
        
        let o = match other {
            &Int(val) => val,
        };
        
        Int(s+o)
    }
}

macro_rules! rec_sequence {
    ($arr:expr,[$idx:expr,]: $ty:ty; $($init:expr),+; -> $eq:expr) => {{
        struct Sequence {
            mem: [$ty; 2],
            pos: usize,
        }
        
        impl Iterator for Sequence {
            type Item = i32;
            
            fn next(&mut self) -> Option<$ty> {
                if self.pos < self.mem.len() {
                    self.pos += 1;
                    Some(self.mem[self.pos-1])
                }
                else {
                    let res;
                    {
                        let $arr = &self.mem;
                        let $idx = self.pos;
                        res = $eq;
                    }
                    for i in 0 .. self.pos-1 {
                        self.mem[i] = self.mem[i+1];
                    }
                    self.mem[self.pos-1] = res;
                    Some(res)
                }
            }
        }
        Sequence{mem: [$($init),*], pos: 0}
    }};
}

fn main() {
    let fib = rec_sequence!(a[n]: Integer; Int(0), Int(1); -> {a[n-2].add(&a[n-1])});
    for i in fib.take(10) {
        println!("{:?}", i);
    }
}