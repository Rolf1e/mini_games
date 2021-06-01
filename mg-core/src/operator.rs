use crate::board::Board;
use crate::error::MGError;
use crate::player::Action;
use crate::player::Player;
use crate::state::State;

pub struct Operator {
    board: Board,
    player_1: Box<dyn Player>,
    player_2: Box<dyn Player>,
    state: Box<dyn State>,
}

impl Operator {
    pub fn new(
        board: Board,
        player_1: Box<dyn Player>,
        player_2: Box<dyn Player>,
        state: Box<dyn State>,
    ) -> Self {
        Operator {
            board,
            player_1,
            player_2,
            state,
        }
    }

    pub fn play(&mut self) -> Result<(), MGError> {
        let action = self.ask_player()?;
        self.board.play(action)?;
        self.update_state()
    }

    pub fn state(&mut self) -> &Box<dyn State> {
        &self.state
    }

    fn ask_player(&self) -> Result<Action, MGError> {
        self.state
            .ask_next_player(&vec![&self.player_1, &self.player_2], &self.board)
    }

    /// TODO Only for testing, should be removed
    pub fn test_update_state(&mut self) -> Result<(), MGError> {
        self.update_state()
    }

    fn update_state(&mut self) -> Result<(), MGError> {
        eprintln!("Changing state");
        self.state.next(&self.board)
    }
}
