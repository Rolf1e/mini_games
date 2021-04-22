use crate::board::Board;
use crate::case::Case;
use crate::error::Error;
use crate::piece::{Piece, Rank};
use crate::state::State;
use std::fmt;

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

/// To check game state
pub fn check_is_over(board: &Board, pon: &i8) -> Option<ConnectFourColor> {
    if let Some(color) = check_board_is_full(board) {
        Some(color)
    } else if let Some(color) = check_by_row(board, pon) {
        Some(color)
    } else if let Some(color) = check_by_column(board, pon) {
        Some(color)
    } else if let Some(color) = check_by_diagonal(board, pon) {
        Some(color)
    } else  {
        None
    }
}

fn check_board_is_full(board: &Board) -> Option<ConnectFourColor> {
    let Board::ConnectFour(cases) = board;
    let max_row = cases.len();
    let max_column = cases[0].len() - 1;
    for i in 0..max_row {
        if Case::Empty == cases[i][max_column] {
            return None;
        }
    }
    Some(ConnectFourColor::Equality)
}

/// Row is Board[x][0] -> we fix 0
fn check_by_row(board: &Board, pon: &i8) -> Option<ConnectFourColor> {
    eprintln!("{}", board.display());
    let Board::ConnectFour(cases) = board;
    let max_row = cases.len();
    let max_column = cases[0].len();
    for i in 0..max_column {
        let mut row = Vec::with_capacity(max_row);

        for j in 0..max_row {
            row.push(cases[j][i].clone());
        }
        if let Some(color) = check_line(&row, pon) {
            return Some(color);
        }
    }
    None
}

/// Column is Board[0][y] -> we fix 0
fn check_by_column(board: &Board, pon: &i8) -> Option<ConnectFourColor> {
    let Board::ConnectFour(cases) = board;
    for column in cases {
        eprintln!("\n COLUMN => {:?} \n", column);
        if let Some(color) = check_line(column, pon) {
            return Some(color);
        }
    }
    None
}

fn check_by_diagonal(board: &Board, pon: &i8) -> Option<ConnectFourColor> {
    todo!()
}

fn check_line(cases: &[Case], pon: &i8) -> Option<ConnectFourColor> {
    let mut stack = 1;

    let case = &cases[0];
    if &Case::Empty == case {
        return None;
    }
    let Piece::ConnectFour(color, _) = case
        .get_content()
        .unwrap_or_else(|| panic!("Failed to get content from cases when check winner"));

    let mut color = color;
    for i in 1..cases.len() {
        if &stack == pon {
            return Some(color.clone());
        }
            
        let case = &cases[i];
        if &Case::Empty == case {
            return None;
        }
        let Piece::ConnectFour(new_color, _) = case
            .get_content()
            .unwrap_or_else(|| panic!("Failed to get content from cases when check winner"));
        if new_color == color {
            stack += 1;
        } else {
            stack = 1;
            color = new_color;
        }
    }
    if &stack == pon {
        Some(color.clone())
    } else {
        None
    }
}

impl State for ConnectFourState {
    fn next(&mut self, board: &Board) {
        *self = match self {
            ConnectFourState::Red => {
                if let Some(color) = check_is_over(board, &4) {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Yellow
                }
            }
            ConnectFourState::Yellow => {
                if let Some(color) = check_is_over(board, &4) {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Red
                }
            }
            ConnectFourState::Over(_) => self.clone(),
        };
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
