use crate::board::Board;
use crate::case::Case;

use crate::error::Error;
use crate::piece::{Piece, Rank};
use crate::state::State;
use std::fmt;

#[derive(Debug)]
pub enum ConnectFourException {
    Extraction(String),
}

impl ConnectFourException {
    pub fn to_error(&self) -> Error {
        match self {
            ConnectFourException::Extraction(e) => Error::Infra(e.to_owned()),
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

pub fn play_at_connect_four(
    column_index: usize,
    color: ConnectFourColor,
    board: &mut Board,
) -> Result<(), Error> {
    let Board::ConnectFour(cases) = board;
    if let Some(column) = cases.get(column_index) {
        if let Some(row_index) = index_of_new_pon(&column) {
            board.place(
                Case::ConnectFour(Piece::ConnectFour(color, Rank::ConnectFour)),
                column_index,
                row_index,
            )?;
            Ok(())
        } else {
            Err(Error::IllegalMove(String::from("Column is full")))
        }
    } else {
        Err(Error::IllegalMove(format!(
            "Column {} is out of bounds",
            column_index
        )))
    }
}

pub fn check_is_over(board: &Board, pon: &i8) -> Result<Option<ConnectFourColor>, Error> {
    if let Some(equality) = check_board_is_full(board) {
        return Ok(Some(equality));
    }

    match (
        check_has_win(&board, pon, &ConnectFourColor::Red),
        check_has_win(&board, pon, &ConnectFourColor::Yellow),
    ) {
        (Ok(false), Ok(false)) => Ok(None),
        (Ok(true), _) => Ok(Some(ConnectFourColor::Red)),
        (_, Ok(true)) => Ok(Some(ConnectFourColor::Yellow)),
        (Err(e), _) | (_, Err(e)) => Err(e.to_error()),
    }
}

//https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
fn check_has_win(
    board: &Board,
    pon: &i8,
    color: &ConnectFourColor,
) -> Result<bool, ConnectFourException> {
    let (bitboard, last_column_index) = transform_to_bitboard(board, color)?;
    eprintln!("bitboard {}: -> {:b}", color, bitboard);
    Ok(check_is_win(bitboard, last_column_index))
}

//  3	 7 11	15
//  2	 6 10	14
//  1	 5  9	13
//  0	 4  8	12
fn check_is_win(bitboard: i64, width: i16) -> bool {
    let directions = resolve_directions(width);
    let mut bb: i64;
    for direction in directions {
        // eprintln!("{:?}", direction);
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
    board: &Board,
    color: &ConnectFourColor,
) -> Result<(i64, i16), ConnectFourException> {
    let Board::ConnectFour(cases) = board;
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

fn check_board_is_full(board: &Board) -> Option<ConnectFourColor> {
    let Board::ConnectFour(cases) = board;
    let max_row = cases.len();
    let max_column = cases[0].len() - 1;
    for i in 0..max_row {
        if Case::Empty == cases[i][max_column] {
            return None;
        }
        eprintln!("C:{:?}", cases[i][max_column]);
    }
    Some(ConnectFourColor::Equality)
}

impl State for ConnectFourState {
    fn next(&mut self, board: &Board) -> Result<(), Error> {
        *self = match self {
            ConnectFourState::Red => {
                if let Some(color) = check_is_over(board, &4)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Yellow
                }
            }
            ConnectFourState::Yellow => {
                if let Some(color) = check_is_over(board, &4)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Red
                }
            }
            ConnectFourState::Over(_) => self.clone(),
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
    let Board::ConnectFour(cases) = board;
    let cases_length = cases.len();
    let mut message = String::new();
    for y in (0..cases_length).rev() {
        message.push('|');
        for x in 0..cases_length {
            let case = board.at((x, y));
            message.push_str(format!(" {} |", case.display()).as_str());
        }
        message.push('\n');
    }
    message
}

fn index_of_new_pon(column: &[Case]) -> Option<usize> {
    for (i, case) in column.iter().enumerate() {
        if &Case::Empty == case {
            return Some(i);
        }
    }
    None
}
