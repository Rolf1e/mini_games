use crate::state::State;
use crate::board::Board;
use crate::error::Error;
use crate::games::connect_four::ConnectFourState;
use crate::player::Action;
use crate::player::Player;

pub struct Operator {
    board: Board,
    player_1: Box<dyn Player>,
    player_2: Box<dyn Player>,
    state: ConnectFourState, // generify
}

impl Operator {
    pub fn new(board: Board, player_1: Box<dyn Player>, player_2: Box<dyn Player>) -> Self {
        Operator {
            board,
            player_1,
            player_2,
            state: ConnectFourState::Red,
        }
    }

    pub fn play(&mut self) -> Result<(), Error> {
        let action = self.ask_player();
        self.board.play(action)?;
        self.change_state()
    }

    pub fn state(&mut self) -> Result<&dyn State, Error> {
        match self.change_state() {
            Ok(_) => Ok(&self.state),
            Err(e) => Err(e),
        }
    }

    fn ask_player(&self) -> Action {
        match &self.state {
            ConnectFourState::Red => self.player_1.ask_next_move(&self.board),
            ConnectFourState::Yellow => self.player_2.ask_next_move(&self.board),
            ConnectFourState::Over(_) => {
                panic!("You shouldn't be able to call this state, you hacker")
            }
        }
    }

    fn change_state(&mut self) -> Result<(), Error> {
        self.state.next(&self.board)
    }
}

