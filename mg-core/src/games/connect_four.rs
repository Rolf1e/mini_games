use crate::board::Board;
use crate::case::Case;
use crate::error::MGError;
use crate::piece::{Piece, Rank};
use crate::player::Action;
use crate::player::Player;
use crate::state::State;
use std::fmt;

#[derive(Debug)]
pub enum ConnectFourException {
    Extraction(String),
}

impl ConnectFourException {
    pub fn to_error(&self) -> MGError {
        match self {
            ConnectFourException::Extraction(e) => MGError::Infra(e.to_owned()),
        }
    }
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ConnectFourState {
    Red,
    Yellow,
    Over(Option<ConnectFourColor>),
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ConnectFourColor {
    Red,
    Yellow,
    Equality,
}

pub fn parse_input(input: String, color: &ConnectFourColor) -> Result<Action, MGError> {
    if let Ok(parsed) = input.parse::<i16>() {
        Ok(Action::ConnectFour(parsed as usize, color.clone()))
    } else {
        Err(MGError::ParseInputAction(input))
    }
}

pub fn play_at_connect_four(
    column_index: usize,
    color: ConnectFourColor,
    board: &mut Board,
) -> Result<(), MGError> {
    if let Board::ConnectFour(cases) = board {
        if let Some(column) = cases.get(column_index) {
            if let Some(row_index) = index_of_new_pon(&column) {
                board.place(
                    Case::ConnectFour(Piece::ConnectFour(color, Rank::ConnectFour)),
                    column_index,
                    row_index,
                )?;
                Ok(())
            } else {
                Err(MGError::IllegalMove(String::from("Column is full")))
            }
        } else {
            Err(MGError::IllegalMove(format!(
                "Column {} is out of bounds",
                column_index
            )))
        }
    } else {
        Err(MGError::BadGame)
    }
}

fn check_is_over(board: &Board) -> Result<Option<ConnectFourColor>, MGError> {
    if let Board::ConnectFour(cases) = board {
        if let Some(equality) = check_board_is_full(cases) {
            return Ok(Some(equality));
        }

        match (
            check_has_win(&cases, &ConnectFourColor::Red),
            check_has_win(&cases, &ConnectFourColor::Yellow),
        ) {
            (Ok(false), Ok(false)) => Ok(None),
            (Ok(true), _) => Ok(Some(ConnectFourColor::Red)),
            (_, Ok(true)) => Ok(Some(ConnectFourColor::Yellow)),
            (Err(e), _) | (_, Err(e)) => Err(e.to_error()),
        }
    } else {
        Err(MGError::BadGame)
    }
}

//https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
fn check_has_win(
    cases: &Vec<Vec<Case>>,
    color: &ConnectFourColor,
) -> Result<bool, ConnectFourException> {
    let (bitboard, last_column_index) = transform_to_bitboard(cases, color)?;
    Ok(check_is_win(bitboard, last_column_index))
}

fn check_is_win(bitboard: i64, width: i16) -> bool {
    let directions = resolve_directions(width);
    let mut bb: i64;
    for direction in directions {
        bb = bitboard & (bitboard >> direction);
        if (bb & (bb >> (2 * direction))) != 0 {
            return true;
        }
    }
    false
}

fn resolve_directions(width: i16) -> Vec<i16> {
    vec![1, width, width - 1, width + 1]
}

fn transform_to_bitboard(
    cases: &Vec<Vec<Case>>,
    color: &ConnectFourColor,
) -> Result<(i64, i16), ConnectFourException> {
    let last_column_index = cases.len() as i16;
    let mut bitboard: i64 = 0;

    for (i, case) in cases.iter().flatten().enumerate() {
        if &Case::Empty == case {
            bitboard ^= 0 << i;
        } else if is_color(&get_piece(case)?, &color) {
            bitboard ^= 1 << i;
        } else {
            bitboard ^= 0 << i;
        }
    }

    Ok((bitboard, last_column_index))
}

fn get_piece(case: &Case) -> Result<Piece, ConnectFourException> {
    if let Some(piece) = case.get_content() {
        Ok(piece.clone())
    } else {
        Err(ConnectFourException::Extraction(format!(
            "Failed to extract content for case, {}",
            case.display()
        )))
    }
}

fn is_color(piece: &Piece, color: &ConnectFourColor) -> bool {
    let Piece::ConnectFour(actual_color, _) = piece;
    actual_color == color
}

fn check_board_is_full(cases: &Vec<Vec<Case>>) -> Option<ConnectFourColor> {
    let max_row = cases.len();
    let max_column = cases[0].len() - 1;
    for i in 0..max_row {
        if Case::Empty == cases[i][max_column] {
            return None;
        }
    }
    Some(ConnectFourColor::Equality)
}

impl State for ConnectFourState {
    fn default() -> Self
    where
        Self: Sized,
    {
        ConnectFourState::Red
    }

    fn next(&mut self, board: &Board) -> Result<(), MGError> {
        *self = match self {
            ConnectFourState::Red => {
                if let Some(color) = check_is_over(board)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Yellow
                }
            }
            ConnectFourState::Yellow => {
                if let Some(color) = check_is_over(board)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Red
                }
            }
            ConnectFourState::Over(Some(color)) => ConnectFourState::Over(Some(color.clone())),
            ConnectFourState::Over(None) => ConnectFourState::Over(None),
        };
        Ok(())
    }

    fn message(&self) -> String {
        match &self {
            ConnectFourState::Red => String::from("Red is playing"),
            ConnectFourState::Yellow => String::from("Yellow is playing"),
            ConnectFourState::Over(color) => {
                if let Some(color) = color {
                    format!("Game is over, {} has win !", color)
                } else {
                    String::from("Not over yet")
                }
            }
        }
    }

    fn ask_next_player(
        &self,
        players: &Vec<&Box<dyn Player>>,
        board: &Board,
    ) -> Result<Action, MGError> {
        match &self {
            ConnectFourState::Red => Ok(players[0].ask_next_move(board)?),
            ConnectFourState::Yellow => Ok(players[1].ask_next_move(board)?),
            ConnectFourState::Over(_) => Err(MGError::IllegalMove(String::from(
                "You shouldn't be able to call this state, you hacker",
            ))),
        }
    }
}

impl fmt::Display for ConnectFourColor {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        let s = match self {
            ConnectFourColor::Red => "Red",
            ConnectFourColor::Yellow => "Yel",
            ConnectFourColor::Equality => "NoO",
        };
        write!(f, "{}", s)
    }
}

pub fn display(board: &Board) -> String {
    if let Board::ConnectFour(cases) = board {
        let mut message = String::new();
        let cases_len = cases.len();
        let col_len = cases[0].len();
        for y in (0..col_len).rev() {
            message.push('|');
            for x in 0..cases_len {
                let case = board.at((x, y));
                message.push_str(format!(" {} |", case.display()).as_str());
            }
            message.push('\n');
        }

        message 
    } else {
        String::from("Bad board type")
    }
}

fn index_of_new_pon(column: &[Case]) -> Option<usize> {
    for (i, case) in column.iter().enumerate() {
        if &Case::Empty == case {
            return Some(i);
        }
    }
    None
}
