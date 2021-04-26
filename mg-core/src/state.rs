use crate::board::Board;
use crate::error::Error;

pub trait State {
    fn next(&mut self, board: &Board) -> Result<(), Error>;
    
    fn message(&self) -> String;
}

