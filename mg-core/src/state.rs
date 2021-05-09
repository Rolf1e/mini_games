use crate::board::Board;
use crate::error::Error;
use crate::player::Action;
use crate::player::Player;

pub trait State {
    fn default() -> Self
    where
        Self: Sized;

    fn next(&mut self, board: &Board) -> Result<(), Error>;

    fn message(&self) -> String;

    fn ask_next_player(
        &self,
        players: &Vec<&Box<dyn Player>>,
        board: &Board,
    ) -> Result<Action, Error>;
}
