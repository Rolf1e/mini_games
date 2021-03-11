use crate::board::Board;
use crate::error::Error;
use crate::player::Action;
use crate::player::Player;

pub struct Operator {
    board: Board,
    player_1: Box<dyn Player>,
    player_2: Box<dyn Player>,
    turn: bool,
}

impl Operator {

    pub fn new(
    board: Board,
    player_1: Box<dyn Player>,
    player_2: Box<dyn Player>,
    ) -> Self {
        Operator { board, player_1, player_2, turn: true, }
    }

    pub fn play(&mut self) -> Result<(), Error> {
        let action = self.ask_player();
        self.board.play(action)?;
        Ok(self.switch_turn())
    }
        
    
    fn ask_player(&self) -> Action {
        if self.turn {
            self.player_1.ask_next_move(&self.board)
        } else {
            self.player_2.ask_next_move(&self.board)
        }
    }

    fn switch_turn(&mut self) {
        self.turn = !self.turn
    }
}

