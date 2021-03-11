use crate::board::Board;

pub trait Player {
    fn ask_next_move(&self, board: &Board) -> Action;

    fn get_color(&self) -> &str;

    fn get_name(&self) -> &str;
}

pub enum Action {
    ConnectFour(usize, String),
}
