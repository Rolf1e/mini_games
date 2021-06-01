use crate::case::Case;
use crate::error::MGError;
use crate::player::Color;
use crate::games::connect_four;
use crate::player::Action;

#[derive(Debug, Clone)]
pub enum Board {
    ConnectFour(Vec<Vec<Case>>),
}

pub type Coordinate = (usize, usize);

impl Board {

    pub fn parse_input(input: String, color: &Color) -> Result<Action, MGError>  {
        match color {
            Color::ConnectFour(c) => connect_four::parse_input(input, c)
        }
    }

    pub fn play(&mut self, action: Action) -> Result<(), MGError> {
        match (&self, action) {
            (Board::ConnectFour(_), Action::ConnectFour(column_index, color)) => {
                connect_four::play_at_connect_four(column_index, color, self)
            }
        }
    }

    pub fn at(&self, (x, y): Coordinate) -> &Case {
        match self {
            Board::ConnectFour(board) => &board[x][y],
        }
    }

    pub fn display(&self) -> String {
        match &self {
            Board::ConnectFour(_) => connect_four::display(self),
        }
    }

    pub fn place(&mut self, case: Case, x: usize, y: usize) -> Result<(), MGError> {
        match self {
            Board::ConnectFour(board) => {
                board[x][y] = case;
                Ok(())
            }
        }
    }
}

