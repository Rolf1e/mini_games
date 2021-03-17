use crate::board::Board;

pub trait State {
    fn next(&mut self, board: &Board);
    
    fn message(&self) -> String;
}

