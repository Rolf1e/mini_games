use crate::board::Board;
use crate::case::Case;
use crate::error::Error;
use crate::piece::{Piece, Rank};
use std::fmt;

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ConnectFourColor {
    Red,
    Yellow,
}

impl fmt::Display for ConnectFourColor {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        let s = match self {
            ConnectFourColor::Red => "Red",
            ConnectFourColor::Yellow => "Yel",
        };
        write!(f, "{}", s)
    }
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

